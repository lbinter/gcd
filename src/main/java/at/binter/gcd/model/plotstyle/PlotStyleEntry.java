package at.binter.gcd.model.plotstyle;

import at.binter.gcd.model.HasPlotStyle;
import at.binter.gcd.model.Updatable;
import jakarta.xml.bind.annotation.XmlElement;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class PlotStyleEntry implements HasPlotStyle, Updatable<PlotStyleEntry>, Cloneable {
    private static final Logger log = LoggerFactory.getLogger(PlotStyleEntry.class);
    private final SimpleStringProperty name = new SimpleStringProperty();
    private final SimpleStringProperty description = new SimpleStringProperty();
    private final SimpleStringProperty plotColor = new SimpleStringProperty();
    private final SimpleDoubleProperty plotThickness = new SimpleDoubleProperty();
    private final SimpleStringProperty plotLineStyle = new SimpleStringProperty();

    @Override
    public void update(PlotStyleEntry modified) {
        setName(modified.getName());
        setDescription(modified.getDescription());
        setPlotColor(modified.getPlotColor());
        setPlotThickness(modified.getPlotThickness());
        setPlotLineStyle(modified.getPlotLineStyle());
    }

    @XmlElement(name = "name")
    public String getName() {
        return name.get();
    }

    public void setName(String name) {
        this.name.setValue(name);
    }

    public SimpleStringProperty nameProperty() {
        return name;
    }

    @XmlElement(name = "description")
    public String getDescription() {
        return description.get();
    }

    public SimpleStringProperty descriptionProperty() {
        return description;
    }

    public void setDescription(String description) {
        this.description.set(description);
    }

    @Override
    @XmlElement(name = "color")
    public String getPlotColor() {
        return plotColor.get();
    }

    @Override
    public void setPlotColor(String plotColor) {
        this.plotColor.set(plotColor);
    }

    @Override
    @XmlElement(name = "thickness")
    public Double getPlotThickness() {
        return plotThickness.getValue();
    }

    @Override
    public void setPlotThickness(Double plotThickness) {
        this.plotThickness.set(plotThickness);
    }

    @Override
    @XmlElement(name = "line-style")
    public String getPlotLineStyle() {
        return plotLineStyle.get();
    }

    @Override
    public void setPlotLineStyle(String plotLineStyle) {
        this.plotLineStyle.set(plotLineStyle);
    }

    @Override
    public boolean hasValidPlotStyle() {
        return StringUtils.isNotBlank(plotColor.get()) && plotThickness.getValue() != null;
    }

    public SimpleStringProperty plotColorProperty() {
        return plotColor;
    }

    public SimpleDoubleProperty plotThicknessProperty() {
        return plotThickness;
    }

    public SimpleStringProperty plotLineStyleProperty() {
        return plotLineStyle;
    }

    @Override
    public PlotStyleEntry clone() {
        PlotStyleEntry entry = new PlotStyleEntry();
        entry.setName(getName());
        entry.setPlotColor(getPlotColor());
        entry.setPlotThickness(getPlotThickness());
        entry.setPlotLineStyle(getPlotLineStyle());
        return entry;
    }
}
