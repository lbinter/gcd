package at.binter.gcd.mathematica.elements;

import at.binter.gcd.mathematica.HTMLBuilder;
import at.binter.gcd.mathematica.MBase;
import at.binter.gcd.mathematica.syntax.IExpression;
import at.binter.gcd.mathematica.syntax.MExpression;
import at.binter.gcd.mathematica.syntax.RowBox;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MParameter extends MBase implements IExpression {
    private final List<IExpression> parameter = new ArrayList<>();

    private final IExpression name;

    public MParameter(String name) {
        this.name = new MVariable(name);
    }

    public MParameter(IExpression name) {
        this.name = name;
    }

    public MParameter(String name, IExpression... parameter) {
        this(name);
        for (IExpression p : parameter) {
            addParameter(p);
        }
    }

    public MParameter(IExpression name, String... parameter) {
        this(name);
        for (String p : parameter) {
            addParameter(p);
        }
    }

    public MParameter(String name, String... parameter) {
        this(name);
        for (String p : parameter) {
            addParameter(p);
        }
    }

    public MParameter(IExpression... expressions) {
        this(expressions[0]);
        for (int i = 1; i < expressions.length; i++) {
            addParameter(expressions[i]);
        }
    }

    public String getName() {
        return name.getExpression();
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
        name.toHTML(builder);
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

    @Override
    public String getCssClass() {
        return null;
    }

    @Override
    public String getExpression() {
        return toHTML();
    }

    @Override
    public String getMathematicaExpression() {
        RowBox inner = new RowBox();

        Iterator<IExpression> it = parameter.iterator();
        while (it.hasNext()) {
            IExpression element = it.next();
            inner.add(new MExpression("\"" + element.getMathematicaExpression() + "\""));
            if (it.hasNext()) {
                inner.add(new MExpression("\",\""));
            }
        }

        RowBox outer = new RowBox();
        outer.add(new MExpression(name.getMathematicaExpression()));
        outer.add(new MExpression("\"[\""));
        outer.add(inner);
        outer.add(new MExpression("\"]\""));
        return outer.getMathematicaExpression();
    }
}