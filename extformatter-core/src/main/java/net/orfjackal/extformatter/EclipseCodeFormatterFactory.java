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

import java.io.File;

/**
 * @author Esko Luontola
 * @since 4.12.2007
 */
public class EclipseCodeFormatterFactory implements CodeFormatterFactory<EclipseCodeFormatter> {

    @Nullable private File eclipseExecutable;
    @Nullable private File eclipsePrefs;

    @Nullable
    public EclipseCodeFormatter newFormatter() {
        if (eclipseExecutable != null && eclipsePrefs != null) {
            return new EclipseCodeFormatter(eclipseExecutable, eclipsePrefs);
        } else {
            return null;
        }
    }

    @Nullable
    public File getEclipseExecutable() {
        return eclipseExecutable;
    }

    public void setEclipseExecutable(@Nullable File eclipseExecutable) {
        this.eclipseExecutable = eclipseExecutable;
    }

    @Nullable
    public File getEclipsePrefs() {
        return eclipsePrefs;
    }

    public void setEclipsePrefs(@Nullable File eclipsePrefs) {
        this.eclipsePrefs = eclipsePrefs;
    }
}
