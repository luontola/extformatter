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

/**
 * @author Esko Luontola
 * @since 6.12.2007
 */
@RunWith(JDaveRunner.class)
public class AdaptiveCodeFormatterSpec extends Specification<AdaptiveCodeFormatter> {
    
    // TODO: improve the readability of the tests

    public class WhenFormatterSupportsReformatFile {

        private CodeFormatter formatter;
        private AdaptiveCodeFormatter adapter;

        public AdaptiveCodeFormatter create() {
            formatter = mock(CodeFormatter.class);
            adapter = new AdaptiveCodeFormatter(formatter);
            checking(new Expectations() {{
                allowing(formatter).supportsFileType(with(any(File.class))); will(returnValue(true));
                allowing(formatter).supportsReformatFile(); will(returnValue(true));
                allowing(formatter).supportsReformatFiles(); will(returnValue(false));
                allowing(formatter).supportsReformatFilesInDirectory(); will(returnValue(false));
                allowing(formatter).supportsReformatFilesInDirectoryRecursively(); will(returnValue(false));
            }});
            return adapter;
        }

        public void adapterShouldSupportReformatFile() {
            specify(adapter.supportsReformatFile());
        }

        public void adapterShouldSupportReformatFiles() {
            specify(adapter.supportsReformatFiles());
        }

        public void adapterShouldSupportReformatFilesInDirectory() {
            specify(adapter.supportsReformatFilesInDirectory());
        }

        public void adapterShouldSupportReformatFilesInDirectoryRecursively() {
            specify(adapter.supportsReformatFilesInDirectoryRecursively());
        }

        public void theSupportedCommandShouldBeUsedForReformatFile() {
            checking(new Expectations() {{
                one(formatter).reformatFile(FOO_FILE);
            }});
            adapter.reformatFile(FOO_FILE);
        }

        public void theSupportedCommandShouldBeUsedForReformatFiles() {
            checking(new Expectations() {{
                one(formatter).reformatFile(FOO_FILE);
                one(formatter).reformatFile(GAZONK_FILE);
            }});
            adapter.reformatFiles(FOO_FILE, GAZONK_FILE);
        }

        public void theSupportedCommandShouldBeUsedForReformatFilesInDirectory() {
            checking(new Expectations() {{
                one(formatter).reformatFile(FOO_FILE);
                one(formatter).reformatFile(BAR_FILE);
            }});
            adapter.reformatFilesInDirectory(TESTFILES_DIR);
        }

        public void theSupportedCommandShouldBeUsedForReformatFilesInDirectoryRecursively() {
            checking(new Expectations() {{
                one(formatter).reformatFile(FOO_FILE);
                one(formatter).reformatFile(BAR_FILE);
                one(formatter).reformatFile(GAZONK_FILE);
            }});
            adapter.reformatFilesInDirectoryRecursively(TESTFILES_DIR);
        }
    }

    public class WhenFormatterSupportsReformatFiles {

        private CodeFormatter formatter;
        private AdaptiveCodeFormatter adapter;

        public AdaptiveCodeFormatter create() {
            formatter = mock(CodeFormatter.class);
            adapter = new AdaptiveCodeFormatter(formatter);
            checking(new Expectations() {{
                allowing(formatter).supportsFileType(with(any(File.class))); will(returnValue(true));
                allowing(formatter).supportsReformatFile(); will(returnValue(false));
                allowing(formatter).supportsReformatFiles(); will(returnValue(true));
                allowing(formatter).supportsReformatFilesInDirectory(); will(returnValue(false));
                allowing(formatter).supportsReformatFilesInDirectoryRecursively(); will(returnValue(false));
            }});
            return adapter;
        }

        public void adapterShouldSupportReformatFile() {
            specify(adapter.supportsReformatFile());
        }

        public void adapterShouldSupportReformatFiles() {
            specify(adapter.supportsReformatFiles());
        }

        public void adapterShouldSupportReformatFilesInDirectory() {
            specify(adapter.supportsReformatFilesInDirectory());
        }

        public void adapterShouldSupportReformatFilesInDirectoryRecursively() {
            specify(adapter.supportsReformatFilesInDirectoryRecursively());
        }

        public void theSupportedCommandShouldBeUsedForReformatFile() {
            checking(new Expectations() {{
                one(formatter).reformatFiles(FOO_FILE);
            }});
            adapter.reformatFile(FOO_FILE);
        }

        public void theSupportedCommandShouldBeUsedForReformatFiles() {
            checking(new Expectations() {{
                one(formatter).reformatFiles(FOO_FILE, GAZONK_FILE);
            }});
            adapter.reformatFiles(FOO_FILE, GAZONK_FILE);
        }

