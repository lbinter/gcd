package at.binter.gcd.mathematica;

import java.io.FileOutputStream;

public interface NotebookWriter {
    String toNBRaw();

    void toNBRaw(FileOutputStream output);
}