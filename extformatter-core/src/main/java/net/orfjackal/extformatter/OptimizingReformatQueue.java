/*
 * External Code Formatter
 * Copyright (c) 2007-2009  Esko Luontola, www.orfjackal.net
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

import net.orfjackal.extformatter.util.*;
import org.jetbrains.annotations.*;

import java.io.File;
import java.util.*;

/**
 * Uses as few reformat method calls as possible to reformat all the queued files.
 *
 * @author Esko Luontola
 * @since 7.12.2007
 */
public class OptimizingReformatQueue implements ReformatQueue {

    @NotNull private final CodeFormatter formatter;
    @NotNull private final List<File> fileQueue = new ArrayList<File>();

    public OptimizingReformatQueue(@NotNull CodeFormatter formatter) {
        this.formatter = formatter;
    }

    public boolean supportsFileType(@NotNull File file) {
        return formatter.supportsFileType(file);
    }

    public boolean supportsReformatOne() {
        return true;
    }

    public void reformatOne(@NotNull File file) {
        assert supportsFileType(file);
        fileQueue.add(file);
    }

    public boolean isEmpty() {
        return fileQueue.isEmpty();
    }

    public void flush() {
        fileQueue.removeAll(useReformatMany());
        fileQueue.removeAll(useReformatRecursively());
        fileQueue.removeAll(useReformatDirectory());
        fileQueue.removeAll(useReformatOne());
        mustBeEmpty(fileQueue);
    }

    @NotNull
    private List<File> useReformatOne() {
        List<File> reformatted = new ArrayList<File>();
        if (formatter.supportsReformatOne()) {
            for (File file : fileQueue) {
                formatter.reformatOne(file);
                reformatted.add(file);
            }
        }
        return reformatted;
    }

    @NotNull
    private List<File> useReformatMany() {
        List<File> reformatted = new ArrayList<File>();
        if (formatter.supportsReformatMany() && fileQueue.size() > 0) {
            formatter.reformatMany(toArray(fileQueue));
            reformatted.addAll(fileQueue);
        }
        return reformatted;
    }

    @NotNull
    private List<File> useReformatDirectory() {
        List<File> reformatted = new ArrayList<File>();
        if (formatter.supportsReformatDirectory()) {
            Map<File, List<File>> groups = groupByDirectory(fileQueue);
            for (Map.Entry<File, List<File>> group : groups.entrySet()) {
                File directory = group.getKey();
                List<File> files = group.getValue();
                if (noOthersInTheSameDirectory(directory, files)) {
                    formatter.reformatDirectory(directory);
                    reformatted.addAll(files);
                }
            }
        }
        return reformatted;
    }

    @NotNull
    private List<File> useReformatRecursively() {
        List<File> reformatted = new ArrayList<File>();
        if (formatter.supportsReformatRecursively()) {
            File directory = commonParentDirectory(fileQueue);
            if (directory != null && noOthersInTheSameDirectoryTree(directory, fileQueue)) {
                formatter.reformatRecursively(directory);
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

    private boolean noOthersInTheSameDirectory(@NotNull File directory, @NotNull List<File> files) {
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
        // Assumes that the common parent directory contains some files.
        // Otherwise there should be some check that this does not go
        // up to the root directory and possibly by accident reformat
        // files outside the project source directories.
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

    @NotNull
    private static File[] toArray(@NotNull List<File> files) {
        return files.toArray(new File[files.size()]);
    }

    // Unsupported operations

    public boolean supportsReformatMany() {
        return false;
    }

    public void reformatMany(@NotNull File... files) {
        throw new UnsupportedOperationException();
    }

    public boolean supportsReformatDirectory() {
        return false;
    }

    public void reformatDirectory(@NotNull File directory) {
        throw new UnsupportedOperationException();
    }

    public boolean supportsReformatRecursively() {
        return false;
    }

    public void reformatRecursively(@NotNull File directory) {
        throw new UnsupportedOperationException();
    }
}
