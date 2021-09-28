package at.binter.gcd.mathematica.syntax.group;

import at.binter.gcd.mathematica.HTMLBuilder;
import at.binter.gcd.mathematica.syntax.IExpression;
import at.binter.gcd.mathematica.syntax.MExpression;
import at.binter.gcd.mathematica.syntax.RowBox;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MList extends MGroup implements IExpression {
    public static final String symbolSpecial = "List";
    public static final String groupStartSymbol = "{";
    public static final String groupCloseSymbol = "}";
    public String delimiter = ",";
    private int elementsLinebreak = -1;
    private int elementsBlock = -1;
    protected final List<IExpression> elements = new ArrayList<>();

    public MList() {
    }

    public MList(IExpression... elements) {
        add(elements);
    }

    public MList(String... elements) {
        for (String s : elements) {
            add(new MExpression(s));
        }
    }


    public List<IExpression> getElements() {
        return elements;
    }

    public void add(IExpression expr) {
        if (expr == null) return;
        elements.add(expr);
    }

    public void add(IExpression... expr) {
        for (IExpression e : expr) {
            add(e);
        }
    }

    public String getDelimiter() {
        return delimiter;
    }

    public void setDelimiter(String delimiter) {
        this.delimiter = delimiter;
    }

    @Override
    public String getGroupStartSymbol() {
        return groupStartSymbol;
    }

    @Override
    public String getGroupCloseSymbol() {
        return groupCloseSymbol;
    }

    @Override
    public String getCssClass() {
        return null;
    }

    public int getElementsLinebreak() {
        return elementsLinebreak;
    }

    public void setElementsLinebreak(int elementsLinebreak) {
        this.elementsLinebreak = elementsLinebreak;
    }

    public int getElementsBlock() {
        return elementsBlock;
    }

    public void setElementsBlock(int elementsBlock) {
        this.elementsBlock = elementsBlock;
    }

    @Override
    public void toHTML(HTMLBuilder builder) {
        int count = 0;
        builder.write(getGroupStartSymbol());
        Iterator<IExpression> it = elements.iterator();
        boolean addLineBreaks = elementsLinebreak != -1;
        if (addLineBreaks) {
            builder.increaseIndent(4);
        }
        while (it.hasNext()) {
            if (addLineBreaks) {
                if (count % elementsLinebreak == 0) {
                    builder.linebreak();
                }
                if (count == elementsBlock) {
                    builder.linebreak();
                    builder.linebreak();
                    count = 0;
                }
            }
            it.next().toHTML(builder);
            if (it.hasNext()) {
                builder.write(delimiter);
            } else if (addLineBreaks) {
                builder.linebreak();
            }
            count++;
        }
        if (addLineBreaks) {
            builder.decreaseIndent(4);
        }
        builder.write(getGroupCloseSymbol());
    }

    @Override
    public String getExpression() {
        HTMLBuilder builder = new HTMLBuilder();
        toHTML(builder);
        return builder.toString();
    }

    @Override
    public String getMathematicaExpression() {
        RowBox inner = new RowBox();

        Iterator<IExpression> it = elements.iterator();
        while (it.hasNext()) {
            IExpression element = it.next();
            inner.addExpressions(new MExpression("\"" + element.getMathematicaExpression() + "\""));
            if (it.hasNext()) {
                inner.addExpressions(new MExpression("\",\""));
            }
        }

        RowBox outer = new RowBox();
        outer.addExpressions(new MExpression("\"" + getGroupStartSymbol() + "\""));
        outer.addExpressions(inner);
        outer.addExpressions(new MExpression("\"" + getGroupCloseSymbol() + "\""));

        return outer.getMathematicaExpression();
    }
}