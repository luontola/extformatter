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
 * @since 7.12.2007
 */
@RunWith(JDaveRunner.class)
public class OptimizingReformatQueueSpec extends Specification<ReformatQueue> {

    public class ACodeFormatterQueue {

        private CodeFormatter formatter;
        private ReformatQueue queue;

        public ReformatQueue create() {
            formatter = mock(CodeFormatter.class);
            queue = new OptimizingReformatQueue(formatter);
            checking(new Expectations() {{
                allowing (formatter).supportsFileType(JAVA_FILE);   will(returnValue(true));
                allowing (formatter).supportsFileType(XML_FILE);    will(returnValue(false));
                allowing (formatter).supportsFileType(with(any(File.class))); will(returnValue(true));
                allowing (formatter).supportsReformatOne();         will(returnValue(true));
                allowing (formatter).supportsReformatMany();        will(returnValue(false));
                allowing (formatter).supportsReformatDirectory();   will(returnValue(false));
                allowing (formatter).supportsReformatRecursively(); will(returnValue(false));
            }});
            return queue;
        }

        public void shouldNotCallTheFormatterImmediately() {
            checking(new Expectations() {{
            }});
            queue.reformatOne(FOO_FILE);
        }

        public void shouldCallTheFormatterWhenFlushed() {
            checking(new Expectations() {{
                one (formatter).reformatOne(FOO_FILE);
            }});
            queue.reformatOne(FOO_FILE);
            queue.flush();
        }

        public void afterFlushingTheQueueShouldBeEmpty() {
            checking(new Expectations() {{
                one (formatter).reformatOne(FOO_FILE);
            }});
                                            specify(queue.isEmpty());
            queue.reformatOne(FOO_FILE);    specify(should.not().be.isEmpty());
            queue.flush();                  specify(queue.isEmpty());
            queue.flush();                  specify(queue.isEmpty());
        }

        public void shouldExecuteAllQueuedCommandsWhenFlushed() {
            checking(new Expectations() {{
                one (formatter).reformatOne(FOO_FILE);
                one (formatter).reformatOne(GAZONK_FILE);
            }});
            queue.reformatOne(FOO_FILE);
            queue.reformatOne(GAZONK_FILE);
            queue.flush();
        }

        public void shouldSupportOnlyReformatOne() {
            specify(queue.supportsReformatOne());
            specify(should.not().be.supportsReformatMany());
            specify(should.not().be.supportsReformatDirectory());
            specify(should.not().be.supportsReformatRecursively());
        }

        public void shouldSupportTheSameFileTypesAsTheFormatter() {
            specify(queue.supportsFileType(JAVA_FILE), should.equal(true));
            specify(queue.supportsFileType(XML_FILE), should.equal(false));
        }
    }

    public class WhenFormatterSupportsReformatMany {

        private CodeFormatter formatter;
        private ReformatQueue queue;

        public ReformatQueue create() {
            formatter = mock(CodeFormatter.class);
            queue = new OptimizingReformatQueue(formatter);
            checking(new Expectations() {{
                allowing (formatter).supportsFileType(with(any(File.class))); will(returnValue(true));
                allowing (formatter).supportsReformatOne();         will(returnValue(true));
                allowing (formatter).supportsReformatMany();        will(returnValue(true));
                allowing (formatter).supportsReformatDirectory();   will(returnValue(false));
                allowing (formatter).supportsReformatRecursively(); will(returnValue(false));
            }});
            return queue;
        }

        public void shouldUseReformatManyWhenAskedToFormatManyIndividualFiles() {
            checking(new Expectations() {{
                one (formatter).reformatMany(FOO_FILE, GAZONK_FILE);
            }});
            queue.reformatOne(FOO_FILE);
            queue.reformatOne(GAZONK_FILE);
            queue.flush();
            specify(queue.isEmpty());
        }
    }

    public class WhenFormatterSupportsReformatDirectory {

        private CodeFormatter formatter;
        private ReformatQueue queue;

        public ReformatQueue create() {
            formatter = mock(CodeFormatter.class);
            queue = new OptimizingReformatQueue(formatter);
            checking(new Expectations() {{
                allowing (formatter).supportsFileType(with(any(File.class))); will(returnValue(true));
                allowing (formatter).supportsReformatOne();         will(returnValue(false));
                allowing (formatter).supportsReformatMany();        will(returnValue(false));
                allowing (formatter).supportsReformatDirectory();   will(returnValue(true));
                allowing (formatter).supportsReformatRecursively(); will(returnValue(false));
            }});
            return queue;
        }

