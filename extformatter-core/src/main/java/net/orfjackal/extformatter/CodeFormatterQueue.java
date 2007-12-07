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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Esko Luontola
 * @since 7.12.2007
 */
public class CodeFormatterQueue implements CodeFormatter {

    @NotNull private final CodeFormatter formatter;
    @NotNull private final List<File> fileQueue = new ArrayList<File>();

    public CodeFormatterQueue(@NotNull CodeFormatter formatter) {
        this.formatter = formatter;
    }

    public boolean supportsFileType(@NotNull File file) {
        return formatter.supportsFileType(file);
    }

    public boolean supportsReformatFile() {
        return true;
    }

    public void reformatFile(@NotNull File file) {
        assert supportsFileType(file);
        fileQueue.add(file);
    }

    public boolean isEmpty() {
        return fileQueue.isEmpty();
    }

    public void flush() {
        // TODO: slice into smaller methods
        // TODO: formatter.supportsReformatFilesInDirectoryRecursively()
        if (fileQueue.isEmpty()) {
            return;
        }
        if (formatter.supportsReformatFilesInDirectory()) {
            Map<File, List<File>> groups = groupByCommonDirectory(fileQueue);
            for (Map.Entry<File, List<File>> group : groups.entrySet()) {
                File directory = group.getKey();
                List<File> files = group.getValue();
                if (noOthersInTheSameDirectory(files)) {
                    formatter.reformatFilesInDirectory(directory);
                    fileQueue.removeAll(files);
                }
            }
        }
        if (formatter.supportsReformatFiles()) {
            File[] files = fileQueue.toArray(new File[fileQueue.size()]);
            formatter.reformatFiles(files);
            fileQueue.clear();
        }
        if (formatter.supportsReformatFile()) {
            for (File file : fileQueue) {
                formatter.reformatFile(file);
            }
            fileQueue.clear();
        }
        if (!fileQueue.isEmpty()) {
            String notReformatted = "";
            for (File file : fileQueue) {
                notReformatted += "\n" + file;
            }
            fileQueue.clear();
            throw new IllegalStateException("The following files could not be reformatted: " + notReformatted);
        }
    }

    @NotNull
    private static Map<File, List<File>> groupByCommonDirectory(@NotNull List<File> fileList) {
        Map<File, List<File>> groups = new HashMap<File, List<File>>();
        for (File file : fileList) {
            File directory = file.getParentFile();
            List<File> files = groups.get(directory);
            if (files == null) {
                files = new ArrayList<File>();
                groups.put(directory, files);
            }
            files.add(file);
        }
        return groups;
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

    // Unsupported operations

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
