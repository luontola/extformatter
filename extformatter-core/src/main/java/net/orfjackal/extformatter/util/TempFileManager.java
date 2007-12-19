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

package net.orfjackal.extformatter.util;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Creates temporary copies of files, maintaining a mapping between the original files and their copies.
 *
 * @author Esko Luontola
 * @since 18.12.2007
 */
public class TempFileManager {

    @NotNull private final File tempDirectory;
    @NotNull private final Map<File, File> tempsToOriginals = new HashMap<File, File>();

    public TempFileManager() {
        tempDirectory = createTempDirectory();
    }

    public void add(@NotNull File file) {
        try {
            File tempFile = copyToTemp(file);
            tempsToOriginals.put(tempFile, file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Original files in no particular order.
     */
    @NotNull
    public File[] originalFiles() {
        Collection<File> files = tempsToOriginals.values();
        return files.toArray(new File[files.size()]);
    }

    /**
     * Temporary files in no particular order.
     */
    @NotNull
    public File[] tempFiles() {
        Set<File> files = tempsToOriginals.keySet();
        return files.toArray(new File[files.size()]);
    }

    /**
     * Mapping from the temporary files (keys) to the original files (values).
     */
    @NotNull
    public Map<File, File> tempsToOriginals() {
        return new HashMap<File, File>(tempsToOriginals);
    }

    public void dispose() {
        FileUtil.deleteRecursively(tempDirectory);
        tempsToOriginals.clear();
    }

    @NotNull
    protected File tempDirectory() {
        return tempDirectory;
    }

    @NotNull
    protected File tempDirectory(int subdir) {
        return new File(tempDirectory, String.valueOf(subdir));
    }

    // Helper methods
    @NotNull
    private File copyToTemp(@NotNull File file) throws IOException {
        File tempFile = newTempFile(file);
        createParentDir(tempFile);
        FileUtil.copy(file, tempFile);
        return tempFile;
    }

    @NotNull
    private File newTempFile(@NotNull File file) {
        int i = 0;
        File tempFile;
        do {
            i++;
            tempFile = new File(tempDirectory(i), file.getName());
        } while (tempFile.exists());
        assert !tempFile.exists();
        return tempFile;
    }

    private static void createParentDir(@NotNull File file) {
        File dir = file.getParentFile();
        if (!dir.exists()) {
            dir.mkdir();
        }
    }

    @NotNull
    private static File createTempDirectory() {
        File dir = newTempDir();
        if (!dir.mkdir()) {
            throw new RuntimeException("Unable to create directory: " + dir);
        }
        return dir;
    }

    @NotNull
    private static File newTempDir() {
        int i = 0;
        File dir;
        do {
            i++;
            dir = new File(System.getProperty("java.io.tmpdir"), TempFileManager.class.getName() + "." + i);
        } while (dir.exists());
        assert !dir.exists();
        return dir;
    }
}
