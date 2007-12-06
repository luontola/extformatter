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

import javax.swing.*;
import java.io.File;

/**
 * @author Esko Luontola
 * @since 6.12.2007
 */
@RunWith(JDaveRunner.class)
public class ResourcesSpec extends Specification<Object> {

    public class ConcerningTheResources {

        public Object create() {
            return null;
        }

        public void allFilesShouldExist() {
            specify(new File(Resources.PROGRAM_LOGO_16.getPath()).isFile());
            specify(new File(Resources.PROGRAM_LOGO_32.getPath()).isFile());
        }

        public void allIconsShouldHaveTheSpecifiedSize() {
            ImageIcon logo16 = new ImageIcon(Resources.PROGRAM_LOGO_16);
            specify(logo16.getIconWidth(), should.equal(16));
            specify(logo16.getIconHeight(), should.equal(16));
            ImageIcon logo32 = new ImageIcon(Resources.PROGRAM_LOGO_32);
            specify(logo32.getIconWidth(), should.equal(32));
            specify(logo32.getIconHeight(), should.equal(32));
        }
    }
}
