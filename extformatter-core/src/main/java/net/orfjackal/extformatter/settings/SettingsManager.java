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

package net.orfjackal.extformatter.settings;

import net.orfjackal.extformatter.CodeFormatter;
import net.orfjackal.extformatter.CodeFormatterFactory;
import net.orfjackal.extformatter.EclipseCodeFormatterFactory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;

/**
 * @author Esko Luontola
 * @since 4.12.2007
 */
public class SettingsManager {

    @Nullable
    public static CodeFormatter newFormatter(@NotNull Settings settings) throws IllegalSettingsException {
        if (!settings.isPluginEnabled()) {
            return null;
        }
        CodeFormatterFactory<?> factory = eclipseFactory(settings);
        return factory.newFormatter();
    }

    @NotNull
    private static EclipseCodeFormatterFactory eclipseFactory(@NotNull Settings settings) throws IllegalSettingsException {
        mustNotBeEmpty(settings.getEclipseExecutable(), "eclipseExecutable");
        mustNotBeEmpty(settings.getEclipsePrefs(), "eclipsePrefs");

        File eclipsePrefs = new File(settings.getEclipsePrefs());
        File eclipseExecutable = new File(settings.getEclipseExecutable());

        fileMustExist(eclipseExecutable, "eclipseExecutable");
        fileMustExist(eclipsePrefs, "eclipsePrefs");

        EclipseCodeFormatterFactory factory = new EclipseCodeFormatterFactory();
        factory.setEclipseExecutable(eclipseExecutable);
        factory.setEclipsePrefs(eclipsePrefs);
        return factory;
    }

    private static void mustNotBeEmpty(@NotNull String s, @NotNull String field) throws IllegalSettingsException {
        if (isEmpty(s)) {
            throw new IllegalSettingsException(field, "Field is empty");
        }
    }

    private static void fileMustExist(@NotNull File file, @NotNull String field) throws IllegalSettingsException {
        if (!file.isFile()) {
            throw new IllegalSettingsException(field, "File does not exist: " + file);
        }
    }

    private static boolean isEmpty(@NotNull String s) {
        return s.trim().length() == 0;
    }
}
