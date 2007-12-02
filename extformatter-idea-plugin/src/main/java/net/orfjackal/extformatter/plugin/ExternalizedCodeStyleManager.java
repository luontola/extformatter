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

import com.intellij.openapi.fileTypes.StdFileTypes;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiFile;
import com.intellij.psi.impl.source.codeStyle.CodeStyleManagerEx;
import com.intellij.util.IncorrectOperationException;
import net.orfjackal.extformatter.CodeFormatter;
import org.jetbrains.annotations.NotNull;

import java.io.File;

/**
 * @author Esko Luontola
 * @since 3.12.2007
 */
public class ExternalizedCodeStyleManager extends DelegatingCodeStyleManager {

    @NotNull private final CodeFormatter replacement;

    public ExternalizedCodeStyleManager(@NotNull CodeStyleManagerEx original, @NotNull CodeFormatter replacement) {
        super(original);
        this.replacement = replacement;
    }

    public void reformatText(@NotNull PsiFile file, int startOffset, int endOffset) throws IncorrectOperationException {
        if (wholeFile(file, startOffset, endOffset) && canReformat(file)) {
            // TODO: does not synchronize the file with filesystem
            replacement.reformatFile(physicalFile(file));
        } else {
            super.reformatText(file, startOffset, endOffset);
        }
    }

    private static boolean canReformat(@NotNull PsiFile file) {
        VirtualFile virtualFile = file.getVirtualFile();
        return virtualFile != null && canReformat(virtualFile);
    }

    private static boolean wholeFile(@NotNull PsiFile file, int startOffset, int endOffset) {
        return startOffset == 0
                && endOffset == file.getTextRange().getEndOffset();
    }

    @NotNull
    private static File physicalFile(@NotNull PsiFile psiFile) {
        VirtualFile virtualFile = psiFile.getVirtualFile();
        assert virtualFile != null;
        return new File(virtualFile.getPath());
    }

    public static boolean canReformat(@NotNull VirtualFile... selection) {
        return notEmpty(selection)
                && allAreInLocalFileSystem(selection)
                && allAreWritable(selection)
                && (allFileTypesAreSupported(selection) || isOneDirectory(selection));
    }

    private static boolean notEmpty(@NotNull VirtualFile[] selection) {
        return selection.length > 0;
    }

    private static boolean allAreInLocalFileSystem(@NotNull VirtualFile[] selection) {
        for (VirtualFile file : selection) {
            if (!file.isInLocalFileSystem()) {
                return false;
            }
        }
        return true;
    }

    private static boolean allAreWritable(@NotNull VirtualFile[] selection) {
        for (VirtualFile file : selection) {
            if (!file.isWritable()) {
                return false;
            }
        }
        return true;
    }

    private static boolean allFileTypesAreSupported(@NotNull VirtualFile[] selection) {
        for (VirtualFile file : selection) {
            // TODO: ask the formatter if it supports the file type
            if (!file.getFileType().equals(StdFileTypes.JAVA)) {
                return false;
            }
        }
        return true;
    }

    private static boolean isOneDirectory(@NotNull VirtualFile[] selection) {
        return selection.length == 1
                && selection[0].isDirectory();
    }
}
