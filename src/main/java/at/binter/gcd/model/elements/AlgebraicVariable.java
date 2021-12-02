package at.binter.gcd.model.elements;

import at.binter.gcd.GCDApplication;
import at.binter.gcd.model.HasPlotStyle;
import at.binter.gcd.model.PlotStyle;
import at.binter.gcd.model.Status;
import at.binter.gcd.model.Updatable;
import at.binter.gcd.model.plotstyle.PlotStyleEntry;
import at.binter.gcd.util.Tools;
import org.apache.commons.lang3.StringUtils;

import static at.binter.gcd.model.Status.INVALID;
import static at.binter.gcd.model.Status.VALID;
import static at.binter.gcd.util.GuiUtils.sanitizeString;

public class AlgebraicVariable extends Function implements HasPlotStyle, Updatable<AlgebraicVariable>, Comparable<AlgebraicVariable> {
    public static final String assignmentSymbol = "->";
    private final PlotStyle plotStyle = new PlotStyle();
    private String description;

    @Override
    public void update(AlgebraicVariable modified) {
        setName(modified.getName());
        setParameter(modified.getParameter());
        setFunction(modified.getFunction());
        setDescription(modified.getDescription());
        plotStyle.update(modified);
    }

    @Override
    public String getAssignmentSymbol() {
        return assignmentSymbol;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = sanitizeString(description);
    }

    public PlotStyle getPlotStyle() {
        return plotStyle;
    }

    @Override
    public String getPlotColor() {
        return plotStyle.getPlotColor();
    }

    @Override
    public void setPlotColor(String plotColor) {
        plotStyle.setPlotColor(plotColor);
    }

    @Override
    public Double getPlotThickness() {
        return plotStyle.getPlotThickness();
    }

    @Override
    public void setPlotThickness(Double plotThickness) {
        plotStyle.setPlotThickness(plotThickness);
    }

    @Override
    public String getPlotLineStyle() {
        return plotStyle.getPlotLineStyle();
    }

    @Override
    public void setPlotLineStyle(String plotLineStyle) {
        plotStyle.setPlotLineStyle(plotLineStyle);
    }

    public String getDefaultPlotColor() {
        if (GCDApplication.app != null) {
            PlotStyleEntry entry = GCDApplication.app.plotStyles.getPlotStyle(getName());
            if (entry != null) {
                return entry.getPlotColor();
            }
        }
        return null;
    }

    public Double getDefaultPlotThickness() {
        if (GCDApplication.app != null) {
            PlotStyleEntry entry = GCDApplication.app.plotStyles.getPlotStyle(getName());
            if (entry != null) {
                return entry.getPlotThickness();
            }
        }
        return null;
    }

    public String getDefaultPlotLineStyle() {
        if (GCDApplication.app != null) {
            PlotStyleEntry entry = GCDApplication.app.plotStyles.getPlotStyle(getName());
            if (entry != null) {
                return entry.getPlotLineStyle();
            }
        }
        return null;
    }

    @Override
    public boolean hasValidPlotStyle() {
        if (!plotStyle.hasValidPlotStyle()) {
            return (StringUtils.isNotBlank(getPlotColor()) || StringUtils.isNotBlank(getDefaultPlotColor()))
                    && (getPlotThickness() != null || getDefaultPlotThickness() != null);
        }
        return true;
    }

    @Override
    public String toString() {
        return Tools.transformMathematicaGreekToUnicodeLetters(toStringRaw());
    }

    public String toStringRaw() {
        return getCompleteName() + assignmentSymbol + getFunction();
    }

    @Override
    public int compareTo(AlgebraicVariable o) {
        return getName().compareTo(o.getName());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof AlgebraicVariable) {
            return compareTo((AlgebraicVariable) obj) == 0;
        }
        return false;
    }

    public Status getStatus() {
        return hasValidPlotStyle() ? VALID : INVALID;
    }
}