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
import org.jetbrains.annotations.Nullable;

import java.io.File;

/**
 * @author Esko Luontola
 * @since 4.12.2007
 */
public class SettingsManager {

    @Nullable
    public static CodeFormatter newFormatter(Settings settings) {
        if (!settings.isPluginEnabled()) {
            return null;
        }
        CodeFormatterFactory<?> factory = eclipseFactory(settings);
        return factory.newFormatter();
    }

    private static EclipseCodeFormatterFactory eclipseFactory(Settings settings) {
        EclipseCodeFormatterFactory factory = new EclipseCodeFormatterFactory();
        factory.setEclipseExecutable(new File(settings.getEclipseExecutable()));
        factory.setEclipsePrefs(new File(settings.getEclipsePrefs()));
        return factory;
    }
}
