package at.binter.gcd.mathematica.syntax.unary;

import at.binter.gcd.mathematica.syntax.IExpression;
import at.binter.gcd.mathematica.syntax.binary.MBinaryOperator;

public class MReplaceAll extends MBinaryOperator {
    public static final String symbolSpecial = "ReplaceAll";
    public static final String symbolKeyboard = "/.";

    public MReplaceAll(IExpression expr1, IExpression expr2) {
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
