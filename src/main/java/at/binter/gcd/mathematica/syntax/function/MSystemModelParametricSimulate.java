package at.binter.gcd.mathematica.syntax.function;

import at.binter.gcd.mathematica.syntax.IExpression;
import at.binter.gcd.mathematica.syntax.MExpression;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MSystemModelParametricSimulate extends MFunction {
    private static final Logger log = LoggerFactory.getLogger(MSystemModelParametricSimulate.class);
    public static final String function = "SystemModelParametricSimulate";
    public static final String htmlTag = null;

    public MSystemModelParametricSimulate() {
    }

    public MSystemModelParametricSimulate(String parameter) {
        addParameter(new MExpression(parameter));
    }

    public MSystemModelParametricSimulate(IExpression... parameters) {
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