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
    private final ObservableList<Constraint> constraints = FXCollections.observableArrayList();
    private final ObservableList<Variable> variables = FXCollections.observableArrayList();
    private final Map<String, Variable> variableNameMap = new HashMap<>();
    private final ObservableList<Parameter> parameters = FXCollections.observableArrayList();
    private final Map<String, Parameter> parameterNameMap = new HashMap<>();
    private final ObservableList<ChangeMu> changeMus = FXCollections.observableArrayList();

    public GCDModel() {
        algebraicVariables.addListener((ListChangeListener<AlgebraicVariable>) c -> {
            while (c.next()) {
                /* if (c.wasUpdated()) {
                    // see nameProperty listener
                }*/
                if (c.wasAdded()) {
                    c.getAddedSubList().forEach(algVar -> {
                        algebraicVariableNameMap.put(algVar.getName(), algVar);
                        addVariables(algVar);
                        addParameters(algVar);
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
                    c.getRemoved().forEach(algVar -> {
                        removeVariables(algVar);
                        removeParameters(algVar);
                        algebraicVariableNameMap.remove(algVar.getName());
                        log.trace("Removed from algebraicVariableNameMap entry \"{}\"", algVar.getName());
                    });
                }
            }
        });

        agents.addListener((ListChangeListener<Agent>) c -> {
            while (c.next()) {
                /* if (c.wasUpdated()) {
                    // see nameProperty listener
                }*/
                if (c.wasAdded()) {
                    c.getAddedSubList().forEach(agent -> {
                        agentNameMap.put(agent.getName(), agent);
                        addVariables(agent);
                        addParameters(agent);
                        if (log.isTraceEnabled()) {
                            log.trace("Added to agentNameMap entry \"{}\"", agent.getName());
                        }
                        agent.nameProperty().addListener((observable, oldValue, newValue) -> {
                            if (log.isDebugEnabled()) {
                                log.debug("Agent was renamed from \"{}\" to \"{}\"", oldValue, newValue);
                            }
                            Agent removed = agentNameMap.remove(oldValue);
                            if (removed != null) {
                                if (log.isTraceEnabled()) {
                                    log.trace("Removed from agentNameMap entry \"{}\"", oldValue);
                                }
                            } else {
                                log.error("Could not remove \"{}\" from agentNameMap", oldValue);
                            }
                            agentNameMap.put(newValue, agent);
                            log.trace("Added to agentNameMap entry \"{}\"", newValue);
                        });
                    });
                }
                if (c.wasRemoved()) {
                    c.getRemoved().forEach(agent -> {
                        removeVariables(agent);
                        removeParameters(agent);
                        agentNameMap.remove(agent.getName());
                        log.trace("Removed from agentNameMap entry \"{}\"", agent.getName());
                    });
                }
            }
        });

        constraints.addListener((ListChangeListener<Constraint>) c -> {
            while (c.next()) {
                if (c.wasAdded()) {
                    c.getAddedSubList().forEach(constraint -> {
                        addVariables(constraint);
                        addParameters(constraint);
                        constraint.setId(c.getFrom() + 1);
                        if (log.isTraceEnabled()) {
                            log.trace("Added constraint \"{}\"", constraint);
                        }
                    });
                }
                if (c.wasRemoved()) {
                    c.getRemoved().forEach(constraint -> {
                        removeVariables(constraint);
                        removeParameters(constraint);
                        log.trace("Removed constraint {}", constraint);
                        for (int i = c.getFrom(); i < constraints.size(); i++) {
                            constraints.get(i).setId(i + 1);
                        }
                    });
                }
            }
        });

        variables.addListener((ListChangeListener<Variable>) c -> {
            while (c.next()) {
                if (c.wasAdded()) {
                    c.getAddedSubList().forEach(variable -> {
                        variableNameMap.put(variable.getName(), variable);
                        if (log.isTraceEnabled()) {
                            log.trace("Added to variableNameMap entry \"{}\"", variable.getName());
                        }
                    });
                }
                if (c.wasRemoved()) {
                    c.getAddedSubList().forEach(variable -> {
                        variableNameMap.remove(variable.getName());
                        log.trace("Removed from variableNameMap entry \"{}\"", variable.getName());
                    });
                }
            }
        });

        parameters.addListener((ListChangeListener<Parameter>) c -> {
            while (c.next()) {
                if (c.wasAdded()) {
                    c.getAddedSubList().forEach(parameter -> {
                        parameterNameMap.put(parameter.getName(), parameter);
                        if (log.isTraceEnabled()) {
                            log.trace("Added to parameterNameMap entry \"{}\"", parameter.getName());
                        }
                    });
                }
                if (c.wasRemoved()) {
                    c.getAddedSubList().forEach(parameter -> {
                        parameterNameMap.remove(parameter.getName());
                        log.trace("Removed from parameterNameMap entry \"{}\"", parameter.getName());
                    });
                }
            }
        });
    }

    public boolean hasAlgebraicVariable(String name) {
        return algebraicVariableNameMap.containsKey(name);
    }

    public boolean hasVariable(String name) {
        return variableNameMap.containsKey(name);
    }

    public boolean hasParameter(String name) {
        return parameterNameMap.containsKey(name);
    }

    private void addVariables(HasVariableStringList source) {
        source.getVariables().forEach(name -> {
            if (hasAlgebraicVariable(name)) return;
            if (hasVariable(name)) {
                // TODO add source to variable parent list
            } else {
                Variable v = new Variable(name);
                // TODO add source to variable parent list
                variables.add(v);
            }
        });
    }

    private void removeVariables(HasVariableStringList source) {
        // TODO
    }

    private void addParameters(HasParameterStringList source) {
        source.getParameters().forEach(name -> {
            if (hasParameter(name)) {
                // TODO add source to variable parent list
            } else {
                Parameter p = new Parameter(name);
                // TODO add source to variable parent list
                parameters.add(p);
            }
        });
    }

    private void removeParameters(HasParameterStringList source) {
        // TODO
    }

    public boolean canAddAlgebraicVariable(String name) {
        // boolean nameExists = algebraicVariables.stream().anyMatch(algVar -> name.equals(algVar.getName()));
        return !StringUtils.isBlank(name) &&
                !algebraicVariableNameMap.containsKey(name) &&
                !agentNameMap.containsKey(name) &&
                !variableNameMap.containsKey(name) &&
                !parameterNameMap.containsKey(name);
    }

    public boolean canAddAgent(String name) {
        return !StringUtils.isBlank(name) &&
                !algebraicVariableNameMap.containsKey(name) &&
                !agentNameMap.containsKey(name) &&
                !variableNameMap.containsKey(name) &&
                !parameterNameMap.containsKey(name);
    }

    public boolean canAddConstraint(String name) {
        return !StringUtils.isBlank(name) &&
                !algebraicVariableNameMap.containsKey(name) &&
                !agentNameMap.containsKey(name) &&
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

    public ObservableList<Constraint> getConstraints() {
        return constraints;
    }

    public ObservableList<Variable> getVariables() {
        return variables;
    }

    public ObservableList<Parameter> getParameters() {
        return parameters;
    }

    public ObservableList<ChangeMu> getChangeMus() {
        return changeMus;
    }
}