package at.binter.gcd.model;

import at.binter.gcd.model.elements.Agent;
import at.binter.gcd.model.elements.AlgebraicVariable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class GCDModel {
    private final ObservableList<AlgebraicVariable> algebraicVariables = FXCollections.observableArrayList();
    private final ObservableList<Agent> agents = FXCollections.observableArrayList();
    private final ObservableList<String> constraints = FXCollections.observableArrayList();
    private final ObservableList<String> variables = FXCollections.observableArrayList();
    private final ObservableList<String> parameters = FXCollections.observableArrayList();
    private final ObservableList<String> changeMus = FXCollections.observableArrayList();

    public boolean canAddAlgebraicVariable(String name) {
        return true;
    }

    public boolean canAddAgent(String name) {
        return true;
    }

    public boolean canAddConstraint(String name) {
        return true;
    }

    public int getNextConstraintId() {
        return 1; // todo count constraints
    }

    public ObservableList<AlgebraicVariable> getAlgebraicVariables() {
        return algebraicVariables;
    }

    public ObservableList<Agent> getAgents() {
        return agents;
    }

    public ObservableList<String> getConstraints() {
        return constraints;
    }

    public ObservableList<String> getVariables() {
        return variables;
    }

    public ObservableList<String> getParameters() {
        return parameters;
    }

    public ObservableList<String> getChangeMus() {
        return changeMus;
    }
}