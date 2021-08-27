package at.binter.gcd.model;

public interface HasMinMaxValues {

    Double getStartValue();

    void setStartValue(Double startValue);

    Double getMinValue();

    void setMinValue(Double minValue);

    Double getMaxValue();

    void setMaxValue(Double maxValue);

    boolean hasAllValues();

    boolean hasNoValues();
}
