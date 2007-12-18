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
import static net.orfjackal.extformatter.TestResources.BAR_FILE;
import static net.orfjackal.extformatter.TestResources.FOO_FILE;
import static net.orfjackal.extformatter.util.FileUtil.contentsOf;
import org.junit.runner.RunWith;

import java.io.File;
import java.io.IOException;

/**
 * @author Esko Luontola
 * @since 18.12.2007
 */
@RunWith(JDaveRunner.class)
public class TempFileManagerSpec extends Specification<TempFileManager> {

    public class ATempFileManager {

        private TempFileManager manager;

        public TempFileManager create() {
            manager = new TempFileManager();
            manager.add(FOO_FILE);
            manager.add(BAR_FILE);
            return manager;
        }

        public void destroy() {
            manager.dispose();
        }

        public void shouldContainAddedFiles() {
            File[] files = manager.files();
            specify(files.length, should.equal(2));
            specify(files, should.containExactly(FOO_FILE, BAR_FILE));
        }

        public void tempDirectoryShouldContainTempFilesWithTheSameNames() {
            String[] fileNames = manager.tempDirectory(1).list();
            specify(fileNames, should.containExactly(FOO_FILE.getName(), BAR_FILE.getName()));
        }

        public void filesInTempDirectoryShouldBeCopiesOfTheOriginalFiles() throws IOException {
            File tmpFoo = new File(manager.tempDirectory(1), FOO_FILE.getName());
            specify(contentsOf(tmpFoo), should.equal(contentsOf(FOO_FILE)));
        }

        public void afterDisposingTheTempDirectoryShouldNotExist() {
            manager.dispose();
            specify(manager.tempDirectory().exists(), should.equal(false));
        }
    }

    public class WhenManagerIsEmpty {

        private TempFileManager manager;

        public TempFileManager create() {
            manager = new TempFileManager();
            return manager;
        }

        public void destroy() {
            manager.dispose();
        }

        public void shouldContainNoFiles() {
            specify(manager.files().length, should.equal(0));
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

    public class WhenThereAreManyFilesWithTheSameName {

        private TempFileManager manager;

        public TempFileManager create() {
            manager = new TempFileManager();
            manager.add(FOO_FILE);
            manager.add(FOO_FILE);
            return manager;
        }

        public void destroy() {
            manager.dispose();
        }

        public void shouldContainAddedFiles() {
            File[] files = manager.files();
            specify(files.length, should.equal(2));
            specify(files, should.containExactly(FOO_FILE, FOO_FILE));
        }

        public void tempFilesShouldBeInDifferentDirectories() {
            File tmpDir1 = manager.tempDirectory(1);
            File tmpDir2 = manager.tempDirectory(2);
            specify(tmpDir1, should.not().equal(tmpDir2));
            specify(tmpDir1.list(), should.containExactly(FOO_FILE.getName()));
            specify(tmpDir2.list(), should.containExactly(FOO_FILE.getName()));
        }
    }

    public class WhenThereAreManyManagers {

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
