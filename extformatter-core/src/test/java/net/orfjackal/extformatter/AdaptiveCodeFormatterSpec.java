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
import org.jmock.Expectations;
import org.junit.runner.RunWith;

/**
 * @author Esko Luontola
 * @since 6.12.2007
 */
@RunWith(JDaveRunner.class)
public class AdaptiveCodeFormatterSpec extends Specification<AdaptiveCodeFormatter> {

    public class WhenFormatterSupportsReformatFile {

        private CodeFormatter formatter;
        private AdaptiveCodeFormatter adapter;

        public AdaptiveCodeFormatter create() {
            formatter = mock(CodeFormatter.class);
            adapter = new AdaptiveCodeFormatter(formatter);
            checking(new Expectations() {{
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
                one(formatter).reformatFile(TestResources.FOO_FILE);
            }});
            adapter.reformatFile(TestResources.FOO_FILE);
        }

        public void theSupportedCommandShouldBeUsedForReformatFiles() {
            checking(new Expectations() {{
                one(formatter).reformatFile(TestResources.FOO_FILE);
                one(formatter).reformatFile(TestResources.GAZONK_FILE);
            }});
            adapter.reformatFiles(TestResources.FOO_FILE, TestResources.GAZONK_FILE);
        }

        public void theSupportedCommandShouldBeUsedForReformatFilesInDirectory() {
            checking(new Expectations() {{
                one(formatter).reformatFile(TestResources.FOO_FILE);
                one(formatter).reformatFile(TestResources.BAR_FILE);
            }});
            adapter.reformatFilesInDirectory(TestResources.TESTFILES_DIR);
        }

        public void theSupportedCommandShouldBeUsedForReformatFilesInDirectoryRecursively() {
            checking(new Expectations() {{
                one(formatter).reformatFile(TestResources.FOO_FILE);
                one(formatter).reformatFile(TestResources.BAR_FILE);
                one(formatter).reformatFile(TestResources.GAZONK_FILE);
            }});
            adapter.reformatFilesInDirectoryRecursively(TestResources.TESTFILES_DIR);
        }
    }

    public class WhenFormatterSupportsReformatFiles {

        private CodeFormatter formatter;
        private AdaptiveCodeFormatter adapter;

        public AdaptiveCodeFormatter create() {
            formatter = mock(CodeFormatter.class);
            adapter = new AdaptiveCodeFormatter(formatter);
            checking(new Expectations() {{
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
                one(formatter).reformatFiles(TestResources.FOO_FILE);
            }});
            adapter.reformatFile(TestResources.FOO_FILE);
        }

        public void theSupportedCommandShouldBeUsedForReformatFiles() {
            checking(new Expectations() {{
                one(formatter).reformatFiles(TestResources.FOO_FILE, TestResources.GAZONK_FILE);
            }});
            adapter.reformatFiles(TestResources.FOO_FILE, TestResources.GAZONK_FILE);
        }

        public void theSupportedCommandShouldBeUsedForReformatFilesInDirectory() {
            checking(new Expectations() {{
                one(formatter).reformatFiles(TestResources.BAR_FILE, TestResources.FOO_FILE);
            }});
            adapter.reformatFilesInDirectory(TestResources.TESTFILES_DIR);
        }

        public void theSupportedCommandShouldBeUsedForReformatFilesInDirectoryRecursively() {
            checking(new Expectations() {{
                one(formatter).reformatFiles(TestResources.BAR_FILE, TestResources.FOO_FILE);
                one(formatter).reformatFiles(TestResources.GAZONK_FILE);
            }});
            adapter.reformatFilesInDirectoryRecursively(TestResources.TESTFILES_DIR);
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
                    adapter.reformatFile(TestResources.FOO_FILE);
                }
            }, should.raise(UnsupportedOperationException.class));
        }

        public void itShouldBeAnErrorToUseReformatFiles() {
            specify(new Block() {
                public void run() throws Throwable {
                    adapter.reformatFiles(TestResources.FOO_FILE, TestResources.GAZONK_FILE);
                }
            }, should.raise(UnsupportedOperationException.class));
        }

        public void theSupportedCommandShouldBeUsedForReformatFilesInDirectory() {
            checking(new Expectations() {{
                one(formatter).reformatFilesInDirectory(TestResources.TESTFILES_DIR);
            }});
            adapter.reformatFilesInDirectory(TestResources.TESTFILES_DIR);
        }

        public void theSupportedCommandShouldBeUsedForReformatFilesInDirectoryRecursively() {
            checking(new Expectations() {{
                one(formatter).reformatFilesInDirectory(TestResources.TESTFILES_DIR);
                one(formatter).reformatFilesInDirectory(TestResources.TESTFILES_SUBDIR);
            }});
            adapter.reformatFilesInDirectoryRecursively(TestResources.TESTFILES_DIR);
        }
    }

    public class WhenFormatterSupportsReformatFilesInDirectoryRecursively {

        private CodeFormatter formatter;
        private AdaptiveCodeFormatter adapter;

        public AdaptiveCodeFormatter create() {
            formatter = mock(CodeFormatter.class);
            adapter = new AdaptiveCodeFormatter(formatter);
            checking(new Expectations() {{
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
                    adapter.reformatFile(TestResources.FOO_FILE);
                }
            }, should.raise(UnsupportedOperationException.class));
        }

        public void itShouldBeAnErrorToUseReformatFiles() {
            specify(new Block() {
                public void run() throws Throwable {
                    adapter.reformatFiles(TestResources.FOO_FILE, TestResources.GAZONK_FILE);
                }
            }, should.raise(UnsupportedOperationException.class));
        }

        public void itShouldBeAnErrorToUseReformatFilesInDirectory() {
            specify(new Block() {
                public void run() throws Throwable {
                    adapter.reformatFilesInDirectory(TestResources.TESTFILES_DIR);
                }
            }, should.raise(UnsupportedOperationException.class));
        }

        public void theSupportedCommandShouldBeUsedForReformatFilesInDirectoryRecursively() {
            checking(new Expectations() {{
                one(formatter).reformatFilesInDirectoryRecursively(TestResources.TESTFILES_DIR);
            }});
            adapter.reformatFilesInDirectoryRecursively(TestResources.TESTFILES_DIR);
        }
    }

    public class WhenFormatterSupportsNothing {

        private CodeFormatter formatter;
        private AdaptiveCodeFormatter adapter;

        public AdaptiveCodeFormatter create() {
            formatter = mock(CodeFormatter.class);
            adapter = new AdaptiveCodeFormatter(formatter);
            checking(new Expectations() {{
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
                    adapter.reformatFile(TestResources.FOO_FILE);
                }
            }, should.raise(UnsupportedOperationException.class));
        }

        public void itShouldBeAnErrorToUseReformatFiles() {
            specify(new Block() {
                public void run() throws Throwable {
                    adapter.reformatFiles(TestResources.FOO_FILE, TestResources.GAZONK_FILE);
                }
            }, should.raise(UnsupportedOperationException.class));
        }

        public void itShouldBeAnErrorToUseReformatFilesInDirectory() {
            specify(new Block() {
                public void run() throws Throwable {
                    adapter.reformatFilesInDirectory(TestResources.TESTFILES_DIR);
                }
            }, should.raise(UnsupportedOperationException.class));
        }

        public void itShouldBeAnErrorToUseReformatFilesInDirectoryRecursively() {
            specify(new Block() {
                public void run() throws Throwable {
                    adapter.reformatFilesInDirectoryRecursively(TestResources.TESTFILES_DIR);
                }
            }, should.raise(UnsupportedOperationException.class));
        }
    }
}
