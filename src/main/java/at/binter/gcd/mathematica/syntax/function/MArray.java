package at.binter.gcd.mathematica.syntax.function;

import at.binter.gcd.mathematica.HTMLBuilder;
import at.binter.gcd.mathematica.syntax.IExpression;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;

public class MArray extends MFunction implements IExpression {
    private static final Logger log = LoggerFactory.getLogger(MArray.class);
    public static final String function = "Array";
    public static final String htmlTag = null;

    public MArray() {
    }

    public MArray(String parameter) {
        addParameter(parameter);
    }

    public MArray(IExpression... parameters) {
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
        builder.write(function);
        builder.write("[");
        Iterator<IExpression> it = getParameters().iterator();
        while (it.hasNext()) {
            it.next().toHTML(builder);
            if (it.hasNext()) {
                builder.write(delimiter);
            }
        }
        builder.write("]");
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
        Iterator<IExpression> it = getParameters().iterator();
        while (it.hasNext()) {
            b.append(it.next());
            if (it.hasNext()) {
                b.append(delimiter);
            }
        }
        b.append("]");
        return b.toString();
    }
}