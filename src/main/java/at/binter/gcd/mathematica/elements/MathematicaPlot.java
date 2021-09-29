package at.binter.gcd.mathematica.elements;

import at.binter.gcd.mathematica.HTMLBuilder;
import at.binter.gcd.mathematica.syntax.IExpression;
import at.binter.gcd.mathematica.syntax.RowBox;

import static at.binter.gcd.mathematica.syntax.function.MFunction.linebreak;

public class MathematicaPlot implements IExpression {
    private IExpression plotFunction;
    private IExpression plotParameter;
    private IExpression plotRange;
    private IExpression plotStyle;
    private IExpression plotLegends;

    public MathematicaPlot() {
    }

    public MathematicaPlot(IExpression plotFunction, IExpression plotParameter, IExpression plotRange, IExpression plotStyle, IExpression plotLegends) {
        this.plotFunction = plotFunction;
        this.plotParameter = plotParameter;
        this.plotRange = plotRange;
        this.plotStyle = plotStyle;
        this.plotLegends = plotLegends;
    }

    public IExpression getPlotFunction() {
        return plotFunction;
    }

    public void setPlotFunction(IExpression plotFunction) {
        this.plotFunction = plotFunction;
    }

    public IExpression getPlotParameter() {
        return plotParameter;
    }

    public void setPlotParameter(IExpression plotParameter) {
        this.plotParameter = plotParameter;
    }

    public IExpression getPlotRange() {
        return plotRange;
    }

    public void setPlotRange(IExpression plotRange) {
        this.plotRange = plotRange;
    }

    public IExpression getPlotStyle() {
        return plotStyle;
    }

    public void setPlotStyle(IExpression plotStyle) {
        this.plotStyle = plotStyle;
    }

    public IExpression getPlotLegends() {
        return plotLegends;
    }

    public void setPlotLegends(IExpression plotLegends) {
        this.plotLegends = plotLegends;
    }

    @Override
    public String toHTML() {
        return null;
    }

    @Override
    public void toHTML(HTMLBuilder builder) {

    }

    @Override
    public String getCssClass() {
        return null;
    }

    @Override
    public String getExpression() {
        return null;
    }

    @Override
    public String getMathematicaExpression() {
        RowBox box = new RowBox();
        box.add(new MVariable("Plot"));
        box.add(new MVariable("["));
        box.add(plotFunction);
        box.add(new MVariable(","));
        box.add(linebreak);
        box.add(plotParameter);
        box.add(new MVariable(","));
        box.add(linebreak);
        box.add(plotRange);
        box.add(new MVariable(","));
        box.add(linebreak);
        box.add(plotStyle);
        box.add(new MVariable(","));
        box.add(linebreak);
        box.add(plotLegends);
        box.add(new MVariable("]"));
        return box.getMathematicaExpression();
    }
}