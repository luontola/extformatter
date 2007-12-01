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

import java.io.*;

/**
 * @author Esko Luontola
 * @since 1.12.2007
 */
public class EclipseCodeFormatterManualTests {

    private static File testfilesDir;
    private static File eclipsePrefsFile;
    private static File fooFile;
    private static File barFile;
    private static File bazFile;

    public static class ReformatFileTest {

        /**
         * Expected: The file "Foo.java" formatted according to Eclipse's rules. Other files untouched.
         */
        public static void main(String[] args) {
            prepareTestFiles();
            CodeFormatter formatter = new EclipseCodeFormatter(eclipsePrefsFile);
            formatter.reformatFile(fooFile);
            showResultingFiles();
        }
    }

    public static class ReformatFilesInDirectoryTest {

        /**
         * Expected: The files "Foo.java" and "Bar.java" formatted according to Eclipse's rules. Other files untouched.
         */
        public static void main(String[] args) {
            prepareTestFiles();
            CodeFormatter formatter = new EclipseCodeFormatter(eclipsePrefsFile);
            formatter.reformatFilesInDirectory(testfilesDir);
            showResultingFiles();
        }
    }

    public static class ReformatFilesInDirectoryRecursivelyTest {

        /**
         * Expected: All files formatted according to Eclipse's rules.
         */
        public static void main(String[] args) {
            prepareTestFiles();
            CodeFormatter formatter = new EclipseCodeFormatter(eclipsePrefsFile);
            formatter.reformatFilesInDirectoryRecursively(testfilesDir);
            showResultingFiles();
        }
    }

    private static void prepareTestFiles() {
        File tempDir = new File(System.getProperty("java.io.tmpdir"), EclipseCodeFormatterManualTests.class.getName());
        testfilesDir = new File(tempDir, "testfiles");
        File testfilesSubdir = new File(testfilesDir, "subdir");
        tempDir.mkdir();
        testfilesDir.mkdir();
        testfilesSubdir.mkdir();

        eclipsePrefsFile = new File(tempDir, "org.eclipse.jdt.core.prefs");
        fooFile = new File(testfilesDir, "Foo.java");
        barFile = new File(testfilesDir, "Bar.java");
        bazFile = new File(testfilesSubdir, "Baz.java");
        copy(TestResources.ECLIPSE_PREFS_FILE, eclipsePrefsFile);
        copy(TestResources.FOO_FILE, fooFile);
        copy(TestResources.BAR_FILE, barFile);
        copy(TestResources.BAZ_FILE, bazFile);

        deleteOnExit(tempDir, testfilesDir, testfilesSubdir, eclipsePrefsFile, fooFile, barFile, bazFile);
    }

    private static void showResultingFiles() {
        System.out.println("--- testfiles/Foo.java ---");
        System.out.println(contentsOf(fooFile));
        System.out.println("--- testfiles/Bar.java ---");
        System.out.println(contentsOf(barFile));
        System.out.println("--- testfiles/subdir/Baz.java ---");
        System.out.println(contentsOf(bazFile));
        System.out.println("--- END ---");
    }

    private static void copy(File from, File to) {
        try {
            InputStream readFrom = new FileInputStream(from);
            OutputStream writeTo = new FileOutputStream(to);
            byte[] buf = new byte[1024];
            int len;
            while ((len = readFrom.read(buf)) > 0) {
                writeTo.write(buf, 0, len);
            }
            readFrom.close();
            writeTo.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void deleteOnExit(File... files) {
        for (File file : files) {
            file.deleteOnExit();
        }
    }

    private static String contentsOf(File file) {
        StringBuilder contents = new StringBuilder();
        try {
            Reader reader = new FileReader(file);
            char[] buf = new char[1024];
            int len;
            while ((len = reader.read(buf)) > 0) {
                contents.append(buf, 0, len);
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return contents.toString();
    }
}
