package at.binter.gcd.mathematica.syntax;

import at.binter.gcd.mathematica.HTMLBuilder;
import at.binter.gcd.mathematica.MBase;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MExpressionList extends MBase implements IExpression {
    private final List<IExpression> expressions = new ArrayList<>();

    private String delimiter = " ";

    public MExpressionList() {
    }

    public MExpressionList(boolean noDelimiter) {
        if (noDelimiter) {
            setDelimiter(null);
        }
    }

    public MExpressionList(String delimiter) {
        setDelimiter(delimiter);
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

    public String getDelimiter() {
        return delimiter;
    }

    public void setDelimiter(String delimiter) {
        this.delimiter = delimiter;
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
        Iterator<IExpression> it = expressions.iterator();
        while (it.hasNext()) {
            it.next().toHTML(builder);
            if (delimiter != null && it.hasNext()) {
                builder.write(delimiter);
            }
        }
    }

    @Override
    public String getMathematicaExpression() {
        RowBox inner = new RowBox();
        for (IExpression expression : expressions) {
            inner.add(new MExpression(expression.getMathematicaExpression()));
        }
        return inner.getMathematicaExpression();
    }
}