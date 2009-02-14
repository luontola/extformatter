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

import org.jetbrains.annotations.NotNull;

/**
 * @author Esko Luontola
 * @since 4.12.2007
 */
public class Settings implements Cloneable {

    public static enum Formatter {
        DEFAULT, ECLIPSE, COMMAND_LINE
    }

    @NotNull private Formatter formatter = Formatter.DEFAULT;

    @NotNull private String eclipseExecutable = "";
    @NotNull private String eclipsePrefs = "";

    private boolean cliReformatOneEnabled = false;
    private boolean cliReformatManyEnabled = false;
    private boolean cliReformatDirectoryEnabled = false;
    private boolean cliReformatRecursivelyEnabled = false;
    @NotNull private String cliSupportedFileTypes = "*.*";
    @NotNull private String cliReformatOne = "";
    @NotNull private String cliReformatMany = "";
    @NotNull private String cliReformatDirectory = "";
    @NotNull private String cliReformatRecursively = "";

    @NotNull
    public final Settings clone() {
        try {
            return (Settings) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

    // Generated getters and setters

    @NotNull
    public Formatter getFormatter() {
        return formatter;
    }

    public void setFormatter(@NotNull Formatter formatter) {
        this.formatter = formatter;
    }

    @NotNull
    public String getEclipseExecutable() {
        return eclipseExecutable;
    }

    public void setEclipseExecutable(@NotNull String eclipseExecutable) {
        this.eclipseExecutable = eclipseExecutable;
    }

    @NotNull
    public String getEclipsePrefs() {
        return eclipsePrefs;
    }

    public void setEclipsePrefs(@NotNull String eclipsePrefs) {
        this.eclipsePrefs = eclipsePrefs;
    }

    public boolean isCliReformatOneEnabled() {
        return cliReformatOneEnabled;
    }

    public void setCliReformatOneEnabled(boolean cliReformatOneEnabled) {
        this.cliReformatOneEnabled = cliReformatOneEnabled;
    }

    public boolean isCliReformatManyEnabled() {
        return cliReformatManyEnabled;
    }

    public void setCliReformatManyEnabled(boolean cliReformatManyEnabled) {
        this.cliReformatManyEnabled = cliReformatManyEnabled;
    }

    public boolean isCliReformatDirectoryEnabled() {
        return cliReformatDirectoryEnabled;
    }

    public void setCliReformatDirectoryEnabled(boolean cliReformatDirectoryEnabled) {
        this.cliReformatDirectoryEnabled = cliReformatDirectoryEnabled;
    }

    public boolean isCliReformatRecursivelyEnabled() {
        return cliReformatRecursivelyEnabled;
    }

    public void setCliReformatRecursivelyEnabled(boolean cliReformatRecursivelyEnabled) {
        this.cliReformatRecursivelyEnabled = cliReformatRecursivelyEnabled;
    }

    @NotNull
    public String getCliSupportedFileTypes() {
        return cliSupportedFileTypes;
    }

    public void setCliSupportedFileTypes(@NotNull String cliSupportedFileTypes) {
        this.cliSupportedFileTypes = cliSupportedFileTypes;
    }

    @NotNull
    public String getCliReformatOne() {
        return cliReformatOne;
    }

    public void setCliReformatOne(@NotNull String cliReformatOne) {
        this.cliReformatOne = cliReformatOne;
    }

    @NotNull
    public String getCliReformatMany() {
        return cliReformatMany;
    }

    public void setCliReformatMany(@NotNull String cliReformatMany) {
        this.cliReformatMany = cliReformatMany;
    }

    @NotNull
    public String getCliReformatDirectory() {
        return cliReformatDirectory;
    }

    public void setCliReformatDirectory(@NotNull String cliReformatDirectory) {
        this.cliReformatDirectory = cliReformatDirectory;
    }

    @NotNull
    public String getCliReformatRecursively() {
        return cliReformatRecursively;
    }

    public void setCliReformatRecursively(@NotNull String cliReformatRecursively) {
        this.cliReformatRecursively = cliReformatRecursively;
    }
}
