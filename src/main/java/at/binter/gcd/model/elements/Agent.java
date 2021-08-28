package at.binter.gcd.model.elements;

import at.binter.gcd.model.HasPlotStyle;
import at.binter.gcd.model.PlotStyle;
import at.binter.gcd.model.Updatable;
import at.binter.gcd.util.Tools;

public class Agent extends Function implements HasPlotStyle, Updatable<Agent>, Comparable<Agent> {
    public static String assignmentSymbol = ":=";
    private String description;

    private final PlotStyle plotStyle = new PlotStyle();

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
    public void update(Agent modified) {
        setName(modified.getName());
        setFunction(modified.getFunction());
        setDescription(modified.getDescription());
        plotStyle.update(modified);
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
    public int compareTo(Agent o) {
        return name.compareTo(o.getName());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Agent) {
            return compareTo((Agent) obj) == 0;
        }
        return false;
    }
}