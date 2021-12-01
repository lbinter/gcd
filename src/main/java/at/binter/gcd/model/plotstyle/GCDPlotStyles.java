package at.binter.gcd.model.plotstyle;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElementWrapper;
import jakarta.xml.bind.annotation.XmlRootElement;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

@XmlRootElement(name = "gcd-plot-styles")
public class GCDPlotStyles {
    @XmlElementWrapper(name = "styles")
    @XmlElement(name = "entry")
    private final ObservableList<PlotStyleEntry> plotStyles = FXCollections.observableArrayList();

    public ObservableList<PlotStyleEntry> getPlotStyles() {
        return plotStyles;
    }

    public void addPlotStyle(PlotStyleEntry ps) {
        plotStyles.add(ps);
    }

    public void removePlotStyle(PlotStyleEntry ps) {
        plotStyles.remove(ps);
    }
}