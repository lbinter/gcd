package at.binter.gcd.mathematica.syntax;

import at.binter.gcd.mathematica.HTMLBuilder;
import at.binter.gcd.mathematica.MBase;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MExpressionList extends MBase implements IExpression {
    private final List<IExpression> expressions = new ArrayList<>();

    public MExpressionList() {
    }

    public MExpressionList(IExpression... expressions) {
        addAll(expressions);
    }

    public List<IExpression> getExpressions() {
        return expressions;
    }

    public void addAll(IExpression... expressions) {
        if (expressions == null || expressions.length == 0) return;
        for (IExpression e : expressions) {
            add(e);
        }
    }

    public void add(IExpression expression) {
        if (expression == null || StringUtils.isBlank(expression.getExpression())) return;
        expressions.add(expression);
    }

    @Override
    public String getCssClass() {
        return null;
    }

    @Override
    public String getExpression() {
        StringBuilder b = new StringBuilder();
        Iterator<IExpression> it = expressions.iterator();
        while (it.hasNext()) {
            b.append(it.next().getExpression());
            if (it.hasNext()) {
                b.append(" ");
            }
        }
        return b.toString();
    }

    @Override
    public void toHTML(HTMLBuilder builder) {
        Iterator<IExpression> it = expressions.iterator();
        while (it.hasNext()) {
            it.next().toHTML(builder);
            if (it.hasNext()) {
                builder.write(" ");
            }
        }
    }
}