package at.binter.gcd.model.elements;

import at.binter.gcd.model.*;
import at.binter.gcd.util.Tools;

import java.util.List;
import java.util.Set;

import static at.binter.gcd.model.Status.INVALID;
import static at.binter.gcd.model.Status.VALID;
import static at.binter.gcd.util.GuiUtils.sanitizeString;

public class Parameter implements Comparable<Parameter>, Updatable<Parameter>, HasMinMaxValues {
    private String name;
    private String description;
    private final MinMaxValues minMaxValues = new MinMaxValues();
    private final FunctionReference functionReference = new FunctionReference();

    public Parameter(String name) {
        setName(name);
    }

    @Override
    public void update(Parameter modified) {
        setName(modified.getName());
        setDescription(modified.getDescription());
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

    @Override
    public boolean hasValidValues() {
        return minMaxValues.hasValidValues();
    }

    @Override
    public boolean hasAllValues() {
        return minMaxValues.hasAllValues();
    }

    @Override
    public List<GCDWarning> getWarnings() {
        return minMaxValues.getWarnings();
    }

    @Override
    public boolean hasNoValues() {
        return minMaxValues.hasNoValues();
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

    public Set<Variable> getVariables() {
        return functionReference.getVariables();
    }

    public String getAlgebraicVariablesAsString() {
        return functionReference.getAlgebraicVariablesAsString();
    }

    public String getAgentsAsString() {
        return functionReference.getAgentsAsString();
    }

    public String getConstraintsAsString() {
        return functionReference.getConstraintsAsString();
    }

    public String getVariablesAsString() {
        return functionReference.getVariablesAsString();
    }

    public boolean hasReferences() {
        return functionReference.hasReferences();
    }

    @Override
    public String toString() {
        return Tools.transformMathematicaGreekToUnicodeLetters(name);
    }

    @Override
    public int compareTo(Parameter o) {
        return getName().compareTo(o.getName());
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Parameter)) {
            return false;
        }
        return compareTo((Parameter) obj) == 0;
    }

    public Status getStatus() {
        return hasValidValues() ? VALID : INVALID;
    }
}