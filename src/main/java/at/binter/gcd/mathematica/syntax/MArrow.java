package at.binter.gcd.mathematica.syntax;

import at.binter.gcd.mathematica.HTMLBuilder;

public class MArrow implements IExpression {
    @Override
    public String toHTML() {
        HTMLBuilder builder = new HTMLBuilder();
        toHTML(builder);
        return builder.toString();
    }

    @Override
    public void toHTML(HTMLBuilder builder) {
        builder.write("&#8594;");
    }

    @Override
    public String getCssClass() {
        return null;
    }

    @Override
    public String getExpression() {
        return "->";
    }

    @Override
    public String getMathematicaExpression() {
        return "\"\\[Rule]\"";
    }
}
