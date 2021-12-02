package at.binter.gcd.model;

import at.binter.gcd.model.elements.*;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static at.binter.gcd.model.GCDWarning.*;
import static at.binter.gcd.model.Status.INVALID;

public abstract class GCDBaseModel {
    private static final Logger log = LoggerFactory.getLogger(GCDBaseModel.class);

    protected final ObservableList<AlgebraicVariable> algebraicVariables = FXCollections.observableArrayList(algebraicVariable -> new Observable[]{algebraicVariable.nameProperty()});
    protected final SortedList<AlgebraicVariable> algebraicVariablesSorted = algebraicVariables.sorted();
    protected final Map<String, AlgebraicVariable> algebraicVariableNameMap = new HashMap<>();
    protected final ObservableList<Agent> agents = FXCollections.observableArrayList();
    protected final SortedList<Agent> agentsSorted = agents.sorted();
    protected final Map<String, Agent> agentNameMap = new HashMap<>();
    protected final ObservableList<Constraint> constraints = FXCollections.observableArrayList();
    protected final ObservableList<Variable> variables = FXCollections.observableArrayList();
    protected final SortedList<Variable> variablesSorted = variables.sorted();
    protected final Map<String, Variable> variableNameMap = new HashMap<>();
    protected final ObservableList<Parameter> parameters = FXCollections.observableArrayList();
    protected final SortedList<Parameter> parametersSorted = parameters.sorted();
    protected final Map<String, Parameter> parameterNameMap = new HashMap<>();
    protected final ObservableList<ChangeMu> allChangeMu = FXCollections.observableArrayList();
    protected final ObservableList<ChangeMu> changeMus = FXCollections.observableArrayList();
    protected final Map<String, ChangeMu> changeMuNameMap = new HashMap<>();

    private Map<GCDWarning, List<Object>> warnings = new HashMap<>();

    private boolean runGenerateChangeMu = true;

    protected abstract ListChangeListener<AlgebraicVariable> getAlgebraicVariableListChangeListener();

    protected abstract ListChangeListener<Agent> getAgentListChangeListener();

    protected abstract ListChangeListener<Constraint> getConstraintListChangeListener();

    protected abstract ListChangeListener<Variable> getVariableListChangeListener();

    protected abstract ListChangeListener<Parameter> getParameterListChangeListener();

    protected abstract ListChangeListener<ChangeMu> getChangeMuListChangeListener();

    public void registerChangeListeners() {
        if (getAlgebraicVariableListChangeListener() != null) {
            algebraicVariables.addListener(getAlgebraicVariableListChangeListener());
        }
        if (getAgentListChangeListener() != null) {
            agents.addListener(getAgentListChangeListener());
        }
        if (getConstraintListChangeListener() != null) {
            constraints.addListener(getConstraintListChangeListener());
        }
        if (getVariableListChangeListener() != null) {
            variables.addListener(getVariableListChangeListener());
        }
        if (getParameterListChangeListener() != null) {
            parameters.addListener(getParameterListChangeListener());
        }
        if (getChangeMuListChangeListener() != null) {
            changeMus.addListener(getChangeMuListChangeListener());
        }
    }

    public void unregisterChangeListeners() {
        if (getAlgebraicVariableListChangeListener() != null) {
            algebraicVariables.removeListener(getAlgebraicVariableListChangeListener());
        }
        if (getAgentListChangeListener() != null) {
            agents.removeListener(getAgentListChangeListener());
        }
        if (getConstraintListChangeListener() != null) {
            constraints.removeListener(getConstraintListChangeListener());
        }
        if (getVariableListChangeListener() != null) {
            variables.removeListener(getVariableListChangeListener());
        }
        if (getParameterListChangeListener() != null) {
            parameters.removeListener(getParameterListChangeListener());
        }
        if (getChangeMuListChangeListener() != null) {
            changeMus.removeListener(getChangeMuListChangeListener());
        }
    }

