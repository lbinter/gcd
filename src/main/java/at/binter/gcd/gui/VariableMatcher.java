package at.binter.gcd.gui;

import at.binter.gcd.model.elements.Variable;

import java.util.function.Predicate;

public class VariableMatcher implements Predicate<Variable> {

    private final String filter;

    public VariableMatcher(String filter) {
        this.filter = filter;
    }

    @Override
    public boolean test(Variable variable) {
        if (variable == null) return false;
        if (filter.equals("*")) {
            return true;
        }
        return variable.getName().toLowerCase().contains(filter.toLowerCase());
    }
}