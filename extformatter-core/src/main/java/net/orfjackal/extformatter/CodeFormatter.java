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

import org.jetbrains.annotations.NotNull;

import java.io.File;

/**
 * Reformats source code files. Not all formatters might support all files and methods.
 *
 * @author Esko Luontola
 * @since 30.11.2007
 */
public interface CodeFormatter {

    boolean supportsFileType(@NotNull File file);

    boolean supportsReformatOne();

    void reformatOne(@NotNull File file);

    boolean supportsReformatMany();

    void reformatMany(@NotNull File... files);

    boolean supportsReformatDirectory();

    void reformatDirectory(@NotNull File directory);

    boolean supportsReformatRecursively();

    void reformatRecursively(@NotNull File directory);
}
