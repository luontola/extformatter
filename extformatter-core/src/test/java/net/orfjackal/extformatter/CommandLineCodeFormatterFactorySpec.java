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
import org.junit.runner.RunWith;

/**
 * @author Esko Luontola
 * @since 4.12.2007
 */
@RunWith(JDaveRunner.class)
public class CommandLineCodeFormatterFactorySpec extends Specification<CommandLineCodeFormatter> {

    public class FactoryWithNothingConfigured {

        private CommandLineCodeFormatter formatter;

        public CommandLineCodeFormatter create() {
            CommandLineCodeFormatterFactory factory = new CommandLineCodeFormatterFactory();
            formatter = factory.newFormatter();
            return formatter;
        }

        public void shouldNotCreateAFormatter() {
            specify(formatter, should.equal(null));
        }
    }

    public class NonConfiguredFormatter {

        public CommandLineCodeFormatter create() {
            return new CommandLineCodeFormatter(null, null, null);
        }

        public void shouldNotSupportReformatFile() {
            specify(should.not().be.supportsReformatFile());
        }

        public void shouldNotSupportReformatFiles() {
            specify(should.not().be.supportsReformatFiles());
        }

        public void shouldNotSupportReformatFilesInDirectory() {
            specify(should.not().be.supportsReformatFilesInDirectory());
        }

        public void shouldNotSupportReformatFilesInDirectoryRecursively() {
            specify(should.not().be.supportsReformatFilesInDirectoryRecursively());
        }
    }

    public class FactoryWithOnlyReformatFileConfigured {

        private CommandLineCodeFormatter formatter;

        public CommandLineCodeFormatter create() {
            CommandLineCodeFormatterFactory factory = new CommandLineCodeFormatterFactory();
            factory.setSingleFileCommand("command");
            formatter = factory.newFormatter();
            return formatter;
        }

        public void shouldCreateAFormatter() {
            specify(formatter, should.not().equal(null));
        }

        public void formatterShouldSupportReformatFile() {
            specify(formatter.supportsReformatFile());
        }

        public void formatterShouldSupportReformatFiles() {
            specify(formatter.supportsReformatFiles());
        }

        public void formatterShouldSupportReformatFilesInDirectory() {
            specify(formatter.supportsReformatFilesInDirectory());
        }

        public void formatterShouldSupportReformatFilesInDirectoryRecursively() {
            specify(formatter.supportsReformatFilesInDirectoryRecursively());
        }
    }

    public class FactoryWithOnlyReformatDirectoryConfigured {

        private CommandLineCodeFormatter formatter;

        public CommandLineCodeFormatter create() {
            CommandLineCodeFormatterFactory factory = new CommandLineCodeFormatterFactory();
            factory.setDirectoryCommand("command");
            formatter = factory.newFormatter();
            return formatter;
        }

        public void shouldCreateAFormatter() {
            specify(formatter, should.not().equal(null));
        }

        public void formatterShouldNotSupportReformatFile() {
            specify(should.not().be.supportsReformatFile());
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

    public class FactoryWithOnlyReformatDirectoryRecursivelyConfigured {

        private CommandLineCodeFormatter formatter;

        public CommandLineCodeFormatter create() {
            CommandLineCodeFormatterFactory factory = new CommandLineCodeFormatterFactory();
            factory.setRecursiveDirectoryCommand("command");
            formatter = factory.newFormatter();
            return formatter;
        }

        public void shouldCreateAFormatter() {
            specify(formatter, should.not().equal(null));
        }

        public void formatterShouldNotSupportReformatFile() {
            specify(should.not().be.supportsReformatFile());
        }

        public void formatterShouldNotSupportReformatFiles() {
            specify(should.not().be.supportsReformatFiles());
        }

        public void formatterShouldNotSupportReformatFilesInDirectory() {
            specify(should.not().be.supportsReformatFilesInDirectory());
        }

        public void formatterShouldSupportReformatFilesInDirectoryRecursively() {
            specify(formatter.supportsReformatFilesInDirectoryRecursively());
        }
    }

    public class FullyConfiguredFactory {

        private CommandLineCodeFormatter formatter;

        public CommandLineCodeFormatter create() {
            CommandLineCodeFormatterFactory factory = new CommandLineCodeFormatterFactory();
            factory.setSingleFileCommand("command");
            factory.setDirectoryCommand("command");
            factory.setRecursiveDirectoryCommand("command");
            formatter = factory.newFormatter();
            return formatter;
        }

        public void shouldCreateAFormatter() {
            specify(formatter, should.not().equal(null));
        }

        public void formatterShouldSupportReformatFile() {
            specify(formatter.supportsReformatFile());
        }

        public void formatterShouldSupportReformatFiles() {
            specify(formatter.supportsReformatFiles());
        }

        public void formatterShouldSupportReformatFilesInDirectory() {
            specify(formatter.supportsReformatFilesInDirectory());
        }

        public void formatterShouldSupportReformatFilesInDirectoryRecursively() {
            specify(formatter.supportsReformatFilesInDirectoryRecursively());
        }
    }
}