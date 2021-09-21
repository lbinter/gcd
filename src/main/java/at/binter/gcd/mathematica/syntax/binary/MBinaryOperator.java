package at.binter.gcd.mathematica.syntax.binary;

import at.binter.gcd.mathematica.HTMLBuilder;
import at.binter.gcd.mathematica.MBase;
import at.binter.gcd.mathematica.syntax.IExpression;

public abstract class MBinaryOperator extends MBase implements IExpression {
    public abstract String getSymbolKeyboard();

    public abstract String getSymbolSpecial();

    private boolean linebreakAfterOperator = false;

    protected IExpression expr1;
    protected IExpression expr2;

    public MBinaryOperator(IExpression expr1, IExpression expr2) {
        this.expr1 = expr1;
        this.expr2 = expr2;
    }

    public boolean isLinebreakAfterOperator() {
        return linebreakAfterOperator;
    }

    public void setLinebreakAfterOperator(boolean linebreakAfterOperator) {
        this.linebreakAfterOperator = linebreakAfterOperator;
    }

    public IExpression getExpr1() {
        return expr1;
    }

    public IExpression getExpr2() {
        return expr2;
    }

    @Override
    public void toHTML(HTMLBuilder builder) {
        int increaseIntent = 0;
        expr1.toHTML(builder);
        builder.write(" ");
        builder.write(getSymbolKeyboard());
        if (linebreakAfterOperator) {
            builder.linebreak();
            builder.increaseIndent(4);
            increaseIntent += 4;
        } else {
            builder.write(" ");
        }
        expr2.toHTML(builder);
        if (isAddSemicolon()) {
            builder.write(";");
        }
        builder.decreaseIndent(increaseIntent);
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
        return toHTML();
    }
}