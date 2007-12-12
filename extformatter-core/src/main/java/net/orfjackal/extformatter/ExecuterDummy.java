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

/**
 * For debugging purposes, does not execute anything - only prints the command.
 *
 * @author Esko Luontola
 * @since 1.12.2007
 */
public class ExecuterDummy implements Executer {

    private static final int LINE_LENGTH = 120;

    public void execute(@NotNull String command) {
        System.out.println(ExecuterDummy.class.getName() + ".execute(), command:");
        System.out.print(lineWrap(command));
    }

    @NotNull
    private static String lineWrap(@NotNull String text) {
        StringBuilder wrapped = new StringBuilder();
        for (int begin = 0; begin < text.length(); begin += LINE_LENGTH) {
            int end = Math.min(begin + LINE_LENGTH, text.length());
            wrapped.append(text.substring(begin, end));
            wrapped.append("\n");
        }
        return wrapped.toString();
    }
}
