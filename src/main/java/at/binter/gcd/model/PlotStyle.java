package at.binter.gcd.model;

import org.apache.commons.lang3.StringUtils;

public class PlotStyle implements HasPlotStyle, Updatable<PlotStyle> {
    private String plotColor;
    private Double plotThickness;
    private String plotLineStyle;

    @Override
    public void update(PlotStyle modified) {
        setPlotColor(modified.getPlotColor());
        setPlotThickness(modified.getPlotThickness());
        setPlotLineStyle(modified.getPlotLineStyle());
    }

    @Override
    public String getPlotColor() {
        return plotColor;
    }

    @Override
    public void setPlotColor(String plotColor) {
        this.plotColor = plotColor;
    }

    @Override
    public Double getPlotThickness() {
        return plotThickness;
    }

    @Override
    public void setPlotThickness(Double plotThickness) {
        this.plotThickness = plotThickness;
    }

    @Override
    public String getPlotLineStyle() {
        return plotLineStyle;
    }

    @Override
    public void setPlotLineStyle(String plotLineStyle) {
        this.plotLineStyle = plotLineStyle;
    }

    @Override
    public boolean hasValidPlotStyle() {
        return StringUtils.isNotBlank(plotColor) && plotThickness != null;
    }

}