package at.binter.gcd.mathematica;

public interface GCDBuilder {
    void comment(String comment);

    void write(String line);

    void writeln(String line);

    void linebreak();

    String linebreakString();
}