package at.binter.gcd.mathematica.syntax;

import at.binter.gcd.mathematica.HTMLBuilder;

public class BoxData implements IExpression {
    private RowBox box;

    public BoxData() {
    }

    public BoxData(RowBox box) {
        setBox(box);
    }

    public RowBox getBox() {
        return box;
    }

    public void setBox(RowBox box) {
        this.box = box;
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
        b.append("BoxData[\r\n");
        b.append(box.getMathematicaExpression());
        b.append("]");
        return b.toString();
    }
}