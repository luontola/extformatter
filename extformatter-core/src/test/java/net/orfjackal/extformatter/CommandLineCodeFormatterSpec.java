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
import static net.orfjackal.extformatter.TestResources.FOO_FILE;
import static net.orfjackal.extformatter.TestResources.TESTFILES_DIR;
import org.jmock.Expectations;
import org.junit.runner.RunWith;

import java.io.File;
import java.io.IOException;

/**
 * @author Esko Luontola
 * @since 30.11.2007
 */
@RunWith(JDaveRunner.class)
public class CommandLineCodeFormatterSpec extends Specification<CodeFormatter> {

    public class ACommandLineCodeFormatter {

        private Executer executer;
        private CodeFormatter formatter;

        public CodeFormatter create() {
            executer = mock(Executer.class);
            formatter = new CommandLineCodeFormatter("format %FILE%", "formatDir %DIRECTORY%", "formatDirRec %DIRECTORY%", executer);
            return formatter;
        }

        public void shouldExecuteCommandForASingleFile() throws IOException {
            checking(new Expectations() {{
                one(executer).execute("format \"" + FOO_FILE.getAbsolutePath() + "\"");
            }});
            formatter.reformatFile(FOO_FILE);
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

        public void shouldNotAllowFormattingANonExistingFile() {
            specify(new Block() {
                public void run() throws Throwable {
                    formatter.reformatFile(new File("doesNotExist"));
                }
            }, should.raise(IllegalArgumentException.class));
        }

        public void shouldNotAllowFormattingANonExistingDirectory() {
            specify(new Block() {
                public void run() throws Throwable {
                    formatter.reformatFilesInDirectory(new File("doesNotExist"));
                }
            }, should.raise(IllegalArgumentException.class));
        }

        public void shouldNotAcceptADirectoryAsAFile() {
            specify(new Block() {
                public void run() throws Throwable {
                    formatter.reformatFile(TESTFILES_DIR);
                }
            }, should.raise(IllegalArgumentException.class));
        }

        public void shouldNotAcceptAFileAsADirectory() {
            specify(new Block() {
                public void run() throws Throwable {
                    formatter.reformatFilesInDirectory(FOO_FILE);
                }
            }, should.raise(IllegalArgumentException.class));
        }
    }

    public class WhenOnlyReformatFileCommandIsSpecified {

        private CodeFormatter formatter;

        public CodeFormatter create() {
            formatter = new CommandLineCodeFormatter("format %FILE%", null, null);
            return formatter;
        }

        public void shouldSupportTheSpecifiedCommand() {
            specify(formatter.supportsReformatFile());
        }

        public void shouldNotSupportOtherCommands() {
            specify(should.not().be.supportsReformatFiles());
            specify(should.not().be.supportsReformatFilesInDirectory());
            specify(should.not().be.supportsReformatFilesInDirectoryRecursively());
        }
    }

    public class WhenOnlyReformatFilesInDirectoryCommandIsSpecified {

        private CodeFormatter formatter;

        public CodeFormatter create() {
            formatter = new CommandLineCodeFormatter(null, "formatDir %DIRECTORY%", null);
            return formatter;
        }

        public void shouldSupportTheSpecifiedCommand() {
            specify(formatter.supportsReformatFilesInDirectory());
        }

        public void shouldNotSupportOtherCommands() {
            specify(should.not().be.supportsReformatFile());
            specify(should.not().be.supportsReformatFiles());
            specify(should.not().be.supportsReformatFilesInDirectoryRecursively());
        }
    }

    public class WhenOnlyReformatFilesInDirectoryRecursivelyCommandIsSpecified {

        private CodeFormatter formatter;

        public CodeFormatter create() {
            formatter = new CommandLineCodeFormatter(null, null, "formatDirRec %DIRECTORY%");
            return formatter;
        }

        public void shouldSupportTheSpecifiedCommand() {
            specify(formatter.supportsReformatFilesInDirectoryRecursively());
        }

        public void shouldNotSupportOtherCommands() {
            specify(should.not().be.supportsReformatFile());
            specify(should.not().be.supportsReformatFiles());
            specify(should.not().be.supportsReformatFilesInDirectory());
        }
    }
}
