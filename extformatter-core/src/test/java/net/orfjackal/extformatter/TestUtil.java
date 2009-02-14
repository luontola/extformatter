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

package net.orfjackal.extformatter;

import org.jmock.Expectations;

import java.io.File;

/**
 * @author Esko Luontola
 * @since 11.1.2008
 */
public class TestUtil {

    public static Expectations supportsReformatting(final CodeFormatter formatter,
                                                    final boolean reformatOne,
                                                    final boolean reformatMany,
                                                    final boolean reformatDirectory,
                                                    final boolean reformatRecursively) {
        return new Expectations() {{
            allowing (formatter).supportsFileType(with(any(File.class))); will(returnValue(true));
            allowing (formatter).supportsReformatOne();         will(returnValue(reformatOne));
            allowing (formatter).supportsReformatMany();        will(returnValue(reformatMany));
            allowing (formatter).supportsReformatDirectory();   will(returnValue(reformatDirectory));
            allowing (formatter).supportsReformatRecursively(); will(returnValue(reformatRecursively));
        }};
    }
}
