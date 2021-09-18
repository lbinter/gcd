package at.binter.gcd.mathematica.syntax.binary;

import at.binter.gcd.mathematica.HTMLBuilder;
import at.binter.gcd.mathematica.MBase;
import at.binter.gcd.mathematica.syntax.IExpression;

public abstract class MBinaryOperator extends MBase implements IExpression {
    public abstract String getSymbolKeyboard();

    public abstract String getSymbolSpecial();

    protected IExpression expr1;
    protected IExpression expr2;

    public MBinaryOperator(IExpression expr1, IExpression expr2) {
        this.expr1 = expr1;
        this.expr2 = expr2;
    }

    @Override
    public void toHTML(HTMLBuilder builder) {
        expr1.toHTML(builder);
        builder.write(" ");
        builder.write(getSymbolKeyboard());
        builder.write(" ");
        expr2.toHTML(builder);
        if (isAddSemicolon()) {
            builder.write(";");
        }
        if (isDoLinebreaks()) {
            builder.linebreak();
        }
    }

    @Override
    public String getCssClass() {
        return null;
    }

    @Override
    public String getExpression() {
        return expr1.getExpression() +
                " " +
                getSymbolKeyboard() +
                " " +
                expr2.getExpression();
    }
}