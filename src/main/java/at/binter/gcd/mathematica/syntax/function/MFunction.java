package at.binter.gcd.mathematica.syntax.function;

import at.binter.gcd.mathematica.HTMLBuilder;
import at.binter.gcd.mathematica.MBase;
import at.binter.gcd.mathematica.syntax.IExpression;
import at.binter.gcd.mathematica.syntax.MExpression;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public abstract class MFunction extends MBase implements IExpression {
    public static final String delimiter = ", ";

    protected final List<IExpression> parameters = new ArrayList<>();

    public MFunction() {
    }

    public MFunction(IExpression... parameters) {
        super();
        addParameters(parameters);
    }

    public abstract String getFunction();

    public abstract String getHtmlTag();

    public List<IExpression> getParameters() {
        return parameters;
    }

    public void addParameters(IExpression... params) {
        if (params == null || params.length == 0) return;
        for (IExpression e : params) {
            addParameter(e);
        }
    }

    public void addParameters(String... params) {
        if (params == null || params.length == 0) return;
        for (String s : params) {
            addParameter(s);
        }
    }

    public void addParameter(IExpression expr) {
        if (expr == null) return;
        parameters.add(expr);
    }

    public void addParameter(String expr) {
        if (StringUtils.isBlank(expr)) return;
        parameters.add(new MExpression(expr));
    }

    @Override
    public String getCssClass() {
        return null;
    }

    @Override
    public String getExpression() {
        return toHTML();
    }

    @Override
    public void toHTML(HTMLBuilder builder) {
        builder.write(getFunction());
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
}