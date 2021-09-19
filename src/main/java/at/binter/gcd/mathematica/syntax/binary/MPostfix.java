package at.binter.gcd.mathematica.syntax.binary;

import at.binter.gcd.mathematica.syntax.IExpression;

public class MPostfix extends MBinaryOperator {
    public static final String symbolSpecial = "Postfix";
    public static final String symbolKeyboard = "//";

    public MPostfix(IExpression expr1, IExpression expr2) {
        super(expr1, expr2);
    }

    public MPostfix(IExpression expr1, IExpression expr2, boolean addSemicolon) {
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