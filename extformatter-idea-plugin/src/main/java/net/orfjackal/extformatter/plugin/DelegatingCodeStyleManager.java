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
 * @author Esko Luontola
 * @since 2.12.2007
 */
@SuppressWarnings({"deprecation"})
public class DelegatingCodeStyleManager extends CodeStyleManagerEx {
    /* HACK:
    This class must be a subclass of CodeStyleManagerEx (part of private API)
    and not CodeStyleManager (part of OpenAPI), because IDEA tries to cast it
    to CodeStyleManagerEx in some parts of the code. If this class were to
    extend CodeStyleManager, the following exception would happen often:

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

    // DELEGATED METHODS

    @NotNull
    public Project getProject() {
        System.out.println("DelegatingCodeStyleManager.getProject");
        return target.getProject();
    }

    @NotNull
    public PsiElement reformat(@NotNull PsiElement element) throws IncorrectOperationException {
        System.out.println("DelegatingCodeStyleManager.reformat");
        return target.reformat(element);
    }

    @NotNull
    public PsiElement reformat(@NotNull PsiElement element, boolean canChangeWhiteSpacesOnly) throws IncorrectOperationException {
        System.out.println("DelegatingCodeStyleManager.reformat");
        return target.reformat(element, canChangeWhiteSpacesOnly);
    }

    public PsiElement reformatRange(@NotNull PsiElement element, int startOffset, int endOffset) throws IncorrectOperationException {
        System.out.println("DelegatingCodeStyleManager.reformatRange");
        return target.reformatRange(element, startOffset, endOffset);
    }

    public PsiElement reformatRange(@NotNull PsiElement element, int startOffset, int endOffset, boolean canChangeWhiteSpacesOnly) throws IncorrectOperationException {
        System.out.println("DelegatingCodeStyleManager.reformatRange");
        return target.reformatRange(element, startOffset, endOffset, canChangeWhiteSpacesOnly);
    }

    public void reformatText(@NotNull PsiFile element, int startOffset, int endOffset) throws IncorrectOperationException {
        System.out.println("DelegatingCodeStyleManager.reformatText");
        System.out.println("element = " + element);
        System.out.println("startOffset = " + startOffset);
        System.out.println("endOffset = " + endOffset);
        target.reformatText(element, startOffset, endOffset);
    }

    public PsiElement shortenClassReferences(@NotNull PsiElement element) throws IncorrectOperationException {
        System.out.println("DelegatingCodeStyleManager.shortenClassReferences");
        return target.shortenClassReferences(element);
    }

    public void shortenClassReferences(@NotNull PsiElement element, int startOffset, int endOffset) throws IncorrectOperationException {
        System.out.println("DelegatingCodeStyleManager.shortenClassReferences");
        target.shortenClassReferences(element, startOffset, endOffset);
    }

    public void optimizeImports(@NotNull PsiFile file) throws IncorrectOperationException {
        System.out.println("DelegatingCodeStyleManager.optimizeImports");
        target.optimizeImports(file);
    }

    public PsiImportList prepareOptimizeImportsResult(@NotNull PsiJavaFile file) {
        System.out.println("DelegatingCodeStyleManager.prepareOptimizeImportsResult");
        System.out.println("file = " + file);
        return target.prepareOptimizeImportsResult(file);
    }

    public void adjustLineIndent(@NotNull PsiFile file, TextRange rangeToAdjust) throws IncorrectOperationException {
        System.out.println("DelegatingCodeStyleManager.adjustLineIndent");
        target.adjustLineIndent(file, rangeToAdjust);
    }

    public int adjustLineIndent(@NotNull PsiFile file, int offset) throws IncorrectOperationException {
        System.out.println("DelegatingCodeStyleManager.adjustLineIndent");
        return target.adjustLineIndent(file, offset);
    }

    public int adjustLineIndent(@NotNull Document document, int offset) {
        System.out.println("DelegatingCodeStyleManager.adjustLineIndent");
        return target.adjustLineIndent(document, offset);
    }

    public boolean isLineToBeIndented(@NotNull PsiFile file, int offset) {
        System.out.println("DelegatingCodeStyleManager.isLineToBeIndented");
        return target.isLineToBeIndented(file, offset);
    }

    public String getLineIndent(@NotNull PsiFile file, int offset) {
        System.out.println("DelegatingCodeStyleManager.getLineIndent");
        return target.getLineIndent(file, offset);
    }

    public String getLineIndent(@NotNull Editor editor) {
        System.out.println("DelegatingCodeStyleManager.getLineIndent");
        return target.getLineIndent(editor);
    }

    public Indent getIndent(String text, FileType fileType) {
        System.out.println("DelegatingCodeStyleManager.getIndent");
        return target.getIndent(text, fileType);
    }

    public String fillIndent(Indent indent, FileType fileType) {
        System.out.println("DelegatingCodeStyleManager.fillIndent");
        return target.fillIndent(indent, fileType);
    }

    public Indent zeroIndent() {
        System.out.println("DelegatingCodeStyleManager.zeroIndent");
        return target.zeroIndent();
    }

    public PsiElement insertNewLineIndentMarker(@NotNull PsiFile file, int offset) throws IncorrectOperationException {
        System.out.println("DelegatingCodeStyleManager.insertNewLineIndentMarker");
        return target.insertNewLineIndentMarker(file, offset);
    }

    public VariableKind getVariableKind(@NotNull PsiVariable variable) {
        System.out.println("DelegatingCodeStyleManager.getVariableKind");
        return target.getVariableKind(variable);
    }

    public SuggestedNameInfo suggestVariableName(@NotNull VariableKind kind, @Nullable String propertyName, @Nullable PsiExpression expr, @Nullable PsiType type) {
        System.out.println("DelegatingCodeStyleManager.suggestVariableName");
        return target.suggestVariableName(kind, propertyName, expr, type);
    }

    public String variableNameToPropertyName(@NonNls String name, VariableKind variableKind) {
        System.out.println("DelegatingCodeStyleManager.variableNameToPropertyName");
        return target.variableNameToPropertyName(name, variableKind);
    }

    public String propertyNameToVariableName(@NonNls String propertyName, VariableKind variableKind) {
        System.out.println("DelegatingCodeStyleManager.propertyNameToVariableName");
        return target.propertyNameToVariableName(propertyName, variableKind);
    }

    public String suggestUniqueVariableName(@NonNls String baseName, PsiElement place, boolean lookForward) {
        System.out.println("DelegatingCodeStyleManager.suggestUniqueVariableName");
        return target.suggestUniqueVariableName(baseName, place, lookForward);
    }

    @NotNull
    public SuggestedNameInfo suggestUniqueVariableName(@NotNull SuggestedNameInfo baseNameInfo, PsiElement place, boolean lookForward) {
        System.out.println("DelegatingCodeStyleManager.suggestUniqueVariableName");
        return target.suggestUniqueVariableName(baseNameInfo, place, lookForward);
    }

    public PsiElement qualifyClassReferences(@NotNull PsiElement element) {
        System.out.println("DelegatingCodeStyleManager.qualifyClassReferences");
        return target.qualifyClassReferences(element);
    }

    public void removeRedundantImports(@NotNull PsiJavaFile file) throws IncorrectOperationException {
        System.out.println("DelegatingCodeStyleManager.removeRedundantImports");
        target.removeRedundantImports(file);
    }

    public void reformatNewlyAddedElement(@NotNull ASTNode block, @NotNull ASTNode addedElement) throws IncorrectOperationException {
        System.out.println("DelegatingCodeStyleManager.reformatNewlyAddedElement");
        target.reformatNewlyAddedElement(block, addedElement);
    }

    public boolean addImport(@NotNull PsiJavaFile psiJavaFile, @NotNull PsiClass psiClass) {
        System.out.println("DelegatingCodeStyleManager.addImport");
        return target.addImport(psiJavaFile, psiClass);
    }

    public PsiElement shortenClassReferences(@NotNull PsiElement psiElement, int i) throws IncorrectOperationException {
        System.out.println("DelegatingCodeStyleManager.shortenClassReferences");
        return target.shortenClassReferences(psiElement, i);
    }

    @NotNull
    public String getPrefixByVariableKind(VariableKind variableKind) {
        System.out.println("DelegatingCodeStyleManager.getPrefixByVariableKind");
        return target.getPrefixByVariableKind(variableKind);
    }

    @NotNull
    public String getSuffixByVariableKind(VariableKind variableKind) {
        System.out.println("DelegatingCodeStyleManager.getSuffixByVariableKind");
        return target.getSuffixByVariableKind(variableKind);
    }

    public int findEntryIndex(@NotNull PsiImportStatementBase psiImportStatementBase) {
        System.out.println("DelegatingCodeStyleManager.findEntryIndex");
        return target.findEntryIndex(psiImportStatementBase);
    }
}
