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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Checks whether a file's name matches any of the specified file masks (*.java, *.* and so on).
 *
 * @author Esko Luontola
 * @since 7.12.2007
 */
public class SupportedFileTypes {

    @NotNull private final List<Pattern> patterns;

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
    private static List<Pattern> toRegex(@NotNull String[] fileMasks) {
        List<Pattern> patterns = new ArrayList<Pattern>();
        for (String fileMask : fileMasks) {
            fileMask = fileMask.trim();
            if (fileMask.length() > 0) {
                patterns.add(toRegex(fileMask));
            }
        }
        return Collections.unmodifiableList(patterns);
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