        public void theSupportedCommandShouldBeUsedForReformatFilesInDirectory() {
            checking(new Expectations() {{
                one(formatter).reformatFiles(BAR_FILE, FOO_FILE);
            }});
            adapter.reformatFilesInDirectory(TESTFILES_DIR);
        }

        public void theSupportedCommandShouldBeUsedForReformatFilesInDirectoryRecursively() {
            checking(new Expectations() {{
                one(formatter).reformatFiles(BAR_FILE, FOO_FILE);
                one(formatter).reformatFiles(GAZONK_FILE);
            }});
            adapter.reformatFilesInDirectoryRecursively(TESTFILES_DIR);
        }
    }

    public class WhenFormatterSupportsReformatFilesInDirectory {

        private CodeFormatter formatter;
        private AdaptiveCodeFormatter adapter;

        public AdaptiveCodeFormatter create() {
            formatter = mock(CodeFormatter.class);
            adapter = new AdaptiveCodeFormatter(formatter);
            checking(new Expectations() {{
                allowing(formatter).supportsReformatFile(); will(returnValue(false));
                allowing(formatter).supportsReformatFiles(); will(returnValue(false));
                allowing(formatter).supportsReformatFilesInDirectory(); will(returnValue(true));
                allowing(formatter).supportsReformatFilesInDirectoryRecursively(); will(returnValue(false));
            }});
            return adapter;
        }

        public void adapterShouldNotSupportReformatFile() {
            specify(should.not().be.supportsReformatFile());
        }

        public void adapterShouldNotSupportReformatFiles() {
            specify(should.not().be.supportsReformatFiles());
        }

        public void adapterShouldSupportReformatFilesInDirectory() {
            specify(adapter.supportsReformatFilesInDirectory());
        }

        public void adapterShouldSupportReformatFilesInDirectoryRecursively() {
            specify(adapter.supportsReformatFilesInDirectoryRecursively());
        }

        public void itShouldBeAnErrorToUseReformatFile() {
            specify(new Block() {
                public void run() throws Throwable {
                    adapter.reformatFile(FOO_FILE);
                }
            }, should.raise(UnsupportedOperationException.class));
        }

        public void itShouldBeAnErrorToUseReformatFiles() {
            specify(new Block() {
                public void run() throws Throwable {
                    adapter.reformatFiles(FOO_FILE, GAZONK_FILE);
                }
            }, should.raise(UnsupportedOperationException.class));
        }

        public void theSupportedCommandShouldBeUsedForReformatFilesInDirectory() {
            checking(new Expectations() {{
                one(formatter).reformatFilesInDirectory(TESTFILES_DIR);
            }});
            adapter.reformatFilesInDirectory(TESTFILES_DIR);
        }

        public void theSupportedCommandShouldBeUsedForReformatFilesInDirectoryRecursively() {
            checking(new Expectations() {{
                one(formatter).reformatFilesInDirectory(TESTFILES_DIR);
                one(formatter).reformatFilesInDirectory(TESTFILES_SUBDIR);
            }});
            adapter.reformatFilesInDirectoryRecursively(TESTFILES_DIR);
        }
    }

    public class WhenFormatterSupportsReformatFilesInDirectoryRecursively {

        private CodeFormatter formatter;
        private AdaptiveCodeFormatter adapter;

        public AdaptiveCodeFormatter create() {
            formatter = mock(CodeFormatter.class);
            adapter = new AdaptiveCodeFormatter(formatter);
            checking(new Expectations() {{
                allowing(formatter).supportsFileType(with(any(File.class))); will(returnValue(true));
                allowing(formatter).supportsReformatFile(); will(returnValue(false));
                allowing(formatter).supportsReformatFiles(); will(returnValue(false));
                allowing(formatter).supportsReformatFilesInDirectory(); will(returnValue(false));
                allowing(formatter).supportsReformatFilesInDirectoryRecursively(); will(returnValue(true));
            }});
            return adapter;
        }

        public void adapterShouldNotSupportReformatFile() {
            specify(should.not().be.supportsReformatFile());
        }

        public void adapterShouldNotSupportReformatFiles() {
            specify(should.not().be.supportsReformatFiles());
        }

        public void adapterShouldNotSupportReformatFilesInDirectory() {
            specify(should.not().be.supportsReformatFilesInDirectory());
        }

        public void adapterShouldSupportReformatFilesInDirectoryRecursively() {
            specify(adapter.supportsReformatFilesInDirectoryRecursively());
        }

