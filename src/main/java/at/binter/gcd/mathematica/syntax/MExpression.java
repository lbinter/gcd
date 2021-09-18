package at.binter.gcd.mathematica.syntax;

import at.binter.gcd.mathematica.HTMLBuilder;
import at.binter.gcd.mathematica.MBase;

public class MExpression extends MBase implements IExpression {
    private final String cssClass;
    private final String expression;

    public MExpression(String expression) {
        this.cssClass = null;
        this.expression = expression;
    }

    public MExpression(int value) {
        this.cssClass = null;
        this.expression = String.valueOf(value);
    }

    public MExpression(String cssClass, String expression) {
        this.cssClass = cssClass;
        this.expression = expression;
    }

    @Override
    public String getCssClass() {
        return cssClass;
    }

    @Override
    public String getExpression() {
        return expression;
    }

    @Override
    public void toHTML(HTMLBuilder builder) {
        if (cssClass != null) {
            builder.write(getCssClass(), getExpression());
        } else {
            builder.write(getExpression());
        }
    }
}