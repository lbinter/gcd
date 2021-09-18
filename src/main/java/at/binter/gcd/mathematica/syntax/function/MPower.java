package at.binter.gcd.mathematica.syntax.function;

import at.binter.gcd.mathematica.HTMLBuilder;
import at.binter.gcd.mathematica.syntax.MExpression;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MPower extends MFunction {
    private static final Logger log = LoggerFactory.getLogger(MPower.class);
    public static final String function = "Power";
    public static final String htmlTag = "sup";

    public MPower() {
    }

    public MPower(MExpression... parameters) {
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
        if (getParameters().size() != 2) {
            log.error("Power[x,y] needs 2 parameters");
            return;
        }
        getParameters().get(0).toHTML(builder);
        builder.write("<" + getHtmlTag() + ">");
        getParameters().get(1).toHTML(builder);
        builder.write("</" + getHtmlTag() + ">");
    }
}