package at.binter.gcd.mathematica.syntax.binary;

import at.binter.gcd.mathematica.syntax.IExpression;

public class MSetDelayed extends MBinaryOperator {
    public static final String symbolSpecial = ":=";
    public static final String symbolKeyboard = ":=";

    public MSetDelayed(IExpression expr1, IExpression expr2) {
        super(expr1, expr2);
    }

    public MSetDelayed(IExpression expr1, IExpression expr2, boolean addSemicolon) {
        super(expr1, expr2);
        setAddSemicolon(addSemicolon);
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