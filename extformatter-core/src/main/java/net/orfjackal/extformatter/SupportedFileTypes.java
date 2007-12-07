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

/**
 * @author Esko Luontola
 * @since 7.12.2007
 */
public class SupportedFileTypes {

    private final String[] fileTypes;

    public SupportedFileTypes(String... fileTypes) {
        this.fileTypes = fileTypes;
    }

    public boolean matches(@NotNull File file) {
        for (String fileType : fileTypes) {
            if (file.getName().endsWith(fileType)) {
                return true;
            }
        }
        return false;
    }
}
