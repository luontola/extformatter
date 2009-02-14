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

package net.orfjackal.extformatter;

import jdave.Specification;
import jdave.junit4.JDaveRunner;
import org.junit.runner.RunWith;

import java.io.ByteArrayOutputStream;

/**
 * @author Esko Luontola
 * @since 6.12.2007
 */
@RunWith(JDaveRunner.class)
public class ExecuterSpec extends Specification<Executer> {

    public class AnExecuter {

        private Executer executer;
        private ByteArrayOutputStream stdout;
        private ByteArrayOutputStream stderr;

        public Executer create() {
            stdout = new ByteArrayOutputStream();
            stderr = new ByteArrayOutputStream();
            executer = new ExecuterImpl(stdout, stderr);
            return executer;
        }

        public void shouldExecuteTheSystemCommand() {
            // tested with the following tests and net.orfjackal.extformatter.integration.ExecuterManualTests
        }

        public void shouldRedirectStdout() {
            // "cmd /c" is required by Windows because "echo" is not a file (unlike in Linux) but a shell command
            executer.execute("cmd /c echo foo");
            specify(stdout.toString(), should.equal("foo\r\n"));
            specify(stderr.toString(), should.equal(""));
        }

        public void shouldRedirectStderr() {
            executer.execute("cmd /c echo bar>&2");
            specify(stdout.toString(), should.equal(""));
            specify(stderr.toString(), should.equal("bar\r\n"));
        }
    }
}
