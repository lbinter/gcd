package at.binter.gcd.model.xml;

import at.binter.gcd.model.HasPlotStyle;
import at.binter.gcd.model.PlotStyle;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * maps  and its subclasses
 */
@XmlRootElement
public class XmlVariable extends XmlBasicVariable implements HasPlotStyle {
    @XmlElement
    public String initialConditions;

    private PlotStyle plotStyle = new PlotStyle();

    public XmlVariable() {
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