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

import static net.orfjackal.extformatter.util.FileUtil.*;
import net.orfjackal.extformatter.util.*;
import org.jetbrains.annotations.*;

import java.io.File;
import java.util.Arrays;
import java.util.regex.Matcher;

/**
 * {@link CodeFormatter} for a generic command line tool.
 *
 * @author Esko Luontola
 * @since 30.11.2007
 */
public class CommandLineCodeFormatter implements CodeFormatter {

    public static final String FILE_TAG = "%FILE%";
    public static final String FILES_TAG = "%FILES%";
    public static final String DIRECTORY_TAG = "%DIRECTORY%";

    @NotNull private final SupportedFileTypes supportedFileTypes;
    @Nullable private final String oneFileCommand;
    @Nullable private final String manyFilesCommand;
    @Nullable private final String directoryCommand;
    @Nullable private final String recursiveCommand;
    @NotNull private final ProcessExecutor1 executor;

    public CommandLineCodeFormatter(@NotNull String[] supportedFileTypes,
                                    @Nullable String oneFileCommand,
                                    @Nullable String manyFilesCommand,
                                    @Nullable String directoryCommand,
                                    @Nullable String recursiveCommand,
                                    @NotNull ProcessExecutor1 executor) {
        this.supportedFileTypes = new SupportedFileTypes(supportedFileTypes);
        this.oneFileCommand = oneFileCommand;
        this.manyFilesCommand = manyFilesCommand;
        this.directoryCommand = directoryCommand;
        this.recursiveCommand = recursiveCommand;
        this.executor = executor;
    }

    public CommandLineCodeFormatter(@NotNull String[] supportedFileTypes,
                                    @Nullable String oneFileCommand,
                                    @Nullable String manyFilesCommand,
                                    @Nullable String directoryCommand,
                                    @Nullable String recursiveCommand) {
        this(supportedFileTypes, oneFileCommand, manyFilesCommand, directoryCommand, recursiveCommand, new ProcessExecutor1Impl());
    }

    public boolean supportsFileType(@NotNull File file) {
        return supportedFileTypes.matches(file);
    }

    public boolean supportsReformatOne() {
        return oneFileCommand != null;
    }

    public void reformatOne(@NotNull File file) {
        if (oneFileCommand != null) {
            executor.executeAndWait(parsed(oneFileCommand, file));
        }
    }

    public boolean supportsReformatMany() {
        return manyFilesCommand != null;
    }

    public void reformatMany(@NotNull File... files) {
        if (manyFilesCommand != null) {
            executor.executeAndWait(parsed(manyFilesCommand, files));
        }
    }

    public boolean supportsReformatDirectory() {
        return directoryCommand != null;
    }

    public void reformatDirectory(@NotNull File directory) {
        if (directoryCommand != null) {
            executor.executeAndWait(parsed(directoryCommand, directory));
        }
    }

    public boolean supportsReformatRecursively() {
        return recursiveCommand != null;
    }

    public void reformatRecursively(@NotNull File directory) {
        if (recursiveCommand != null) {
            executor.executeAndWait(parsed(recursiveCommand, directory));
        }
    }

    @NotNull
    private String parsed(@NotNull String command, @NotNull File file) {
        if (command.contains(FILE_TAG) && file.isFile()) {
            assert supportsFileType(file);
            command = command.replaceAll(FILE_TAG, Matcher.quoteReplacement(quoted(file)));
        } else if (command.contains(DIRECTORY_TAG) && file.isDirectory()) {
            command = command.replaceAll(DIRECTORY_TAG, Matcher.quoteReplacement(quoted(file)));
        } else {
            throw new IllegalArgumentException("command '" + command + "',  file '" + file + "'");
        }
        return command;
    }

    @NotNull
    private String parsed(@NotNull String command, @NotNull File[] files) {
        if (command.contains(FILES_TAG) && areFiles(files)) {
            command = command.replaceAll(FILES_TAG, Matcher.quoteReplacement(quotedListOf(files)));
        } else {
            throw new IllegalArgumentException("command '" + command + "',  file '" + Arrays.toString(files) + "'");
        }
        return command;
    }

    private boolean areFiles(@NotNull File[] files) {
        for (File file : files) {
            if (!file.isFile()) {
                return false;
            }
            assert supportsFileType(file);
        }
        return true;
    }
}
