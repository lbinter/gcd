package at.binter.gcd.mathematica;

public interface GCDBuilder {
    void comment(String comment);

    void write(String line);

    void writeLine(String line);

    void linebreak();

    String linebreakString();
}