
// DelegatingCodeStyleManager.java --
//
// DelegatingCodeStyleManager.java is part of ElectricCommander.
//
// Copyright (c) 2005-2011 Electric Cloud, Inc.
// All rights reserved.
//

package net.orfjackal.extformatter.plugin;

import java.util.Collection;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.intellij.lang.ASTNode;

import com.intellij.openapi.editor.Document;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Computable;
import com.intellij.openapi.util.TextRange;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.codeStyle.CodeStyleManager;
import com.intellij.psi.codeStyle.Indent;

import com.intellij.util.IncorrectOperationException;
import com.intellij.util.ThrowableRunnable;

/**
 * Wrapper for intercepting the method calls to a {@link CodeStyleManager}
 * instance.
 *
 * @author  Esko Luontola
 * @since   2.12.2007
 */
@SuppressWarnings({"deprecation"})
public class DelegatingCodeStyleManager
    extends CodeStyleManager
{

    //~ Instance fields --------------------------------------------------------

    @NotNull private final CodeStyleManager m_target;

    //~ Constructors -----------------------------------------------------------

    public DelegatingCodeStyleManager(@NotNull CodeStyleManager target)
    {
        m_target = target;
    }

    //~ Methods ----------------------------------------------------------------

    @Override public void adjustLineIndent(
            @NotNull PsiFile file,
            TextRange        rangeToAdjust)
        throws IncorrectOperationException
    {
        m_target.adjustLineIndent(file, rangeToAdjust);
    }

    @Override public int adjustLineIndent(
            @NotNull PsiFile file,
            int              offset)
        throws IncorrectOperationException
    {
        return m_target.adjustLineIndent(file, offset);
    }

    @Override public int adjustLineIndent(
            @NotNull Document document,
            int               offset)
    {
        return m_target.adjustLineIndent(document, offset);
    }

    @Override public String fillIndent(
            Indent   indent,
            FileType fileType)
    {
        return m_target.fillIndent(indent, fileType);
    }

    @Override public void performActionWithFormatterDisabled(Runnable runnable)
    {
        m_target.performActionWithFormatterDisabled(runnable);
    }

    @Override public <T extends Throwable> void performActionWithFormatterDisabled(
            ThrowableRunnable<T> tThrowableRunnable)
        throws T
    {
        m_target.performActionWithFormatterDisabled(tThrowableRunnable);
    }

    @Override public <T> T performActionWithFormatterDisabled(
            Computable<T> tComputable)
    {
        return m_target.performActionWithFormatterDisabled(tComputable);
    }

    @NotNull @Override public PsiElement reformat(@NotNull PsiElement element)
        throws IncorrectOperationException
    {
        return m_target.reformat(element);
    }

    @NotNull @Override public PsiElement reformat(
            @NotNull PsiElement element,
            boolean             canChangeWhiteSpacesOnly)
        throws IncorrectOperationException
    {
        return m_target.reformat(element, canChangeWhiteSpacesOnly);
    }

    @Override public void reformatNewlyAddedElement(
            @NotNull ASTNode block,
            @NotNull ASTNode addedElement)
        throws IncorrectOperationException
    {
        m_target.reformatNewlyAddedElement(block, addedElement);
    }

    @Override public PsiElement reformatRange(
            @NotNull PsiElement element,
            int                 startOffset,
            int                 endOffset)
        throws IncorrectOperationException
    {
        return m_target.reformatRange(element, startOffset, endOffset);
    }

    @Override public PsiElement reformatRange(
            @NotNull PsiElement element,
            int                 startOffset,
            int                 endOffset,
            boolean             canChangeWhiteSpacesOnly)
        throws IncorrectOperationException
    {
        return m_target.reformatRange(element, startOffset, endOffset,
            canChangeWhiteSpacesOnly);
    }

    @Override public void reformatText(
            @NotNull PsiFile element,
            int              startOffset,
            int              endOffset)
        throws IncorrectOperationException
    {
        m_target.reformatText(element, startOffset, endOffset);
    }

    @Override
    public void reformatText(
        @NotNull PsiFile psiFile,
        @NotNull Collection<TextRange> textRanges)
        throws IncorrectOperationException
    {
        m_target.reformatText(psiFile, textRanges);
    }

    @Override public Indent zeroIndent()
    {
        return m_target.zeroIndent();
    }

    @Override public Indent getIndent(
            String   text,
            FileType fileType)
    {
        return m_target.getIndent(text, fileType);
    }

    @Nullable @Override public String getLineIndent(
            @NotNull PsiFile file,
            int              offset)
    {
        return m_target.getLineIndent(file, offset);
    }

    @Override public String getLineIndent(
            @NotNull Document document,
            int               offset)
    {
        return m_target.getLineIndent(document, offset);
    }

    @NotNull @Override public Project getProject()
    {
        return m_target.getProject();
    }

    @NotNull public CodeStyleManager getTarget()
    {
        return m_target;
    }

    @Override public boolean isLineToBeIndented(
            @NotNull PsiFile file,
            int              offset)
    {
        return m_target.isLineToBeIndented(file, offset);
    }

    @Override public boolean isSequentialProcessingAllowed()
    {
        return m_target.isSequentialProcessingAllowed();
    }
}
