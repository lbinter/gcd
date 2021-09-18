package at.binter.gcd.mathematica.syntax.binary;

import at.binter.gcd.mathematica.syntax.IExpression;

public class MNotEqual extends MBinaryOperator {
    public static final String symbolSpecial = "\\[NotEqual]";
    public static final String symbolKeyboard = "!=";

    public MNotEqual(IExpression expr1, IExpression expr2) {
        super(expr1, expr2);
    }

    @Override
    public String getSymbolKeyboard() {
        return symbolKeyboard;
    }

    @Override
    public String getSymbolSpecial() {
        return symbolSpecial;
    }
}