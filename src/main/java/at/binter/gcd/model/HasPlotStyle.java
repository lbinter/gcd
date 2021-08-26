package at.binter.gcd.model;

public interface HasPlotStyle {
    String getPlotColor();

    void setPlotColor(String plotColor);

    Double getPlotThickness();

    void setPlotThickness(Double plotThickness);

    String getPlotLineStyle();

    void setPlotLineStyle(String plotLineStyle);

    boolean hasValidPlotStyle();
}
