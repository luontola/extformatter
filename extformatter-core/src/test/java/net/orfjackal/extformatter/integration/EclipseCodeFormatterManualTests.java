/*
 * External Code Formatter
 * Copyright (c) 2007-2009  Esko Luontola, www.orfjackal.net
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

package net.orfjackal.extformatter.integration;

import net.orfjackal.extformatter.*;
import net.orfjackal.extformatter.util.*;

import java.io.*;

/**
 * @author Esko Luontola
 * @since 1.12.2007
 */
public class EclipseCodeFormatterManualTests {

    /**
     * "eclipsec.exe" does not pop up a black command line dialog, so it's best to use it instead of "eclipse.exe"
     */
//    private static final File ECLIPSE_EXECUTABLE = new File("C:\\eclipse-SDK-3.3.1-win32\\eclipse\\eclipsec.exe");
    private static final File ECLIPSE_EXECUTABLE = new File("C:\\eclipse-java-europa-fall2-win32\\eclipse\\eclipsec.exe");
    private static final ProcessExecutor1 EXECUTOR = new ProcessExecutor1Impl();
//    private static final ProcessExecutor1 EXECUTOR = new ProcessExecutor1Dummy();

    private static File testfilesDir;
    private static File eclipsePrefsFile;
    private static File fooFile;
    private static File barFile;
    private static File gazonkFile;

    private static EclipseCodeFormatter newFormatter() {
        return new EclipseCodeFormatter(ECLIPSE_EXECUTABLE, eclipsePrefsFile, EXECUTOR);
    }

    public static class ReformatOneTest {

        public static void main(String[] args) {
            showCurrentTest(ReformatOneTest.class, "Foo.java (1)");
            prepareTestFiles();
            newFormatter().reformatOne(fooFile);
            showResultingFiles();
        }
    }

    public static class ReformatManyTest {

        public static void main(String[] args) {
            showCurrentTest(ReformatManyTest.class, "Foo.java (1), Gazonk.java (3)");
            prepareTestFiles();
            newFormatter().reformatMany(fooFile, gazonkFile);
            showResultingFiles();
        }
    }

    public static class ReformatDirectoryTest {

        public static void main(String[] args) {
            System.out.println("Does not support reformat directory");
//            showCurrentTest(ReformatDirectoryTest.class, "Foo.java (1), Bar.java (2)");
//            prepareTestFiles();
//            newFormatter().reformatDirectory(testfilesDir);
//            showResultingFiles();
        }
    }

    public static class ReformatRecursivelyTest {

        public static void main(String[] args) {
            showCurrentTest(ReformatRecursivelyTest.class, "Foo.java (1), Bar.java (2), Gazonk.java (3)");
            prepareTestFiles();
            newFormatter().reformatRecursively(testfilesDir);
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
        gazonkFile = new File(testfilesSubdir, "Gazonk.java");
        copy(TestResources.ECLIPSE_PREFS_FILE, eclipsePrefsFile);
        copy(TestResources.FOO_FILE, fooFile);
        copy(TestResources.BAR_FILE, barFile);
        copy(TestResources.GAZONK_FILE, gazonkFile);

        deleteOnExit(tempDir, testfilesDir, testfilesSubdir, eclipsePrefsFile, fooFile, barFile, gazonkFile);
    }

    private static void showCurrentTest(Class<?> clazz, String expected) {
        String readableName = separateWords(clazz.getSimpleName());
        System.out.println("--- " + readableName + " ---");
        System.out.println("Expected files to be formatted: " + expected);
        System.out.println();
    }

    private static String separateWords(String className) {
        StringBuilder readableName = new StringBuilder();
        for (String letter : className.split("")) {
            if (letter.length() > 0 && Character.isUpperCase(letter.charAt(0))) {
                readableName.append(" ");
            }
            readableName.append(letter.toLowerCase());
        }
        return readableName.toString().trim();
    }

    private static void showResultingFiles() {
        System.out.println();
        System.out.println("--- testfiles/Foo.java (1) ---");
        System.out.println(contentsOf(fooFile));
        System.out.println("--- testfiles/Bar.java (2) ---");
        System.out.println(contentsOf(barFile));
        System.out.println("--- testfiles/subdir/Gazonk.java (3) ---");
        System.out.println(contentsOf(gazonkFile));
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
