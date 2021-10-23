package at.binter.gcd.mathematica.syntax.function;

import at.binter.gcd.mathematica.HTMLBuilder;
import at.binter.gcd.mathematica.syntax.IExpression;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;

public class MNamedCall extends MFunction implements IExpression {
    private static final Logger log = LoggerFactory.getLogger(MNamedCall.class);
    private String function = null;
    public static final String htmlTag = null;

    public MNamedCall(String function, IExpression... parameters) {
        this.function = function;
        addParameters(parameters);
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
        Iterator<IExpression> it = getParameters().iterator();
        while (it.hasNext()) {
            builder.append(it.next().getMathematicaExpression());
            if (it.hasNext()) {
                builder.append(",");
            }
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