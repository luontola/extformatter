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

    public static final File TEST_DIR = file("/testFiles");
    public static final File FOO_FILE = file("/testFiles/Foo.java.txt");
    public static final File BAR_FILE = file("/testFiles/Bar.java.txt");
    public static final File TEST_SUBDIR = file("/testFiles/subdir");
    public static final File BAZ_FILE = file("/testFiles/subdir/Baz.java.txt");

    private TestResources() {
    }

    private static File file(String path) {
        try {
            URL resource = TestResources.class.getResource(path);
            System.out.println("resource = " + resource);
            return new File(resource.toURI());
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
}
