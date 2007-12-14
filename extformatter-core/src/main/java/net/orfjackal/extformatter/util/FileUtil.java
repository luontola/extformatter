package net.orfjackal.extformatter.util;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;

/**
 * @author Esko Luontola
 * @since 14.12.2007
 */
public class FileUtil {
    @NotNull
    public static String listOf(@NotNull File... files) throws IOException {
        if (files.length == 0) {
            throw new IllegalArgumentException("No files");
        }
        String paths = "";
        for (File file : files) {
            if (paths.length() > 0) {
                paths += " ";
            }
            paths += quoted(file.getCanonicalPath());
        }
        return paths;
    }

    @NotNull
    public static String quoted(@NotNull String s) {
        return '"' + s + '"';
    }
}
