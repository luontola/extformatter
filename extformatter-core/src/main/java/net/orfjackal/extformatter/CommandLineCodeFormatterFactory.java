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

import org.jetbrains.annotations.Nullable;

/**
 * @author Esko Luontola
 * @since 4.12.2007
 */
public class CommandLineCodeFormatterFactory implements CodeFormatterFactory<CommandLineCodeFormatter> {

    @Nullable private String singleFileCommand;
    @Nullable private String directoryCommand;
    @Nullable private String recursiveDirectoryCommand;

    @Nullable
    public CommandLineCodeFormatter newFormatter() {
        if (singleFileCommand != null || directoryCommand != null || recursiveDirectoryCommand != null) {
            return new CommandLineCodeFormatter(singleFileCommand, directoryCommand, recursiveDirectoryCommand);
        } else {
            return null;
        }
    }

    @Nullable
    public String getSingleFileCommand() {
        return singleFileCommand;
    }

    public void setSingleFileCommand(@Nullable String singleFileCommand) {
        this.singleFileCommand = singleFileCommand;
    }

    @Nullable
    public String getDirectoryCommand() {
        return directoryCommand;
    }

    public void setDirectoryCommand(@Nullable String directoryCommand) {
        this.directoryCommand = directoryCommand;
    }

    @Nullable
    public String getRecursiveDirectoryCommand() {
        return recursiveDirectoryCommand;
    }

    public void setRecursiveDirectoryCommand(@Nullable String recursiveDirectoryCommand) {
        this.recursiveDirectoryCommand = recursiveDirectoryCommand;
    }
}
