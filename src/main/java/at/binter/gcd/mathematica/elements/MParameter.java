package at.binter.gcd.mathematica.elements;

import at.binter.gcd.mathematica.HTMLBuilder;
import at.binter.gcd.mathematica.syntax.IExpression;
import at.binter.gcd.mathematica.syntax.MExpression;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MParameter extends MExpression {
    private final List<IExpression> parameter = new ArrayList<>();

    public MParameter(String name) {
        super("variable", name);
    }

    public MParameter(String name, IExpression... parameter) {
        this(name);
        for (IExpression p : parameter) {
            addParameter(p);
        }
    }

    public MParameter(String name, String... parameter) {
        this(name);
        for (String p : parameter) {
            addParameter(p);
        }
    }

    public String getName() {
        return getExpression();
    }

    public List<IExpression> getParameter() {
        return parameter;
    }

    public void addParameter(IExpression p) {
        parameter.add(p);
    }

    public void addParameter(String p) {
        addParameter(new MExpression("parameter", p));
    }

    @Override
    public void toHTML(HTMLBuilder builder) {
        super.toHTML(builder);
        builder.write("[");
        Iterator<IExpression> it = parameter.iterator();
        while (it.hasNext()) {
            IExpression p = it.next();
            if (StringUtils.isNumeric(p.getExpression())) {
                builder.write(p.getExpression());
            } else {
                p.toHTML(builder);
            }
            if (it.hasNext()) {
                builder.write(", ");
            }
        }
        builder.write("]");
    }
}