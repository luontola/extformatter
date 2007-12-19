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

import com.intellij.lang.ASTNode;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.fileTypes.FileType;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.*;
import com.intellij.psi.codeStyle.Indent;
import com.intellij.psi.codeStyle.SuggestedNameInfo;
import com.intellij.psi.codeStyle.VariableKind;
import com.intellij.psi.impl.source.codeStyle.CodeStyleManagerEx;
import com.intellij.util.IncorrectOperationException;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Wrapper for intercepting the method calls to a {@link CodeStyleManagerEx} instance.
 *
 * @author Esko Luontola
 * @since 2.12.2007
 */
@SuppressWarnings({"deprecation"})
public class DelegatingCodeStyleManager extends CodeStyleManagerEx {
    /* HACK: This class must be a subclass of CodeStyleManagerEx (part of private API)
    and not CodeStyleManager (part of Open API), because IDEA tries to cast it to
    CodeStyleManagerEx in some parts of the code. If this class were to extend CodeStyleManager, 
    the following exception would happen often:

    java.lang.ClassCastException: net.orfjackal.extformatter.plugin.DelegatingCodeStyleManager cannot be cast to com.intellij.psi.impl.source.codeStyle.CodeStyleManagerEx
        at com.intellij.codeInsight.daemon.impl.PostHighlightingPass.<init>(PostHighlightingPass.java:24)
        at com.intellij.codeInsight.daemon.impl.PostHighlightingPass.<init>(PostHighlightingPass.java:258)
        at com.intellij.openapi.vcs.impl.AbstractVcsHelperImpl.a(AbstractVcsHelperImpl.java:111)
        at com.intellij.openapi.vcs.impl.AbstractVcsHelperImpl.access$500(AbstractVcsHelperImpl.java:29)
        at com.intellij.openapi.vcs.impl.AbstractVcsHelperImpl$8.run(AbstractVcsHelperImpl.java:14)
        at com.intellij.openapi.progress.impl.ProgressManagerImpl$2.run(ProgressManagerImpl.java:6)
        ...

    P.S. It's not possible to extend CodeStyleManagerImpl (the actual class),
    because at least I don't know how to get a StatisticsManagerEx which could
    be given to CodeStyleManagerImpl's constructor.
     */

    @NotNull private final CodeStyleManagerEx target;

    public DelegatingCodeStyleManager(@NotNull CodeStyleManagerEx target) {
        this.target = target;
    }

    @NotNull
    public CodeStyleManagerEx getTarget() {
        return target;
    }

    // Generated delegate methods

    public boolean addImport(@NotNull PsiJavaFile psiJavaFile, @NotNull PsiClass psiClass) {
        return target.addImport(psiJavaFile, psiClass);
    }

    public PsiElement shortenClassReferences(@NotNull PsiElement psiElement, int i) throws IncorrectOperationException {
        return target.shortenClassReferences(psiElement, i);
    }

    @NotNull
    public String getPrefixByVariableKind(VariableKind variableKind) {
        return target.getPrefixByVariableKind(variableKind);
    }

    @NotNull
    public String getSuffixByVariableKind(VariableKind variableKind) {
        return target.getSuffixByVariableKind(variableKind);
    }

    public int findEntryIndex(@NotNull PsiImportStatementBase psiImportStatementBase) {
        return target.findEntryIndex(psiImportStatementBase);
    }

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

    public PsiElement shortenClassReferences(@NotNull PsiElement element) throws IncorrectOperationException {
        return target.shortenClassReferences(element);
    }

    public void shortenClassReferences(@NotNull PsiElement element, int startOffset, int endOffset) throws IncorrectOperationException {
        target.shortenClassReferences(element, startOffset, endOffset);
    }

    public void optimizeImports(@NotNull PsiFile file) throws IncorrectOperationException {
        target.optimizeImports(file);
    }

    public PsiImportList prepareOptimizeImportsResult(@NotNull PsiJavaFile file) {
        return target.prepareOptimizeImportsResult(file);
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

    public String getLineIndent(@NotNull PsiFile file, int offset) {
        return target.getLineIndent(file, offset);
    }

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

    public PsiElement insertNewLineIndentMarker(@NotNull PsiFile file, int offset) throws IncorrectOperationException {
        return target.insertNewLineIndentMarker(file, offset);
    }

    public VariableKind getVariableKind(@NotNull PsiVariable variable) {
        return target.getVariableKind(variable);
    }

    public SuggestedNameInfo suggestVariableName(@NotNull VariableKind kind, @Nullable String propertyName, @Nullable PsiExpression expr, @Nullable PsiType type) {
        return target.suggestVariableName(kind, propertyName, expr, type);
    }

    public String variableNameToPropertyName(@NonNls String name, VariableKind variableKind) {
        return target.variableNameToPropertyName(name, variableKind);
    }

    public String propertyNameToVariableName(@NonNls String propertyName, VariableKind variableKind) {
        return target.propertyNameToVariableName(propertyName, variableKind);
    }

    public String suggestUniqueVariableName(@NonNls String baseName, PsiElement place, boolean lookForward) {
        return target.suggestUniqueVariableName(baseName, place, lookForward);
    }

    @NotNull
    public SuggestedNameInfo suggestUniqueVariableName(@NotNull SuggestedNameInfo baseNameInfo, PsiElement place, boolean lookForward) {
        return target.suggestUniqueVariableName(baseNameInfo, place, lookForward);
    }

    public PsiElement qualifyClassReferences(@NotNull PsiElement element) {
        return target.qualifyClassReferences(element);
    }

    public void removeRedundantImports(@NotNull PsiJavaFile file) throws IncorrectOperationException {
        target.removeRedundantImports(file);
    }

    public void reformatNewlyAddedElement(@NotNull ASTNode block, @NotNull ASTNode addedElement) throws IncorrectOperationException {
        target.reformatNewlyAddedElement(block, addedElement);
    }
}
