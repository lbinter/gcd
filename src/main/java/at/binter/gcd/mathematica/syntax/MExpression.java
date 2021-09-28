package at.binter.gcd.mathematica.syntax;

import at.binter.gcd.mathematica.HTMLBuilder;
import at.binter.gcd.mathematica.MBase;
import at.binter.gcd.util.Tools;

public class MExpression extends MBase implements IExpression {
    private String cssClass;
    private final String expression;

    public MExpression(String expression) {
        this.cssClass = null;
        this.expression = expression;
    }

    public MExpression(String expression, boolean doLinebreaks) {
        this(null, expression);
        setDoLinebreaks(doLinebreaks);
    }


    public MExpression(int value) {
        this.cssClass = null;
        this.expression = String.valueOf(value);
    }

    public MExpression(double value) {
        this.cssClass = null;
        String val = String.valueOf(value);
        if (val.endsWith(".0")) {
            expression = val.replace(".0", "");
        } else {
            expression = val;
        }
    }

    public MExpression(String cssClass, String expression) {
        this.cssClass = cssClass;
        this.expression = expression;
    }

    public void setCssClass(String cssClass) {
        this.cssClass = cssClass;
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
    public String getMathematicaExpression() {
        return expression;
    }

    @Override
    public void toHTML(HTMLBuilder builder) {
        if (cssClass != null) {
            builder.write(getCssClass(), Tools.transformMathematicaGreekToGreekHtmlLetters(getExpression()));
        } else {
            builder.write(Tools.transformMathematicaGreekToGreekHtmlLetters(getExpression()));
        }
    }
}