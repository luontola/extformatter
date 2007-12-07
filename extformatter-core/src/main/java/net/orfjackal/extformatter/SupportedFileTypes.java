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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Esko Luontola
 * @since 7.12.2007
 */
public class SupportedFileTypes {

    @NotNull private final Pattern[] patterns;

    public SupportedFileTypes(@NotNull String... fileMasks) {
        this.patterns = toRegex(fileMasks);
    }

    public boolean matches(@NotNull File file) {
        for (Pattern p : patterns) {
            if (p.matcher(file.getName()).matches()) {
                return true;
            }
        }
        return false;
    }

    @NotNull
    private static Pattern[] toRegex(@NotNull String[] fileMasks) {
        Pattern[] patterns = new Pattern[fileMasks.length];
        for (int i = 0; i < fileMasks.length; i++) {
            patterns[i] = toRegex(fileMasks[i]);
        }
        return patterns;
    }

    @NotNull
    private static Pattern toRegex(@NotNull String fileMask) {
        StringBuilder regex = new StringBuilder();
        for (int i = 0; i < fileMask.length(); i++) {
            char c = fileMask.charAt(i);
            if (c == '*') {
                regex.append(".*");
            } else {
                regex.append(Matcher.quoteReplacement(String.valueOf(c)));
            }
        }
        return Pattern.compile(regex.toString(), Pattern.CASE_INSENSITIVE);
    }
}
