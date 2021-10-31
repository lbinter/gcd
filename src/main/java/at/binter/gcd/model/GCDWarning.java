package at.binter.gcd.model;

import java.awt.*;

import static at.binter.gcd.util.GuiUtils.defaultBackground;
import static at.binter.gcd.util.GuiUtils.defaultForeground;


public enum GCDWarning {
    DUPLICATE_VARIABLE_PARAMETER("warning.model.duplicate.variable.parameter", defaultBackground, defaultForeground),
    MISSING_START_VALUE("warning.model.missing.start.value", defaultBackground, defaultForeground),
    MISSING_MIN_VALUE("warning.model.missing.min.value", defaultBackground, defaultForeground),
    MISSING_MAX_VALUE("warning.model.missing.max.value", defaultBackground, defaultForeground),
    MAX_VALUE_LESSER_MIN_VALUE("warning.model.max.value.lesser.min.value", defaultBackground, defaultForeground),
    START_VALUE_LESSER_MIN_VALUE("warning.model.start.value.lesser.min.value", defaultBackground, defaultForeground),
    START_VALUE_GREATER_MAX_VALUE("warning.model.start.value.greater.max.value", defaultBackground, defaultForeground);

    private final String i18n;
    private final Color background;
    private final Color foreground;

    GCDWarning(String i18n, Color backgroundColor, Color foreground) {
        this.i18n = i18n;
        this.background = backgroundColor;
        this.foreground = foreground;
    }

    public String getI18n() {
        return i18n;
    }

    public Color getBackground() {
        return background;
    }

    public Color getForeground() {
        return foreground;
    }
}