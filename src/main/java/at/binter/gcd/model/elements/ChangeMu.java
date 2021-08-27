package at.binter.gcd.model.elements;

public class ChangeMu {
    private Agent agent;
    private Variable variable;
    private int index;
    private Double startValue;
    private Double minValue;
    private Double maxValue;

    @Override
    public String toString() {
        return "\\[Mu][" + agent.getName() + "," + index + "]";
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj instanceof ChangeMu) {
            return agent.getName().equalsIgnoreCase(((ChangeMu) obj).getAgent().getName());
        }
        if (obj instanceof String) {
            return toString().equalsIgnoreCase((String) obj);
        }
        return false;
    }

    public Agent getAgent() {
        return agent;
    }

    public void setAgent(Agent agent) {
        this.agent = agent;
    }

    public Variable getVariable() {
        return variable;
    }

    public void setVariable(Variable variable) {
        this.variable = variable;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
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
}