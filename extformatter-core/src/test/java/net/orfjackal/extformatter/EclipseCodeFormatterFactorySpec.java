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

import java.io.File;

/**
 * @author Esko Luontola
 * @since 4.12.2007
 */
@RunWith(JDaveRunner.class)
public class EclipseCodeFormatterFactorySpec extends Specification<EclipseCodeFormatterFactory> {

    public class NonConfiguredFactory {

        private EclipseCodeFormatterFactory factory;

        public EclipseCodeFormatterFactory create() {
            factory = new EclipseCodeFormatterFactory();
            return factory;
        }

        public void shouldNotCreateAFormatter() {
            specify(factory.newFormatter(), should.equal(null));
        }
    }

    public class ProperlyConfiguredFactory {

        private EclipseCodeFormatterFactory factory;

        public EclipseCodeFormatterFactory create() {
            factory = new EclipseCodeFormatterFactory();
            factory.setEclipseExecutable(new File("somewhere/eclipse.exe"));
            factory.setEclipsePrefs(new File("somewhere/org.eclipse.jdt.core.prefs"));
            return factory;
        }

        public void shouldCreateAFormatter() {
            specify(factory.newFormatter(), should.not().equal(null));
        }
    }
}
