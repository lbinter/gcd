package at.binter.gcd.mathematica.syntax.group;

import at.binter.gcd.mathematica.HTMLBuilder;
import at.binter.gcd.mathematica.syntax.IExpression;
import at.binter.gcd.mathematica.syntax.MExpression;
import at.binter.gcd.mathematica.syntax.RowBox;

public class MParentheses extends MGroup {
    public static final String groupStartSymbol = "(";
    public static final String groupCloseSymbol = ")";

    protected IExpression expr;

    public MParentheses(IExpression expr) {
        this.expr = expr;
    }

    @Override
    public String getGroupStartSymbol() {
        return groupStartSymbol;
    }

    @Override
    public String getGroupCloseSymbol() {
        return groupCloseSymbol;
    }

    @Override
    public void toHTML(HTMLBuilder builder) {
        builder.write(getGroupStartSymbol());
        expr.toHTML(builder);
        builder.write(getGroupCloseSymbol());
    }

    @Override
    public String getCssClass() {
        return null;
    }

    @Override
    public String getExpression() {
        return toHTML();
    }

    @Override
    public String getMathematicaExpression() {
        RowBox outer = new RowBox();
        outer.add(new MExpression("\"" + getGroupStartSymbol() + "\""));
        outer.add(expr);
        outer.add(new MExpression("\"" + getGroupCloseSymbol() + "\""));
        return outer.getMathematicaExpression();
    }
}