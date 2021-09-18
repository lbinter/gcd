package at.binter.gcd.mathematica.syntax;

public class MathFactory {
    public static MExpression createExpression(String expr) {
        return new MExpression(expr);
    }
}