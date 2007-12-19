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
    
    public class WhenFormatterSupportsReformatOne {

        private CodeFormatter formatter;
        private AdaptiveCodeFormatter adapter;

        public AdaptiveCodeFormatter create() {
            formatter = mock(CodeFormatter.class);
            adapter = new AdaptiveCodeFormatter(formatter);
            checking(new Expectations() {{
                allowing (formatter).supportsFileType(with(any(File.class))); will(returnValue(true));
                allowing (formatter).supportsReformatOne();         will(returnValue(true));
                allowing (formatter).supportsReformatMany();        will(returnValue(false));
                allowing (formatter).supportsReformatDirectory();   will(returnValue(false));
                allowing (formatter).supportsReformatRecursively(); will(returnValue(false));
            }});
            return adapter;
        }

        public void adapterShouldSupportReformatOne() {
            specify(adapter.supportsReformatOne());
        }

        public void adapterShouldSupportReformatMany() {
            specify(adapter.supportsReformatMany());
        }

        public void adapterShouldSupportReformatDirectory() {
            specify(adapter.supportsReformatDirectory());
        }

        public void adapterShouldSupportReformatRecursively() {
            specify(adapter.supportsReformatRecursively());
        }

        public void theSupportedCommandShouldBeUsedForReformatOne() {
            checking(new Expectations() {{
                one (formatter).reformatOne(FOO_FILE);
            }});
            adapter.reformatOne(FOO_FILE);
        }

        public void theSupportedCommandShouldBeUsedForReformatMany() {
            checking(new Expectations() {{
                one (formatter).reformatOne(FOO_FILE);
                one (formatter).reformatOne(GAZONK_FILE);
            }});
            adapter.reformatMany(FOO_FILE, GAZONK_FILE);
        }

        public void theSupportedCommandShouldBeUsedForReformatDirectory() {
            checking(new Expectations() {{
                one (formatter).reformatOne(FOO_FILE);
                one (formatter).reformatOne(BAR_FILE);
            }});
            adapter.reformatDirectory(TESTFILES_DIR);
        }

        public void theSupportedCommandShouldBeUsedForReformatRecursively() {
            checking(new Expectations() {{
                one (formatter).reformatOne(FOO_FILE);
                one (formatter).reformatOne(BAR_FILE);
                one (formatter).reformatOne(GAZONK_FILE);
            }});
            adapter.reformatRecursively(TESTFILES_DIR);
        }
    }

    public class WhenFormatterSupportsReformatMany {

        private CodeFormatter formatter;
        private AdaptiveCodeFormatter adapter;

        public AdaptiveCodeFormatter create() {
            formatter = mock(CodeFormatter.class);
            adapter = new AdaptiveCodeFormatter(formatter);
            checking(new Expectations() {{
                allowing (formatter).supportsFileType(with(any(File.class))); will(returnValue(true));
                allowing (formatter).supportsReformatOne();         will(returnValue(false));
                allowing (formatter).supportsReformatMany();        will(returnValue(true));
                allowing (formatter).supportsReformatDirectory();   will(returnValue(false));
                allowing (formatter).supportsReformatRecursively(); will(returnValue(false));
            }});
            return adapter;
        }

        public void adapterShouldSupportReformatOne() {
            specify(adapter.supportsReformatOne());
        }

        public void adapterShouldSupportReformatMany() {
            specify(adapter.supportsReformatMany());
        }

        public void adapterShouldSupportReformatDirectory() {
            specify(adapter.supportsReformatDirectory());
        }

        public void adapterShouldSupportReformatRecursively() {
            specify(adapter.supportsReformatRecursively());
        }

        public void theSupportedCommandShouldBeUsedForReformatOne() {
            checking(new Expectations() {{
                one (formatter).reformatMany(FOO_FILE);
            }});
            adapter.reformatOne(FOO_FILE);
        }

        public void theSupportedCommandShouldBeUsedForReformatMany() {
            checking(new Expectations() {{
                one (formatter).reformatMany(FOO_FILE, GAZONK_FILE);
            }});
            adapter.reformatMany(FOO_FILE, GAZONK_FILE);
        }

        public void theSupportedCommandShouldBeUsedForReformatDirectory() {
            checking(new Expectations() {{
                one (formatter).reformatMany(BAR_FILE, FOO_FILE);
            }});
            adapter.reformatDirectory(TESTFILES_DIR);
        }

        public void theSupportedCommandShouldBeUsedForReformatRecursively() {
            checking(new Expectations() {{
                one (formatter).reformatMany(BAR_FILE, FOO_FILE);
                one (formatter).reformatMany(GAZONK_FILE);
            }});
            adapter.reformatRecursively(TESTFILES_DIR);
        }
    }

    public class WhenFormatterSupportsReformatDirectory {

        private CodeFormatter formatter;
        private AdaptiveCodeFormatter adapter;

        public AdaptiveCodeFormatter create() {
            formatter = mock(CodeFormatter.class);
            adapter = new AdaptiveCodeFormatter(formatter);
            checking(new Expectations() {{
                allowing (formatter).supportsFileType(with(any(File.class))); will(returnValue(true));
                allowing (formatter).supportsReformatOne();         will(returnValue(false));
                allowing (formatter).supportsReformatMany();        will(returnValue(false));
                allowing (formatter).supportsReformatDirectory();   will(returnValue(true));
                allowing (formatter).supportsReformatRecursively(); will(returnValue(false));
            }});
            return adapter;
        }

        public void adapterShouldNotSupportReformatOne() {
            specify(should.not().be.supportsReformatOne());
        }

        public void adapterShouldNotSupportReformatMany() {
            specify(should.not().be.supportsReformatMany());
        }

        public void adapterShouldSupportReformatDirectory() {
            specify(adapter.supportsReformatDirectory());
        }

        public void adapterShouldSupportReformatRecursively() {
            specify(adapter.supportsReformatRecursively());
        }

        public void itShouldBeAnErrorToUseReformatOne() {
            specify(new Block() {
                public void run() throws Throwable {
                    adapter.reformatOne(FOO_FILE);
                }
            }, should.raise(UnsupportedOperationException.class));
        }

        public void itShouldBeAnErrorToUseReformatMany() {
            specify(new Block() {
                public void run() throws Throwable {
                    adapter.reformatMany(FOO_FILE, GAZONK_FILE);
                }
            }, should.raise(UnsupportedOperationException.class));
        }

        public void theSupportedCommandShouldBeUsedForReformatDirectory() {
            checking(new Expectations() {{
                one (formatter).reformatDirectory(TESTFILES_DIR);
            }});
            adapter.reformatDirectory(TESTFILES_DIR);
        }

        public void theSupportedCommandShouldBeUsedForReformatRecursively() {
            checking(new Expectations() {{
                one (formatter).reformatDirectory(TESTFILES_DIR);
                one (formatter).reformatDirectory(TESTFILES_SUBDIR);
            }});
            adapter.reformatRecursively(TESTFILES_DIR);
        }
    }

    public class WhenFormatterSupportsReformatRecursively {

        private CodeFormatter formatter;
        private AdaptiveCodeFormatter adapter;

        public AdaptiveCodeFormatter create() {
            formatter = mock(CodeFormatter.class);
            adapter = new AdaptiveCodeFormatter(formatter);
            checking(new Expectations() {{
                allowing (formatter).supportsFileType(with(any(File.class))); will(returnValue(true));
                allowing (formatter).supportsReformatOne();         will(returnValue(false));
                allowing (formatter).supportsReformatMany();        will(returnValue(false));
                allowing (formatter).supportsReformatDirectory();   will(returnValue(false));
                allowing (formatter).supportsReformatRecursively(); will(returnValue(true));
            }});
            return adapter;
        }

        public void adapterShouldNotSupportReformatOne() {
            specify(should.not().be.supportsReformatOne());
        }

        public void adapterShouldNotSupportReformatMany() {
            specify(should.not().be.supportsReformatMany());
        }

        public void adapterShouldNotSupportReformatDirectory() {
            specify(should.not().be.supportsReformatDirectory());
        }

        public void adapterShouldSupportReformatRecursively() {
            specify(adapter.supportsReformatRecursively());
        }

        public void itShouldBeAnErrorToUseReformatOne() {
            specify(new Block() {
                public void run() throws Throwable {
                    adapter.reformatOne(FOO_FILE);
                }
            }, should.raise(UnsupportedOperationException.class));
        }

        public void itShouldBeAnErrorToUseReformatMany() {
            specify(new Block() {
                public void run() throws Throwable {
                    adapter.reformatMany(FOO_FILE, GAZONK_FILE);
                }
            }, should.raise(UnsupportedOperationException.class));
        }

        public void itShouldBeAnErrorToUseReformatDirectory() {
            specify(new Block() {
                public void run() throws Throwable {
                    adapter.reformatDirectory(TESTFILES_DIR);
                }
            }, should.raise(UnsupportedOperationException.class));
        }

        public void theSupportedCommandShouldBeUsedForReformatRecursively() {
            checking(new Expectations() {{
                one (formatter).reformatRecursively(TESTFILES_DIR);
            }});
            adapter.reformatRecursively(TESTFILES_DIR);
        }
    }

    public class WhenFormatterSupportsNothing {

        private CodeFormatter formatter;
        private AdaptiveCodeFormatter adapter;

        public AdaptiveCodeFormatter create() {
            formatter = mock(CodeFormatter.class);
            adapter = new AdaptiveCodeFormatter(formatter);
            checking(new Expectations() {{
                allowing (formatter).supportsFileType(with(any(File.class))); will(returnValue(true));
                allowing (formatter).supportsReformatOne();         will(returnValue(false));
                allowing (formatter).supportsReformatMany();        will(returnValue(false));
                allowing (formatter).supportsReformatDirectory();   will(returnValue(false));
                allowing (formatter).supportsReformatRecursively(); will(returnValue(false));
            }});
            return adapter;
        }

        public void adapterShouldNotSupportReformatOne() {
            specify(should.not().be.supportsReformatOne());
        }

        public void adapterShouldNotSupportReformatMany() {
            specify(should.not().be.supportsReformatMany());
        }

        public void adapterShouldNotSupportReformatDirectory() {
            specify(should.not().be.supportsReformatDirectory());
        }

        public void adapterShouldNotSupportReformatRecursively() {
            specify(should.not().be.supportsReformatRecursively());
        }

        public void itShouldBeAnErrorToUseReformatOne() {
            specify(new Block() {
                public void run() throws Throwable {
                    adapter.reformatOne(FOO_FILE);
                }
            }, should.raise(UnsupportedOperationException.class));
        }

        public void itShouldBeAnErrorToUseReformatMany() {
            specify(new Block() {
                public void run() throws Throwable {
                    adapter.reformatMany(FOO_FILE, GAZONK_FILE);
                }
            }, should.raise(UnsupportedOperationException.class));
        }

        public void itShouldBeAnErrorToUseReformatDirectory() {
            specify(new Block() {
                public void run() throws Throwable {
                    adapter.reformatDirectory(TESTFILES_DIR);
                }
            }, should.raise(UnsupportedOperationException.class));
        }

        public void itShouldBeAnErrorToUseReformatRecursively() {
            specify(new Block() {
                public void run() throws Throwable {
                    adapter.reformatRecursively(TESTFILES_DIR);
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
                allowing (formatter).supportsFileType(FOO_FILE);    will(returnValue(false));
                allowing (formatter).supportsFileType(BAR_FILE);    will(returnValue(true));
                allowing (formatter).supportsReformatOne();         will(returnValue(true));
                allowing (formatter).supportsReformatMany();        will(returnValue(false));
                allowing (formatter).supportsReformatDirectory();   will(returnValue(false));
                allowing (formatter).supportsReformatRecursively(); will(returnValue(false));
            }});
            return adapter;
        }

        public void shouldReformatOnlyTheSupportedFiles() {
            checking(new Expectations() {{
                one (formatter).reformatOne(BAR_FILE);
            }});
            adapter.reformatDirectory(TESTFILES_DIR);
        }
    }
}
