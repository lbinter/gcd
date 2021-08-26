package at.binter.gcd.gui;


import javafx.scene.paint.Color;

public enum StatusColor {
    VALID(true, Color.WHITE, Color.BLACK, Color.rgb(184, 207, 229), Color.BLACK),
    INVALID(false, Color.PINK, Color.BLACK, Color.RED, Color.WHITE);

    public final boolean isValid;
    public final Color background;
    public final Color foreground;
    public final Color backgroundSelected;
    public final Color foregroundSelected;

    StatusColor(boolean isValid, Color background, Color foreground, Color backgroundSelected, Color foregroundSelected) {
        this.isValid = isValid;
        this.background = background;
        this.foreground = foreground;
        this.backgroundSelected = backgroundSelected;
        this.foregroundSelected = foregroundSelected;
    }

    public boolean isValid() {
        return isValid;
    }

    public boolean useCurrentLookAndFeelColorsForValid() {
        return true;
    }

    public Color getBackground(boolean isSelected) {
        if (isSelected) return backgroundSelected;
        return background;
    }

    public Color getForeground(boolean isSelected) {
        if (isSelected) return foregroundSelected;
        return foreground;
    }
}