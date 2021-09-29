package at.binter.gcd.gui;

import at.binter.gcd.model.elements.PlotVariable;
import at.binter.gcd.model.elements.Variable;
import javafx.collections.ObservableList;

import java.util.function.Predicate;

public class VariableMatcher implements Predicate<Variable> {
    private final ObservableList<PlotVariable> selectedVariables;
    private final String filter;

    public VariableMatcher(ObservableList<PlotVariable> selectedVariables, String filter) {
        this.selectedVariables = selectedVariables;
        this.filter = filter;
    }

    @Override
    public boolean test(Variable variable) {
        if (variable == null) return false;
        for (PlotVariable v : selectedVariables) {
            if (v.variable.equals(variable)) {
                return false;
            }
        }
        if (filter.equals("*")) {
            return true;
        }
        return variable.getName().toLowerCase().contains(filter.toLowerCase());
    }
}