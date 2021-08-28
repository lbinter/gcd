package at.binter.gcd.model;

public class MinMaxValues implements HasMinMaxValues, Updatable<MinMaxValues> {
    private Double startValue;
    private Double minValue;
    private Double maxValue;

    @Override
    public void update(MinMaxValues modified) {
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
    public boolean hasAllValues() {
        return startValue != null && minValue != null && maxValue != null;
    }

    @Override
    public boolean hasNoValues() {
        return startValue == null && minValue == null && maxValue == null;
    }
}