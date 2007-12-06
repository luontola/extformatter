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

package net.orfjackal.extformatter.plugin;

import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiJavaFile;
import com.intellij.psi.impl.source.codeStyle.CodeStyleManagerEx;
import com.intellij.psi.xml.XmlFile;
import com.intellij.util.IncorrectOperationException;
import jdave.Specification;
import jdave.junit4.JDaveRunner;
import net.orfjackal.extformatter.CodeFormatter;
import org.jmock.Expectations;
import org.junit.runner.RunWith;

import java.io.File;

/**
 * @author Esko Luontola
 * @since 6.12.2007
 */
@RunWith(JDaveRunner.class)
public class ExternalizedCodeStyleManagerSpec extends Specification<ExternalizedCodeStyleManager> {

    /**
     * For now, these tests exist only for documentation purposes,
     * because I haven't figured out a way to run these tests.
     * IDEA has some unit testing support (com.intellij.testFramework.IdeaTestCase)
     * but I haven't found enough examples on how to use it.
     */
    private static boolean TEST_DISABLED = true;

    public class ASupportedFileType {

        private PsiFile file;
        private CodeStyleManagerEx original;
        private CodeFormatter replacement;
        private ExternalizedCodeStyleManager manager;

        public ExternalizedCodeStyleManager create() throws Exception {
            if (TEST_DISABLED) return null;
            file = mock(PsiJavaFile.class); // 'file' is a real writable file in local file system
            original = mock(CodeStyleManagerEx.class);
            replacement = mock(CodeFormatter.class);
            manager = new ExternalizedCodeStyleManager(original, replacement);
            return manager;
        }

        public void wholeFileShouldBeReformattedByTheReplacement() throws IncorrectOperationException {
            if (TEST_DISABLED) return;
            checking(new Expectations() {{
                one(replacement).reformatFile(new File("Foo.java"));
            }});
            manager.reformatText(file, 0, 100);
        }

        public void selectedTextShouldBeReformattedByTheOriginal() throws IncorrectOperationException {
            if (TEST_DISABLED) return;
            checking(new Expectations() {{
                one(original).reformatText(file, 30, 40);
            }});
            manager.reformatText(file, 30, 40);
        }
    }

    public class AnUnsupportedFileType {

        private PsiFile file;
        private CodeStyleManagerEx original;
        private ExternalizedCodeStyleManager manager;

        public ExternalizedCodeStyleManager create() throws Exception {
            if (TEST_DISABLED) return null;
            file = mock(XmlFile.class);
            original = mock(CodeStyleManagerEx.class);
            CodeFormatter replacement = mock(CodeFormatter.class);
            manager = new ExternalizedCodeStyleManager(original, replacement);
            return manager;
        }

        public void shouldAlwaysBeReformattedByTheOriginal() throws IncorrectOperationException {
            if (TEST_DISABLED) return;
            checking(new Expectations() {{
                one(original).reformatText(file, 0, 100);
            }});
            manager.reformatText(file, 0, 100);
        }
    }
}
