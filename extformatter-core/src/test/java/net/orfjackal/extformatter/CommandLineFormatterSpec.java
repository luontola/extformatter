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

import jdave.Specification;
import jdave.junit4.JDaveRunner;
import org.jmock.Expectations;
import org.junit.runner.RunWith;

import java.io.File;

/**
 * @author Esko Luontola
 * @since 30.11.2007
 */
@RunWith(JDaveRunner.class)
public class CommandLineFormatterSpec extends Specification<Formatter> {

    public class FormatterWithAllCommandsSpecified {

        private Executer executer;
        private Formatter formatter;

        public Formatter create() {
            executer = mock(Executer.class);
            formatter = new CommandLineFormatter(executer, "format %FILE%", "formatDir %FILE%", "formatDirRec %FILE%");
            return formatter;
        }

        public void shouldExecuteCommandForSingleFile() {
            final File file = TestResources.FOO_FILE;
            checking(new Expectations() {{
                one(executer).execute("format " + file);
            }});
            formatter.reformatSingleFile(file);
        }

        public void shouldExecuteCommandForFilesInDirectory() {
            final File directory = TestResources.TEST_DIR;
            checking(new Expectations() {{
                one(executer).execute("formatDir " + directory);
            }});
            formatter.reformatFilesInDirectory(directory);
        }

        public void shouldExecuteCommandForFilesInDirectoryRecursively() {
            final File directory = TestResources.TEST_DIR;
            checking(new Expectations() {{
                one(executer).execute("formatDirRec " + directory);
            }});
            formatter.reformatFilesInDirectoryRecursively(directory);
        }
    }
}
