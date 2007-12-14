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

package net.orfjackal.extformatter.settings;

import jdave.Block;
import jdave.Specification;
import jdave.junit4.JDaveRunner;
import net.orfjackal.extformatter.CodeFormatter;
import net.orfjackal.extformatter.CommandLineCodeFormatter;
import net.orfjackal.extformatter.EclipseCodeFormatter;
import net.orfjackal.extformatter.TestResources;
import org.junit.runner.RunWith;

import java.io.IOException;

/**
 * @author Esko Luontola
 * @since 6.12.2007
 */
@RunWith(JDaveRunner.class)
public class SettingsManagerSpec extends Specification<Settings> {

    public class WhenPluginIsDisabled {

        private Settings settings;

        public Settings create() {
            settings = new Settings();
            settings.setFormatter(Settings.Formatter.IDEA_DEFAULT);
            return settings;
        }

        public void shouldNotCreateAFormatter() throws IllegalSettingsException {
            specify(SettingsManager.newFormatter(settings), should.equal(null));
        }
    }

    public class WhenEclipseFormatterIsSelected {

        private Settings settings;

        public Settings create() throws IOException {
            settings = new Settings();
            settings.setFormatter(Settings.Formatter.ECLIPSE);
            settings.setEclipseExecutable(TestResources.ECLIPSE_EXE_FILE.getPath());
            settings.setEclipsePrefs(TestResources.ECLIPSE_PREFS_FILE.getPath());
            return settings;
        }

        public void shouldCreateAnEclipseFormatter() throws IllegalSettingsException {
            specify(SettingsManager.newFormatter(settings) instanceof EclipseCodeFormatter);
        }

        public void shouldNotAllowAnEmptyParameterForEclipseExecutable() {
            settings.setEclipseExecutable("");
            specify(new Block() {
                public void run() throws Throwable {
                    SettingsManager.newFormatter(settings);
                }
            }, should.raise(IllegalSettingsException.class));
        }

        public void shouldNotAllowAnEmptyParameterForEclipsePrefs() {
            settings.setEclipsePrefs("");
            specify(new Block() {
                public void run() throws Throwable {
                    SettingsManager.newFormatter(settings);
                }
            }, should.raise(IllegalSettingsException.class));
        }

        public void shouldNotAllowAMissingFileForEclipseExecutable() {
            settings.setEclipseExecutable("nowhere/eclipse.exe");
            specify(new Block() {
                public void run() throws Throwable {
                    SettingsManager.newFormatter(settings);
                }
            }, should.raise(IllegalSettingsException.class));
        }

        public void shouldNotAllowAMissingFileForEclipsePrefs() {
            settings.setEclipsePrefs("nowhere/org.eclipse.jdt.core.prefs");
            specify(new Block() {
                public void run() throws Throwable {
                    SettingsManager.newFormatter(settings);
                }
            }, should.raise(IllegalSettingsException.class));
        }
    }

    public class WhenCommandLineFormatterIsSelected {

        private Settings settings;

        public Settings create() throws IOException {
            settings = new Settings();
            settings.setFormatter(Settings.Formatter.COMMAND_LINE);
            settings.setCliReformatOne("reformat %FILE%");
            settings.setCliReformatOneEnabled(true);
            settings.setCliReformatMany("reformat %FILES%");
            settings.setCliReformatManyEnabled(true);
            settings.setCliReformatDirectory("reformat %DIRECTORY%");
            settings.setCliReformatDirectoryEnabled(true);
            settings.setCliReformatRecursively("reformat -R %DIRECTORY%");
            settings.setCliReformatRecursivelyEnabled(true);
            settings.setCliSupportedFileTypes("*.java *.xml");
            return settings;
        }

        public void shouldCreateACommandLineFormatter() throws IllegalSettingsException {
            specify(SettingsManager.newFormatter(settings) instanceof CommandLineCodeFormatter);
        }

        public void formatterShouldSupportTheSpecifiedCommands() throws IllegalSettingsException {
            CodeFormatter formatter = SettingsManager.newFormatter(settings);
            assert formatter != null;
            specify(formatter.supportsReformatOne());
            specify(formatter.supportsReformatMany());
            specify(formatter.supportsReformatDirectory());
            specify(formatter.supportsReformatRecursively());
        }

