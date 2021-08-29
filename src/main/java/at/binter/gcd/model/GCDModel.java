package at.binter.gcd.model;

import at.binter.gcd.model.elements.*;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class GCDModel {
    private static final Logger log = LoggerFactory.getLogger(GCDModel.class);

    private final ObservableList<AlgebraicVariable> algebraicVariables = FXCollections.observableArrayList(algebraicVariable -> new Observable[]{algebraicVariable.nameProperty()});
    private final Map<String, AlgebraicVariable> algebraicVariableNameMap = new HashMap<>();
    private final ObservableList<Agent> agents = FXCollections.observableArrayList();
    private final Map<String, Agent> agentNameMap = new HashMap<>();
    private final ObservableList<String> constraints = FXCollections.observableArrayList();
    private final Map<String, Constraint> constraintNameMap = new HashMap<>();
    private final ObservableList<String> variables = FXCollections.observableArrayList();
    private final Map<String, Variable> variableNameMap = new HashMap<>();
    private final ObservableList<String> parameters = FXCollections.observableArrayList();
    private final Map<String, Parameter> parameterNameMap = new HashMap<>();
    private final ObservableList<String> changeMus = FXCollections.observableArrayList();
    private final Map<String, ChangeMu> changeMuNameMap = new HashMap<>();

    public GCDModel() {
        algebraicVariables.addListener((ListChangeListener<AlgebraicVariable>) c -> {
            while (c.next()) {
                /* if (c.wasUpdated()) {
                    // see nameProperty listener
                }*/
                if (c.wasAdded()) {
                    c.getAddedSubList().forEach(algVar -> {
                        algebraicVariableNameMap.put(algVar.getName(), algVar);
                        if (log.isTraceEnabled()) {
                            log.trace("Added to algebraicVariableNameMap entry \"{}\"", algVar.getName());
                        }
                        algVar.nameProperty().addListener((observable, oldValue, newValue) -> {
                            if (log.isDebugEnabled()) {
                                log.debug("Algebraic Variable was renamed from \"{}\" to \"{}\"", oldValue, newValue);
                            }
                            AlgebraicVariable removed = algebraicVariableNameMap.remove(oldValue);
                            if (removed != null) {
                                if (log.isTraceEnabled()) {
                                    log.trace("Removed from algebraicVariableNameMap entry \"{}\"", oldValue);
                                }
                            } else {
                                log.error("Could not remove \"{}\" from algebraicVariableNameMap", oldValue);
                            }
                            algebraicVariableNameMap.put(newValue, algVar);
                            log.trace("Added to algebraicVariableNameMap entry \"{}\"", newValue);
                        });
                    });
                }
                if (c.wasRemoved()) {
                    c.getAddedSubList().forEach(algVar -> {
                        algebraicVariableNameMap.remove(algVar.getName());
                        log.trace("Removed from algebraicVariableNameMap entry \"{}\"", algVar.getName());
                    });
                }
            }
        });
    }

    public boolean canAddAlgebraicVariable(String name) {
        // boolean nameExists = algebraicVariables.stream().anyMatch(algVar -> name.equals(algVar.getName()));
        return !StringUtils.isBlank(name) &&
                !algebraicVariableNameMap.containsKey(name) &&
                !agentNameMap.containsKey(name) &&
                !constraintNameMap.containsKey(name) &&
                !variableNameMap.containsKey(name) &&
                !parameterNameMap.containsKey(name);
    }

    public boolean canAddAgent(String name) {
        return !StringUtils.isBlank(name) &&
                !algebraicVariableNameMap.containsKey(name) &&
                !agentNameMap.containsKey(name) &&
                !constraintNameMap.containsKey(name) &&
                !variableNameMap.containsKey(name) &&
                !parameterNameMap.containsKey(name);
    }

    public boolean canAddConstraint(String name) {
        return !StringUtils.isBlank(name) &&
                !algebraicVariableNameMap.containsKey(name) &&
                !agentNameMap.containsKey(name) &&
                !constraintNameMap.containsKey(name) &&
                !variableNameMap.containsKey(name) &&
                !parameterNameMap.containsKey(name);
    }

    public boolean addAlgebraicVariable(AlgebraicVariable algebraicVariable) {
        if (algebraicVariable == null) return false;
        if (!canAddAlgebraicVariable(algebraicVariable.getName())) {
            log.info("Can not add Algebraic Variable \"{}\": there already is an (algebraic) variable or parameter with that name", algebraicVariable);
            return false;
        }
        algebraicVariableNameMap.put(algebraicVariable.getName(), algebraicVariable);
        algebraicVariables.add(algebraicVariable);
        return true;
    }

    public boolean addAgent(Agent agent) {
        if (agent == null) return false;
        if (!canAddAgent(agent.getName())) {
            log.info("Can not add Agent \"{}\": there already is an (algebraic) variable or parameter with that name", agent);
            return false;
        }
        agentNameMap.put(agent.getName(), agent);
        agents.add(agent);
        return true;
    }

    public boolean removeAlgebraicVariable(AlgebraicVariable algebraicVariable) {
        if (algebraicVariable == null) return false;
        if (!algebraicVariableNameMap.containsKey(algebraicVariable.getName())) return false;
        algebraicVariableNameMap.remove(algebraicVariable.getName());
        algebraicVariables.remove(algebraicVariable);
        return true;
    }

    public boolean removeAlgebraicVariable(int index) {
        if (index < 0 || index > algebraicVariables.size()) return false;
        AlgebraicVariable algebraicVariable = algebraicVariables.get(index);
        algebraicVariableNameMap.remove(algebraicVariable.getName());
        algebraicVariables.remove(index);
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