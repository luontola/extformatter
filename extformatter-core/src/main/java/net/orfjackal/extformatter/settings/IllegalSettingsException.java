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
 * @since 5.12.2007
 */
public class IllegalSettingsException extends Exception {

    @NotNull private final String field;
    @NotNull private final String explanation;

    public IllegalSettingsException(@NotNull String field, @NotNull String explanation) {
        super(field + ": " + explanation);
        this.field = field;
        this.explanation = explanation;
    }

    @NotNull
    public String getField() {
        return field;
    }

    @NotNull
    public String getExplanation() {
        return explanation;
    }
}
