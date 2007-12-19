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

import net.orfjackal.extformatter.util.Directories;
import net.orfjackal.extformatter.util.FilesSupportedBy;
import org.jetbrains.annotations.NotNull;

import java.io.File;

/**
 * Uses alternative reformat methods if the underlying formatter does not support all methods.
 *
 * @author Esko Luontola
 * @since 6.12.2007
 */
public class AdaptiveCodeFormatter implements CodeFormatter {

    private final CodeFormatter formatter;

    public AdaptiveCodeFormatter(@NotNull CodeFormatter formatter) {
        this.formatter = formatter;
    }

    public boolean supportsFileType(@NotNull File file) {
        return formatter.supportsFileType(file);
    }

    public boolean supportsReformatOne() {
        return formatter.supportsReformatOne()
                || formatter.supportsReformatMany();
    }

    public void reformatOne(@NotNull File file) {
        assert supportsFileType(file);
        if (formatter.supportsReformatOne()) {
            formatter.reformatOne(file);
        } else if (formatter.supportsReformatMany()) {
            formatter.reformatMany(file);
        } else {
            throw new UnsupportedOperationException();
        }
    }

    public boolean supportsReformatMany() {
        return formatter.supportsReformatOne()
                || formatter.supportsReformatMany();
    }

    public void reformatMany(@NotNull File... files) {
        for (File file : files) {
            assert supportsFileType(file);
        }
        if (formatter.supportsReformatMany()) {
            formatter.reformatMany(files);
        } else if (formatter.supportsReformatOne()) {
            for (File file : files) {
                formatter.reformatOne(file);
            }
        } else {
            throw new UnsupportedOperationException();
        }
    }

    public boolean supportsReformatDirectory() {
        return formatter.supportsReformatOne()
                || formatter.supportsReformatMany()
                || formatter.supportsReformatDirectory();
    }

    public void reformatDirectory(@NotNull File directory) {
        if (formatter.supportsReformatDirectory()) {
            formatter.reformatDirectory(directory);
        } else {
            File[] files = directory.listFiles(new FilesSupportedBy(this));
            reformatMany(files);
        }
    }

    public boolean supportsReformatRecursively() {
        return formatter.supportsReformatOne()
                || formatter.supportsReformatMany()
                || formatter.supportsReformatDirectory()
                || formatter.supportsReformatRecursively();
    }

    public void reformatRecursively(@NotNull File directory) {
        if (formatter.supportsReformatRecursively()) {
            formatter.reformatRecursively(directory);
        } else {
            File[] subdirs = directory.listFiles(new Directories());
            reformatDirectory(directory);
            for (File subdir : subdirs) {
                reformatRecursively(subdir);
            }
        }
    }
}
