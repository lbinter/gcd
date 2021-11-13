package at.binter.gcd.util;

import javafx.stage.FileChooser;

import java.io.File;

public class FileUtils {
    public static FileChooser.ExtensionFilter gcdFileExt = new FileChooser.ExtensionFilter("GCD", "*.gcd");
    public static FileChooser.ExtensionFilter nbFileExt = new FileChooser.ExtensionFilter("Mathematica", "*.nb");
    public static FileChooser.ExtensionFilter mathKernelExt = new FileChooser.ExtensionFilter("MathKernel", "MathKernel.exe");

    public static boolean isValidGCDFile(File file) {
        return !file.isDirectory() && file.getAbsolutePath().endsWith(".gcd");
    }

    public static boolean isValidSettingsFile(File file) {
        return !file.isDirectory() && file.getAbsolutePath().endsWith(".xml");
    }
}