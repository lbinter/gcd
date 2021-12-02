package at.binter.gcd.model.elements;

import at.binter.gcd.model.*;
import at.binter.gcd.util.Tools;
import javafx.collections.ObservableList;

import java.util.List;

import static at.binter.gcd.model.Status.INVALID;
import static at.binter.gcd.model.Status.VALID;

public class ChangeMu implements Updatable<ChangeMu>, HasMinMaxValues {
    private final Agent agent;
    private final Variable variable;
    private final int index;
    private final String identifier;
    private final MinMaxValues minMaxValues = new MinMaxValues();

    public ChangeMu(Agent agent, Variable variable, int index) {
        this.agent = agent;
        this.variable = variable;
        this.index = index;
        identifier = "\\[Mu]" + agent.getName() + variable.getName();

    }

    @Override
    public void update(ChangeMu modified) {
        minMaxValues.update(modified);
    }

    @Override
    public String toString() {
        return Tools.transformMathematicaGreekToUnicodeLetters(getIndexName() + " -> " + getIdentifier());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj instanceof ChangeMu) {
            return agent.getName().equalsIgnoreCase(((ChangeMu) obj).getAgent().getName())
                    && variable.getName().equalsIgnoreCase(((ChangeMu) obj).getVariable().getName());
        }
        if (obj instanceof String) {
            return toString().equalsIgnoreCase((String) obj);
        }
        return false;
    }

    public Agent getAgent() {
        return agent;
    }

    public Variable getVariable() {
        return variable;
    }

    public int getIndex() {
        return index;
    }

    public String getIndexName() {
        return "\\[Mu][" + agent.getName() + "," + getIndex() + "]";
    }

    public String getIdentifier() {
        return identifier;
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
    public boolean hasNoValues() {
        return minMaxValues.hasNoValues();
    }

    @Override
    public List<GCDWarning> getWarnings() {
        return minMaxValues.getWarnings();
    }

    public boolean requireDoubleValues(ObservableList<AlgebraicVariable> algebraicVariables) {
        if (agent.getVariables().contains(variable.getName())) {
            return true;
        }
        for (AlgebraicVariable algVar : algebraicVariables) {
            if (agent.getVariables().contains(algVar.getName()) && algVar.getVariables().contains(variable.getName())) {
                return true;
            }
        }
        return false;
    }

    public Status getStatus() {
        return hasValidValues() ? VALID : INVALID;
    }
}