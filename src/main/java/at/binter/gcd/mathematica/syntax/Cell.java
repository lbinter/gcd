package at.binter.gcd.mathematica.syntax;


import at.binter.gcd.mathematica.HTMLBuilder;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Cell implements IExpression {
    private final List<IExpression> expressions = new ArrayList<>();

    public Cell() {
    }

    public Cell(IExpression... expressions) {
        addExpressions(expressions);
    }

    public void addExpressions(IExpression... expressions) {
        for (IExpression expr : expressions) {
            if (expr != null) {
                this.expressions.add(expr);
            }
        }
    }

    @Override
    public String toHTML() {
        HTMLBuilder builder = new HTMLBuilder();
        toHTML(builder);
        return builder.toString();
    }

    @Override
    public void toHTML(HTMLBuilder builder) {
        builder.write(getMathematicaExpression());
    }

    @Override
    public String getCssClass() {
        return null;
    }

    @Override
    public String getExpression() {
        return getMathematicaExpression();
    }

    @Override
    public String getMathematicaExpression() {
        StringBuilder b = new StringBuilder();
        b.append("Cell[\r\n");
        Iterator<IExpression> it = expressions.iterator();
        while (it.hasNext()) {
            IExpression box = it.next();
            b.append(box.getMathematicaExpression());
            if (it.hasNext()) {
                b.append(", ");
            }
        }
        b.append(", \"Input\"]");
        return b.toString();
    }
}