    public boolean isRunGenerateChangeMu() {
        return runGenerateChangeMu;
    }

    public void setRunGenerateChangeMu(boolean runGenerateChangeMu) {
        this.runGenerateChangeMu = runGenerateChangeMu;
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

    protected void addVariables(HasVariableStringList source) {
        source.getVariables().forEach(name -> addVariable(source, name));
    }

    protected void addVariable(HasVariableStringList source, String name) {
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

    protected void removeVariables(HasVariableStringList source) {
        source.getVariables().forEach(name -> removeVariable(source, name));
    }

    protected void removeVariable(HasVariableStringList source, String name) {
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

    protected void removeVariable(Variable variable) {
        if (variable == null) return;
        if (!variables.remove(variable)) {
            log.debug("Could not remove variable \"{}\"", variable.getName());
        }
    }

    protected void updateVariables(HasVariableStringList source) {
        source.getVariablesAdded().forEach(name -> addVariable(source, name));
        source.getVariablesRemoved().forEach(name -> removeVariable(source, name));
    }

    protected void addParameters(HasParameterStringList source) {
        source.getParameters().forEach(name -> addParameter(source, name));
    }

    protected void addParameter(HasParameterStringList source, String name) {
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

    protected void removeParameters(HasParameterStringList source) {
        source.getParameters().forEach(name -> removeParameter(source, name));
    }

    protected void removeParameter(HasParameterStringList source, String name) {
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

    protected void removeParameter(Parameter parameter) {
        if (parameter == null) return;
        if (!parameters.remove(parameter)) {
            log.debug("Could not remove parameter \"{}\"", parameter.getName());
        }
    }

    protected void updateParameters(HasParameterStringList source) {
        source.getParametersAdded().forEach(name -> addParameter(source, name));
        source.getParametersRemoved().forEach(name -> removeParameter(source, name));
    }

    public void generateChangeMu() {
        if (!isRunGenerateChangeMu()) {
            return;
        }
        if (log.isTraceEnabled()) {
            log.trace("generateChangeMu called");
        }
        allChangeMu.clear();
        Map<String, ChangeMu> oldChangeMus = new HashMap<>();
        changeMus.forEach(changeMu -> {
            oldChangeMus.put(changeMu.getIdentifier(), changeMu);
        });
        changeMus.clear();
        changeMuNameMap.clear();
        SortedList<Agent> agentList = agents.sorted();
        SortedList<Variable> variableList = variables.sorted();
        for (int i = 0; i < agentList.size(); i++) {
            for (int j = 0; j < variableList.size(); j++) {
                ChangeMu changeMu = new ChangeMu(agentList.get(i), variableList.get(j), j + 1);
                allChangeMu.add(changeMu);
                if (changeMu.requireDoubleValues(algebraicVariables)) {
                    ChangeMu old = oldChangeMus.get(changeMu.getIdentifier());
                    if (old != null) {
                        changeMu.update(old);
                    }
                    changeMus.add(changeMu);
                }
            }
        }
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

    public SortedList<AlgebraicVariable> getAlgebraicVariablesSorted() {
        return algebraicVariablesSorted;
    }

    public AlgebraicVariable getAlgebraicVariable(String name) {
        if (StringUtils.isBlank(name)) return null;
        return algebraicVariableNameMap.get(name);
    }

    public ObservableList<Agent> getAgents() {
        return agents;
    }

    public SortedList<Agent> getAgentsSorted() {
        return agentsSorted;
    }

    public Agent getAgent(String name) {
        if (StringUtils.isBlank(name)) return null;
        return agentNameMap.get(name);
    }

    public ObservableList<Constraint> getConstraints() {
        return constraints;
    }

    public ObservableList<Variable> getVariables() {
        return variables;
    }

    public SortedList<Variable> getVariablesSorted() {
        return variablesSorted;
    }

    public Variable getVariable(String name) {
        if (StringUtils.isBlank(name)) return null;
        return variableNameMap.get(name);
    }

    public ObservableList<Parameter> getParameters() {
        return parameters;
    }

    public SortedList<Parameter> getParametersSorted() {
        return parametersSorted;
    }

    public Parameter getParameter(String name) {
        if (StringUtils.isBlank(name)) return null;
        return parameterNameMap.get(name);
    }

    public ObservableList<ChangeMu> getAllChangeMu() {
        return allChangeMu;
    }

    public ObservableList<ChangeMu> getChangeMus() {
        return changeMus;
    }

    public ChangeMu getChangeMu(String name) {
        if (StringUtils.isBlank(name)) return null;
        return changeMuNameMap.get(name);
    }

    public boolean isEmpty() {
        return algebraicVariables.size() + agents.size() + constraints.size() + variables.size() + parameters.size() + allChangeMu.size() + changeMus.size() == 0;
    }

    public Map<GCDWarning, List<Object>> getWarnings() {
        warnings = new HashMap<>();

        warnings.put(ALGEBRAIC_VARIABLE_RECURSION, new ArrayList<>());
        warnings.put(DUPLICATE_VARIABLE_PARAMETER, new ArrayList<>());
        warnings.put(MAX_VALUE_LESSER_MIN_VALUE, new ArrayList<>());
        warnings.put(START_VALUE_LESSER_MIN_VALUE, new ArrayList<>());
        warnings.put(START_VALUE_GREATER_MAX_VALUE, new ArrayList<>());
        warnings.put(MISSING_START_VALUE, new ArrayList<>());
        warnings.put(MISSING_MIN_VALUE, new ArrayList<>());
        warnings.put(MISSING_MAX_VALUE, new ArrayList<>());

        for (AlgebraicVariable algVar : algebraicVariablesSorted) {
            for (String name : algVar.getParameters()) {
                if (getAlgebraicVariable(name) != null) {
                    warnings.get(ALGEBRAIC_VARIABLE_RECURSION).add(algVar);
                }
            }
            for (String name : algVar.getVariables()) {
                if (getAlgebraicVariable(name) != null) {
                    warnings.get(ALGEBRAIC_VARIABLE_RECURSION).add(algVar);
                }
            }
        }

        for (Variable v : variablesSorted) {
            for (Parameter p : parametersSorted) {
                if (v.getName().equals(p.getName())) {
                    warnings.get(DUPLICATE_VARIABLE_PARAMETER).add(v);
                }
            }
            if (v.getStatus() == INVALID) {
                checkDoubleValues(v);
            }
        }
        for (Parameter p : parametersSorted) {
            checkDoubleValues(p);
        }
        for (ChangeMu mu : changeMus) {
            checkDoubleValues(mu);
        }
        return warnings;
    }

    private void checkDoubleValues(HasMinMaxValues b) {
        if (b.getMaxValue() != null && b.getMinValue() != null) {
            if (b.getMaxValue() < b.getMinValue()) {
                warnings.get(MAX_VALUE_LESSER_MIN_VALUE).add(b);
            }
        }
        if (b.getStartValue() != null) {
            if (b.getMinValue() != null && b.getStartValue() < b.getMinValue()) {
                warnings.get(START_VALUE_LESSER_MIN_VALUE).add(b);
            }
            if (b.getMaxValue() != null && b.getStartValue() > b.getMaxValue()) {
                warnings.get(START_VALUE_GREATER_MAX_VALUE).add(b);
            }
        }

        if (b.getStartValue() == null) {
            warnings.get(MISSING_START_VALUE).add(b);
        }
        if (b.getMinValue() == null) {
            warnings.get(MISSING_MIN_VALUE).add(b);
        }
        if (b.getMaxValue() == null) {
            warnings.get(MISSING_MAX_VALUE).add(b);
        }
    }
}