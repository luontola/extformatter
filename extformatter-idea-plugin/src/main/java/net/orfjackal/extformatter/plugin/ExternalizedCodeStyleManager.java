
// ExternalizedCodeStyleManager.java --
//
// ExternalizedCodeStyleManager.java is part of ElectricCommander.
//
// Copyright (c) 2005-2012 Electric Cloud, Inc.
// All rights reserved.
//

package net.orfjackal.extformatter.plugin;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

import net.orfjackal.extformatter.AdaptiveCodeFormatter;
import net.orfjackal.extformatter.CodeFormatter;
import net.orfjackal.extformatter.OptimizingReformatQueue;
import net.orfjackal.extformatter.util.FileUtil;
import net.orfjackal.extformatter.util.TempFileManager;

import org.jetbrains.annotations.NotNull;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.TextRange;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.ReadonlyStatusHandler;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiFileSystemItem;
import com.intellij.psi.codeStyle.CodeStyleManager;
import com.intellij.psi.util.PsiUtilBase;
import com.intellij.util.IncorrectOperationException;

/**
 * Intercepts the calls to {@link CodeStyleManager#reformatText} and redirects
 * them to a {@link CodeFormatter} if the formatter supports reformatting that
 * file. Otherwise falls back to using the original {@link CodeStyleManager}
 * instance.
 *
 * @author  Esko Luontola
 * @since   3.12.2007
 */
