package at.binter.gcd.util;

import javafx.stage.FileChooser;

import java.io.File;
import java.nio.file.FileSystems;

public class FileUtils {
    public static final String fileSeparator = FileSystems.getDefault().getSeparator();

    public static final FileChooser.ExtensionFilter gcdFileExt = new FileChooser.ExtensionFilter("GCD", "*.gcd");
    public static final FileChooser.ExtensionFilter nbFileExt = new FileChooser.ExtensionFilter("Mathematica", "*.nb");
    public static final FileChooser.ExtensionFilter mathKernelExt = new FileChooser.ExtensionFilter("MathKernel", "MathKernel.exe");
    public static final FileChooser.ExtensionFilter mathKernelLinuxExt = new FileChooser.ExtensionFilter("MathKernel", "MathKernel");

    public static boolean isValidGCDFile(File file) {
        return !file.isDirectory() && file.getAbsolutePath().endsWith(".gcd");
    }

    public static boolean isValidSettingsFile(File file) {
        return !file.isDirectory() && file.getAbsolutePath().endsWith(".xml");
    }
}