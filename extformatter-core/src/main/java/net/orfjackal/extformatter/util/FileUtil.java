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

import java.io.*;

/**
 * @author Esko Luontola
 * @since 14.12.2007
 */
public class FileUtil {

    private FileUtil() {
    }

    @NotNull
    public static String listOf(@NotNull File... files) {
        if (files.length == 0) {
            throw new IllegalArgumentException("No files");
        }
        String paths = "";
        for (File file : files) {
            if (paths.length() > 0) {
                paths += " ";
            }
            paths += quoted(file);
        }
        return paths;
    }

    @NotNull
    public static String quoted(@NotNull File file) {
        try {
            return '"' + file.getCanonicalPath() + '"';
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @NotNull
    public static String contentsOf(@NotNull File file) throws IOException {
        FileReader reader = new FileReader(file);
        StringBuilder result;
        try {
            result = new StringBuilder((int) (file.length() * 1.2));
            char[] buf = new char[1024];
            int len;
            while ((len = reader.read(buf)) >= 0) {
                result.append(buf, 0, len);
            }
        } finally {
            reader.close();
        }
        return result.toString();
    }

    public static void copy(@NotNull File from, @NotNull File to) throws IOException {
        InputStream readFrom = new FileInputStream(from);
        OutputStream writeTo = new FileOutputStream(to);
        try {
            byte[] buf = new byte[1024];
            int len;
            while ((len = readFrom.read(buf)) >= 0) {
                writeTo.write(buf, 0, len);
            }
        } finally {
            readFrom.close();
            writeTo.close();
        }
    }

    public static void deleteRecursively(@NotNull File file) {
        if (file.isDirectory()) {
            for (File f : file.listFiles()) {
                deleteRecursively(f);
            }
        }
        if (!file.delete()) {
            file.deleteOnExit();
        }
    }
}
