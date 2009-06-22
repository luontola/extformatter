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

package net.orfjackal.extformatter.util;

import java.io.OutputStream;

/**
 * Executes a shell command.
 *
 * @author Esko Luontola
 * @since 30.11.2007
 */
public interface ProcessExecutor2 {

    void executeAndWait(String... command);

    /**
     * Executes the command and redirects stdout and stderr to System.out and System.err.
     */
    Process exec(String... command);

    /**
     * Executes the command and redirects stdout and stderr to the specified streams.
     */
    Process exec(String[] command, OutputStream stdout, OutputStream stderr);
}
