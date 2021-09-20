package at.binter.gcd.mathematica.syntax.function;

import at.binter.gcd.mathematica.HTMLBuilder;
import at.binter.gcd.mathematica.syntax.IExpression;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;

public class MSubscript extends MFunction {
    private static final Logger log = LoggerFactory.getLogger(MSubscript.class);
    public static final String function = "Subscript";
    public static final String htmlTag = "sub";

    public MSubscript() {
    }

    public MSubscript(IExpression... operators) {
        super(operators);
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
        if (getParameters().size() < 2) {
            log.error("Subscript[x,y] needs at least 2 parameters");
            return;
        }
        getParameters().get(0).toHTML(builder);
        builder.write("<" + getHtmlTag() + ">");
        Iterator<IExpression> it = getParameters().iterator();
        it.next();
        while (it.hasNext()) {
            it.next().toHTML(builder);
            if (it.hasNext()) {
                builder.write(getDelimiter());
            }
        }
        builder.write("</" + getHtmlTag() + ">");
    }
}