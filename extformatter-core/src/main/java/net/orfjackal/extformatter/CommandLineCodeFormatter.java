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
import java.io.IOException;
import java.util.regex.Matcher;

/**
 * @author Esko Luontola
 * @since 30.11.2007
 */
public class CommandLineCodeFormatter implements CodeFormatter {

    private static final String FILE_TAG = "%FILE%";
    private static final String DIRECTORY_TAG = "%DIRECTORY%";

    @NotNull private final SupportedFileTypes supportedFileTypes;
    @Nullable private final String fileCommand;
    @Nullable private final String directoryCommand;
    @Nullable private final String recursiveDirectoryCommand;
    @NotNull private final Executer executer;

    public CommandLineCodeFormatter(@NotNull String[] supportedFileTypes,
                                    @Nullable String fileCommand,
                                    @Nullable String directoryCommand,
                                    @Nullable String recursiveDirectoryCommand,
                                    @NotNull Executer executer) {
        this.supportedFileTypes = new SupportedFileTypes(supportedFileTypes);
        this.fileCommand = fileCommand;
        this.directoryCommand = directoryCommand;
        this.recursiveDirectoryCommand = recursiveDirectoryCommand;
        this.executer = executer;
    }

    public CommandLineCodeFormatter(@NotNull String[] supportedFileTypes,
                                    @Nullable String fileCommand,
                                    @Nullable String directoryCommand,
                                    @Nullable String recursiveDirectoryCommand) {
        this(supportedFileTypes, fileCommand, directoryCommand, recursiveDirectoryCommand, new ExecuterImpl());
    }

    public boolean supportsFileType(@NotNull File file) {
        return supportedFileTypes.matches(file);
    }

    public boolean supportsReformatFile() {
        return fileCommand != null;
    }

    public void reformatFile(@NotNull File file) {
        if (fileCommand != null) {
            executer.execute(parsed(fileCommand, file));
        }
    }

    public boolean supportsReformatFiles() {
        return false;
    }

    public void reformatFiles(@NotNull File... files) {
        throw new UnsupportedOperationException();
    }

    public boolean supportsReformatFilesInDirectory() {
        return directoryCommand != null;
    }

    public void reformatFilesInDirectory(@NotNull File directory) {
        if (directoryCommand != null) {
            executer.execute(parsed(directoryCommand, directory));
        }
    }

    public boolean supportsReformatFilesInDirectoryRecursively() {
        return recursiveDirectoryCommand != null;
    }

    public void reformatFilesInDirectoryRecursively(@NotNull File directory) {
        if (recursiveDirectoryCommand != null) {
            executer.execute(parsed(recursiveDirectoryCommand, directory));
        }
    }

    private String parsed(@NotNull String command, @NotNull File file) {
        try {
            if (command.contains(FILE_TAG) && file.isFile()) {
                assert supportsFileType(file);
                command = command.replaceAll(FILE_TAG, Matcher.quoteReplacement(quoted(file.getCanonicalPath())));
            } else if (command.contains(DIRECTORY_TAG) && file.isDirectory()) {
                command = command.replaceAll(DIRECTORY_TAG, Matcher.quoteReplacement(quoted(file.getCanonicalPath())));
            } else {
                throw new IllegalArgumentException("command '" + command + "',  file '" + file + "'");
            }
            return command;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @NotNull
    private static String quoted(@NotNull String s) {
        return '"' + s + '"';
    }
}
