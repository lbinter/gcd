package at.binter.gcd.mathematica.syntax;

import at.binter.gcd.mathematica.HTMLBuilder;
import at.binter.gcd.mathematica.MBase;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static at.binter.gcd.util.MathematicaUtils.linebreak;

public class RowBox extends MBase implements IExpression {
    private final List<IExpression> expressions = new ArrayList<>();

    public RowBox() {
    }

    public RowBox(IExpression... expressions) {
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
        b.append("RowBox[{");
        Iterator<IExpression> it = expressions.iterator();
        while (it.hasNext()) {
            IExpression box = it.next();
            b.append(box.getMathematicaExpression());
            if (it.hasNext()) {
                b.append(", ");
            }
        }
        b.append("}]");
        if (isDoLinebreaks()) {
            b.append(linebreak);
        }
        return b.toString();
    }
}