/*
 * External Code Formatter
 * Copyright (c) 2007-2008 Esko Luontola, www.orfjackal.net
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.orfjackal.extformatter.plugin;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.command.UndoConfirmationPolicy;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.*;
import com.intellij.psi.PsiFile;
import com.intellij.psi.codeStyle.CodeStyleManager;
import com.intellij.util.IncorrectOperationException;
import net.orfjackal.extformatter.*;
import net.orfjackal.extformatter.plugin.util.*;
import net.orfjackal.extformatter.util.TempFileManager;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.*;

/**
 * Intercepts the calls to {@link CodeStyleManager#reformatText} and redirects them
 * to a {@link CodeFormatter} if the formatter supports reformatting that file.
 * Otherwise falls back to using the original {@link CodeStyleManager} instance.
 *
 * @author Esko Luontola
 * @since 3.12.2007
 */
public class ExternalizedCodeStyleManager extends DelegatingCodeStyleManager {

    private static final Logger LOG = Logger.getInstance(ExternalizedCodeStyleManager.class.getName());

    @NotNull private final CodeFormatter replacement;
    @NotNull private final List<VirtualFile> toBeReformatted = new ArrayList<VirtualFile>();

    public ExternalizedCodeStyleManager(@NotNull CodeStyleManager original, @NotNull CodeFormatter replacement) {
        super(original);
        this.replacement = replacement;
    }

    public void reformatText(@NotNull PsiFile psiFile, int startOffset, int endOffset) throws IncorrectOperationException {
        VirtualFile file = psiFile.getVirtualFile();
        Project project = psiFile.getProject();
        if (file != null
                && canReformat(file, project)
                && wholeFile(psiFile, startOffset, endOffset)) {
            queueReformatOf(file, project);
        } else {
            super.reformatText(psiFile, startOffset, endOffset);
        }
    }

    /**
     * The formatting will be executed after IDEA has called the 'reformatText' method for all files
     * which should be reformatted in one go. Starting the formatter for each file would be very slow,
     * especially in the case of EclipseCodeFormatter, so that's why a the files are first put into
     * a queue which is then processed in one go.
     */
    private void queueReformatOf(@NotNull VirtualFile file, @NotNull Project project) {
        LOG.info("Queue for reformat: " + file);
        toBeReformatted.add(file);
        ApplicationManager.getApplication().invokeLater(reformatQueuedFiles(project));
    }

    @NotNull
    private Runnable reformatQueuedFiles(@NotNull Project project) {
        Runnable reformatAll = new Runnable() {
            public void run() {
                LOG.info("Reformatting files: " + toBeReformatted);
                try {
                    reformatWithUndoSupport(toBeReformatted);
                } finally {
                    toBeReformatted.clear();
                }
            }
        };

        // IDEA requires 'reformatAll' to be executed as a command and a write action
        CommandRunner reformatCommand = new CommandRunner(project, new WriteActionRunner(reformatAll),
                Messages.message("command.reformatCode"), null, UndoConfirmationPolicy.REQUEST_CONFIRMATION);

        return new ConditionalRunner(reformatCommand) {
            public boolean runOnlyWhen() {
                return !toBeReformatted.isEmpty();
            }
        };
    }

    /**
     * HACK: We can't reformat the original files and then use {@link VirtualFile#refresh}
     * so that IDEA would load the changes, because then it would not be possible to
     * use the Undo (Ctrl+Z) command. The solution that is used here is to make
     * temporary copies of all files, reformat them, and finally copy the text contents
     * from the temporary files to the original files using the Document interface.
     * (<a href="http://www.intellij.net/forums/thread.jspa?threadID=271800&tstart=0">See also</a>)
     */
    private void reformatWithUndoSupport(@NotNull List<VirtualFile> files) {
        TempFileManager manager = tempFileManagerFor(files);
        reformatOptimally(manager.tempFiles());
        copyTextFromTempFiles(manager);
        manager.dispose();
    }

    @NotNull
    private static TempFileManager tempFileManagerFor(@NotNull List<VirtualFile> files) {
        TempFileManager manager = new TempFileManager();
        for (VirtualFile file : files) {
            save(file);
            manager.add(ioFile(file));
        }
        return manager;
    }

    private void reformatOptimally(@NotNull File[] files) {
        OptimizingReformatQueue optimizer = new OptimizingReformatQueue(replacement);
        new AdaptiveCodeFormatter(optimizer).reformatMany(files);
        optimizer.flush();
    }

    private static void copyTextFromTempFiles(@NotNull TempFileManager manager) {
        for (Map.Entry<File, File> entry : manager.tempsToOriginals().entrySet()) {
            File temp = entry.getKey();
            File original = entry.getValue();
            copyText(temp, original);
        }
    }

    private static void copyText(@NotNull File from, @NotNull File to) {
        VirtualFile fromFile = LocalFileSystem.getInstance().refreshAndFindFileByIoFile(from);
        VirtualFile toFile = LocalFileSystem.getInstance().refreshAndFindFileByIoFile(to);
        if (fromFile != null && toFile != null) {
            // for some reason refreshAndFindFileByIoFile does not reset a cached document, so another refresh is needed
            fromFile.refresh(false, false);
            Document readFrom = FileDocumentManager.getInstance().getDocument(fromFile);
            Document writeTo = FileDocumentManager.getInstance().getDocument(toFile);
            writeTo.setText(readFrom.getText());
        } else {
            LOG.error("Error in copying text from \"" + from + "\" to \"" + to + "\""
                    + "\nfromFile = " + fromFile
                    + "\ntoFile = " + toFile);
        }
    }

    private boolean canReformat(@NotNull VirtualFile file, @NotNull Project project) {
        return file.isInLocalFileSystem()
                && isWritable(file, project)
                && fileTypeIsSupported(file);
    }

    private static boolean isWritable(@NotNull VirtualFile file, @NotNull Project project) {
        return !ReadonlyStatusHandler.getInstance(project).ensureFilesWritable(file).hasReadonlyFiles();
    }

    private boolean fileTypeIsSupported(@NotNull VirtualFile file) {
        return replacement.supportsFileType(ioFile(file));
    }

    private static boolean wholeFile(@NotNull PsiFile file, int startOffset, int endOffset) {
        return startOffset == 0
                && endOffset == file.getTextRange().getEndOffset();
    }

    private static void save(@NotNull VirtualFile file) {
        FileDocumentManager.getInstance().saveDocument(
                FileDocumentManager.getInstance().getDocument(file));
    }

    @NotNull
    private static File ioFile(@NotNull VirtualFile file) {
        return new File(file.getPath());
    }
}
