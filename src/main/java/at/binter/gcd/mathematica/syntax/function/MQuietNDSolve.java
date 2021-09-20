package at.binter.gcd.mathematica.syntax.function;

import at.binter.gcd.mathematica.syntax.IExpression;
import at.binter.gcd.mathematica.syntax.MExpression;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MQuietNDSolve extends MFunction implements IExpression {
    private static final Logger log = LoggerFactory.getLogger(MQuietNDSolve.class);
    public static final String function = "Quiet@NDSolve";
    public static final String htmlTag = null;

    public MQuietNDSolve() {
    }

    public MQuietNDSolve(String parameter) {
        addParameter(new MExpression(parameter));
    }

    public MQuietNDSolve(IExpression... parameters) {
        super(parameters);
    }

    @Override
    public String getFunction() {
        return function;
    }

    @Override
    public String getHtmlTag() {
        return htmlTag;
    }
}