package at.binter.gcd.mathematica.syntax.function;

import at.binter.gcd.mathematica.HTMLBuilder;
import at.binter.gcd.mathematica.syntax.IExpression;
import at.binter.gcd.mathematica.syntax.MExpression;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MClearAll extends MFunction implements IExpression {
    private static final Logger log = LoggerFactory.getLogger(MClearAll.class);
    public static final String function = "ClearAll";
    public static final String htmlTag = null;

    public MClearAll() {
    }

    public MClearAll(String parameter) {
        addParameter(new MExpression(parameter));
    }

    public MClearAll(MExpression... parameters) {
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

    @Override
    public void toHTML(HTMLBuilder builder) {
        builder.append(function);
        builder.append("[");
        if (getParameters().size() == 1) {
            getParameters().get(0).toHTML(builder);
        } else {
            log.error("ClearAll[x] has to many parameters ({})", getParameters().size());
        }
        builder.append("]");
    }

    @Override
    public String getCssClass() {
        return null;
    }

    @Override
    public String getExpression() {
        return toHTML();
    }
}