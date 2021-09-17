package at.binter.gcd.mathematica;

import at.binter.gcd.util.Tools;

public class HTMLBuilder implements GCDBuilder {
    private final StringBuilder builder = new StringBuilder();
    private int indent = 0;

    private boolean currentLineEmpty = true;

    @Override
    public void comment(String comment) {
        append("(* ");
        append(comment);
        append(" *)");
        linebreak();
    }

    @Override
    public void write(String text) {
        if (currentLineEmpty) {
            appendIndent();
        }
        append(text);
    }


    @Override
    public void writeLine(String text) {
        write(text);
        linebreak();
    }

    @Override
    public void linebreak() {
        append(linebreakString());
        currentLineEmpty = true;
    }

    @Override
    public String linebreakString() {
        return "<br>";
    }

    public void append(String str) {
        currentLineEmpty = false;
        builder.append(str);
    }

    @Override
    public String toString() {
        return Tools.transformMathematicaGreekToGreekHtmlLetters(builder.toString());
    }

    public void increaseIndent(int i) {
        indent = indent + i;
    }

    public void decreaseIndent(int i) {
        indent = indent - i;
        if (indent < 0) indent = 0;
    }

    public void appendIndent() {
        if (indent > 0) {
            for (int i = 0; i <= indent; i++) {
                append("&nbsp;");
            }
        }
    }
}