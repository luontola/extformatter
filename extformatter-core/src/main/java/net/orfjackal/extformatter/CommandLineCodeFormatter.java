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
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.regex.Matcher;

/**
 * @author Esko Luontola
 * @since 30.11.2007
 */
public class CommandLineCodeFormatter implements CodeFormatter {

    // TODO: more descriptive names, also in factory?
    @Nullable private final String singleFileCommand;
    @Nullable private final String directoryCommand;
    @Nullable private final String recursiveDirectoryCommand;

    @NotNull private final Executer executer;

    public CommandLineCodeFormatter(@Nullable String singleFileCommand, @Nullable String directoryCommand,
                                    @Nullable String recursiveDirectoryCommand, @NotNull Executer executer) {
        this.singleFileCommand = singleFileCommand;
        this.directoryCommand = directoryCommand;
        this.recursiveDirectoryCommand = recursiveDirectoryCommand;
        this.executer = executer;
    }

    public CommandLineCodeFormatter(@Nullable String singleFileCommand, @Nullable String directoryCommand,
                                    @Nullable String recursiveDirectoryCommand) {
        this(singleFileCommand, directoryCommand, recursiveDirectoryCommand, new ExecuterImpl());
    }

    public boolean supportsReformatFile() {
        return singleFileCommand != null;
    }

    public void reformatFile(@NotNull File file) {
        if (singleFileCommand != null) {
            executer.execute(parsed(singleFileCommand, file));
        } else {
            throw new IllegalStateException("Reformatting a single file is not supported");
        }
    }

    public boolean supportsReformatFiles() {
        return supportsReformatFile();
    }

    public void reformatFiles(@NotNull File... files) {
        for (File file : files) {
            reformatFile(file);
        }
    }

    public boolean supportsReformatFilesInDirectory() {
        return directoryCommand != null || supportsReformatFile();
    }

    public void reformatFilesInDirectory(@NotNull File directory) {
        if (directoryCommand != null) {
            executer.execute(parsed(directoryCommand, directory));
        } else {
            File[] files = directory.listFiles(new FileFilter() {
                public boolean accept(File pathname) {
                    return pathname.isFile();
                }
            });
            reformatFiles(files);
        }
    }

    public boolean supportsReformatFilesInDirectoryRecursively() {
        return recursiveDirectoryCommand != null || supportsReformatFilesInDirectory();
    }

    public void reformatFilesInDirectoryRecursively(@NotNull File directory) {
        if (recursiveDirectoryCommand != null) {
            executer.execute(parsed(recursiveDirectoryCommand, directory));
        } else {
            File[] subDirs = directory.listFiles(new FileFilter() {
                public boolean accept(File pathname) {
                    return pathname.isDirectory();
                }
            });
            reformatFilesInDirectory(directory);
            for (File dir : subDirs) {
                reformatFilesInDirectoryRecursively(dir);
            }
        }
    }

    private String parsed(@NotNull String command, @NotNull File file) {
        try {
            if (file.isFile()) {
                command = command.replaceAll("%FILE%", Matcher.quoteReplacement(quoted(file.getCanonicalPath())));
            } else if (file.isDirectory()) {
                command = command.replaceAll("%DIRECTORY%", Matcher.quoteReplacement(quoted(file.getCanonicalPath())));
            } else {
                throw new IllegalArgumentException("Not a file nor a directory: " + file);
            }
            return command;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static String quoted(String s) {
        return '"' + s + '"';
    }
}
