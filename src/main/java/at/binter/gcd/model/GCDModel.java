package at.binter.gcd.model;

import at.binter.gcd.model.elements.*;
import at.binter.gcd.model.xml.XmlBasicVariable;
import at.binter.gcd.model.xml.XmlFunction;
import at.binter.gcd.model.xml.XmlModel;
import at.binter.gcd.model.xml.XmlVariable;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.ListChangeListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;

public class GCDModel extends GCDBaseModel {
    private static final Logger log = LoggerFactory.getLogger(GCDModel.class);

    private File file;
    private final BooleanProperty savedToFile = new SimpleBooleanProperty(false);

    private boolean clearGlobal = true;

    private final ListChangeListener<AlgebraicVariable> algebraicVariableListChangeListener = c -> {
        while (c.next()) {
            if (c.wasAdded()) {
                c.getAddedSubList().forEach(algVar -> {
                    algebraicVariableNameMap.put(algVar.getName(), algVar);
                    if (hasVariable(algVar.getName())) {
                        removeVariable(getVariable(algVar.getName()));
                    }
                    addVariables(algVar);
                    addParameters(algVar);
                    if (log.isDebugEnabled()) {
                        log.debug("Added algebraic variable \"{}\"", algVar.getName());
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
                            log.trace("Added algebraicVariableNameMap entry \"{}\"", newValue);
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
    };

    private final ListChangeListener<Agent> agentListChangeListener = c -> {
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
                        generateChangeMu();
                    });
                });
                generateChangeMu();
            }
            if (c.wasRemoved()) {
                c.getRemoved().forEach(agent -> {
                    removeVariables(agent);
                    removeParameters(agent);
                    agentNameMap.remove(agent.getName());
                    log.trace("Removed agent \"{}\"", agent);
                });
                generateChangeMu();
            }
        }
    };

    private final ListChangeListener<Constraint> constraintListChangeListener = c -> {
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
    };

    private final ListChangeListener<Variable> variableListChangeListener = c -> {
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
    };

    private final ListChangeListener<Parameter> parameterListChangeListener = c -> {
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
    };

    private final ListChangeListener<ChangeMu> changeMuListChangeListener = c -> {
        while (c.next()) {
            if (c.wasAdded()) {
                c.getAddedSubList().forEach(changeMu -> {
                    changeMuNameMap.put(changeMu.getIdentifier(), changeMu);
                    if (log.isTraceEnabled()) {
                        log.trace("Added changeMu \"{}\"", changeMu.getIdentifier());
                    }
                });
            }
            if (c.wasRemoved()) {
                c.getRemoved().forEach(changeMu -> {
                    changeMuNameMap.remove(changeMu.getIdentifier());
                    if (log.isTraceEnabled()) {
                        log.trace("Added removed \"{}\"", changeMu.getIdentifier());
                    }
                });
            }
        }
    };

    public GCDModel() {
        registerChangeListeners();
    }

    @Override
    protected ListChangeListener<AlgebraicVariable> getAlgebraicVariableListChangeListener() {
        return algebraicVariableListChangeListener;
    }

    @Override
    protected ListChangeListener<Agent> getAgentListChangeListener() {
        return agentListChangeListener;
    }

    @Override
    protected ListChangeListener<Constraint> getConstraintListChangeListener() {
        return constraintListChangeListener;
    }

    @Override
    protected ListChangeListener<Variable> getVariableListChangeListener() {
        return variableListChangeListener;
    }

    @Override
    protected ListChangeListener<Parameter> getParameterListChangeListener() {
        return parameterListChangeListener;
    }

    @Override
    protected ListChangeListener<ChangeMu> getChangeMuListChangeListener() {
        return changeMuListChangeListener;
    }

    public void clearModel() {
        if (isEmpty()) return;
        unregisterChangeListeners();
        setFile(null);
        algebraicVariables.clear();
        algebraicVariableNameMap.clear();
        agents.clear();
        agentNameMap.clear();
        constraints.clear();
        variables.clear();
        variableNameMap.clear();
        parameters.clear();
        parameterNameMap.clear();
        allChangeMu.clear();
        changeMus.clear();
        registerChangeListeners();
    }

    public void loadXmlModel(XmlModel model) {
        if (model == null) return;
        setRunGenerateChangeMu(false);
        clearModel();
        setFile(model.file);
        ArrayList<AlgebraicVariable> newAlgebraicVariables = new ArrayList<>();
        for (XmlFunction algVar : model.algebraicVariables) {
            newAlgebraicVariables.add(algVar.createAlgebraicVariable());
        }
        getAlgebraicVariables().addAll(newAlgebraicVariables);

        ArrayList<Agent> newAgents = new ArrayList<>();
        for (XmlFunction agent : model.agents) {
            newAgents.add(agent.createAgent());
        }
        getAgents().addAll(newAgents);

        for (XmlFunction constraint : model.constraints) {
            getConstraints().add(constraint.createConstraint());
        }

        for (XmlVariable variable : model.variables) {
            Variable v = getVariable(variable.name);
            v.update(variable.createVariable());
        }

        for (XmlBasicVariable parameter : model.parameters) {
            Parameter p = getParameter(parameter.name);
            p.update(parameter.createParameter());
        }

        setRunGenerateChangeMu(true);
        generateChangeMu();
        for (XmlBasicVariable changeMu : model.changeMu) {
            ChangeMu mu = getChangeMu(changeMu.name);
            mu.update(changeMu.createChangeMu(mu.getAgent(), mu.getVariable(), mu.getIndex()));
        }
    }

    public boolean isClearGlobal() {
        return clearGlobal;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public File getFile() {
        return file;
    }

    public boolean isSavedToFile() {
        return savedToFile.get();
    }

    public BooleanProperty savedToFileProperty() {
        return savedToFile;
    }

    public void setSavedToFile(boolean savedToFile) {
        this.savedToFile.set(savedToFile);
    }
}