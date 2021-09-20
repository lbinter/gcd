package at.binter.gcd.mathematica.syntax.function;

import at.binter.gcd.mathematica.syntax.IExpression;
import at.binter.gcd.mathematica.syntax.MExpression;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MColumn extends MFunction implements IExpression {
    private static final Logger log = LoggerFactory.getLogger(MColumn.class);
    public static final String function = "Column";
    public static final String htmlTag = null;

    public MColumn() {
    }

    public MColumn(String parameter) {
        addParameter(new MExpression(parameter));
    }

    public MColumn(IExpression... parameters) {
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