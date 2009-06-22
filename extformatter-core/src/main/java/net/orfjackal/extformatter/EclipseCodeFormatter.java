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
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.util.*;

/**
 * {@link CodeFormatter} for Eclipse 3.2 or later.
 *
 * @author Esko Luontola
 * @since 1.12.2007
 */
public class EclipseCodeFormatter implements CodeFormatter {

    public static final String SUPPORTED_FILE_TYPES = "*.java";

    @NotNull private final File eclipseExecutable;
    @NotNull private final File eclipsePrefs;
    @NotNull private final ProcessExecutor2 executor;

    public EclipseCodeFormatter(@NotNull File eclipseExecutable, @NotNull File eclipsePrefs, @NotNull ProcessExecutor2 executor) {
        this.eclipseExecutable = eclipseExecutable;
        this.eclipsePrefs = eclipsePrefs;
        this.executor = executor;
    }

    public EclipseCodeFormatter(@NotNull File eclipseExecutable, @NotNull File eclipsePrefs) {
        this(eclipseExecutable, eclipsePrefs, new ProcessExecutor2Impl());
    }

    public boolean supportsFileType(@NotNull File file) {
        return file.getName().endsWith(".java");
    }

    public boolean supportsReformatOne() {
        return true;
    }

    public void reformatOne(@NotNull File file) {
        assert supportsFileType(file);
        executor.executeAndWait(commandFor(file));
    }

    public boolean supportsReformatMany() {
        return true;
    }

    public void reformatMany(@NotNull File... files) {
        for (File file : files) {
            assert supportsFileType(file);
        }
        executor.executeAndWait(commandFor(files));
    }

    public boolean supportsReformatDirectory() {
        return false;
    }

    public void reformatDirectory(@NotNull File directory) {
        throw new UnsupportedOperationException();
    }

    public boolean supportsReformatRecursively() {
        return true;
    }

    public void reformatRecursively(@NotNull File directory) {
        executor.executeAndWait(commandFor(directory));
    }

    @NotNull
    private String[] commandFor(@NotNull File... files) {
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

        assert eclipseExecutable.isFile() : "Not a file, eclipseExecutable: " + eclipseExecutable;
        assert eclipsePrefs.isFile() : "Not a file, eclipsePrefs: " + eclipsePrefs;

        List<String> command = new ArrayList<String>();
        command.add(pathTo(eclipseExecutable));
        command.add("-application");
        command.add("org.eclipse.jdt.core.JavaCodeFormatter");
        command.add("-verbose");
        command.add("-vm");
        command.add(pathTo(new File(System.getProperty("java.home"), "bin/java")));
        command.add("-config");
        command.add(pathTo(eclipsePrefs));
        for (File file : files) {
            command.add(pathTo(file));
        }
        return command.toArray(new String[command.size()]);
    }

    private static String pathTo(File file) {
        try {
            return file.getCanonicalPath();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
