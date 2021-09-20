package at.binter.gcd.model;

import at.binter.gcd.model.elements.*;
import javafx.collections.ListChangeListener;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class GCDPlot extends GCDBaseModel {
    private static final Logger log = LoggerFactory.getLogger(GCDPlot.class);

    private final GCDModel model;
    private String name;

    private final ListChangeListener<AlgebraicVariable> algebraicVariableListChangeListener = c -> {
        while (c.next()) {
            if (c.wasAdded()) {
                c.getAddedSubList().forEach(algVar -> {
                    algebraicVariableNameMap.put(algVar.getName(), algVar);
                    if (log.isTraceEnabled()) {
                        log.trace("Plot \"{}\" added algebraic variable \"{}\"", name, algVar.getName());
                    }
                    algVar.nameProperty().addListener((observable, oldValue, newValue) -> {
                        AlgebraicVariable removed = algebraicVariableNameMap.remove(oldValue);
                        if (removed != null) {
                            if (log.isTraceEnabled()) {
                                log.trace("Plot \"{}\" removed from algebraicVariableNameMap entry \"{}\"", name, oldValue);
                            }
                        } else {
                            log.error("Plot \"{}\" :could not remove \"{}\" from algebraicVariableNameMap", name, oldValue);
                        }
                        algebraicVariableNameMap.put(newValue, algVar);
                        if (log.isTraceEnabled()) {
                            log.trace("Plot \"{}\" added algebraicVariableNameMap entry \"{}\"", name, newValue);
                        }
                    });
                });
            }
            if (c.wasRemoved()) {
                c.getRemoved().forEach(algVar -> {
                    algebraicVariableNameMap.remove(algVar.getName());
                    if (log.isTraceEnabled()) {
                        log.trace("Plot \"{}\" removed algebraic variable \"{}\"", name, algVar);
                    }
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
                        log.trace("Plot \"{}\" added agent \"{}\"", name, agent.getName());
                    }
                    agent.nameProperty().addListener((observable, oldValue, newValue) -> {
                        if (log.isDebugEnabled()) {
                            log.debug("Plot \"{}\" agent was renamed from \"{}\" to \"{}\"", name, oldValue, newValue);
                        }
                        Agent removed = agentNameMap.remove(oldValue);
                        if (removed != null) {
                            if (log.isTraceEnabled()) {
                                log.trace("Plot \"{}\" removed from agentNameMap entry \"{}\"", name, oldValue);
                            }
                        } else {
                            log.error("Plot \"{}\" could not remove \"{}\" from agentNameMap", name, oldValue);
                        }
                        agentNameMap.put(newValue, agent);
                        if (log.isTraceEnabled()) {
                            log.trace("Plot \"{}\" added to agentNameMap entry \"{}\"", name, newValue);
                        }
                    });
                });
            }
            if (c.wasRemoved()) {
                c.getRemoved().forEach(agent -> {
                    agentNameMap.remove(agent.getName());
                    log.trace("Plot \"{}\" removed agent \"{}\"", name, agent);
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
                        log.trace("Plot \"{}\" added variable \"{}\"", name, variable.getName());
                    }
                });
            }
            if (c.wasRemoved()) {
                c.getRemoved().forEach(variable -> {
                    variableNameMap.remove(variable.getName());
                    if (log.isTraceEnabled()) {
                        log.trace("Plot \"{}\" removed variable \"{}\"", name, variable.getName());
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
                        log.trace("Plot \"{}\" added parameter \"{}\"", name, parameter.getName());
                    }
                });
            }
            if (c.wasRemoved()) {
                c.getRemoved().forEach(parameter -> {
                    parameterNameMap.remove(parameter.getName());
                    if (log.isTraceEnabled()) {
                        log.trace("Plot \"{}\" removed parameter \"{}\"", name, parameter.getName());
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
                        log.trace("Plot \"{}\" added changeMu \"{}\"", name, changeMu.getIdentifier());
                    }
                });
            }
            if (c.wasRemoved()) {
                c.getRemoved().forEach(changeMu -> {
                    changeMuNameMap.remove(changeMu.getIdentifier());
                    if (log.isTraceEnabled()) {
                        log.trace("Plot \"{}\" removed changeMu \"{}\"", name, changeMu.getIdentifier());
                    }
                });
            }
        }
    };

    public GCDPlot(GCDModel model, String name) {
        this.model = model;
        this.name = name;
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
        return null;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void addAlgebraicVariable(AlgebraicVariable algebraicVariable) {
        if (algebraicVariables.contains(algebraicVariable)) {
            return;
        }
        algebraicVariables.add(algebraicVariable);
        addVariables(algebraicVariable.getVariables());
        addParameters(algebraicVariable.getParameters());
    }

    public void removeAlgebraicVariable(AlgebraicVariable algebraicVariable,
                                        AlgebraicVariable[] selectedAlgebraicVariables,
                                        Agent[] selectedAgents,
                                        Variable[] selectedVariables,
                                        Parameter[] selectedParameters) {
        algebraicVariables.remove(algebraicVariable);
        removeVariables(algebraicVariable.getVariables(), selectedAlgebraicVariables, selectedAgents, selectedVariables, selectedParameters);
        removeParameters(algebraicVariable.getParameters(), selectedParameters);
    }

    public void addAgent(Agent agent) {
        if (agents.contains(agent)) {
            return;
        }
        agents.add(agent);
        addVariables(agent.getVariables());
        addParameters(agent.getParameters());
    }

    public void removeAgent(Agent agent,
                            AlgebraicVariable[] selectedAlgebraicVariables,
                            Agent[] selectedAgents,
                            Variable[] selectedVariables,
                            Parameter[] selectedParameters) {
        agents.remove(agent);
        removeVariables(agent.getVariables(), selectedAlgebraicVariables, selectedAgents, selectedVariables, selectedParameters);
        removeParameters(agent.getParameters(), selectedParameters);
    }

    public void addVariable(Variable variable) {
        if (!variables.contains(variable)) {
            variables.add(variable);
        }
    }

    public void removeVariable(Variable variable,
                               AlgebraicVariable[] selectedAlgebraicVariables,
                               Agent[] selectedAgents,
                               Variable[] selectedVariables) {
        for (Variable v : selectedVariables) {
            if (v.equals(variable)) {
                return;
            }
        }
        for (AlgebraicVariable algVar : selectedAlgebraicVariables) {
            for (String name : algVar.getVariables()) {
                for (Variable var : variables) {
                    if (name.equals(var.getName())) {
                        return;
                    }
                }
            }
        }
        for (Agent a : selectedAgents) {
            for (String name : a.getVariables()) {
                for (Variable var : variables) {
                    if (name.equals(var.getName())) {
                        return;
                    }
                }
            }
        }
        variables.remove(variable);
    }

    public void addParameter(Parameter p) {
        if (!parameters.contains(p)) {
            parameters.add(p);
        }
    }

    public void removeParameter(Parameter parameter,
                                AlgebraicVariable[] selectedAlgebraicVariables,
                                Agent[] selectedAgents,
                                Parameter[] selectedParameters) {
        for (Parameter p : selectedParameters) {
            if (p.equals(parameter)) {
                return;
            }
        }
        for (AlgebraicVariable algVar : selectedAlgebraicVariables) {
            for (String name : algVar.getParameters()) {
                for (Parameter p : parameters) {
                    if (name.equals(p.getName())) {
                        return;
                    }
                }
            }
        }
        for (Agent a : selectedAgents) {
            for (String name : a.getParameters()) {
                for (Parameter p : parameters) {
                    if (name.equals(p.getName())) {
                        return;
                    }
                }
            }
        }
        parameters.remove(parameter);
    }

    public void addChangeMu(ChangeMu mu) {
        if (!changeMus.contains(mu)) {
            changeMus.add(mu);
        }
    }

    public void removeChangeMu(ChangeMu mu) {
        changeMus.remove(mu);
    }

    private void addVariables(List<String> variables) {
        for (String name : variables) {
            if (model.hasVariable(name)) {
                Variable var = model.getVariable(name);
                addVariable(var);
            } else if (model.hasAlgebraicVariable(name)) {
                AlgebraicVariable algVar = model.getAlgebraicVariable(name);
                addAlgebraicVariable(algVar);
            }
        }
    }

    /**
     * @param variables                  variable names to remove
     * @param selectedAlgebraicVariables variables to skip from remove
     * @param selectedVariables          selected variables to skip from remove
     * @param selectedParameters         selected parameters needed for removeAlgebraicVariable
     */
    private void removeVariables(List<String> variables,
                                 AlgebraicVariable[] selectedAlgebraicVariables,
                                 Agent[] selectedAgents,
                                 Variable[] selectedVariables,
                                 Parameter[] selectedParameters) {
        for (String name : variables) {
            if (model.hasVariable(name)) {
                Variable variable = model.getVariable(name);
                if (!ArrayUtils.contains(selectedVariables, variable)) {
                    removeVariable(variable, selectedAlgebraicVariables, selectedAgents, selectedVariables);
                }
            } else if (model.hasAlgebraicVariable(name)) {
                AlgebraicVariable algVar = model.getAlgebraicVariable(name);
                if (!ArrayUtils.contains(selectedAlgebraicVariables, algVar)) {
                    removeAlgebraicVariable(algVar, selectedAlgebraicVariables, selectedAgents, selectedVariables, selectedParameters);
                }
            }
        }
    }

    private void addParameters(List<String> parameters) {
        for (String name : parameters) {
            if (model.hasParameter(name)) {
                Parameter parameter = model.getParameter(name);
                addParameter(parameter);
            }
        }
    }

    /**
     * @param parameters         parameter names to remove
     * @param selectedParameters selected parameters needed for removeAlgebraicVariable
     */
    private void removeParameters(List<String> parameters,
                                  Parameter[] selectedParameters) {
        for (String name : parameters) {
            if (model.hasParameter(name)) {
                Parameter parameter = model.getParameter(name);
                if (!ArrayUtils.contains(selectedParameters, parameter)) {
                    removeParameter(parameter);
                }
            }
        }
    }
}