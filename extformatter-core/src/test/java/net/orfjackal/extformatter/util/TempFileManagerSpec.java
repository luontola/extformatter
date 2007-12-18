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

package net.orfjackal.extformatter.util;

import jdave.Specification;
import jdave.junit4.JDaveRunner;
import org.junit.runner.RunWith;

/**
 * @author Esko Luontola
 * @since 18.12.2007
 */
@RunWith(JDaveRunner.class)
public class TempFileManagerSpec extends Specification<TempFileManager> {

    public class WhenManagerIsEmpty {
        
        private TempFileManager manager;

        public TempFileManager create() {
            manager = new TempFileManager();
            return manager;
        }

        public void destroy() {
            manager.dispose();
        }

        public void managerShouldContainNoFiles() {
            specify(manager.size(), should.equal(0));
        }


        public void tempDirectoryShouldExists() {
            specify(manager.tempDirectory().isDirectory());
        }

        public void tempDirectoryShouldBeEmpty() {
            specify(manager.tempDirectory().listFiles().length, should.equal(0));
        }

        public void afterDisposingTheTempDirectoryShouldNotExist() {
            manager.dispose();
            specify(manager.tempDirectory().exists(), should.equal(false));
        }
    }

    public class WhenThereAreTwoManagers {
        private TempFileManager managerA;
        private TempFileManager managerB;

        public TempFileManager create() {
            managerA = new TempFileManager();
            managerB = new TempFileManager();
            return null;
        }

        public void destroy() {
            managerA.dispose();
            managerB.dispose();
        }

        public void theyShouldHaveDifferentTempDirectories() {
            specify(managerA.tempDirectory(), should.not().equal(managerB.tempDirectory()));
        }
    }
}
