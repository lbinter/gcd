package at.binter.gcd.util;

import java.io.File;

public class FileUtils {
    public static boolean isValidGCDFile(File file) {
        return !file.isDirectory() && file.getAbsolutePath().endsWith(".gcd");
    }
}