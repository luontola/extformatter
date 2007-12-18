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

    public File[] files() {
        return files.toArray(new File[files.size()]);
    }

    protected File tempDirectory() {
        return tempDirectory;
    }

    protected File tempDirectory(int subdir) {
        return new File(tempDirectory, String.valueOf(subdir));
    }

    public void dispose() {
        FileUtil.deleteRecursively(tempDirectory);
    }

    private static File createTempDirectory() {
        int i = 0;
        File dir;
        do {
            i++;
            dir = new File(System.getProperty("java.io.tmpdir"), TempFileManager.class.getName() + "." + i);
        } while (dir.exists());
        if (dir.mkdir()) {
            return dir;
        } else {
            throw new RuntimeException("Unable to create directory: " + dir);
        }
    }

    public void add(File file) {
        try {
            copyToTemp(file);
            files.add(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void copyToTemp(File file) throws IOException {
        int i = 0;
        File tmpFile;
        do {
            i++;
            File dir = tempDirectory(i);
            if (!dir.exists()) {
                dir.mkdir();
            }
            tmpFile = new File(dir, file.getName());
        } while (tmpFile.exists());
        FileUtil.copy(file, tmpFile);
    }

}
