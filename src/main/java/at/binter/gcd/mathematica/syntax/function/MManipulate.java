package at.binter.gcd.mathematica.syntax.function;

import at.binter.gcd.mathematica.syntax.IExpression;
import at.binter.gcd.mathematica.syntax.MExpression;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MManipulate extends MFunction {
    private static final Logger log = LoggerFactory.getLogger(MManipulate.class);
    public static final String function = "Manipulate";
    public static final String htmlTag = null;

    public MManipulate() {
    }

    public MManipulate(String parameter) {
        addParameter(new MExpression(parameter));
    }

    public MManipulate(IExpression... parameters) {
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