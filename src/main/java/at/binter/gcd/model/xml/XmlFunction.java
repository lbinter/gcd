package at.binter.gcd.model.xml;

import at.binter.gcd.model.HasPlotStyle;
import at.binter.gcd.model.PlotStyle;
import jakarta.xml.bind.annotation.XmlElement;

/**
 * maps and its subclasses
 */
public class XmlFunction implements HasPlotStyle {
    @XmlElement
    String function;

    @XmlElement
    String description;

    private PlotStyle plotStyle = new PlotStyle();

    public XmlFunction() {
    }

    @Override
    @XmlElement
    public String getPlotColor() {
        return plotStyle.getPlotColor();
    }

    @Override
    public void setPlotColor(String plotColor) {
        plotStyle.setPlotColor(plotColor);
    }

    @Override
    @XmlElement
    public Double getPlotThickness() {
        return plotStyle.getPlotThickness();
    }

    @Override
    public void setPlotThickness(Double plotThickness) {
        plotStyle.setPlotThickness(plotThickness);
    }

    @Override
    @XmlElement
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
}