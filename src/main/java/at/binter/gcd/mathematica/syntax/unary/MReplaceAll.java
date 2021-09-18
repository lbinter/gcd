package at.binter.gcd.mathematica.syntax.unary;

import at.binter.gcd.mathematica.syntax.IExpression;

public class MReplaceAll extends MUnaryOperator {
    public static final String symbolSpecial = "ReplaceAll";
    public static final String symbolKeyboard = "/.";

    public MReplaceAll(IExpression expr) {
        super(expr);
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
