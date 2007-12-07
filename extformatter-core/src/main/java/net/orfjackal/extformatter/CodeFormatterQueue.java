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
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Esko Luontola
 * @since 7.12.2007
 */
public class CodeFormatterQueue implements CodeFormatter {

    @NotNull private final CodeFormatter formatter;
    @NotNull private final List<File> queue = new ArrayList<File>();

    public CodeFormatterQueue(@NotNull CodeFormatter formatter) {
        this.formatter = formatter;
    }

    public void flush() {
        if (queue.isEmpty()) {
            return;
        }
        if (formatter.supportsReformatFilesInDirectory()
                && allInTheSameDirectory(queue)
                && noOthersInTheSameDirectory(queue)) {
//            File directory = commonDirectory(queue);

        } else if (formatter.supportsReformatFiles()) {
            File[] files = queue.toArray(new File[queue.size()]);
            formatter.reformatFiles(files);
        } else {
            for (File file : queue) {
                formatter.reformatFile(file);
            }
        }
        queue.clear();
    }

    private boolean allInTheSameDirectory(@NotNull List<File> files) {
        File commonDirectory = null;
        for (File file : files) {
            File directory = file.getParentFile();
            if (commonDirectory == null) {
                commonDirectory = directory;
            }
            if (!directory.equals(commonDirectory)) {
                return false;
            }
        }
        return true;
    }

    private boolean noOthersInTheSameDirectory(@NotNull List<File> files) {
        File directory = files.get(0).getParentFile();
        File[] allFilesInDir = directory.listFiles(new FileFilter() {
            public boolean accept(File pathname) {
                return pathname.isFile() && supportsFileType(pathname);
            }
        });
        for (File fileInDir : allFilesInDir) {
            if (!files.contains(fileInDir)) {
                return false;
            }
        }
        return true;
    }

    public boolean supportsFileType(@NotNull File file) {
        return formatter.supportsFileType(file);
    }

    public boolean supportsReformatFile() {
        return true;
    }

    public void reformatFile(@NotNull File file) {
        assert supportsFileType(file);
        queue.add(file);
    }

    public boolean supportsReformatFiles() {
        return false;
    }

    public void reformatFiles(@NotNull File... files) {
        throw new UnsupportedOperationException();
    }

    public boolean supportsReformatFilesInDirectory() {
        return false;
    }

    public void reformatFilesInDirectory(@NotNull File directory) {
        throw new UnsupportedOperationException();
    }

    public boolean supportsReformatFilesInDirectoryRecursively() {
        return false;
    }

    public void reformatFilesInDirectoryRecursively(@NotNull File directory) {
        throw new UnsupportedOperationException();
    }
}
