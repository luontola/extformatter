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

import net.orfjackal.extformatter.util.Directories;
import net.orfjackal.extformatter.util.FilesSupportedBy;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
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
        fileQueue.removeAll(useReformatFiles());
        fileQueue.removeAll(useReformatFilesInDirectoryRecursively());
        fileQueue.removeAll(useReformatFilesInDirectory());
        fileQueue.removeAll(useReformatFile());
        mustBeEmpty(fileQueue);
    }

    private List<File> useReformatFile() {
        List<File> reformatted = new ArrayList<File>();
        if (formatter.supportsReformatFile()) {
            for (File file : fileQueue) {
                formatter.reformatFile(file);
                reformatted.add(file);
            }
        }
        return reformatted;
    }

    private List<File> useReformatFiles() {
        List<File> reformatted = new ArrayList<File>();
        if (formatter.supportsReformatFiles() && fileQueue.size() > 0) {
            formatter.reformatFiles(toArray(fileQueue));
            reformatted.addAll(fileQueue);
        }
        return reformatted;
    }

    private List<File> useReformatFilesInDirectory() {
        List<File> reformatted = new ArrayList<File>();
        if (formatter.supportsReformatFilesInDirectory()) {
            Map<File, List<File>> groups = groupByDirectory(fileQueue);
            for (Map.Entry<File, List<File>> group : groups.entrySet()) {
                File directory = group.getKey();
                List<File> files = group.getValue();
                if (noOthersInTheSameDirectory(directory, files)) {
                    formatter.reformatFilesInDirectory(directory);
                    reformatted.addAll(files);
                }
            }
        }
        return reformatted;
    }

    private List<File> useReformatFilesInDirectoryRecursively() {
        List<File> reformatted = new ArrayList<File>();
        if (formatter.supportsReformatFilesInDirectoryRecursively()) {
            File directory = commonParentDirectory(fileQueue);
            if (directory != null && noOthersInTheSameDirectoryTree(directory, fileQueue)) {
                formatter.reformatFilesInDirectoryRecursively(directory);
                reformatted.addAll(fileQueue);
            }
        }
        return reformatted;
    }

    private static void mustBeEmpty(@NotNull List<File> files) {
        if (!files.isEmpty()) {
            try {
                throw new IllegalStateException("The following files could not be reformatted: " + files);
            } finally {
                files.clear();
            }
        }
    }

    private boolean noOthersInTheSameDirectoryTree(@NotNull File directory, @NotNull List<File> files) {
        if (!noOthersInTheSameDirectory(directory, files)) {
            return false;
        }
        File[] subDirs = directory.listFiles(new Directories());
        for (File subDir : subDirs) {
            if (!noOthersInTheSameDirectoryTree(subDir, files)) {
                return false;
            }
        }
        return true;
    }

    private boolean noOthersInTheSameDirectory(File directory, @NotNull List<File> files) {
        File[] allFilesInDir = directory.listFiles(new FilesSupportedBy(this));
        for (File fileInDir : allFilesInDir) {
            if (!files.contains(fileInDir)) {
                return false;
            }
        }
        return true;
    }

    @Nullable
    private static File commonParentDirectory(@NotNull List<File> files) {
        File commonParent = null;
        for (File file : files) {
            if (commonParent == null) {
                commonParent = file.getParentFile();
            }
            if (isParent(file.getParentFile(), commonParent)) {
                commonParent = file.getParentFile();
            }
        }
        return commonParent;
    }

    private static boolean isParent(@NotNull File parent, @NotNull File child) {
        for (File dir = child; dir.getParentFile() != null; dir = dir.getParentFile()) {
            if (dir.equals(parent)) {
                return true;
            }
        }
        return false;
    }

    @NotNull
    private static Map<File, List<File>> groupByDirectory(@NotNull List<File> files) {
        Map<File, List<File>> groups = new HashMap<File, List<File>>();
        for (File file : files) {
            File directory = file.getParentFile();
            putToList(groups, directory, file);
        }
        return groups;
    }

    private static <T> void putToList(@NotNull Map<T, List<T>> map, @NotNull T key, @NotNull T value) {
        List<T> list = map.get(key);
        if (list == null) {
            list = new ArrayList<T>();
            map.put(key, list);
        }
        list.add(value);
    }

    private static File[] toArray(List<File> files) {
        return files.toArray(new File[files.size()]);
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