        public void itShouldBeAnErrorToUseReformatFile() {
            specify(new Block() {
                public void run() throws Throwable {
                    adapter.reformatFile(FOO_FILE);
                }
            }, should.raise(UnsupportedOperationException.class));
        }

        public void itShouldBeAnErrorToUseReformatFiles() {
            specify(new Block() {
                public void run() throws Throwable {
                    adapter.reformatFiles(FOO_FILE, GAZONK_FILE);
                }
            }, should.raise(UnsupportedOperationException.class));
        }

        public void itShouldBeAnErrorToUseReformatFilesInDirectory() {
            specify(new Block() {
                public void run() throws Throwable {
                    adapter.reformatFilesInDirectory(TESTFILES_DIR);
                }
            }, should.raise(UnsupportedOperationException.class));
        }

        public void theSupportedCommandShouldBeUsedForReformatFilesInDirectoryRecursively() {
            checking(new Expectations() {{
                one(formatter).reformatFilesInDirectoryRecursively(TESTFILES_DIR);
            }});
            adapter.reformatFilesInDirectoryRecursively(TESTFILES_DIR);
        }
    }

    public class WhenFormatterSupportsNothing {

        private CodeFormatter formatter;
        private AdaptiveCodeFormatter adapter;

        public AdaptiveCodeFormatter create() {
            formatter = mock(CodeFormatter.class);
            adapter = new AdaptiveCodeFormatter(formatter);
            checking(new Expectations() {{
                allowing(formatter).supportsFileType(with(any(File.class))); will(returnValue(true));
                allowing(formatter).supportsReformatFile(); will(returnValue(false));
                allowing(formatter).supportsReformatFiles(); will(returnValue(false));
                allowing(formatter).supportsReformatFilesInDirectory(); will(returnValue(false));
                allowing(formatter).supportsReformatFilesInDirectoryRecursively(); will(returnValue(false));
            }});
            return adapter;
        }

        public void adapterShouldNotSupportReformatFile() {
            specify(should.not().be.supportsReformatFile());
        }

        public void adapterShouldNotSupportReformatFiles() {
            specify(should.not().be.supportsReformatFiles());
        }

        public void adapterShouldNotSupportReformatFilesInDirectory() {
            specify(should.not().be.supportsReformatFilesInDirectory());
        }

        public void adapterShouldNotSupportReformatFilesInDirectoryRecursively() {
            specify(should.not().be.supportsReformatFilesInDirectoryRecursively());
        }

        public void itShouldBeAnErrorToUseReformatFile() {
            specify(new Block() {
                public void run() throws Throwable {
                    adapter.reformatFile(FOO_FILE);
                }
            }, should.raise(UnsupportedOperationException.class));
        }

        public void itShouldBeAnErrorToUseReformatFiles() {
            specify(new Block() {
                public void run() throws Throwable {
                    adapter.reformatFiles(FOO_FILE, GAZONK_FILE);
                }
            }, should.raise(UnsupportedOperationException.class));
        }

        public void itShouldBeAnErrorToUseReformatFilesInDirectory() {
            specify(new Block() {
                public void run() throws Throwable {
                    adapter.reformatFilesInDirectory(TESTFILES_DIR);
                }
            }, should.raise(UnsupportedOperationException.class));
        }

        public void itShouldBeAnErrorToUseReformatFilesInDirectoryRecursively() {
            specify(new Block() {
                public void run() throws Throwable {
                    adapter.reformatFilesInDirectoryRecursively(TESTFILES_DIR);
                }
            }, should.raise(UnsupportedOperationException.class));
        }
    }

    public class WhenDirectoryContainsAlsoUnsupportedFileTypes {

        private CodeFormatter formatter;
        private AdaptiveCodeFormatter adapter;

        public AdaptiveCodeFormatter create() {
            formatter = mock(CodeFormatter.class);
            adapter = new AdaptiveCodeFormatter(formatter);
            checking(new Expectations() {{
                allowing(formatter).supportsFileType(FOO_FILE); will(returnValue(false));
                allowing(formatter).supportsFileType(BAR_FILE); will(returnValue(true));
                allowing(formatter).supportsReformatFile(); will(returnValue(true));
                allowing(formatter).supportsReformatFiles(); will(returnValue(false));
                allowing(formatter).supportsReformatFilesInDirectory(); will(returnValue(false));
                allowing(formatter).supportsReformatFilesInDirectoryRecursively(); will(returnValue(false));
            }});
            return adapter;
        }

        public void shouldReformatOnlyTheSupportedFiles() {
            checking(new Expectations() {{
                one(formatter).reformatFile(BAR_FILE);
            }});
            adapter.reformatFilesInDirectory(TESTFILES_DIR);
        }
    }
}
