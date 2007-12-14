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

import java.io.File;

/**
 * @author Esko Luontola
 * @since 7.12.2007
 */
@RunWith(JDaveRunner.class)
public class SupportedFileTypesSpec extends Specification<SupportedFileTypes> {

    public class WhenAllFilesAreSupported {

        private SupportedFileTypes supported;

        public SupportedFileTypes create() {
            supported = new SupportedFileTypes("*.*");
            return supported;
        }

        public void shouldMatchAllFiles() {
            specify(supported.matches(JAVA_FILE));
            specify(supported.matches(XML_FILE));
            specify(supported.matches(TXT_FILE));
        }
    }

    public class WhenManyFileTypesAreSupported {

        private SupportedFileTypes supported;

        public SupportedFileTypes create() {
            supported = new SupportedFileTypes("*.java", "*.xml");
            return supported;
        }

        public void shouldMatchTheSupportedFileTypes() {
            specify(supported.matches(JAVA_FILE));
            specify(supported.matches(XML_FILE));
        }

        public void shouldNotMatchOtherFiles() {
            specify(should.not().be.matches(TXT_FILE));
        }
    }

    public class WhenOnlyJavaFilesAreSupported {

        private SupportedFileTypes supported;

        public SupportedFileTypes create() {
            supported = new SupportedFileTypes("*.java");
            return supported;
        }

        public void shouldMatchJavaFiles() {
            specify(supported.matches(JAVA_FILE));
        }

        public void shouldNotMatchOtherFiles() {
            specify(should.not().be.matches(XML_FILE));
            specify(should.not().be.matches(TXT_FILE));
        }
    }

    public class WhenNothingIsSupported {

        public SupportedFileTypes create() {
            return new SupportedFileTypes();
        }

        public void shouldNotMatchAnyFile() {
            specify(should.not().be.matches(JAVA_FILE));
            specify(should.not().be.matches(XML_FILE));
            specify(should.not().be.matches(TXT_FILE));
        }
    }

    public class WhiteSpaceHandling {

       public SupportedFileTypes create() {
            return new SupportedFileTypes("", " ");
        }

        public void shouldNotAllowEmptyStrings() {
            specify(should.not().be.matches(new File("")));
            specify(should.not().be.matches(new File(" ")));
        }
    }
}
