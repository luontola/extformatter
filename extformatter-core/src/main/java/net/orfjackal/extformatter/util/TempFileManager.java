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
import java.util.ArrayList;
import java.util.List;

/**
 * @author Esko Luontola
 * @since 18.12.2007
 */
public class TempFileManager {

    @NotNull private final File tempDirectory;
    @NotNull private final List<File> files = new ArrayList<File>();

    public TempFileManager() {
        tempDirectory = createTempDirectory();
    }

    public void add(File file) {
        try {
            copyToTemp(file);
            files.add(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public File[] files() {
        return files.toArray(new File[files.size()]);
    }

    public void dispose() {
        FileUtil.deleteRecursively(tempDirectory);
    }

    protected File tempDirectory() {
        return tempDirectory;
    }

    protected File tempDirectory(int subdir) {
        return new File(tempDirectory, String.valueOf(subdir));
    }

    private void copyToTemp(File file) throws IOException {
        File tmpFile = newTempFile(file);
        createParentDir(tmpFile);
        FileUtil.copy(file, tmpFile);
    }

    private File newTempFile(File file) {
        int i = 0;
        File tmpFile;
        do {
            i++;
            tmpFile = new File(tempDirectory(i), file.getName());
        } while (tmpFile.exists());
        return tmpFile;
    }

    private static void createParentDir(File file) {
        File dir = file.getParentFile();
        if (!dir.exists()) {
            dir.mkdir();
        }
    }

    private static File createTempDirectory() {
        File dir = newTempDir();
        if (!dir.mkdir()) {
            throw new RuntimeException("Unable to create directory: " + dir);
        }
        return dir;
    }

    private static File newTempDir() {
        int i = 0;
        File dir;
        do {
            i++;
            dir = new File(System.getProperty("java.io.tmpdir"), TempFileManager.class.getName() + "." + i);
        } while (dir.exists());
        return dir;
    }
}
