package at.binter.gcd.mathematica.syntax.function;

import at.binter.gcd.mathematica.HTMLBuilder;
import at.binter.gcd.mathematica.syntax.IExpression;
import at.binter.gcd.mathematica.syntax.MExpression;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MFlatten extends MFunction implements IExpression {
    private static final Logger log = LoggerFactory.getLogger(MFlatten.class);
    public static final String function = "Flatten";
    public static final String htmlTag = null;

    public MFlatten() {
    }

    public MFlatten(String parameter) {
        addParameter(new MExpression(parameter));
    }

    public MFlatten(MExpression... parameters) {
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
            log.error("Flatten[x] has to many parameters ({})", getParameters().size());
        }
        builder.append("]");
    }

    @Override
    public String getCssClass() {
        return null;
    }

    @Override
    public String getExpression() {
        StringBuilder b = new StringBuilder();
        b.append(function);
        b.append("[");
        if (getParameters().size() == 1) {
            b.append(getParameters().get(0).getExpression());
        } else {
            log.error("Flatten[x] has to many parameters ({})", getParameters().size());
        }
        b.append("]");
        return b.toString();
    }
}