        public void formatterShouldNotSupportCommandsWhichWereNotSpecified() throws IllegalSettingsException {
            settings.setCliReformatManyEnabled(false);
            settings.setCliReformatDirectoryEnabled(false);
            settings.setCliReformatRecursivelyEnabled(false);
            CodeFormatter formatter = SettingsManager.newFormatter(settings);
            assert formatter != null;
            specify(formatter.supportsReformatOne(), should.equal(true));
            specify(formatter.supportsReformatMany(), should.equal(false));
            specify(formatter.supportsReformatDirectory(), should.equal(false));
            specify(formatter.supportsReformatRecursively(), should.equal(false));

            // test disabled reformatOne
            settings.setCliReformatOneEnabled(false);
            settings.setCliReformatManyEnabled(true);
            formatter = SettingsManager.newFormatter(settings);
            assert formatter != null;
            specify(formatter.supportsReformatOne(), should.equal(false));
            specify(formatter.supportsReformatMany(), should.equal(true));
        }

        public void formatterShouldSupportTheSpecifiedFileTypes() throws IllegalSettingsException {
            CodeFormatter formatter = SettingsManager.newFormatter(settings);
            assert formatter != null;
            specify(formatter.supportsFileType(TestResources.JAVA_FILE));
            specify(formatter.supportsFileType(TestResources.XML_FILE));
        }

        public void formatterShouldNotSupportOtherFileTypes() throws IllegalSettingsException {
            CodeFormatter formatter = SettingsManager.newFormatter(settings);
            assert formatter != null;
            specify(formatter.supportsFileType(TestResources.TXT_FILE), should.equal(false));
        }

        public void shouldRequireAtLeastOneOfTheCommandsToBeEnabled() {
            settings.setCliReformatOneEnabled(false);
            settings.setCliReformatManyEnabled(false);
            settings.setCliReformatDirectoryEnabled(false);
            settings.setCliReformatRecursivelyEnabled(false);
            specify(new Block() {
                public void run() throws Throwable {
                    SettingsManager.newFormatter(settings);
                }
            }, should.raise(IllegalSettingsException.class));
        }

        public void shouldNotAllowEnablingAnEmptyCommandForReformatOne() {
            settings.setCliReformatOne("");
            specify(new Block() {
                public void run() throws Throwable {
                    SettingsManager.newFormatter(settings);
                }
            }, should.raise(IllegalSettingsException.class));
        }

        public void shouldNotAllowEnablingAnEmptyCommandForReformatMany() {
            settings.setCliReformatMany("");
            specify(new Block() {
                public void run() throws Throwable {
                    SettingsManager.newFormatter(settings);
                }
            }, should.raise(IllegalSettingsException.class));
        }

        public void shouldNotAllowEnablingAnEmptyCommandForReformatDirectory() {
            settings.setCliReformatDirectory("");
            specify(new Block() {
                public void run() throws Throwable {
                    SettingsManager.newFormatter(settings);
                }
            }, should.raise(IllegalSettingsException.class));
        }

        public void shouldNotAllowEnablingAnEmptyCommandForReformatRecursively() {
            settings.setCliReformatRecursively("");
            specify(new Block() {
                public void run() throws Throwable {
                    SettingsManager.newFormatter(settings);
                }
            }, should.raise(IllegalSettingsException.class));
        }

        public void shouldNotAllowAnEmptyListOfSupportedFiles() {
            settings.setCliSupportedFileTypes("");
            specify(new Block() {
                public void run() throws Throwable {
                    SettingsManager.newFormatter(settings);
                }
            }, should.raise(IllegalSettingsException.class));
        }

        public void shouldRequireFileTagInReformatOneCommand() {
            settings.setCliReformatOne("FILE %FILES% %DIRECTORY%");
            specify(new Block() {
                public void run() throws Throwable {
                    SettingsManager.newFormatter(settings);
                }
            }, should.raise(IllegalSettingsException.class));
        }

        public void shouldRequireFilesTagInReformatManyCommand() {
            settings.setCliReformatMany("%FILE% FILES %DIRECTORY%");
            specify(new Block() {
                public void run() throws Throwable {
                    SettingsManager.newFormatter(settings);
                }
            }, should.raise(IllegalSettingsException.class));
        }

        public void shouldRequireDirectoryTagInReformatDirectoryCommand() {
            settings.setCliReformatDirectory("%FILE% %FILES% DIRECTORY");
            specify(new Block() {
                public void run() throws Throwable {
                    SettingsManager.newFormatter(settings);
                }
            }, should.raise(IllegalSettingsException.class));
        }

        public void shouldRequireDirectoryTagInReformatRecursivelyCommand() {
            settings.setCliReformatRecursively("%FILE% %FILES% DIRECTORY");
            specify(new Block() {
                public void run() throws Throwable {
                    SettingsManager.newFormatter(settings);
                }
            }, should.raise(IllegalSettingsException.class));
        }
    }
}
