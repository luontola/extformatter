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


import java.io.*;

/**
 * Executes a shell command. Prints the output of the command.
 *
 * @author Esko Luontola
 * @since 1.12.2007
 */
public class ProcessExecutor2Impl implements ProcessExecutor2 {

    public Process exec(String... command) {
        return exec(command, System.out, System.err);
    }

    public Process exec(String[] command, OutputStream stdout, OutputStream stderr) {
        try {
            Process process = Runtime.getRuntime().exec(command);
            redirect(process.getInputStream(), stdout);
            redirect(process.getErrorStream(), stderr);
            return process;

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void redirect(final InputStream from, final OutputStream to) {
        Thread t = new Thread() {
            public void run() {
                byte[] buf = new byte[1024];
                int len;
                try {
                    while ((len = from.read(buf)) > 0) {
                        to.write(buf, 0, len);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        t.setDaemon(true);
        t.start();
    }
}
