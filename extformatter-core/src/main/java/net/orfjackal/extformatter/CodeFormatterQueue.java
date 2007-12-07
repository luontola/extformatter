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

package net.orfjackal.extformatter;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Esko Luontola
 * @since 7.12.2007
 */
public class CodeFormatterQueue implements CodeFormatter {

    private final CodeFormatter formatter;

    private final List<File> reformatFile = new ArrayList<File>();

    public CodeFormatterQueue(@NotNull CodeFormatter formatter) {
        this.formatter = formatter;
    }

    public void flush() {
        for (File file : reformatFile) {
            formatter.reformatFile(file);
        }
    }

    public void reformatFile(@NotNull File file) {
        reformatFile.add(file);
    }

    public void reformatFiles(@NotNull File... files) {
    }

    public void reformatFilesInDirectory(@NotNull File directory) {
    }

    public void reformatFilesInDirectoryRecursively(@NotNull File directory) {
    }

    // Generated delegate methods

    public boolean supportsFileType(@NotNull File file) {
        return formatter.supportsFileType(file);
    }

    public boolean supportsReformatFile() {
        return formatter.supportsReformatFile();
    }

    public boolean supportsReformatFiles() {
        return formatter.supportsReformatFiles();
    }

    public boolean supportsReformatFilesInDirectory() {
        return formatter.supportsReformatFilesInDirectory();
    }

    public boolean supportsReformatFilesInDirectoryRecursively() {
        return formatter.supportsReformatFilesInDirectoryRecursively();
    }
}
