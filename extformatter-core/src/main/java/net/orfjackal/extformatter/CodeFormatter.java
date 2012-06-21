
// CodeFormatter.java --
//
// CodeFormatter.java is part of ElectricCommander.
//
// Copyright (c) 2005-2012 Electric Cloud, Inc.
// All rights reserved.
//

package net.orfjackal.extformatter;

import java.io.File;

import org.jetbrains.annotations.NotNull;

/**
 * Reformats source code files. Not all formatters might support all files and
 * methods.
 *
 * @author  Esko Luontola
 * @since   30.11.2007
 */
public interface CodeFormatter
{

    //~ Methods ----------------------------------------------------------------

    void reformatDirectory(@NotNull File directory);

    void reformatMany(@NotNull File... files);

    void reformatOne(@NotNull File file);

    void reformatRecursively(@NotNull File directory);

    boolean supportsFileType(@NotNull File file);

    boolean supportsReformatDirectory();

    boolean supportsReformatMany();

    boolean supportsReformatOne();

    boolean supportsReformatRecursively();
}
