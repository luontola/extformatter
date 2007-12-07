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

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * @author Esko Luontola
 * @since 30.11.2007
 */
public class TestResources {

    public static final File TESTFILES_DIR = file("/testfiles");
    public static final File FOO_FILE = file("/testfiles/Foo.java.txt");
    public static final File BAR_FILE = file("/testfiles/Bar.java.txt");
    public static final File TESTFILES_SUBDIR = file("/testfiles/subdir");
    public static final File GAZONK_FILE = file("/testfiles/subdir/Gazonk.java.txt");
    public static final File ECLIPSE_EXE_FILE = file("/eclipse/eclipse.exe.txt");
    public static final File ECLIPSE_PREFS_FILE = file("/eclipse/org.eclipse.jdt.core.prefs.txt");

    public static final String[] SUPPORTED_FILE_TYPES = new String[]{".java", ".xml"};
    public static final File JAVA_FILE = new File("Foo.java");
    public static final File XML_FILE = new File("Bar.xml");
    public static final File TXT_FILE = new File("Gazonk.txt");

    private TestResources() {
    }

    private static File file(String path) {
        try {
            URL resource = TestResources.class.getResource(path);
            assert resource != null : path;
            return new File(resource.toURI());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
}
