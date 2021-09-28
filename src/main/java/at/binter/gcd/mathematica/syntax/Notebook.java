package at.binter.gcd.mathematica.syntax;


import at.binter.gcd.mathematica.HTMLBuilder;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Notebook implements IExpression {
    private final List<Cell> cells = new ArrayList<>();

    public Notebook() {
    }

    public Notebook(Cell... cells) {
        add(cells);
    }

    public void add(Cell... cells) {
        for (Cell c : cells) {
            if (c != null) {
                this.cells.add(c);
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
        b.append("Notebook[{\r\n");
        Iterator<Cell> it = cells.iterator();
        while (it.hasNext()) {
            Cell c = it.next();
            b.append(c.getMathematicaExpression());
            if (it.hasNext()) {
                b.append(",\r\n");
            }
        }
        b.append("\r\n}]");
        return b.toString();
    }
}