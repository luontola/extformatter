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

import jdave.Specification;
import jdave.junit4.JDaveRunner;
import org.junit.runner.RunWith;

import java.io.*;

/**
 * @author Esko Luontola
 * @since 6.12.2007
 */
@RunWith(JDaveRunner.class)
public class ProcessExecutor2Spec extends Specification<ProcessExecutor2> {

    public class AProcessExecutor {

        private ByteArrayOutputStream stdout;
        private ByteArrayOutputStream stderr;
        private ProcessExecutor2 executor;

        public ProcessExecutor2 create() {
            stdout = new ByteArrayOutputStream();
            stderr = new ByteArrayOutputStream();
            executor = new ProcessExecutor2Impl();
            return executor;
        }

        public void shouldExecuteTheSystemCommand() throws InterruptedException {
            File f = new File("testExecutor.tmp");
            specify(!f.exists());
            Process p = executor.exec("cmd", "/c", "mkdir", "testExecutor.tmp");
            p.waitFor();
            specify(f.exists());
            specify(f.isDirectory());
            specify(f.delete());
        }

        public void shouldRedirectStdout() throws InterruptedException {
            // "cmd /c" is required by Windows because "echo" is not a file (unlike in Linux) but a shell command
            Process p = executor.exec(new String[]{"cmd", "/c", "echo", "foo"}, stdout, stderr);
            p.waitFor();
            specify(stdout.toString(), should.equal("foo\r\n"));
            specify(stderr.toString(), should.equal(""));
        }

        public void shouldRedirectStderr() throws InterruptedException {
            Process p = executor.exec(new String[]{"cmd", "/c", "echo", "bar>&2"}, stdout, stderr);
            p.waitFor();
            specify(stdout.toString(), should.equal(""));
            specify(stderr.toString(), should.equal("bar\r\n"));
        }

        public void shouldReturnTheExitValue() throws InterruptedException {
            int ok = executor.exec(new String[]{"cmd", "/c", "echo", "foo"}, stdout, stderr).waitFor();
            int fail = executor.exec(new String[]{"cmd", "/c", "dir", "doesNotExist"}, stdout, stderr).waitFor();
            specify(ok, should.equal(0));
            specify(fail, should.equal(1));
        }
    }
}
