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

package net.orfjackal.extformatter.settings;

import net.orfjackal.extformatter.*;
import static net.orfjackal.extformatter.CommandLineCodeFormatter.*;
import org.jetbrains.annotations.*;

import java.io.File;

/**
 * Builds a {@link CodeFormatter} based on the {@link Settings}.
 *
 * @author Esko Luontola
 * @since 4.12.2007
 */
public class SettingsManager {

    private static final String WHITESPACE = "\\s+";

    @Nullable
    public static CodeFormatter newFormatter(@NotNull Settings settings) throws IllegalSettingsException {
        if (settings.getFormatter().equals(Settings.Formatter.ECLIPSE)) {
            return eclipseFactory(settings).newFormatter();
        }
        if (settings.getFormatter().equals(Settings.Formatter.COMMAND_LINE)) {
            return commandLineFactory(settings).newFormatter();
        }
        return null;
    }

    @NotNull
    private static EclipseCodeFormatterFactory eclipseFactory(@NotNull Settings settings) throws IllegalSettingsException {
        mustNotBeEmpty(settings.getEclipseExecutable(), "settings.eclipseExecutable");
        mustNotBeEmpty(settings.getEclipsePrefs(), "settings.eclipsePrefs");

        File eclipsePrefs = new File(settings.getEclipsePrefs());
        File eclipseExecutable = new File(settings.getEclipseExecutable());

        fileMustExist(eclipseExecutable, "settings.eclipseExecutable");
        fileMustExist(eclipsePrefs, "settings.eclipsePrefs");

        EclipseCodeFormatterFactory factory = new EclipseCodeFormatterFactory();
        factory.setEclipseExecutable(eclipseExecutable);
        factory.setEclipsePrefs(eclipsePrefs);
        return factory;
    }

    @NotNull
    private static CommandLineCodeFormatterFactory commandLineFactory(@NotNull Settings settings) throws IllegalSettingsException {
        CommandLineCodeFormatterFactory factory = new CommandLineCodeFormatterFactory();
        if (!settings.isCliReformatOneEnabled()
                && !settings.isCliReformatManyEnabled()
                && !settings.isCliReformatDirectoryEnabled()
                && !settings.isCliReformatRecursivelyEnabled()) {
            throw new IllegalSettingsException("settings.cliReformatOne", "error.noCommandsSelected");
        }
        if (settings.isCliReformatOneEnabled()) {
            String s = settings.getCliReformatOne();
            mustNotBeEmpty(s, "settings.cliReformatOne");
            mustContain(FILE_TAG, s, "settings.cliReformatOne");
            factory.setOneFileCommand(s);
        }
        if (settings.isCliReformatManyEnabled()) {
            String s = settings.getCliReformatMany();
            mustNotBeEmpty(s, "settings.cliReformatMany");
            mustContain(FILES_TAG, s, "settings.cliReformatMany");
            factory.setManyFilesCommand(s);
        }
        if (settings.isCliReformatDirectoryEnabled()) {
            String s = settings.getCliReformatDirectory();
            mustNotBeEmpty(s, "settings.cliReformatDirectory");
            mustContain(DIRECTORY_TAG, s, "settings.cliReformatDirectory");
            factory.setDirectoryCommand(s);
        }
        if (settings.isCliReformatRecursivelyEnabled()) {
            String s = settings.getCliReformatRecursively();
            mustNotBeEmpty(s, "settings.cliReformatRecursively");
            mustContain(DIRECTORY_TAG, s, "settings.cliReformatRecursively");
            factory.setRecursiveCommand(s);
        }
        String s = settings.getCliSupportedFileTypes();
        mustNotBeEmpty(s, "settings.cliSupportedFileTypes");
        factory.setSupportedFileTypes(s.split(WHITESPACE));
        return factory;
    }

    private static void mustNotBeEmpty(@NotNull String s, @NotNull String field) throws IllegalSettingsException {
        if (isEmpty(s)) {
            throw new IllegalSettingsException(field, "error.emptyField");
        }
    }

    private static void mustContain(@NotNull String needle, @NotNull String haystack, @NotNull String field) throws IllegalSettingsException {
        if (!haystack.contains(needle)) {
            throw new IllegalSettingsException(field, "error.doesNotContain", needle);
        }
    }

    private static void fileMustExist(@NotNull File file, @NotNull String field) throws IllegalSettingsException {
        if (!file.isFile()) {
            throw new IllegalSettingsException(field, "error.noSuchFile", file.toString());
        }
    }

    private static boolean isEmpty(@NotNull String s) {
        return s.trim().length() == 0;
    }
}
