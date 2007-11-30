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
import java.util.regex.Matcher;

/**
 * @author Esko Luontola
 * @since 30.11.2007
 */
public class CommandLineFormatter implements Formatter {

    @NotNull private Executer executer;
    @Nullable private String singleFileCommand;
    @Nullable private String directoryCommand;
    @Nullable private String recursiveDirectoryCommand;

    public CommandLineFormatter(@NotNull Executer executer, @Nullable String singleFileCommand,
                                @Nullable String directoryCommand, @Nullable String recursiveDirectoryCommand) {
        this.executer = executer;
        this.singleFileCommand = singleFileCommand;
        this.directoryCommand = directoryCommand;
        this.recursiveDirectoryCommand = recursiveDirectoryCommand;
    }

    private String parsed(String rawCommand, File file) {
        return rawCommand.replaceAll("%FILE%", Matcher.quoteReplacement(file.getPath()));
    }

    public void reformatSingleFile(@NotNull File file) {
        executer.execute(parsed(singleFileCommand, file));
    }

    public void reformatFilesInDirectory(@NotNull File directory) {
        executer.execute(parsed(directoryCommand, directory));
    }

    public void reformatFilesInDirectoryRecursively(@NotNull File directory) {
        executer.execute(parsed(recursiveDirectoryCommand, directory));
    }
}
