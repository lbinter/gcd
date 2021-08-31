package at.binter.gcd.model.elements;

import at.binter.gcd.model.FunctionReference;
import at.binter.gcd.model.HasMinMaxValues;
import at.binter.gcd.model.MinMaxValues;
import at.binter.gcd.model.Updatable;
import at.binter.gcd.util.Tools;

import java.util.Set;

import static at.binter.gcd.util.GuiUtils.sanitizeString;

public class Parameter implements Updatable<Parameter>, HasMinMaxValues {
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
    public boolean hasAllValues() {
        return minMaxValues.hasAllValues();
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

    public String getAlgebraicVariablesAsString() {
        return functionReference.getAlgebraicVariablesAsString();
    }

    public String getAgentsAsString() {
        return functionReference.getAgentsAsString();
    }

    public String getConstraintsAsString() {
        return functionReference.getConstraintsAsString();
    }

    public boolean hasReferences() {
        return functionReference.hasReferences();
    }

    @Override
    public String toString() {
        return Tools.transformMathematicaGreekToUnicodeLetters(name);
    }
}