package at.binter.gcd.mathematica.syntax.function;

import at.binter.gcd.mathematica.syntax.IExpression;
import at.binter.gcd.mathematica.syntax.MExpression;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MCreateSystemModel extends MFunction {
    private static final Logger log = LoggerFactory.getLogger(MCreateSystemModel.class);
    public static final String function = "CreateSystemModel";
    public static final String htmlTag = null;

    public MCreateSystemModel() {
    }

    public MCreateSystemModel(String parameter) {
        addParameter(new MExpression(parameter));
    }

    public MCreateSystemModel(IExpression... parameters) {
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