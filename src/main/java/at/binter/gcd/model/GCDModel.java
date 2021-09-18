package at.binter.gcd.model;

import at.binter.gcd.mathematica.elements.MParameter;
import at.binter.gcd.mathematica.elements.MVariable;
import at.binter.gcd.mathematica.syntax.IExpression;
import at.binter.gcd.mathematica.syntax.MComment;
import at.binter.gcd.mathematica.syntax.MExpression;
import at.binter.gcd.mathematica.syntax.MExpressionList;
import at.binter.gcd.mathematica.syntax.binary.*;
import at.binter.gcd.mathematica.syntax.function.MJoin;
import at.binter.gcd.mathematica.syntax.function.MLength;
import at.binter.gcd.mathematica.syntax.function.MTable;
import at.binter.gcd.mathematica.syntax.group.MList;
import at.binter.gcd.mathematica.syntax.group.MParentheses;
import at.binter.gcd.mathematica.syntax.unary.MReplaceAll;
import at.binter.gcd.model.elements.*;
import at.binter.gcd.model.xml.XmlBasicVariable;
import at.binter.gcd.model.xml.XmlFunction;
import at.binter.gcd.model.xml.XmlModel;
import at.binter.gcd.model.xml.XmlVariable;
import javafx.beans.Observable;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GCDModel {
    private static final Logger log = LoggerFactory.getLogger(GCDModel.class);

    private final ObservableList<AlgebraicVariable> algebraicVariables = FXCollections.observableArrayList(algebraicVariable -> new Observable[]{algebraicVariable.nameProperty()});
    private final SortedList<AlgebraicVariable> algebraicVariablesSorted = algebraicVariables.sorted();
    private final Map<String, AlgebraicVariable> algebraicVariableNameMap = new HashMap<>();
    private final ObservableList<Agent> agents = FXCollections.observableArrayList();
    private final SortedList<Agent> agentsSorted = agents.sorted();
    private final Map<String, Agent> agentNameMap = new HashMap<>();
    private final ObservableList<Constraint> constraints = FXCollections.observableArrayList();
    private final ObservableList<Variable> variables = FXCollections.observableArrayList();
    private final SortedList<Variable> variablesSorted = variables.sorted();
    private final Map<String, Variable> variableNameMap = new HashMap<>();
    private final ObservableList<Parameter> parameters = FXCollections.observableArrayList();
    private final Map<String, Parameter> parameterNameMap = new HashMap<>();
    private final ObservableList<ChangeMu> allChangeMu = FXCollections.observableArrayList();
    private final ObservableList<ChangeMu> changeMus = FXCollections.observableArrayList();
    private final Map<String, ChangeMu> changeMuNameMap = new HashMap<>();

    private File file;
    private final BooleanProperty savedToFile = new SimpleBooleanProperty(false);

    private boolean runGenerateChangeMu = true;

    private boolean clearGlobal = true;

    private final ListChangeListener<AlgebraicVariable> algebraicVariableListChangeListener = c -> {
        while (c.next()) {
            if (c.wasAdded()) {
                c.getAddedSubList().forEach(algVar -> {
                    algebraicVariableNameMap.put(algVar.getName(), algVar);
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
                        log.trace("Added changeMu \"{}\"", changeMu.getIdentifier());
                    }
                });
            }
        }
    };

    public GCDModel() {
        registerChangeListeners();
    }

    public void registerChangeListeners() {
        algebraicVariables.addListener(algebraicVariableListChangeListener);
        agents.addListener(agentListChangeListener);
        constraints.addListener(constraintListChangeListener);
        variables.addListener(variableListChangeListener);
        parameters.addListener(parameterListChangeListener);
        changeMus.addListener(changeMuListChangeListener);
    }

    public void unregisterChangeListeners() {
        algebraicVariables.removeListener(algebraicVariableListChangeListener);
        agents.removeListener(agentListChangeListener);
        constraints.removeListener(constraintListChangeListener);
        variables.removeListener(variableListChangeListener);
        parameters.removeListener(parameterListChangeListener);
        changeMus.removeListener(changeMuListChangeListener);
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

    public boolean isClearGlobal() {
        return clearGlobal;
    }

    public void setClearGlobal(boolean clearGlobal) {
        this.clearGlobal = clearGlobal;
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

    public void generateChangeMu() {
        if (!runGenerateChangeMu) {
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
        runGenerateChangeMu = false;
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

        runGenerateChangeMu = true;
        generateChangeMu();
        for (XmlBasicVariable changeMu : model.changeMu) {
            ChangeMu mu = getChangeMu(changeMu.name);
            mu.update(changeMu.createChangeMu(mu.getAgent(), mu.getVariable(), mu.getIndex()));
        }
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

    public static final MComment diffVarComment = new MComment("durch Differentialgleichungen bestimmte Variable");
    public static final MVariable diffVar = new MVariable("diffvar");
    public static final MVariable nDiffVar = new MVariable("ndiffvar");
    public static final MComment algVarComment = new MComment("durch algebraische Definitionsgleichungen bestimmte Variable");
    public static final MVariable algVar = new MVariable("algvar");
    public static final MVariable nAlgVar = new MVariable("nalgvar");
    public static final MVariable var = new MVariable("var");
    public static final MVariable nVar = new MVariable("nvar");

    public static final MComment AGComment = new MComment("Agenten");
    public static final MVariable AG = new MVariable("AG");
    public static final MVariable nAG = new MVariable("nAG");

    public static final MComment substituteComment = new MComment("algebraische Definitionsgleichungen der \"algebraischen\" Variablen algvar");
    public static final MVariable substitute = new MVariable("substitute");
    public static final MParameter defAlgVar = new MParameter("defalgvar", "ii_", "t_", "var_");
    public static final MParameter defAlgVarSubstitute = new MParameter("defalgvarsubstitute", "ii_", "t_", "var_");
    public static final MComment defuVarComment = new MComment("Definitionsgleichungen der Nutzenfunktionen");
    public static final MParameter defuVar = new MParameter("defuvar", "j_", "t_", "var_");
    public static final MParameter defuVarSubstitute = new MParameter("defuvarsubstitute", "j_", "t_", "var_");
    public static final MComment nZwangBComment = new MComment("Definitionsgleichungen der Zwangsbedingungen");
    public static final MVariable nZwangB = new MVariable("nZwangB");
    public static final MParameter defzVar = new MParameter("defzvar", "q_", "t_", "var_");
    public static final MParameter defzVarSubstitute = new MParameter("defzvarsubstitute", "q_", "t_", "var_");
    public static final MComment mfiComment = new MComment("indizierte Machtfaktoren");
    public static final MVariable mfi = new MVariable("MFi");
    public static final MTable mfiTable = new MTable(
            new MParameter("\\[Mu]", "j", "i"),
            new MList(new MExpression("j"), AG),
            new MList(new MExpression("i"), nDiffVar)
    );
    private static final MPostfix mfiMatrix = new MPostfix(mfi, new MExpression("MatrixForm"));

    public MSet getSetDiffVar() {
        MList list = new MList();
        for (Variable v : getVariablesSorted()) {
            list.add(new MExpression("variable", v.getName()));
        }
        return new MSet(diffVar, list);
    }

    public MSet getSetnDiffVar() {
        return new MSet(nDiffVar, new MLength(diffVar));
    }

    public MSet getSetAlgVar() {
        MList list = new MList();
        for (AlgebraicVariable v : getAlgebraicVariablesSorted()) {
            list.add(new MExpression("variable", v.getName()));
        }
        return new MSet(algVar, list);
    }

    public MSet getSetnAlgVar() {
        return new MSet(nAlgVar, new MLength(algVar));
    }

    public MSet getSetVar() {
        return new MSet(var, new MJoin(diffVar, algVar));
    }

    public MSet getSetnVar() {
        return new MSet(nVar, new MLength(var));
    }

    public MSet getSetAG() {
        MList list = new MList();
        for (Agent a : getAgentsSorted()) {
            list.add(new MExpression("variable", a.getName()));
        }
        return new MSet(AG, list);
    }

    public MSet getSetnAG() {
        return new MSet(nAG, new MLength(AG));
    }

    public MSet getSetSubstitute() {
        MList list = new MList();
        list.setElementsLinebreak(1);
        for (AlgebraicVariable aV : getAlgebraicVariablesSorted()) {
            MExpressionList l = new MExpressionList();
            l.add(new MParameter(aV.getName(), aV.getParameter()));
            l.add(new MExpression("&#8594;"));
            l.add(new MExpression(aV.getFunction()));
            list.add(l);
        }
        MSet set = new MSet(substitute, list);
        set.setAddSemicolon(true);
        return set;
    }

    public List<MSetDelayed> getSetDelayedDefalgvar() {
        List<MSetDelayed> list = new ArrayList<>(algebraicVariables.size());
        int ii = 1;
        for (AlgebraicVariable aV : getAlgebraicVariablesSorted()) {
            MParameter algVar = new MParameter(aV.getName(), aV.getParameter());
            MSetDelayed delayed = new MSetDelayed(defAlgVar,
                    new MCondition(
                            algVar,
                            new MEqual(new MExpression("parameter", "ii"), new MExpression(ii))
                    )
            );
            delayed.setAddSemicolon(true);
            list.add(delayed);
            ii++;
        }
        return list;
    }

    private MParameter getDefalgVar(int ii) {
        if (ii > getAlgebraicVariables().size()) {
            log.error("Called getDefalgvar with ii of {}", ii);
        }
        return new MParameter(defAlgVar.getName(), String.valueOf(ii), "t", "var");
    }

    private MParameter getDefAlgVarSubstitute(int ii) {
        if (ii > getAlgebraicVariables().size()) {
            log.error("Called getDefAlgVarSubstitute with ii of {}", ii);
        }
        return new MParameter(defAlgVarSubstitute.getName(), String.valueOf(ii), "t", "var");
    }

    public List<MSetDelayed> getSetDelayedDefAlgVarSubstitute() {
        List<MSetDelayed> list = new ArrayList<>(algebraicVariables.size());
        for (int ii = 1; ii <= getAlgebraicVariables().size(); ii++) {
            MExpressionList expr = new MExpressionList();
            expr.add(getDefalgVar(ii));
            expr.add(new MReplaceAll(substitute));
            MParentheses parentheses = new MParentheses(expr);
            MSetDelayed delayed = new MSetDelayed(defAlgVarSubstitute,
                    new MCondition(
                            parentheses,
                            new MEqual(new MExpression("parameter", "ii"), new MExpression(ii))
                    )
            );
            delayed.setAddSemicolon(true);
            list.add(delayed);
        }
        return list;
    }

    public List<MParameter> getParameterListDefAlgVar() {
        List<MParameter> list = new ArrayList<>();
        for (int ii = 1; ii <= getAlgebraicVariables().size(); ii++) {
            list.add(getDefalgVar(ii));
        }
        return list;
    }

    public List<MParameter> getParameterListDefAlgVarSubstitute() {
        List<MParameter> list = new ArrayList<>();
        for (int ii = 1; ii <= getAlgebraicVariables().size(); ii++) {
            list.add(getDefAlgVarSubstitute(ii));
        }
        return list;
    }

    public MParameter getDefuVar(String index) {
        if (StringUtils.isBlank(index)) {
            log.error("Called getDefuVar with index of null");
        } else if (getAgent(index) == null) {
            log.error("Called getDefuVar with non exists agent index of {}", index);
        }
        return new MParameter(defuVar.getName(), index, "t", "var");
    }

    public MParameter getDefuVarSubstitute(String index) {
        if (StringUtils.isBlank(index)) {
            log.error("Called getDefuVarSubstitute with index of null");
        } else if (getAgent(index) == null) {
            log.error("Called getDefuVarSubstitute with non exists agent index of {}", index);
        }
        return new MParameter(defuVarSubstitute.getName(), index, "t", "var");
    }

    public List<IExpression> getSetDelayedDefuVar() {
        List<IExpression> list = new ArrayList<>(agents.size());
        for (Agent a : getAgentsSorted()) {
            if (StringUtils.isNotBlank(a.getDescription())) {
                list.add(new MComment(a.getDescription()));
            }
            MSetDelayed delayed = new MSetDelayed(defuVar,
                    new MCondition(
                            new MParentheses(new MExpression(a.getFunction())),
                            new MEqual(
                                    new MExpression("parameter", "j"),
                                    new MExpression("variable", a.getName()
                                    ))));
            delayed.setAddSemicolon(true);
            list.add(delayed);
        }
        return list;
    }

    public List<MSetDelayed> getSetDelayedDefuVarSubstitute() {
        List<MSetDelayed> list = new ArrayList<>(agents.size());
        for (Agent a : getAgentsSorted()) {
            MExpressionList expr = new MExpressionList();
            expr.add(getDefuVar(a.getName()));
            expr.add(new MReplaceAll(substitute));
            MParentheses parentheses = new MParentheses(expr);

            MSetDelayed delayed = new MSetDelayed(defuVarSubstitute,
                    new MCondition(
                            parentheses,
                            new MEqual(
                                    new MExpression("parameter", "j"),
                                    new MExpression("variable", a.getName()
                                    ))));
            delayed.setAddSemicolon(true);
            list.add(delayed);
        }
        return list;
    }

    public List<MParameter> getParameterListDefuVar() {
        List<MParameter> list = new ArrayList<>();
        for (Agent a : getAgentsSorted()) {
            list.add(getDefuVar(a.getName()));
        }
        return list;
    }

    public List<MParameter> getParameterListDefuVarSubstitute() {
        List<MParameter> list = new ArrayList<>();
        for (Agent a : getAgentsSorted()) {
            list.add(getDefuVarSubstitute(a.getName()));
        }
        return list;
    }

    public MSet getSetnZwangB() {
        return new MSet(nZwangB, new MExpression(getConstraints().size()));
    }

    public MParameter getDefzVar(int ii) {
        if (ii > getConstraints().size()) {
            log.error("Called getDefzvar with q of {}", ii);
        }
        return new MParameter(defzVar.getName(), String.valueOf(ii), "t", "var");
    }

    public MParameter getDefzVarSubstitute(int ii) {
        if (ii > getConstraints().size()) {
            log.error("Called getDefzVarSubstitute with q of {}", ii);
        }
        return new MParameter(defzVarSubstitute.getName(), String.valueOf(ii), "t", "var");
    }

    public List<IExpression> getSetDelayedDefzVar() {
        List<IExpression> list = new ArrayList<>(agents.size());
        for (Constraint c : getConstraints()) {
            if (StringUtils.isNotBlank(c.getDescription())) {
                list.add(new MComment(c.getDescription()));
            }
            MSetDelayed delayed = new MSetDelayed(defzVar,
                    new MCondition(
                            new MParentheses(new MExpression(c.getCondition())),
                            new MEqual(
                                    new MExpression("parameter", "q"),
                                    new MExpression(c.getId())
                            )));
            delayed.setAddSemicolon(true);
            list.add(delayed);
        }
        return list;
    }

    public List<MSetDelayed> getSetDelayedDefzVarSubstitute() {
        List<MSetDelayed> list = new ArrayList<>(agents.size());
        for (Constraint c : getConstraints()) {
            MExpressionList expr = new MExpressionList();
            expr.add(getDefzVar(c.getId()));
            expr.add(new MReplaceAll(substitute));
            MParentheses parentheses = new MParentheses(expr);

            MSetDelayed delayed = new MSetDelayed(defzVarSubstitute,
                    new MCondition(
                            parentheses,
                            new MEqual(
                                    new MExpression("parameter", "q"),
                                    new MExpression(c.getId())
                            )));
            delayed.setAddSemicolon(true);
            list.add(delayed);
        }
        return list;
    }

    public List<MParameter> getParameterListDefzVar() {
        List<MParameter> list = new ArrayList<>();
        for (Constraint c : getConstraints()) {
            list.add(getDefzVar(c.getId()));
        }
        return list;
    }

    public List<MParameter> getParameterListDefzVarSubstitute() {
        List<MParameter> list = new ArrayList<>();
        for (Constraint c : getConstraints()) {
            list.add(getDefzVarSubstitute(c.getId()));
        }
        return list;
    }

    public MSet getSetMFi() {
        return new MSet(mfi, mfiTable);
    }

    public MPostfix getMFiMatrix() {
        mfiMatrix.setAddSemicolon(true);
        return mfiMatrix;
    }
}