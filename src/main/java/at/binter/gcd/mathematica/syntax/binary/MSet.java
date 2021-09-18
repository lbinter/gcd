package at.binter.gcd.mathematica.syntax.binary;

import at.binter.gcd.mathematica.syntax.IExpression;

public class MSet extends MBinaryOperator {
    public static final String symbolSpecial = "=";
    public static final String symbolKeyboard = "=";

    public MSet(IExpression expr1, IExpression expr2) {
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