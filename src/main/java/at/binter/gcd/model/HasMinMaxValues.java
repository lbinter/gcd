package at.binter.gcd.model;

import java.util.List;

public interface HasMinMaxValues {

    Double getStartValue();

    void setStartValue(Double startValue);

    Double getMinValue();

    void setMinValue(Double minValue);

    Double getMaxValue();

    void setMaxValue(Double maxValue);

    boolean hasValidValues();

    boolean hasAllValues();

    boolean hasNoValues();

    List<GCDWarning> getWarnings();
}
