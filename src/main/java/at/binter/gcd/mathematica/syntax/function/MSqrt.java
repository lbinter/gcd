package at.binter.gcd.mathematica.syntax.function;

import at.binter.gcd.mathematica.HTMLBuilder;
import at.binter.gcd.mathematica.syntax.MExpression;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MSqrt extends MFunction {
    private static final Logger log = LoggerFactory.getLogger(MSqrt.class);
    public static final String function = "Sqrt";
    public static final String htmlTag = null;

    public MSqrt() {
    }

    public MSqrt(MExpression... parameters) {
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
        if (getParameters().size() != 1) {
            log.error("Sqrt[x] needs 1 parameter");
            return;
        }
        builder.write("<span style=\"white-space: nowrap; font-size:larger\">&radic;");
        builder.write("<span style=\"text-decoration:overline;\">");
        getParameters().get(0).toHTML(builder);
        builder.write("</span>");
        builder.write("</span>");
    }
}