@SuppressWarnings("HardcodedLineSeparator")
public class ExternalizedCodeStyleManager
    extends DelegatingCodeStyleManager
{

    //~ Static fields/initializers ---------------------------------------------

    private static final Logger LOG = Logger.getInstance(
            ExternalizedCodeStyleManager.class.getName());

    //~ Instance fields --------------------------------------------------------

    @NotNull private final CodeFormatter m_replacement;

    //~ Constructors -----------------------------------------------------------

    public ExternalizedCodeStyleManager(
            @NotNull CodeStyleManager original,
            @NotNull CodeFormatter    replacement)
    {
        super(original);
        m_replacement = replacement;
    }

    //~ Methods ----------------------------------------------------------------

    @NotNull @Override public PsiElement reformat(@NotNull PsiElement element)
        throws IncorrectOperationException
    {

        if (element instanceof PsiFile) {
            PsiFileSystemItem psiFile = (PsiFile) element;
            VirtualFile       file    = psiFile.getVirtualFile();
            Project           project = psiFile.getProject();

            if (file != null && canReformat(file, project)
                    && wholeFile(psiFile, 0,
                        psiFile.getTextRange()
                           .getEndOffset())) {
                unblockDocument(psiFile, project);
                reformatWithUndoSupport(Collections.singletonList(file));
            }

            return element;
        }
        else {
            return super.reformat(element);
        }
    }

    @Override public void reformatText(
            @NotNull PsiFile               psiFile,
            @NotNull Collection<TextRange> textRanges)
        throws IncorrectOperationException
    {
        VirtualFile file    = psiFile.getVirtualFile();
        Project     project = psiFile.getProject();

        if (file != null && canReformat(file, project)
                && wholeFile(psiFile, 0,
                    psiFile.getTextRange()
                       .getEndOffset())) {
            unblockDocument(psiFile, project);
            reformatWithUndoSupport(Collections.singletonList(file));
        }
        else {
            super.reformatText(psiFile, textRanges);
        }
    }

    @Override public void reformatText(
            @NotNull PsiFile element,
            int              startOffset,
            int              endOffset)
        throws IncorrectOperationException
    {
        VirtualFile file    = element.getVirtualFile();
        Project     project = element.getProject();

        if (file != null && canReformat(file, project)
                && wholeFile(element, startOffset, endOffset)) {
            unblockDocument(element, project);
            reformatWithUndoSupport(Collections.singletonList(file));
        }
        else {
            super.reformatText(element, startOffset, endOffset);
        }
    }

    private boolean canReformat(
            @NotNull VirtualFile file,
            @NotNull Project     project)
    {
        return file.isInLocalFileSystem() && isWritable(file, project)
            && fileTypeIsSupported(file);
    }

    private boolean fileTypeIsSupported(@NotNull VirtualFile file)
    {
        return m_replacement.supportsFileType(ioFile(file));
    }

    private void reformatOptimally(@NotNull File[] files)
    {
        OptimizingReformatQueue optimizer = new OptimizingReformatQueue(
                m_replacement);

        new AdaptiveCodeFormatter(optimizer).reformatMany(files);
        optimizer.flush();
    }

    /**
     * HACK: We can't reformat the original files and then use {@link
     * VirtualFile#refresh} so that IDEA would load the changes, because then it
     * would not be possible to use the Undo (Ctrl+Z) command. The solution that
     * is used here is to make temporary copies of all files, reformat them, and
     * finally copy the text contents from the temporary files to the original
     * files using the Document interface. (<a
     * href="http://www.intellij.net/forums/thread.jspa?threadID=271800&tstart=0">
     * See also</a>)
     *
     * @param  files
     */
    private void reformatWithUndoSupport(@NotNull Iterable<VirtualFile> files)
    {
        TempFileManager manager = tempFileManagerFor(files);

        reformatOptimally(manager.tempFiles());
        copyTextFromTempFiles(manager);
        manager.dispose();
    }

    //~ Methods ----------------------------------------------------------------

    private static void copyText(
            @NotNull File from,
            @NotNull File to)
    {
        VirtualFile toFile = LocalFileSystem.getInstance()
                                            .refreshAndFindFileByIoFile(to);

        if (toFile == null) {
            LOG.error("Error in copying text from \"" + from + "\" to \"" + to
                    + "\"" + "\nfromFile = " + from.getAbsolutePath()
                    + "\ntoFile = " + toFile);
        }
        else {
            Document writeTo = FileDocumentManager.getInstance()
                                                  .getDocument(toFile);

            assert writeTo != null;

//            PsiDocumentManager.getInstance(editor.getProject())
//                              .doPostponedOperationsAndUnblockDocument(writeTo);
            try {
                writeTo.setText(FileUtil.contentsOf(from));
            }
            catch (IOException e) {
                LOG.error("Error in copying text from \"" + from + "\" to \""
                        + to + "\"" + "\nfromFile = " + from.getAbsolutePath()
                        + "\ntoFile = " + toFile + ": " + e.getMessage());
            }
        }
    }

    private static void copyTextFromTempFiles(@NotNull TempFileManager manager)
    {

        for (Map.Entry<File, File> entry
                : manager.tempsToOriginals()
                         .entrySet()) {
            File temp     = entry.getKey();
            File original = entry.getValue();

            copyText(temp, original);
        }
    }

    @NotNull private static File ioFile(@NotNull VirtualFile file)
    {
        return new File(file.getPath());
    }

    private static void save(@NotNull VirtualFile file)
    {
        FileDocumentManager documentManager = FileDocumentManager.getInstance();
        Document            document        = documentManager.getDocument(file);

        assert document != null;
        documentManager.saveDocument(document);
    }

    @NotNull private static TempFileManager tempFileManagerFor(
            @NotNull Iterable<VirtualFile> files)
    {
        TempFileManager manager = new TempFileManager();

        for (VirtualFile file : files) {
            save(file);
            manager.add(ioFile(file));
        }

        return manager;
    }

    private static void unblockDocument(
            PsiElement psiFile,
            Project    project)
    {
        Editor editor = PsiUtilBase.findEditor(psiFile);

        if (editor != null) {
            PsiDocumentManager.getInstance(project)
                              .doPostponedOperationsAndUnblockDocument(
                                  editor.getDocument());
        }
    }

    private static boolean wholeFile(
            @NotNull PsiElement file,
            int                 startOffset,
            int                 endOffset)
    {
        return startOffset == 0
            && endOffset == file.getTextRange()
                                .getEndOffset();
    }

    private static boolean isWritable(
            @NotNull VirtualFile file,
            @NotNull Project     project)
    {
        return !ReadonlyStatusHandler.getInstance(project)
                                     .ensureFilesWritable(file)
                                     .hasReadonlyFiles();
    }
}
