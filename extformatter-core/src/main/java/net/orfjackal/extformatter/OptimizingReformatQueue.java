
// OptimizingReformatQueue.java --
//
// OptimizingReformatQueue.java is part of ElectricCommander.
//
// Copyright (c) 2005-2012 Electric Cloud, Inc.
// All rights reserved.
//

package net.orfjackal.extformatter;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.orfjackal.extformatter.util.Directories;
import net.orfjackal.extformatter.util.FilesSupportedBy;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Uses as few reformat method calls as possible to reformat all the queued
 * files.
 *
 * @author  Esko Luontola
 * @since   7.12.2007
 */
public class OptimizingReformatQueue
    implements ReformatQueue
{

    //~ Instance fields --------------------------------------------------------

    @NotNull private final CodeFormatter m_formatter;
    @NotNull private final List<File>    m_fileQueue = new ArrayList<File>();

    //~ Constructors -----------------------------------------------------------

    public OptimizingReformatQueue(@NotNull CodeFormatter formatter)
    {
        m_formatter = formatter;
    }

    //~ Methods ----------------------------------------------------------------

    @Override public void flush()
    {
        m_fileQueue.removeAll(useReformatMany());
        m_fileQueue.removeAll(useReformatRecursively());
        m_fileQueue.removeAll(useReformatDirectory());
        m_fileQueue.removeAll(useReformatOne());
        mustBeEmpty(m_fileQueue);
    }

    @Override public void reformatDirectory(@NotNull File directory)
    {
        throw new UnsupportedOperationException();
    }

    @Override public void reformatMany(@NotNull File... files)
    {
        throw new UnsupportedOperationException();
    }

    @Override public void reformatOne(@NotNull File file)
    {
        assert supportsFileType(file);
        m_fileQueue.add(file);
    }

    @Override public void reformatRecursively(@NotNull File directory)
    {
        throw new UnsupportedOperationException();
    }

    @Override public boolean supportsFileType(@NotNull File file)
    {
        return m_formatter.supportsFileType(file);
    }

    @Override public boolean supportsReformatDirectory()
    {
        return false;
    }

    // Unsupported operations
    @Override public boolean supportsReformatMany()
    {
        return false;
    }

    @Override public boolean supportsReformatOne()
    {
        return true;
    }

    @Override public boolean supportsReformatRecursively()
    {
        return false;
    }

    private boolean noOthersInTheSameDirectory(
            @NotNull File       directory,
            @NotNull List<File> files)
    {
        File[] allFilesInDir = directory.listFiles(new FilesSupportedBy(this));

        for (File fileInDir : allFilesInDir) {

            if (!files.contains(fileInDir)) {
                return false;
            }
        }

        return true;
    }

    private boolean noOthersInTheSameDirectoryTree(
            @NotNull File       directory,
            @NotNull List<File> files)
    {

        if (!noOthersInTheSameDirectory(directory, files)) {
            return false;
        }

        File[] subDirs = directory.listFiles(new Directories());

        for (File subDir : subDirs) {

            if (!noOthersInTheSameDirectoryTree(subDir, files)) {
                return false;
            }
        }

        return true;
    }

    @NotNull private List<File> useReformatDirectory()
    {
        List<File> reformatted = new ArrayList<File>();

        if (m_formatter.supportsReformatDirectory()) {
            Map<File, List<File>> groups = groupByDirectory(m_fileQueue);

            for (Map.Entry<File, List<File>> group : groups.entrySet()) {
                File       directory = group.getKey();
                List<File> files     = group.getValue();

                if (noOthersInTheSameDirectory(directory, files)) {
                    m_formatter.reformatDirectory(directory);
                    reformatted.addAll(files);
                }
            }
        }

        return reformatted;
    }

    @NotNull private List<File> useReformatMany()
    {
        List<File> reformatted = new ArrayList<File>();

        if (m_formatter.supportsReformatMany() && m_fileQueue.size() > 0) {
            m_formatter.reformatMany(toArray(m_fileQueue));
            reformatted.addAll(m_fileQueue);
        }

        return reformatted;
    }

    @NotNull private List<File> useReformatOne()
    {
        List<File> reformatted = new ArrayList<File>();

        if (m_formatter.supportsReformatOne()) {

            for (File file : m_fileQueue) {
                m_formatter.reformatOne(file);
                reformatted.add(file);
            }
        }

        return reformatted;
    }

    @NotNull private List<File> useReformatRecursively()
    {
        List<File> reformatted = new ArrayList<File>();

        if (m_formatter.supportsReformatRecursively()) {
            File directory = commonParentDirectory(m_fileQueue);

            if (directory != null
                    && noOthersInTheSameDirectoryTree(directory, m_fileQueue)) {
                m_formatter.reformatRecursively(directory);
                reformatted.addAll(m_fileQueue);
            }
        }

        return reformatted;
    }

    @Override public boolean isEmpty()
    {
        return m_fileQueue.isEmpty();
    }

    //~ Methods ----------------------------------------------------------------

    @Nullable private static File commonParentDirectory(
            @NotNull List<File> files)
    {

        // Assumes that the common parent directory contains some files.
        // Otherwise there should be some check that this does not go
        // up to the root directory and possibly by accident reformat
        // files outside the project source directories.
        File commonParent = null;

        for (File file : files) {

            if (commonParent == null) {
                commonParent = file.getParentFile();
            }

            if (isParent(file.getParentFile(), commonParent)) {
                commonParent = file.getParentFile();
            }
        }

        return commonParent;
    }

    @NotNull private static Map<File, List<File>> groupByDirectory(
            @NotNull List<File> files)
    {
        Map<File, List<File>> groups = new HashMap<File, List<File>>();

        for (File file : files) {
            File directory = file.getParentFile();

            putToList(groups, directory, file);
        }

        return groups;
    }

    private static void mustBeEmpty(@NotNull List<File> files)
    {

        if (!files.isEmpty()) {

            try {
                throw new IllegalStateException(
                    "The following files could not be reformatted: " + files);
            }
            finally {
                files.clear();
            }
        }
    }

    private static <T> void putToList(
            @NotNull Map<T, List<T>> map,
            @NotNull T               key,
            @NotNull T               value)
    {
        List<T> list = map.get(key);

        if (list == null) {
            list = new ArrayList<T>();
            map.put(key, list);
        }

        list.add(value);
    }

    @NotNull private static File[] toArray(@NotNull List<File> files)
    {
        return files.toArray(new File[files.size()]);
    }

    private static boolean isParent(
            @NotNull File parent,
            @NotNull File child)
    {

        for (File dir = child; dir.getParentFile() != null;
                dir = dir.getParentFile()) {

            if (dir.equals(parent)) {
                return true;
            }
        }

        return false;
    }
}
