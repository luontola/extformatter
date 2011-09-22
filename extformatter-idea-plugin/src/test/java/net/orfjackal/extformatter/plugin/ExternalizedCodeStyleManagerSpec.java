
// ExternalizedCodeStyleManagerSpec.java --
//
// ExternalizedCodeStyleManagerSpec.java is part of ElectricCommander.
//
// Copyright (c) 2005-2011 Electric Cloud, Inc.
// All rights reserved.
//

package net.orfjackal.extformatter.plugin;

import java.io.File;

import org.jmock.Expectations;
import org.junit.runner.RunWith;
import jdave.Specification;
import jdave.junit4.JDaveRunner;
import net.orfjackal.extformatter.CodeFormatter;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiJavaFile;
import com.intellij.psi.codeStyle.CodeStyleManager;
import com.intellij.psi.xml.XmlFile;
import com.intellij.util.IncorrectOperationException;

/**
 * @author  Esko Luontola
 * @since   6.12.2007
 */
@RunWith(JDaveRunner.class)
public class ExternalizedCodeStyleManagerSpec
    extends Specification<ExternalizedCodeStyleManager>
{

    //~ Static fields/initializers ---------------------------------------------

    /**
     * For now, these tests exist only for documentation purposes, because I
     * haven't figured out a way to run these tests. IDEA has some unit testing
     * support (com.intellij.testFramework.IdeaTestCase) but I haven't found
     * enough examples on how to use it.
     */
    private static boolean TEST_DISABLED = true;

    //~ Inner Classes ----------------------------------------------------------

    public class WhenTheFileIsInLocalFileSystemAndItsTypeIsSupported
    {

        //~ Instance fields ----------------------------------------------------

        private PsiFile                      file;
        private CodeStyleManager             original;
        private CodeFormatter                replacement;
        private ExternalizedCodeStyleManager manager;

        //~ Methods ------------------------------------------------------------

        public ExternalizedCodeStyleManager create()
            throws Exception
        {

            if (TEST_DISABLED) {
                return null;
            }

            file        = mock(PsiJavaFile.class);    // 'file' is a real
                                                      // writable file in local
                                                      // file system
            original    = mock(CodeStyleManager.class);
            replacement = mock(CodeFormatter.class);
            manager     = new ExternalizedCodeStyleManager(original,
                    replacement);

            return manager;
        }

        public void reformattingSelectedTextShouldFallBackToTheOriginalCodeStyleManager()
            throws IncorrectOperationException
        {

            if (TEST_DISABLED) {
                return;
            }

            checking(new Expectations() {

                    {
                        one(original).reformatText(file, 30, 40);
                    }
                });
            manager.reformatText(file, 30, 40);
        }

        public void reformattingWholeFileShouldUseTheReplacementFormatter()
            throws IncorrectOperationException
        {

            if (TEST_DISABLED) {
                return;
            }

            checking(new Expectations() {

                    {
                        one(replacement).reformatOne(new File("Foo.java"));
                    }
                });
            manager.reformatText(file, 0, 100);
        }
    }

    public class WhenTheFileIsNotSupported
    {

        //~ Instance fields ----------------------------------------------------

        private PsiFile                      file;
        private CodeStyleManager             original;
        private ExternalizedCodeStyleManager manager;

        //~ Methods ------------------------------------------------------------

        public ExternalizedCodeStyleManager create()
            throws Exception
        {

            if (TEST_DISABLED) {
                return null;
            }

            file     = mock(XmlFile.class);
            original = mock(CodeStyleManager.class);

            CodeFormatter replacement = mock(CodeFormatter.class);

            manager = new ExternalizedCodeStyleManager(original, replacement);

            return manager;
        }

        public void shouldFallBackToTheOriginalCodeStyleManager()
            throws IncorrectOperationException
        {

            if (TEST_DISABLED) {
                return;
            }

            checking(new Expectations() {

                    {
                        one(original).reformatText(file, 0, 100);
                    }
                });
            manager.reformatText(file, 0, 100);
        }
    }
}
