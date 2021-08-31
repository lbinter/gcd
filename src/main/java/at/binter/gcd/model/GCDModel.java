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
                if (c.wasAdded()) {
                    c.getAddedSubList().forEach(algVar -> {
                        algebraicVariableNameMap.put(algVar.getName(), algVar);
                        addVariables(algVar);
                        addParameters(algVar);
                        if (log.isDebugEnabled()) {
                            log.debug("Added to algebraic variable \"{}\"", algVar.getName());
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
                            if (log.isTraceEnabled()) {
                                log.trace("Added to algebraicVariableNameMap entry \"{}\"", newValue);
                            }
                        });
                        algVar.functionProperty().addListener((observable, oldValue, newValue) -> {
                            if (log.isDebugEnabled()) {
                                log.debug("Algebraic Variable \"{}\" function was changed from \"{}\" to \"{}\"", algVar.getName(), oldValue, newValue);
                                log.debug("Function change: Variables added: {} Variables removed: {} Parameters added: {} Parameters removed: {}",
                                        algVar.getVariablesAdded(),
                                        algVar.getVariablesRemoved(),
                                        algVar.getParametersAdded(),
                                        algVar.getParametersRemoved());
                            }
                            updateVariables(algVar);
                            updateParameters(algVar);
                        });
                    });
                }
                if (c.wasRemoved()) {
                    c.getRemoved().forEach(algVar -> {
                        removeVariables(algVar);
                        removeParameters(algVar);
                        algebraicVariableNameMap.remove(algVar.getName());
                        log.trace("Removed algebraic variable \"{}\"", algVar);
                    });
                }
            }
        });

        agents.addListener((ListChangeListener<Agent>) c -> {
            while (c.next()) {
                if (c.wasAdded()) {
                    c.getAddedSubList().forEach(agent -> {
                        agentNameMap.put(agent.getName(), agent);
                        addVariables(agent);
                        addParameters(agent);
                        if (log.isTraceEnabled()) {
                            log.trace("Added agent \"{}\"", agent.getName());
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
                            if (log.isTraceEnabled()) {
                                log.trace("Added to agentNameMap entry \"{}\"", newValue);
                            }
                        });
                        agent.functionProperty().addListener((observable, oldValue, newValue) -> {
                            if (log.isDebugEnabled()) {
                                log.debug("Agent \"{}\" function was changed from \"{}\" to \"{}\"", agent.getName(), oldValue, newValue);
                                log.debug("Function change: Variables added: {} Variables removed: {} Parameters added: {} Parameters removed: {}",
                                        agent.getVariablesAdded(),
                                        agent.getVariablesRemoved(),
                                        agent.getParametersAdded(),
                                        agent.getParametersRemoved());
                            }
                            updateVariables(agent);
                            updateParameters(agent);
                        });
                    });
                }
                if (c.wasRemoved()) {
                    c.getRemoved().forEach(agent -> {
                        removeVariables(agent);
                        removeParameters(agent);
                        agentNameMap.remove(agent.getName());
                        log.trace("Removed agent \"{}\"", agent);
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
                        constraint.conditionProperty().addListener((observable, oldValue, newValue) -> {
                            if (log.isDebugEnabled()) {
                                log.debug("Constraint \"{}\" condition was changed from \"{}\" to \"{}\"", constraint.getId(), oldValue, newValue);
                                log.debug("Condition change: Variables added: {} Variables removed: {} Parameters added: {} Parameters removed: {}",
                                        constraint.getVariablesAdded(),
                                        constraint.getVariablesRemoved(),
                                        constraint.getParametersAdded(),
                                        constraint.getParametersRemoved());
                            }
                            updateVariables(constraint);
                            updateParameters(constraint);
                        });
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
                            log.trace("Added variable \"{}\"", variable.getName());
                        }
                    });
                }
                if (c.wasRemoved()) {
                    c.getRemoved().forEach(variable -> {
                        variableNameMap.remove(variable.getName());
                        if (log.isTraceEnabled()) {
                            log.trace("Removed variable \"{}\"", variable.getName());
                        }
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
                            log.trace("Added parameter \"{}\"", parameter.getName());
                        }
                    });
                }
                if (c.wasRemoved()) {
                    c.getRemoved().forEach(parameter -> {
                        parameterNameMap.remove(parameter.getName());
                        if (log.isTraceEnabled()) {
                            log.trace("Removed parameter \"{}\"", parameter.getName());
                        }
                    });
                }
            }
        });
    }

    public boolean hasAlgebraicVariable(String name) {
        return algebraicVariableNameMap.containsKey(name);
    }

    public boolean hasAgent(String name) {
        return agentNameMap.containsKey(name);
    }

    public boolean hasVariable(String name) {
        return variableNameMap.containsKey(name);
    }

    public boolean hasParameter(String name) {
        return parameterNameMap.containsKey(name);
    }

    private void addVariables(HasVariableStringList source) {
        source.getVariables().forEach(name -> addVariable(source, name));
    }

    private void addVariable(HasVariableStringList source, String name) {
        if (hasAlgebraicVariable(name)) return;
        Variable v;
        if (hasVariable(name)) {
            v = getVariable(name);
        } else {
            v = new Variable(name);
            variables.add(v);
        }
        if (source instanceof AlgebraicVariable) {
            v.getAlgebraicVariables().add((AlgebraicVariable) source);
        } else if (source instanceof Agent) {
            v.getAgents().add((Agent) source);
        } else if (source instanceof Constraint) {
            v.getConstraints().add((Constraint) source);
        }
    }

    private void removeVariables(HasVariableStringList source) {
        source.getVariables().forEach(name -> removeVariable(source, name));
    }

    private void removeVariable(HasVariableStringList source, String name) {
        if (hasAlgebraicVariable(name)) return;
        Variable v;
        if (hasVariable(name)) {
            v = getVariable(name);
        } else {
            return;
        }
        if (source instanceof AlgebraicVariable) {
            v.getAlgebraicVariables().remove((AlgebraicVariable) source);
        } else if (source instanceof Agent) {
            v.getAgents().remove((Agent) source);
        } else if (source instanceof Constraint) {
            v.getConstraints().remove((Constraint) source);
        }
        if (!v.hasReferences()) {
            removeVariable(v);
        }
    }

    private void removeVariable(Variable variable) {
        if (variable == null) return;
        if (!variables.remove(variable)) {
            log.debug("Could not remove variable \"{}\"", variable.getName());
        }
    }

    private void updateVariables(HasVariableStringList source) {
        source.getVariablesAdded().forEach(name -> addVariable(source, name));
        source.getVariablesRemoved().forEach(name -> removeVariable(source, name));
    }

    private void addParameters(HasParameterStringList source) {
        source.getParameters().forEach(name -> addParameter(source, name));
    }

    private void addParameter(HasParameterStringList source, String name) {
        if (hasAlgebraicVariable(name)) return;
        Parameter p;
        if (hasParameter(name)) {
            p = getParameter(name);
        } else {
            p = new Parameter(name);
            parameters.add(p);
        }
        if (source instanceof AlgebraicVariable) {
            p.getAlgebraicVariables().add((AlgebraicVariable) source);
        } else if (source instanceof Agent) {
            p.getAgents().add((Agent) source);
        } else if (source instanceof Constraint) {
            p.getConstraints().add((Constraint) source);
        }
    }

    private void removeParameters(HasParameterStringList source) {
        source.getParameters().forEach(name -> removeParameter(source, name));
    }

    private void removeParameter(HasParameterStringList source, String name) {
        if (hasAlgebraicVariable(name)) return;
        Parameter p;
        if (hasParameter(name)) {
            p = getParameter(name);
        } else {
            return;
        }
        if (source instanceof AlgebraicVariable) {
            p.getAlgebraicVariables().remove((AlgebraicVariable) source);
        } else if (source instanceof Agent) {
            p.getAgents().remove((Agent) source);
        } else if (source instanceof Constraint) {
            p.getConstraints().remove((Constraint) source);
        }
        if (!p.hasReferences()) {
            removeParameter(p);
        }
    }

    private void removeParameter(Parameter parameter) {
        if (parameter == null) return;
        if (!parameters.remove(parameter)) {
            log.debug("Could not remove parameter \"{}\"", parameter.getName());
        }
    }

    private void updateParameters(HasParameterStringList source) {
        source.getParametersAdded().forEach(name -> addParameter(source, name));
        source.getParametersRemoved().forEach(name -> removeParameter(source, name));
    }

    public boolean canAddAlgebraicVariable(String name) {
        return !StringUtils.isBlank(name) &&
                !hasAlgebraicVariable(name) &&
                !hasAgent(name) &&
                !hasVariable(name) &&
                !hasParameter(name);
    }

    public boolean canAddAgent(String name) {
        return !StringUtils.isBlank(name) &&
                !hasAlgebraicVariable(name) &&
                !hasAgent(name) &&
                !hasVariable(name) &&
                !hasParameter(name);
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

    public Variable getVariable(String name) {
        if (StringUtils.isBlank(name)) return null;
        return variableNameMap.get(name);
    }

    public ObservableList<Parameter> getParameters() {
        return parameters;
    }

    public Parameter getParameter(String name) {
        if (StringUtils.isBlank(name)) return null;
        return parameterNameMap.get(name);
    }


    public ObservableList<ChangeMu> getChangeMus() {
        return changeMus;
    }
}