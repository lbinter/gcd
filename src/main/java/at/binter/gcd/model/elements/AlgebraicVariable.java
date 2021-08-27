package at.binter.gcd.model.elements;

import at.binter.gcd.model.Editable;
import at.binter.gcd.model.HasPlotStyle;
import at.binter.gcd.model.PlotStyle;
import at.binter.gcd.util.Tools;

public class AlgebraicVariable extends Function implements HasPlotStyle, Editable<AlgebraicVariable>, Comparable<AlgebraicVariable> {
    public static final String assignmentSymbol = "->";
    private final PlotStyle plotStyle = new PlotStyle();
    private String description;

    @Override
    public void update(AlgebraicVariable modified) {
        setName(modified.getName());
        setFunction(modified.getFunction());
        setDescription(modified.getDescription());
        plotStyle.update(modified);
    }

    @Override
    public String getAssignmentSymbol() {
        return assignmentSymbol;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String getPlotColor() {
        return plotStyle.getPlotColor();
    }

    @Override
    public void setPlotColor(String plotColor) {
        plotStyle.setPlotColor(plotColor);
    }

    @Override
    public Double getPlotThickness() {
        return plotStyle.getPlotThickness();
    }

    @Override
    public void setPlotThickness(Double plotThickness) {
        plotStyle.setPlotThickness(plotThickness);
    }

    @Override
    public String getPlotLineStyle() {
        return plotStyle.getPlotLineStyle();
    }

    @Override
    public void setPlotLineStyle(String plotLineStyle) {
        plotStyle.setPlotLineStyle(plotLineStyle);
    }

    @Override
    public boolean hasValidPlotStyle() {
        return plotStyle.hasValidPlotStyle();
    }

    @Override
    public String toString() {
        return Tools.transformMathematicaGreekToUnicodeLetters(name + assignmentSymbol + function);
    }

    @Override
    public int compareTo(AlgebraicVariable o) {
        return name.compareTo(o.getName());
    }
}