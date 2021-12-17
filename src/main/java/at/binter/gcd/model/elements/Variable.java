package at.binter.gcd.model.elements;

import at.binter.gcd.GCDApplication;
import at.binter.gcd.model.*;
import at.binter.gcd.model.plotstyle.PlotStyleEntry;
import at.binter.gcd.util.ParsedFunction;
import at.binter.gcd.util.Tools;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Set;

import static at.binter.gcd.model.Status.*;
import static at.binter.gcd.util.GuiUtils.sanitizeString;

public class Variable implements Comparable<Variable>, Updatable<Variable>, HasPlotStyle, HasMinMaxValues, HasParameterStringList {
    private String name;
    private String description;
    private final StringProperty initialCondition = new SimpleStringProperty();
    private final MinMaxValues minMaxValues = new MinMaxValues();
    private final VariableParameterList variableParameterList = new VariableParameterList();
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
        return initialCondition.get();
    }

    public String getDefaultInitialCondition() {
        return getName() + "0";
    }

    public StringProperty initialConditionProperty() {
        return initialCondition;
    }

    public void setInitialCondition(String initialCondition) {
        if (StringUtils.isBlank(initialCondition)) {
            initialConditionProperty().set(null);
            return;
        }
        ParsedFunction parsedFunction = new ParsedFunction(initialCondition);
        fillParameters(parsedFunction.parameters);
        initialConditionProperty().set(parsedFunction.function);
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
        return minMaxValues.hasValidValues() && getDefaultInitialCondition().equals(getInitialCondition());
    }

    @Override
    public boolean hasValidValues() {
        return minMaxValues.hasValidValues();
    }

    @Override
    public boolean hasAllValues() {
        return minMaxValues.hasAllValues();
    }

    @Override
    public boolean hasNoValues() {
        return minMaxValues.hasNoValues();
    }

    @Override
    public List<GCDWarning> getWarnings() {
        return minMaxValues.getWarnings();
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
        if (StringUtils.isBlank(getInitialCondition())) {
            // ==> initialConditions should be automatically calculated by mathematica => no double value allowed
            return hasNoValues() ? VALID_AUTOMATIC : INVALID;
        } else if (getDefaultInitialCondition().equals(getInitialCondition())) {
            // initialConditions == <name>0 => requires all double values
            if (!getWarnings().isEmpty()) return INVALID;
            return hasValidValues() ? VALID_HAS_VALUES : INVALID;
        } else {
            // initialConditions == function => no double value allowed
            return hasNoValues() ? VALID_HAS_FUNCTION : INVALID;
        }
    }

    public void fillParameters(Set<String> newParameters) {
        variableParameterList.fillParameters(newParameters);
    }

    public void fillVariables(Set<String> newVariables) {
        variableParameterList.fillVariables(newVariables);
    }

    @Override
    public List<String> getParameters() {
        return variableParameterList.getParameters();
    }

    @Override
    public Set<String> getParametersRemoved() {
        return variableParameterList.getParametersRemoved();
    }

    @Override
    public Set<String> getParametersAdded() {
        return variableParameterList.getParametersAdded();
    }
}