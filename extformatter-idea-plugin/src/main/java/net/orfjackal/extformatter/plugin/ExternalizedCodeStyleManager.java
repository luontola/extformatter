/*
 * External Code Formatter
 * Copyright (c) 2007 Esko Luontola, www.orfjackal.net
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
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiFile;
import com.intellij.psi.codeStyle.CodeStyleManager;
import com.intellij.psi.impl.source.codeStyle.CodeStyleManagerEx;
import com.intellij.util.IncorrectOperationException;
import net.orfjackal.extformatter.CodeFormatter;
import net.orfjackal.extformatter.Messages;
import net.orfjackal.extformatter.ReformatQueue;
import net.orfjackal.extformatter.plugin.util.CommandRunner;
import net.orfjackal.extformatter.plugin.util.WriteActionRunner;
import org.jetbrains.annotations.NotNull;

import java.io.File;

/**
 * Intercepts the calls to {@link CodeStyleManager#reformatText(com.intellij.psi.PsiFile, int, int)} and redirects
 * them to a {@link CodeFormatter} if the formatter supports reformatting that file. Otherwise falls back to using
 * the original {@link CodeStyleManagerEx} instance.
 *
 * @author Esko Luontola
 * @since 3.12.2007
 */
public class ExternalizedCodeStyleManager extends DelegatingCodeStyleManager {

    private static final Logger LOG = Logger.getInstance(ExternalizedCodeStyleManager.class.getName());

    @NotNull private final ReformatQueue replacement;

    public ExternalizedCodeStyleManager(@NotNull CodeStyleManagerEx original, @NotNull ReformatQueue replacement) {
        super(original);
        this.replacement = replacement;
    }

    public void reformatText(@NotNull PsiFile psiFile, int startOffset, int endOffset) throws IncorrectOperationException {
        VirtualFile file = psiFile.getVirtualFile();
        Project project = psiFile.getProject();
        if (file != null
                && canReformat(file)
                && wholeFile(psiFile, startOffset, endOffset)) {
            queueReformatOf(file, project);
        } else {
            super.reformatText(psiFile, startOffset, endOffset);
        }
    }

    /**
     * The formatting will be executed after IDEA has called the 'reformatText' method for all files
     * which should be reformatted in one go. Starting the formatter for each file would be very slow,
     * especially in the case of EclipseCodeFormatter, so that's why a ReformatQueue must be used here.
     */
    private void queueReformatOf(final @NotNull VirtualFile file, @NotNull Project project) {
        LOG.info("Queue for reformat: " + file.getPath());
        save(file);
        replacement.reformatOne(ioFile(file));
        ApplicationManager.getApplication().invokeLater(flushAndRefresh(file, project));
    }

    @NotNull
    private Runnable flushAndRefresh(final @NotNull VirtualFile file, @NotNull Project project) {
        Runnable flushAndRefresh = new Runnable() {
            public void run() {
                try {
                    LOG.info("Flushing queue");
                    replacement.flush();
                } finally {
                    LOG.info("Refresh reformatted file: " + file.getPath());
                    file.refresh(false, false);
                }
            }
        };
        // TODO: does not support undo (Ctrl+Z)
        // TODO: even if undo would work, might not support undoing a groups of files with one command
        // IDEA requires this to be executed as a command and a write action
        return new CommandRunner(project, new WriteActionRunner(flushAndRefresh),
                Messages.message("command.reformatCode"), null, UndoConfirmationPolicy.REQUEST_CONFIRMATION);
    }

    private boolean canReformat(@NotNull VirtualFile file) {
        return file.isInLocalFileSystem()
                && file.isWritable()
                && fileTypeIsSupported(file);
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
