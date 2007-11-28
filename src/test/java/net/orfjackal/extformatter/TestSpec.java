package net.orfjackal.extformatter;

import jdave.Specification;
import jdave.junit4.JDaveRunner;
import org.junit.runner.RunWith;

/**
 * @author Esko Luontola
 * @since 28.11.2007
 */
@RunWith(JDaveRunner.class)
public class TestSpec extends Specification<Object> {

    public class TestName {

        public Object create() {
            return null;
        }

        public void should() {
            specify(true);
        }

    }
}
