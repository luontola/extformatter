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
import java.io.IOException;

/**
 * @author Esko Luontola
 * @since 1.12.2007
 */
public class EclipseCodeFormatter implements CodeFormatter {

    @NotNull private final File eclipseExecutable;
    @NotNull private final File eclipsePrefs;
    @NotNull private final Executer executer;

    public EclipseCodeFormatter(@NotNull File eclipseExecutable, @NotNull File eclipsePrefs, @NotNull Executer executer) {
        this.eclipseExecutable = eclipseExecutable;
        this.eclipsePrefs = eclipsePrefs;
        this.executer = executer;
    }

    public EclipseCodeFormatter(@NotNull File eclipseExecutable, @NotNull File eclipsePrefs) {
        this(eclipseExecutable, eclipsePrefs, new ExecuterImpl());
    }

    public void reformatFile(@NotNull File file) {
        try {
            executer.execute(commandFor(quoted(file.getCanonicalPath())));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void reformatFiles(@NotNull File... files) {
        try {
            executer.execute(commandFor(listOf(files)));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void reformatFilesInDirectory(@NotNull File directory) {
        File[] files = directory.listFiles(new FileFilter() {
            public boolean accept(File file) {
                return file.isFile() && file.getName().endsWith(".java");
            }
        });
        reformatFiles(files);
    }

    public void reformatFilesInDirectoryRecursively(@NotNull File directory) {
        try {
            executer.execute(commandFor(quoted(directory.getCanonicalPath())));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String commandFor(String path) {
        /*
         *  Usage: eclipse -application org.eclipse.jdt.core.JavaCodeFormatter [ OPTIONS ] -config <configFile> <files>
         *
         *     <files>   Java source files and/or directories to format.
         *               Only files ending with .java will be formatted in the given directory.
         *     -config <configFile> Use the formatting style from the specified properties file.
         *                          Refer to the help documentation to find out how to generate this file.
         *
         *   OPTIONS:
         *
         *     -help                Display this message.
         *     -quiet               Only print error messages.
         *     -verbose             Be verbose about the formatting job.
         */
        // At least this command worked for me:
        // C:\eclipse-SDK-3.3.1-win32\eclipse>eclipse -vm "C:\Program Files\Java\jre1.6.0_02\bin\java.exe"
        //      -application org.eclipse.jdt.core.JavaCodeFormatter -verbose
        //      -config C:\eclipse-SDK-3.3.1-win32\workspace\foo\.settings\org.eclipse.jdt.core.prefs
        //      C:\Temp\weenyconsole\src\main\java\net\orfjackal\weenyconsole\*.java
        try {
            String eclipse = quoted(eclipseExecutable.getCanonicalPath());
            String java = quoted(new File(System.getProperty("java.home"), "bin/java").getCanonicalPath());
            String config = quoted(eclipsePrefs.getCanonicalPath());
            return eclipse + " -application org.eclipse.jdt.core.JavaCodeFormatter -verbose"
                    + " -vm " + java + " -config " + config + " " + path;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static String listOf(File... files) throws IOException {
        if (files.length == 0) {
            throw new IllegalArgumentException("No files");
        }
        String paths = "";
        for (File file : files) {
            if (paths.length() > 0) {
                paths += " ";
            }
            paths += quoted(file.getCanonicalPath());
        }
        return paths;
    }

    private static String quoted(String s) {
        return '"' + s + '"';
    }
}
