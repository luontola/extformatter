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
import static net.orfjackal.extformatter.TestResources.*;
import org.junit.runner.RunWith;

/**
 * @author Esko Luontola
 * @since 4.12.2007
 */
@RunWith(JDaveRunner.class)
public class CommandLineCodeFormatterFactorySpec extends Specification<CodeFormatter> {

    public class ACommandLineCodeFormatterFactory {

        private CodeFormatter formatter;

        public CodeFormatter create() {
            CommandLineCodeFormatterFactory factory = new CommandLineCodeFormatterFactory();
            factory.setSupportedFileTypes(SUPPORTS_JAVA_AND_XML);
            factory.setFileCommand("command");
            factory.setDirectoryCommand("command");
            factory.setRecursiveDirectoryCommand("command");
            formatter = factory.newFormatter();
            return formatter;
        }

        public void shouldCreateAFormatter() {
            specify(formatter, should.not().equal(null));
        }

        public void formatterShouldSupportTheSpecifiedFileTypes() {
            specify(formatter.supportsFileType(JAVA_FILE));
            specify(formatter.supportsFileType(XML_FILE));
        }

        public void formatterShouldNotSupportOtherFiles() {
            specify(should.not().be.supportsFileType(TXT_FILE));
        }

        public void formatterShouldSupportReformatFile() {
            specify(formatter.supportsReformatFile());
        }

        public void formatterShouldNotSupportReformatFiles() {
            specify(should.not().be.supportsReformatFiles());
        }

        public void formatterShouldSupportReformatFilesInDirectory() {
            specify(formatter.supportsReformatFilesInDirectory());
        }

        public void formatterShouldSupportReformatFilesInDirectoryRecursively() {
            specify(formatter.supportsReformatFilesInDirectoryRecursively());
        }
    }

    public class WhenOnlyReformatFileIsConfigured {

        private CodeFormatter formatter;

        public CodeFormatter create() {
            CommandLineCodeFormatterFactory factory = new CommandLineCodeFormatterFactory();
            factory.setFileCommand("command");
            formatter = factory.newFormatter();
            return formatter;
        }

        public void shouldCreateAFormatter() {
            specify(formatter, should.not().equal(null));
        }

        public void formatterShouldSupportReformatFile() {
            specify(formatter.supportsReformatFile());
        }

        public void formatterShouldNotSupportTheOthers() {
            specify(formatter.supportsReformatFile());
            specify(should.not().be.supportsReformatFiles());
            specify(should.not().be.supportsReformatFilesInDirectory());
            specify(should.not().be.supportsReformatFilesInDirectoryRecursively());
        }
    }

    public class WhenOnlyReformatDirectoryIsConfigured {

        private CodeFormatter formatter;

        public CodeFormatter create() {
            CommandLineCodeFormatterFactory factory = new CommandLineCodeFormatterFactory();
            factory.setDirectoryCommand("command");
            formatter = factory.newFormatter();
            return formatter;
        }

        public void shouldCreateAFormatter() {
            specify(formatter, should.not().equal(null));
        }

        public void formatterShouldSupportReformatFilesInDirectory() {
            specify(formatter.supportsReformatFilesInDirectory());
        }

        public void formatterShouldNotSupportTheOthers() {
            specify(should.not().be.supportsReformatFile());
            specify(should.not().be.supportsReformatFiles());
            specify(should.not().be.supportsReformatFilesInDirectoryRecursively());
        }
    }

    public class WhenOnlyReformatDirectoryRecursivelyIsConfigured {

        private CodeFormatter formatter;

        public CodeFormatter create() {
            CommandLineCodeFormatterFactory factory = new CommandLineCodeFormatterFactory();
            factory.setRecursiveDirectoryCommand("command");
            formatter = factory.newFormatter();
            return formatter;
        }

        public void shouldCreateAFormatter() {
            specify(formatter, should.not().equal(null));
        }

        public void formatterShouldSupportReformatFilesInDirectoryRecursively() {
            specify(formatter.supportsReformatFilesInDirectoryRecursively());
        }

        public void formatterShouldNotSupportTheOthers() {
            specify(should.not().be.supportsReformatFile());
            specify(should.not().be.supportsReformatFiles());
            specify(should.not().be.supportsReformatFilesInDirectory());
        }
    }

    public class WhenNothingIsConfigured {

        private CodeFormatter formatter;

        public CodeFormatter create() {
            CommandLineCodeFormatterFactory factory = new CommandLineCodeFormatterFactory();
            formatter = factory.newFormatter();
            return formatter;
        }

        public void shouldNotCreateAFormatter() {
            specify(formatter, should.equal(null));
        }
    }
}