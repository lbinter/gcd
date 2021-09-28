package at.binter.gcd.mathematica.syntax;

import at.binter.gcd.mathematica.HTMLBuilder;
import at.binter.gcd.mathematica.MBase;
import at.binter.gcd.mathematica.syntax.function.MFunction;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static at.binter.gcd.util.MathematicaUtils.linebreakString;

public class RowBox extends MBase implements IExpression {
    private final List<IExpression> expressions = new ArrayList<>();

    public RowBox() {
    }

    public RowBox(IExpression... expressions) {
        add(expressions);
    }

    public RowBox(boolean doLinebreakAfter, IExpression... expressions) {
        this(expressions);
        setDoLinebreakAfter(doLinebreakAfter);
    }

    public RowBox(boolean doLinebreakAfter, boolean doLinebreaks, IExpression... expressions) {
        this(doLinebreakAfter, expressions);
        setDoLinebreaks(doLinebreaks);
    }

    public void add(IExpression... expressions) {
        for (IExpression expr : expressions) {
            if (expr != null) {
                this.expressions.add(expr);
            }
        }
    }

    public void add(List<RowBox> list) {
        for (RowBox box : list) {
            add(box);
        }
    }

    public List<IExpression> getExpressions() {
        return expressions;
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
        boolean appendDelimiter = false;
        while (it.hasNext()) {
            IExpression box = it.next();
            if (box == MFunction.linebreak) {
                if (appendDelimiter) {
                    b.append(",");
                }
                b.append(linebreakString);
                appendDelimiter = true;
                continue;
            }
            if (appendDelimiter) {
                b.append(",");
            }
            b.append(box.getMathematicaExpression());
            appendDelimiter = true;
        }
        b.append("}]");
        if (isDoLinebreakAfter()) {
            b.append(",");
            b.append(linebreakString);
        }
        return b.toString();
    }
}