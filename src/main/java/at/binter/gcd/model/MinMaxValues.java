package at.binter.gcd.model;

import java.util.ArrayList;
import java.util.List;

import static at.binter.gcd.model.GCDWarning.*;

public class MinMaxValues implements HasMinMaxValues, Updatable<HasMinMaxValues> {
    private Double startValue;
    private Double minValue;
    private Double maxValue;

    @Override
    public void update(HasMinMaxValues modified) {
        setStartValue(modified.getStartValue());
        setMinValue(modified.getMinValue());
        setMaxValue(modified.getMaxValue());
    }

    public Double getStartValue() {
        return startValue;
    }

    public void setStartValue(Double startValue) {
        this.startValue = startValue;
    }

    public Double getMinValue() {
        return minValue;
    }

    public void setMinValue(Double minValue) {
        this.minValue = minValue;
    }

    public Double getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(Double maxValue) {
        this.maxValue = maxValue;
    }

    @Override
    public boolean hasValidValues() {
        return getWarnings().isEmpty();
    }

    @Override
    public boolean hasAllValues() {
        return startValue != null && minValue != null && maxValue != null;
    }

    @Override
    public boolean hasNoValues() {
        return startValue == null && minValue == null && maxValue == null;
    }

    @Override
    public List<GCDWarning> getWarnings() {
        List<GCDWarning> list = new ArrayList<>();
        if (getMinValue() == null) {
            list.add(MISSING_MIN_VALUE);
        }
        if (getMaxValue() == null) {
            list.add(MISSING_MAX_VALUE);
        }
        if (getMaxValue() != null && getMinValue() != null) {
            if (getMaxValue() < getMinValue()) {
                list.add(MAX_VALUE_LESSER_MIN_VALUE);
            }
        }
        if (getStartValue() == null) {
            list.add(MISSING_START_VALUE);
        } else {
            if (getMinValue() != null && getStartValue() < getMinValue()) {
                list.add(START_VALUE_LESSER_MIN_VALUE);
            }
            if (getMaxValue() != null && getStartValue() > getMaxValue()) {
                list.add(START_VALUE_GREATER_MAX_VALUE);
            }
        }
        return list;
    }
}