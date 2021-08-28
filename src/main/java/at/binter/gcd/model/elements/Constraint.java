package at.binter.gcd.model.elements;

import at.binter.gcd.model.HasPlotStyle;
import at.binter.gcd.model.PlotStyle;
import at.binter.gcd.model.Updatable;

public class Constraint implements HasPlotStyle, Updatable<Constraint>, Comparable<Constraint> {
    public String id;
    public String condition;
    private String description;
    private final PlotStyle plotStyle = new PlotStyle();

    @Override
    public void update(Constraint modified) {
        plotStyle.update(modified);
        setId(modified.getId());
        setCondition(modified.getCondition());
        setDescription(modified.getDescription());
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
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
    public int compareTo(Constraint o) {
        return condition.compareTo(o.getCondition());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Constraint) {
            return compareTo((Constraint) obj) == 0;
        }
        return false;
    }
}