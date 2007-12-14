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
            formatter = new CommandLineCodeFormatter(
                    SUPPORTS_TXT, "format %FILE%", "formatDir %DIRECTORY%", "formatDirRec %DIRECTORY%", executer);
            return formatter;
        }

        public void shouldExecuteCommandForReformatOne() throws IOException {
            checking(new Expectations() {{
                one (executer).execute("format \"" + FOO_FILE.getAbsolutePath() + "\"");
            }});
            formatter.reformatOne(FOO_FILE);
        }

        public void shouldExecuteCommandForReformatDirectory() throws IOException {
            checking(new Expectations() {{
                one (executer).execute("formatDir \"" + TESTFILES_DIR.getAbsolutePath() + "\"");
            }});
            formatter.reformatDirectory(TESTFILES_DIR);
        }

        public void shouldExecuteCommandForReformatRecursively() throws IOException {
            checking(new Expectations() {{
                one (executer).execute("formatDirRec \"" + TESTFILES_DIR.getAbsolutePath() + "\"");
            }});
            formatter.reformatRecursively(TESTFILES_DIR);
        }

        public void shouldNotAllowFormattingANonExistingFile() {
            specify(new Block() {
                public void run() throws Throwable {
                    formatter.reformatOne(new File("doesNotExist"));
                }
            }, should.raise(IllegalArgumentException.class));
        }

        public void shouldNotAllowFormattingANonExistingDirectory() {
            specify(new Block() {
                public void run() throws Throwable {
                    formatter.reformatDirectory(new File("doesNotExist"));
                }
            }, should.raise(IllegalArgumentException.class));
        }

        public void shouldNotAcceptADirectoryAsAFile() {
            specify(new Block() {
                public void run() throws Throwable {
                    formatter.reformatOne(TESTFILES_DIR);
                }
            }, should.raise(IllegalArgumentException.class));
        }

        public void shouldNotAcceptAFileAsADirectory() {
            specify(new Block() {
                public void run() throws Throwable {
                    formatter.reformatDirectory(FOO_FILE);
                }
            }, should.raise(IllegalArgumentException.class));
        }

        public void shouldSupportOnlyTheSpecifiedFileTypes() {
            specify(formatter.supportsFileType(TXT_FILE));
            specify(should.not().be.supportsFileType(XML_FILE));
        }
    }

    public class WhenOnlyReformatOneCommandIsSpecified {

        private CodeFormatter formatter;

        public CodeFormatter create() {
            formatter = new CommandLineCodeFormatter(SUPPORTS_TXT, "format %FILE%", null, null);
            return formatter;
        }

        public void shouldSupportReformatOne() {
            specify(formatter.supportsReformatOne());
        }

        public void shouldNotSupportTheOthers() {
            specify(should.not().be.supportsReformatMany());
            specify(should.not().be.supportsReformatDirectory());
            specify(should.not().be.supportsReformatRecursively());
        }
    }

    public class WhenOnlyReformatDirectoryCommandIsSpecified {

        private CodeFormatter formatter;

        public CodeFormatter create() {
            formatter = new CommandLineCodeFormatter(SUPPORTS_TXT, null, "formatDir %DIRECTORY%", null);
            return formatter;
        }

        public void shouldSupportReformatDirectory() {
            specify(formatter.supportsReformatDirectory());
        }

        public void shouldNotSupportTheOthers() {
            specify(should.not().be.supportsReformatOne());
            specify(should.not().be.supportsReformatMany());
            specify(should.not().be.supportsReformatRecursively());
        }
    }

    public class WhenOnlyReformatRecursivelyCommandIsSpecified {

        private CodeFormatter formatter;

        public CodeFormatter create() {
            formatter = new CommandLineCodeFormatter(SUPPORTS_TXT, null, null, "formatDirRec %DIRECTORY%");
            return formatter;
        }

        public void shouldSupportReformatRecursively() {
            specify(formatter.supportsReformatRecursively());
        }

        public void shouldNotSupportTheOthers() {
            specify(should.not().be.supportsReformatOne());
            specify(should.not().be.supportsReformatMany());
            specify(should.not().be.supportsReformatDirectory());
        }
    }
}
