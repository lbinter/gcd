package at.binter.gcd.mathematica.syntax.function;

import at.binter.gcd.mathematica.syntax.IExpression;
import at.binter.gcd.mathematica.syntax.MExpression;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MModule extends MFunction implements IExpression {
    private static final Logger log = LoggerFactory.getLogger(MModule.class);
    public static final String function = "Module";
    public static final String htmlTag = null;

    public MModule() {
    }

    public MModule(String parameter) {
        addParameter(new MExpression(parameter));
    }

    public MModule(IExpression... parameters) {
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