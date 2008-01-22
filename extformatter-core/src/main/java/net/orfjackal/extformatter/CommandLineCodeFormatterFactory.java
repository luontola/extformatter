/*
 * External Code Formatter
 * Copyright (c) 2007-2008 Esko Luontola, www.orfjackal.net
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

/**
 * Builds a {@link CommandLineCodeFormatter}.
 *
 * @author Esko Luontola
 * @since 4.12.2007
 */
public class CommandLineCodeFormatterFactory implements CodeFormatterFactory<CommandLineCodeFormatter> {

    @NotNull private String[] supportedFileTypes = new String[0];
    @Nullable private String oneFileCommand;
    @Nullable private String manyFilesCommand;
    @Nullable private String directoryCommand;
    @Nullable private String recursiveCommand;

    @Nullable
    public CommandLineCodeFormatter newFormatter() {
        if (oneFileCommand != null || manyFilesCommand != null || directoryCommand != null || recursiveCommand != null) {
            return new CommandLineCodeFormatter(supportedFileTypes,
                    oneFileCommand, manyFilesCommand, directoryCommand, recursiveCommand);
        } else {
            return null;
        }
    }

    public void setSupportedFileTypes(@NotNull String... supportedFileTypes) {
        assert supportedFileTypes.length > 0;
        this.supportedFileTypes = supportedFileTypes;
    }

    public void setOneFileCommand(@Nullable String oneFileCommand) {
        assert notEmpty(oneFileCommand);
        this.oneFileCommand = oneFileCommand;
    }

    public void setManyFilesCommand(@Nullable String manyFilesCommand) {
        assert notEmpty(manyFilesCommand);
        this.manyFilesCommand = manyFilesCommand;
    }

    public void setDirectoryCommand(@Nullable String directoryCommand) {
        assert notEmpty(directoryCommand);
        this.directoryCommand = directoryCommand;
    }

    public void setRecursiveCommand(@Nullable String recursiveCommand) {
        assert notEmpty(recursiveCommand);
        this.recursiveCommand = recursiveCommand;
    }

    private static boolean notEmpty(@Nullable String s) {
        return s == null || s.length() > 0;
    }
}
