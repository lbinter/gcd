package at.binter.gcd.mathematica.elements;

import at.binter.gcd.mathematica.HTMLBuilder;
import at.binter.gcd.mathematica.syntax.MExpression;

public class MVariable extends MExpression {
    public MVariable(String variableName) {
        super("variable", variableName);
    }

    public MVariable(String cssClass, String name) {
        super(cssClass, name);
    }

    public String getName() {
        return getExpression();
    }

    @Override
    public void toHTML(HTMLBuilder builder) {
        super.toHTML(builder);
    }

    @Override
    public String getMathematicaExpression() {
        return "\"" + super.getMathematicaExpression() + "\"";
    }
}