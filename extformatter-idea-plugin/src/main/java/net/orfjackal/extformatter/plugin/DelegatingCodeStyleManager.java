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

import com.intellij.lang.ASTNode;
import com.intellij.openapi.editor.*;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.*;
import com.intellij.psi.codeStyle.*;
import com.intellij.util.IncorrectOperationException;
import org.jetbrains.annotations.*;

/**
 * Wrapper for intercepting the method calls to a {@link CodeStyleManager} instance.
 *
 * @author Esko Luontola
 * @since 2.12.2007
 */
@SuppressWarnings({"deprecation"})
public class DelegatingCodeStyleManager extends CodeStyleManager {

    @NotNull private final CodeStyleManager target;

    public DelegatingCodeStyleManager(@NotNull CodeStyleManager target) {
        this.target = target;
    }

    @NotNull
    public CodeStyleManager getTarget() {
        return target;
    }

    // Generated delegate methods

    @NotNull
    public Project getProject() {
        return target.getProject();
    }

    @NotNull
    public PsiElement reformat(@NotNull PsiElement element) throws IncorrectOperationException {
        return target.reformat(element);
    }

    @NotNull
    public PsiElement reformat(@NotNull PsiElement element, boolean canChangeWhiteSpacesOnly) throws IncorrectOperationException {
        return target.reformat(element, canChangeWhiteSpacesOnly);
    }

    public PsiElement reformatRange(@NotNull PsiElement element, int startOffset, int endOffset) throws IncorrectOperationException {
        return target.reformatRange(element, startOffset, endOffset);
    }

    public PsiElement reformatRange(@NotNull PsiElement element, int startOffset, int endOffset, boolean canChangeWhiteSpacesOnly) throws IncorrectOperationException {
        return target.reformatRange(element, startOffset, endOffset, canChangeWhiteSpacesOnly);
    }

    public void reformatText(@NotNull PsiFile element, int startOffset, int endOffset) throws IncorrectOperationException {
        target.reformatText(element, startOffset, endOffset);
    }

    public void adjustLineIndent(@NotNull PsiFile file, TextRange rangeToAdjust) throws IncorrectOperationException {
        target.adjustLineIndent(file, rangeToAdjust);
    }

    public int adjustLineIndent(@NotNull PsiFile file, int offset) throws IncorrectOperationException {
        return target.adjustLineIndent(file, offset);
    }

    public int adjustLineIndent(@NotNull Document document, int offset) {
        return target.adjustLineIndent(document, offset);
    }

    public boolean isLineToBeIndented(@NotNull PsiFile file, int offset) {
        return target.isLineToBeIndented(file, offset);
    }

    @Nullable
    public String getLineIndent(@NotNull PsiFile file, int offset) {
        return target.getLineIndent(file, offset);
    }

    @Nullable
    public String getLineIndent(@NotNull Editor editor) {
        return target.getLineIndent(editor);
    }

    public Indent getIndent(String text, FileType fileType) {
        return target.getIndent(text, fileType);
    }

    public String fillIndent(Indent indent, FileType fileType) {
        return target.fillIndent(indent, fileType);
    }

    public Indent zeroIndent() {
        return target.zeroIndent();
    }

    @Nullable
    public PsiElement insertNewLineIndentMarker(@NotNull PsiFile file, int offset) throws IncorrectOperationException {
        return target.insertNewLineIndentMarker(file, offset);
    }

    public void reformatNewlyAddedElement(@NotNull ASTNode block, @NotNull ASTNode addedElement) throws IncorrectOperationException {
        target.reformatNewlyAddedElement(block, addedElement);
    }
}
