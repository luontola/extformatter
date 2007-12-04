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

import jdave.Block;
import jdave.Specification;
import jdave.junit4.JDaveRunner;
import static net.orfjackal.extformatter.TestResources.*;
import org.jmock.Expectations;
import org.junit.runner.RunWith;

import java.io.IOException;

/**
 * @author Esko Luontola
 * @since 30.11.2007
 */
@RunWith(JDaveRunner.class)
public class CommandLineCodeFormatterSpec extends Specification<CodeFormatter> {

    // TODO: move adaptation to the available commands to its own class

    public class FormatterWithAllCommandsSpecified {

        private Executer executer;
        private CodeFormatter formatter;

        public CodeFormatter create() {
            executer = mock(Executer.class);
            formatter = new CommandLineCodeFormatter("format %FILE%", "formatDir %DIRECTORY%", "formatDirRec %DIRECTORY%", executer);
            return formatter;
        }

        public void shouldExecuteCommandForSingleFile() throws IOException {
            checking(new Expectations() {{
                one(executer).execute("format \"" + FOO_FILE.getAbsolutePath() + "\"");
            }});
            formatter.reformatFile(FOO_FILE);
        }

        public void shouldExecuteCommandForManyFiles() throws IOException {
            checking(new Expectations() {{
                one(executer).execute("format \"" + FOO_FILE.getAbsolutePath() + "\"");
                one(executer).execute("format \"" + BAZ_FILE.getAbsolutePath() + "\"");
            }});
            formatter.reformatFiles(FOO_FILE, BAZ_FILE);
        }

        public void shouldExecuteCommandForFilesInDirectory() throws IOException {
            checking(new Expectations() {{
                one(executer).execute("formatDir \"" + TESTFILES_DIR.getAbsolutePath() + "\"");
            }});
            formatter.reformatFilesInDirectory(TESTFILES_DIR);
        }

        public void shouldExecuteCommandForFilesInDirectoryRecursively() throws IOException {
            checking(new Expectations() {{
                one(executer).execute("formatDirRec \"" + TESTFILES_DIR.getAbsolutePath() + "\"");
            }});
            formatter.reformatFilesInDirectoryRecursively(TESTFILES_DIR);
        }
    }

    public class FormatterWithNoRecursiveCommandSpecified {

        private Executer executer;
        private CodeFormatter formatter;

        public CodeFormatter create() {
            executer = mock(Executer.class);
            formatter = new CommandLineCodeFormatter("format %FILE%", "formatDir %DIRECTORY%", null, executer);
            return formatter;
        }

        public void shouldUseDirectoryCommandInsteadOfRecursiveDirectoryCommand() throws IOException {
            checking(new Expectations() {{
                one(executer).execute("formatDir \"" + TESTFILES_DIR.getAbsolutePath() + "\"");
                one(executer).execute("formatDir \"" + TESTFILES_SUBDIR.getAbsolutePath() + "\"");
            }});
            formatter.reformatFilesInDirectoryRecursively(TESTFILES_DIR);
        }
    }

    public class FormatterWithNoDirectoryCommandSpecified {

        private Executer executer;
        private CodeFormatter formatter;

        public CodeFormatter create() {
            executer = mock(Executer.class);
            formatter = new CommandLineCodeFormatter("format %FILE%", null, null, executer);
            return formatter;
        }

        public void shouldUseSingleFileCommandInsteadOfDirectoryCommand() throws IOException {
            checking(new Expectations() {{
                one(executer).execute("format \"" + FOO_FILE.getAbsolutePath() + "\"");
                one(executer).execute("format \"" + BAR_FILE.getAbsolutePath() + "\"");
            }});
            formatter.reformatFilesInDirectory(TESTFILES_DIR);
        }

        public void shouldUseSingleFileCommandInsteadOfRecursiveDirectoryCommand() throws IOException {
            checking(new Expectations() {{
                one(executer).execute("format \"" + FOO_FILE.getAbsolutePath() + "\"");
                one(executer).execute("format \"" + BAR_FILE.getAbsolutePath() + "\"");
                one(executer).execute("format \"" + BAZ_FILE.getAbsolutePath() + "\"");
            }});
            formatter.reformatFilesInDirectoryRecursively(TESTFILES_DIR);
        }
    }

    public class FormatterWithNoSingleFileCommandSpecified {

        private CodeFormatter formatter;

        public CodeFormatter create() {
            Executer executer = mock(Executer.class);
            formatter = new CommandLineCodeFormatter(null, null, null, executer);
            return formatter;
        }

        public void shouldNotFormatSingleFile() {
            specify(new Block() {
                public void run() throws Throwable {
                    formatter.reformatFile(TESTFILES_DIR);
                }
            }, should.raise(IllegalStateException.class));
        }

        public void shouldNotFormatDirectory() {
            specify(new Block() {
                public void run() throws Throwable {
                    formatter.reformatFile(TESTFILES_DIR);
                }
            }, should.raise(IllegalStateException.class));
        }

        public void shouldNotFormatDirectoryRecursively() {
            specify(new Block() {
                public void run() throws Throwable {
                    formatter.reformatFile(TESTFILES_DIR);
                }
            }, should.raise(IllegalStateException.class));
        }
    }
}
