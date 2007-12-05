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
public class EclipseCodeFormatterFactorySpec extends Specification<EclipseCodeFormatter> {

    public class FactoryWithNothingConfigured {

        private EclipseCodeFormatter formatter;

        public EclipseCodeFormatter create() {
            EclipseCodeFormatterFactory factory = new EclipseCodeFormatterFactory();
            formatter = factory.newFormatter();
            return formatter;
        }

        public void shouldNotCreateAFormatter() {
            specify(formatter, should.equal(null));
        }
    }

    public class FactoryWithOnlyEclipseExecutableConfigured {

        private EclipseCodeFormatter formatter;

        public EclipseCodeFormatter create() {
            EclipseCodeFormatterFactory factory = new EclipseCodeFormatterFactory();
            factory.setEclipseExecutable(TestResources.ECLIPSE_EXE_FILE);
            formatter = factory.newFormatter();
            return formatter;
        }

        public void shouldNotCreateAFormatter() {
            specify(formatter, should.equal(null));
        }
    }

    public class FactoryWithOnlyEclipsePrefsConfigured {

        private EclipseCodeFormatter formatter;

        public EclipseCodeFormatter create() {
            EclipseCodeFormatterFactory factory = new EclipseCodeFormatterFactory();
            factory.setEclipsePrefs(TestResources.ECLIPSE_PREFS_FILE);
            formatter = factory.newFormatter();
            return formatter;
        }

        public void shouldNotCreateAFormatter() {
            specify(formatter, should.equal(null));
        }
    }

    public class ProperlyConfiguredFactory {

        private EclipseCodeFormatter formatter;

        public EclipseCodeFormatter create() {
            EclipseCodeFormatterFactory factory = new EclipseCodeFormatterFactory();
            factory.setEclipseExecutable(TestResources.ECLIPSE_EXE_FILE);
            factory.setEclipsePrefs(TestResources.ECLIPSE_PREFS_FILE);
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
