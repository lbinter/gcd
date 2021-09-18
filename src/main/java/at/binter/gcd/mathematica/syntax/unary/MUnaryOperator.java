package at.binter.gcd.mathematica.syntax.unary;

import at.binter.gcd.mathematica.HTMLBuilder;
import at.binter.gcd.mathematica.MBase;
import at.binter.gcd.mathematica.syntax.IExpression;

public abstract class MUnaryOperator extends MBase implements IExpression {
    public abstract String getSymbolKeyboard();

    public abstract String getSymbolSpecial();

    protected IExpression expr;

    public MUnaryOperator(IExpression expr) {
        this.expr = expr;
    }

    @Override
    public void toHTML(HTMLBuilder builder) {
        builder.write(getSymbolKeyboard());
        builder.write(" ");
        expr.toHTML(builder);
    }

    @Override
    public String getCssClass() {
        return null;
    }

    @Override
    public String getExpression() {
        return getSymbolKeyboard() +
                " " +
                expr.getExpression();
    }
}