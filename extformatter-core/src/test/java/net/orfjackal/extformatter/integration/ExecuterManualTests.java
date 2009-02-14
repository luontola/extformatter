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

package net.orfjackal.extformatter.integration;

import net.orfjackal.extformatter.*;

/**
 * @author Esko Luontola
 * @since 1.12.2007
 */
public class ExecuterManualTests {

    public static class ExecuteCommandTest {

        /**
         * EXPECTED: To System.out will be printed the output of the ping command,
         * which should be the help message of ping.
         */
        public static void main(String[] args) {
            Executer executer = new ExecuterImpl();
            System.out.println("--- BEGIN ---");
            executer.execute("ping");
            System.out.println("--- END ---");
        }
    }
}