        public void whenAllFilesAreInTheSameDirectoryShouldReformatThatDirectory() {
            checking(new Expectations() {{
                one (formatter).reformatDirectory(TESTFILES_DIR);
            }});
            queue.reformatOne(FOO_FILE);
            queue.reformatOne(BAR_FILE);
            queue.flush();
            specify(queue.isEmpty());
        }

        public void whenAllFilesAreInDifferentDirectoriesShouldReformatEachDirectory() {
            checking(new Expectations() {{
                one (formatter).reformatDirectory(TESTFILES_DIR);
                one (formatter).reformatDirectory(TESTFILES_SUBDIR);
            }});
            queue.reformatOne(FOO_FILE);
            queue.reformatOne(BAR_FILE);
            queue.reformatOne(GAZONK_FILE);
            queue.flush();
            specify(queue.isEmpty());
        }

        public void whenThereAreAlsoOtherFilesInTheSameDirectoryShouldNotReformatThatDirectory() {
            checking(new Expectations() {{
            }});
            queue.reformatOne(FOO_FILE);
            specify(new Block() {
                public void run() throws Throwable {
                    queue.flush();
                }
            }, should.raise(IllegalStateException.class));
            specify(queue.isEmpty());
        }
    }

    public class WhenFormatterSupportsReformatRecursively {

        private CodeFormatter formatter;
        private ReformatQueue queue;

        public ReformatQueue create() {
            formatter = mock(CodeFormatter.class);
            queue = new OptimizingReformatQueue(formatter);
            checking(new Expectations() {{
                allowing (formatter).supportsFileType(with(any(File.class))); will(returnValue(true));
                allowing (formatter).supportsReformatOne();         will(returnValue(false));
                allowing (formatter).supportsReformatMany();        will(returnValue(false));
                allowing (formatter).supportsReformatDirectory();   will(returnValue(false));
                allowing (formatter).supportsReformatRecursively(); will(returnValue(true));
            }});
            return queue;
        }

        public void whenAllFilesAreInTheSameDirectoryShouldReformatThatDirectory() {
            checking(new Expectations() {{
                one (formatter).reformatRecursively(TESTFILES_SUBDIR);
            }});
            queue.reformatOne(GAZONK_FILE);
            queue.flush();
            specify(queue.isEmpty());
        }

        public void whenAllFilesAreUnderACommonParentDirectoryShouldReformatThatDirectory() {
            checking(new Expectations() {{
                one (formatter).reformatRecursively(TESTFILES_DIR);
            }});
            queue.reformatOne(FOO_FILE);
            queue.reformatOne(BAR_FILE);
            queue.reformatOne(GAZONK_FILE);
            queue.flush();
            specify(queue.isEmpty());
        }

        public void whenThereAreAlsoOtherFilesInTheSameDirectoryShouldNotReformatThatDirectory() {
            checking(new Expectations() {{
            }});
            queue.reformatOne(FOO_FILE);
            queue.reformatOne(GAZONK_FILE);
            specify(new Block() {
                public void run() throws Throwable {
                    queue.flush();
                }
            }, should.raise(IllegalStateException.class));
            specify(queue.isEmpty());
        }

        public void whenThereAreAlsoOtherFilesInASubDirectoryShouldNotReformatThatDirectory() {
            checking(new Expectations() {{
            }});
            queue.reformatOne(FOO_FILE);
            queue.reformatOne(BAR_FILE);
            specify(new Block() {
                public void run() throws Throwable {
                    queue.flush();
                }
            }, should.raise(IllegalStateException.class));
            specify(queue.isEmpty());
        }
    }

    public class WhenQueueIsEmpty {

        private CodeFormatter formatter;
        private ReformatQueue queue;

        public ReformatQueue create() {
            formatter = mock(CodeFormatter.class);
            queue = new OptimizingReformatQueue(formatter);
            checking(new Expectations() {{
                allowing (formatter).supportsFileType(with(any(File.class))); will(returnValue(true));
                allowing (formatter).supportsReformatOne();         will(returnValue(true));
                allowing (formatter).supportsReformatMany();        will(returnValue(true));
                allowing (formatter).supportsReformatDirectory();   will(returnValue(true));
                allowing (formatter).supportsReformatRecursively(); will(returnValue(true));
            }});
            return queue;
        }

        public void flushingShouldDoNothing() {
            checking(new Expectations() {{
            }});
                                specify(queue.isEmpty());
            queue.flush();      specify(queue.isEmpty());
        }
    }
}
