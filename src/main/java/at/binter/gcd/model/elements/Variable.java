package at.binter.gcd.model.elements;

import at.binter.gcd.model.*;
import at.binter.gcd.util.Tools;
import org.apache.commons.lang3.StringUtils;

import java.util.Set;

import static at.binter.gcd.model.GCDWarning.*;
import static at.binter.gcd.model.Status.*;
import static at.binter.gcd.util.GuiUtils.sanitizeString;

public class Variable implements Comparable<Variable>, Updatable<Variable>, HasPlotStyle, HasMinMaxValues {
    private String name;
    private String description;
    private String initialCondition;
    private final MinMaxValues minMaxValues = new MinMaxValues();
    private final PlotStyle plotStyle = new PlotStyle();
    private final FunctionReference functionReference = new FunctionReference();

    public Variable(String name) {
        setName(name);
        setInitialCondition(getDefaultInitialCondition());
    }

    @Override
    public void update(Variable modified) {
        setName(modified.getName());
        setDescription(modified.getDescription());
        setInitialCondition(modified.getInitialCondition());
        plotStyle.update(modified);
        minMaxValues.update(modified);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = sanitizeString(name);
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = sanitizeString(description);
    }

    public String getInitialCondition() {
        return initialCondition;
    }

    public String getDefaultInitialCondition() {
        return getName() + "0";
    }

    public void setInitialCondition(String initialCondition) {
        this.initialCondition = sanitizeString(initialCondition);
    }

    @Override
    public Double getStartValue() {
        return minMaxValues.getStartValue();
    }

    @Override
    public void setStartValue(Double startValue) {
        minMaxValues.setStartValue(startValue);
    }

    @Override
    public Double getMinValue() {
        return minMaxValues.getMinValue();
    }

    @Override
    public void setMinValue(Double minValue) {
        minMaxValues.setMinValue(minValue);
    }

    @Override
    public Double getMaxValue() {
        return minMaxValues.getMaxValue();
    }

    @Override
    public void setMaxValue(Double maxValue) {
        minMaxValues.setMaxValue(maxValue);
    }

    public boolean hasValidInitValues() {
        return minMaxValues.hasAllValues() && getDefaultInitialCondition().equals(getInitialCondition());
    }

    @Override
    public boolean hasAllValues() {
        return minMaxValues.hasAllValues();
    }

    @Override
    public boolean hasNoValues() {
        return minMaxValues.hasNoValues();
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

    @Override
    public boolean hasValidPlotStyle() {
        return plotStyle.hasValidPlotStyle();
    }

    public Set<AlgebraicVariable> getAlgebraicVariables() {
        return functionReference.getAlgebraicVariables();
    }

    public Set<Agent> getAgents() {
        return functionReference.getAgents();
    }

    public Set<Constraint> getConstraints() {
        return functionReference.getConstraints();
    }

    public String getAlgebraicVariablesAsString() {
        return Tools.transformMathematicaGreekToUnicodeLetters(functionReference.getAlgebraicVariablesAsString());
    }

    public String getAgentsAsString() {
        return Tools.transformMathematicaGreekToUnicodeLetters(functionReference.getAgentsAsString());
    }

    public String getConstraintsAsString() {
        return Tools.transformMathematicaGreekToUnicodeLetters(functionReference.getConstraintsAsString());
    }

    public boolean hasReferences() {
        return functionReference.hasReferences();
    }

    @Override
    public int compareTo(Variable o) {
        return getName().compareTo(o.getName());
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Variable)) {
            return false;
        }
        return compareTo((Variable) obj) == 0;
    }

    @Override
    public String toString() {
        return Tools.transformMathematicaGreekToUnicodeLetters(name);
    }

    public Status getStatus() {
        // TODO plot style extra status ?
        if (StringUtils.isBlank(initialCondition)) {
            // ==> initialConditions should be automatically calculated by mathematica => no double value allowed
            return hasNoValues() ? VALID_AUTOMATIC : INVALID;
        } else if (getDefaultInitialCondition().equals(initialCondition)) {
            // initialConditions == <name>0 => requires all double values
            if (getWarning() != null) return INVALID;
            return hasAllValues() ? VALID_HAS_VALUES : INVALID;
            // TODO check false values
        } else {
            // initialConditions == function => no double value allowed
            return hasNoValues() ? VALID_HAS_FUNCTION : INVALID;
        }
    }

    public GCDWarning getWarning() {
        if (getMaxValue() != null && getMinValue() != null) {
            if (getMaxValue() < getMinValue()) {
                return MAX_VALUE_LESSER_MIN_VALUE;
            }
        }
        if (getStartValue() != null) {
            if (getMinValue() != null && getStartValue() < getMinValue()) {
                return START_VALUE_LESSER_MIN_VALUE;
            }
            if (getMaxValue() != null && getStartValue() > getMaxValue()) {
                return START_VALUE_GREATER_MAX_VALUE;
            }
        }

        if (getStartValue() == null) {
            return MISSING_START_VALUE;
        }
        if (getMinValue() == null) {
            return MISSING_MIN_VALUE;
        }
        if (getMaxValue() == null) {
            return MISSING_MAX_VALUE;
        }
        return null;
    }
}