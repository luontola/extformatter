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

import org.jetbrains.annotations.NotNull;

/**
 * @author Esko Luontola
 * @since 4.12.2007
 */
public class ProjectSettings {

    @NotNull private String eclipseExecutable = "";
    @NotNull private String eclipsePrefs = "";
    private boolean pluginEnabled = true;

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

    public boolean isPluginEnabled() {
        return pluginEnabled;
    }

    public void setPluginEnabled(boolean pluginEnabled) {
        this.pluginEnabled = pluginEnabled;
    }
}