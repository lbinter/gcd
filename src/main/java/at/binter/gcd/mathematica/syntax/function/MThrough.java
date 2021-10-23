package at.binter.gcd.mathematica.syntax.function;

import at.binter.gcd.mathematica.syntax.IExpression;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MThrough extends MFunction implements IExpression {
    private static final Logger log = LoggerFactory.getLogger(MThrough.class);
    public static final String function = "Through";
    public static final String htmlTag = null;

    private String target;

    public MThrough(String target) {
        this.target = target;
    }

    @Override
    public String getFunction() {
        return function;
    }

    @Override
    public String getHtmlTag() {
        return htmlTag;
    }

    public String getTarget() {
        return target;
    }
}