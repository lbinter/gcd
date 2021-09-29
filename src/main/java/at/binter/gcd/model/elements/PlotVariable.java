package at.binter.gcd.model.elements;

public class PlotVariable implements Comparable<PlotVariable> {
    public boolean independent;
    public int dependentOn = 0;
    public final Variable variable;

    public PlotVariable(boolean independent, Variable variable) {
        this.independent = independent;
        if (!independent) {
            dependentOn++;
        }
        this.variable = variable;
    }

    @Override
    public String toString() {
        return variable.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof PlotVariable) {
            return variable.equals(((PlotVariable) obj).variable);
        }
        return false;
    }

    @Override
    public int compareTo(PlotVariable o) {
        return variable.compareTo(o.variable);
    }
}
