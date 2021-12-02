package at.binter.gcd.util;

import at.binter.gcd.model.plotstyle.GCDPlotStyles;
import at.binter.gcd.model.plotstyle.PlotStyleEntry;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

import static at.binter.gcd.util.GuiUtils.*;

public class PlotStyleIndicator {
    private static final Logger log = LoggerFactory.getLogger(PlotStyleIndicator.class);

    private final GCDPlotStyles styles;
    private final TextField editorName;
    private final Label editorLabelName;
    private final TextField editorPlotColor;
    private final TextField editorPlotThickness;
    private final TextField editorPlotLineArt;
    private final boolean nameChangeable;

    public PlotStyleIndicator(GCDPlotStyles styles, TextField editorName, Label editorLabelName, TextField editorPlotColor, TextField editorPlotThickness, TextField editorPlotLineArt) {
        this.styles = styles;
        this.editorName = editorName;
        this.editorLabelName = editorLabelName;
        this.editorPlotColor = editorPlotColor;
        this.editorPlotThickness = editorPlotThickness;
        this.editorPlotLineArt = editorPlotLineArt;
        nameChangeable = editorName != null;
        if (!nameChangeable && editorLabelName == null) {
            throw new RuntimeException("Either editorName or editorLabelName must be given");
        }
        addListener();
    }

    private void addListener() {
        if (nameChangeable) {
            editorName.textProperty().addListener(this::nameChanged);
        }
        editorPlotColor.textProperty().addListener(this::plotColorChanged);
        editorPlotThickness.textProperty().addListener(this::plotThicknessChanged);
        editorPlotLineArt.textProperty().addListener(this::plotLineArtChanged);
    }

    public void nameChanged(ObservableValue<? extends String> observable, String oldValue, String newValue) {
        plotColorChanged(null, null, editorPlotColor.getText());
        plotThicknessChanged(null, null, editorPlotThickness.getText());
        plotLineArtChanged(null, null, editorPlotLineArt.getText());
    }

    public void plotColorChanged(ObservableValue<? extends String> observable, String oldValue, String newValue) {
        PlotStyleEntry entry = styles.getPlotStyle(getName());
        newValue = sanitizeString(newValue);
        boolean hasDefaultValue = false;
        boolean hasValue = StringUtils.isNotBlank(newValue);
        boolean differentFromDefault = false;
        if (entry != null) {
            String defaultValue = entry.getPlotColor();
            editorPlotColor.setPromptText(defaultValue);
            hasDefaultValue = StringUtils.isNotBlank(defaultValue);
            if (hasDefaultValue) {
                differentFromDefault = !defaultValue.equals(newValue);
            }
        } else {
            editorPlotColor.setPromptText("");
        }
        editorPlotColor.pseudoClassStateChanged(errorClass, !hasDefaultValue && !hasValue);
        editorPlotColor.pseudoClassStateChanged(plotDefaultClass, hasDefaultValue && !hasValue);
        editorPlotColor.pseudoClassStateChanged(plotDiffClass, hasValue && differentFromDefault);
    }

    public void plotThicknessChanged(ObservableValue<? extends String> observable, String oldValue, String newValue) {
        PlotStyleEntry entry = styles.getPlotStyle(getName());
        boolean hasDefaultValue = false;
        boolean hasValue = StringUtils.isNotBlank(newValue);
        boolean differentFromDefault = false;
        if (entry != null) {
            Double defaultValue = entry.getPlotThickness();
            editorPlotThickness.setPromptText(String.valueOf(defaultValue));
            hasDefaultValue = defaultValue != null;
            if (hasDefaultValue) {
                differentFromDefault = !Objects.equals(readDoubleValueFrom(newValue), defaultValue);
            }
        } else {
            editorPlotThickness.setPromptText("");
        }
        editorPlotThickness.pseudoClassStateChanged(errorClass, !hasDefaultValue && !hasValue);
        editorPlotThickness.pseudoClassStateChanged(plotDefaultClass, hasDefaultValue && !hasValue);
        editorPlotThickness.pseudoClassStateChanged(plotDiffClass, hasValue && differentFromDefault);
    }

    public void plotLineArtChanged(ObservableValue<? extends String> observable, String oldValue, String newValue) {
        PlotStyleEntry entry = styles.getPlotStyle(getName());
        newValue = sanitizeString(newValue);
        boolean hasDefaultValue = false;
        boolean hasValue = StringUtils.isNotBlank(newValue);
        boolean differentFromDefault = false;
        if (entry != null) {
            String defaultValue = entry.getPlotLineStyle();
            editorPlotLineArt.setPromptText(defaultValue);
            hasDefaultValue = StringUtils.isNotBlank(defaultValue);
            if (hasDefaultValue) {
                differentFromDefault = !defaultValue.equals(newValue);
            }
        } else {
            editorPlotLineArt.setPromptText("");
        }
        editorPlotLineArt.pseudoClassStateChanged(plotDefaultClass, hasDefaultValue && !hasValue);
        editorPlotLineArt.pseudoClassStateChanged(plotDiffClass, hasValue && differentFromDefault);
    }

    public String getName() {
        if (nameChangeable) {
            return editorName.getText();
        } else {
            return editorLabelName.getText();
        }
    }
}