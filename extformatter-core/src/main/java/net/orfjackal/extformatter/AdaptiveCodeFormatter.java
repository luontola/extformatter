
// AdaptiveCodeFormatter.java --
//
// AdaptiveCodeFormatter.java is part of ElectricCommander.
//
// Copyright (c) 2005-2012 Electric Cloud, Inc.
// All rights reserved.
//

package net.orfjackal.extformatter;

import java.io.File;

import net.orfjackal.extformatter.util.Directories;
import net.orfjackal.extformatter.util.FilesSupportedBy;

import org.jetbrains.annotations.NotNull;

/**
 * Uses alternative reformat methods if the underlying formatter does not
 * support all methods.
 *
 * @author  Esko Luontola
 * @since   6.12.2007
 */
public class AdaptiveCodeFormatter
    implements CodeFormatter
{

    //~ Instance fields --------------------------------------------------------

    private final CodeFormatter m_formatter;

    //~ Constructors -----------------------------------------------------------

    public AdaptiveCodeFormatter(@NotNull CodeFormatter formatter)
    {
        m_formatter = formatter;
    }

    //~ Methods ----------------------------------------------------------------

    @Override public void reformatDirectory(@NotNull File directory)
    {

        if (m_formatter.supportsReformatDirectory()) {
            m_formatter.reformatDirectory(directory);
        }
        else {
            File[] files = directory.listFiles(new FilesSupportedBy(this));

            reformatMany(files);
        }
    }

    @Override public void reformatMany(@NotNull File... files)
    {

        for (File file : files) {
            assert supportsFileType(file);
        }

        if (m_formatter.supportsReformatMany()) {
            m_formatter.reformatMany(files);
        }
        else if (m_formatter.supportsReformatOne()) {

            for (File file : files) {
                m_formatter.reformatOne(file);
            }
        }
        else {
            throw new UnsupportedOperationException();
        }
    }

    @Override public void reformatOne(@NotNull File file)
    {
        assert supportsFileType(file);

        if (m_formatter.supportsReformatOne()) {
            m_formatter.reformatOne(file);
        }
        else if (m_formatter.supportsReformatMany()) {
            m_formatter.reformatMany(file);
        }
        else {
            throw new UnsupportedOperationException();
        }
    }

    @Override public void reformatRecursively(@NotNull File directory)
    {

        if (m_formatter.supportsReformatRecursively()) {
            m_formatter.reformatRecursively(directory);
        }
        else {
            File[] subdirs = directory.listFiles(new Directories());

            reformatDirectory(directory);

            for (File subdir : subdirs) {
                reformatRecursively(subdir);
            }
        }
    }

    @Override public boolean supportsFileType(@NotNull File file)
    {
        return m_formatter.supportsFileType(file);
    }

    @Override public boolean supportsReformatDirectory()
    {
        return m_formatter.supportsReformatOne()
            || m_formatter.supportsReformatMany()
            || m_formatter.supportsReformatDirectory();
    }

    @Override public boolean supportsReformatMany()
    {
        return m_formatter.supportsReformatOne()
            || m_formatter.supportsReformatMany();
    }

    @Override public boolean supportsReformatOne()
    {
        return m_formatter.supportsReformatOne()
            || m_formatter.supportsReformatMany();
    }

    @Override public boolean supportsReformatRecursively()
    {
        return m_formatter.supportsReformatOne()
            || m_formatter.supportsReformatMany()
            || m_formatter.supportsReformatDirectory()
            || m_formatter.supportsReformatRecursively();
    }
}
