package at.binter.gcd.model.elements;

import at.binter.gcd.model.HasMinMaxValues;
import at.binter.gcd.model.MinMaxValues;
import at.binter.gcd.model.Updatable;
import at.binter.gcd.util.Tools;

public class Parameter implements Updatable<Parameter>, HasMinMaxValues {
    private String name;
    private String description;
    private String initialCondition;
    private final MinMaxValues minMaxValues = new MinMaxValues();

    public Parameter(String name) {
        this.name = name;
    }

    @Override
    public void update(Parameter modified) {
        setName(modified.getName());
        setDescription(modified.getDescription());
        setInitialCondition(modified.getInitialCondition());
        minMaxValues.update(modified);
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getInitialCondition() {
        return initialCondition;
    }

    public void setInitialCondition(String initialCondition) {
        this.initialCondition = initialCondition;
    }

    @Override
    public String toString() {
        return Tools.transformMathematicaGreekToUnicodeLetters(name);
    }
}