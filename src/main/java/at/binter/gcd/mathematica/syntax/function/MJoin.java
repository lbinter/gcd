package at.binter.gcd.mathematica.syntax.function;

import at.binter.gcd.mathematica.syntax.IExpression;
import at.binter.gcd.mathematica.syntax.MExpression;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MJoin extends MFunction implements IExpression {
    private static final Logger log = LoggerFactory.getLogger(MJoin.class);
    public static final String function = "Join";
    public static final String htmlTag = null;

    public MJoin() {
    }

    public MJoin(String parameter) {
        addParameter(new MExpression(parameter));
    }

    public MJoin(IExpression... parameters) {
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