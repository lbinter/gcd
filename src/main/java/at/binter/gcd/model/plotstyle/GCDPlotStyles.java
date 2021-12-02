package at.binter.gcd.model.plotstyle;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElementWrapper;
import jakarta.xml.bind.annotation.XmlRootElement;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

import static at.binter.gcd.util.GuiUtils.sanitizeString;

@XmlRootElement(name = "gcd-plot-styles")
public class GCDPlotStyles {
    private static final Logger log = LoggerFactory.getLogger(GCDPlotStyles.class);
    @XmlElementWrapper(name = "styles")
    @XmlElement(name = "entry")
    private final ObservableList<PlotStyleEntry> plotStyles = FXCollections.observableArrayList();
    private final Map<String, PlotStyleEntry> plotStyleNameMap = new HashMap<>();

    public GCDPlotStyles() {
        plotStyles.addListener((ListChangeListener<? super PlotStyleEntry>) c -> {
            while (c.next()) {
                if (c.wasAdded()) {
                    c.getAddedSubList().forEach(entry -> {
                        plotStyleNameMap.put(entry.getName(), entry);
                        log.trace("Added plotStyleNameMap entry \"{}\"", entry.getName());
                    });
                }
                if (c.wasRemoved()) {
                    c.getRemoved().forEach(entry -> {
                        plotStyleNameMap.remove(entry.getName());
                        log.trace("Removed plotStyleNameMap entry \"{}\"", entry.getName());
                    });
                }
            }
        });
    }

    public ObservableList<PlotStyleEntry> getPlotStyles() {
        return plotStyles;
    }

    public void addPlotStyle(PlotStyleEntry ps) {
        plotStyles.add(ps);
    }

    public void removePlotStyle(PlotStyleEntry ps) {
        plotStyles.remove(ps);
    }

    public PlotStyleEntry getPlotStyle(String name) {
        name = sanitizeString(name);
        if (StringUtils.isBlank(name)) return null;
        return plotStyleNameMap.get(name);
    }
}