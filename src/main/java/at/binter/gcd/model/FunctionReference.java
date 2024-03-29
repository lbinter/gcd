package at.binter.gcd.model;

import at.binter.gcd.model.elements.*;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class FunctionReference {
    Set<AlgebraicVariable> algebraicVariables = new HashSet<>();
    Set<Agent> agents = new HashSet<>();
    Set<Constraint> constraints = new HashSet<>();
    Set<Variable> variables = new HashSet<>();

    public Set<AlgebraicVariable> getAlgebraicVariables() {
        return algebraicVariables;
    }

    public Set<Agent> getAgents() {
        return agents;
    }

    public Set<Constraint> getConstraints() {
        return constraints;
    }

    public Set<Variable> getVariables() {
        return variables;
    }

    public String getAlgebraicVariablesAsString() {
        return algebraicVariables.stream().sorted().map(Function::getName).collect(Collectors.joining(", "));
    }

    public String getAgentsAsString() {
        return agents.stream().sorted().map(Function::getName).collect(Collectors.joining(", "));
    }

    public String getConstraintsAsString() {
        return constraints.stream().map(Constraint::getNameOrId).collect(Collectors.joining(", "));
    }

    public String getVariablesAsString() {
        return variables.stream().map(Variable::getName).collect(Collectors.joining(", "));
    }

    public boolean hasReferences() {
        return algebraicVariables.size() + agents.size() + constraints.size() + variables.size() > 0;
    }
}