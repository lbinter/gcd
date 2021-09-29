package at.binter.gcd.model;

import at.binter.gcd.model.elements.Agent;
import at.binter.gcd.model.elements.AlgebraicVariable;
import at.binter.gcd.model.elements.PlotVariable;
import at.binter.gcd.model.elements.Variable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

import static at.binter.gcd.util.GuiUtils.sanitizeString;

public class GCDPlot {
    private static final Logger log = LoggerFactory.getLogger(GCDPlot.class);

    private final GCDModel model;
    private String name;
    private String legendLabel;
    private String plotStyle;
    private String plotRange;

    private final ObservableList<GCDPlotItem<AlgebraicVariable>> algebraicVariables = FXCollections.observableArrayList();
    private final SortedList<GCDPlotItem<AlgebraicVariable>> algebraicVariablesSorted = algebraicVariables.sorted();
    private final ObservableList<GCDPlotItem<Agent>> agents = FXCollections.observableArrayList();
    private final SortedList<GCDPlotItem<Agent>> agentsSorted = agents.sorted();
    private final ObservableList<PlotVariable> variables = FXCollections.observableArrayList();
    private final SortedList<PlotVariable> variablesSorted = variables.sorted();

    public GCDPlot(GCDModel model, String name) {
        this.model = model;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public GCDModel getModel() {
        return model;
    }

    public String getLegendLabel() {
        return legendLabel;
    }

    public void setLegendLabel(String legendLabel) {
        this.legendLabel = legendLabel;
    }

    public String getPlotStyle() {
        if (StringUtils.isBlank(plotStyle)) {
            getDefaultPlotStyle();
        }
        return plotStyle;
    }

    public String getDefaultPlotStyle() {
        return getDefaultPlotStyleForName(getName());
    }

    public String getDefaultPlotStyleForName(String name) {
        return "PLOTSTYLE" + sanitizeString(name).replace(" ", "");
    }

    public void setPlotStyle(String plotStyle) {
        this.plotStyle = plotStyle;
    }

    public String getPlotRange() {
        if (StringUtils.isBlank(plotRange)) {
            return "{-plotmax, plotmax}";
        }
        return plotRange;
    }

    public void setPlotRange(String plotRange) {
        this.plotRange = plotRange;
    }

    public SortedList<GCDPlotItem<AlgebraicVariable>> getAlgebraicVariablesSorted() {
        return algebraicVariablesSorted;
    }

    public List<AlgebraicVariable> getAlgebraicVariables() {
        List<AlgebraicVariable> list = new ArrayList<>();
        for (GCDPlotItem<AlgebraicVariable> item : algebraicVariables) {
            list.add(item.getItem());
        }
        return list;
    }

    public SortedList<GCDPlotItem<Agent>> getAgentsSorted() {
        return agentsSorted;
    }

    public List<Agent> getAgents() {
        List<Agent> list = new ArrayList<>();
        for (GCDPlotItem<Agent> item : agents) {
            list.add(item.getItem());
        }
        return list;
    }

    public SortedList<PlotVariable> getVariablesSorted() {
        return variablesSorted;
    }

    public GCDPlotItem<AlgebraicVariable> getAlgebraicVariable(AlgebraicVariable algebraicVariable) {
        if (algebraicVariable == null) return null;
        for (GCDPlotItem<AlgebraicVariable> algVar : algebraicVariables) {
            if (algebraicVariable.equals(algVar.getItem())) {
                return algVar;
            }
        }
        return null;
    }

    public boolean hasAlgebraicVariable(AlgebraicVariable algVar) {
        if (algVar == null) return false;
        for (GCDPlotItem<AlgebraicVariable> item : algebraicVariables) {
            if (algVar.equals(item.getItem())) {
                return true;
            }
        }
        return false;
    }

    public GCDPlotItem<Agent> getAgent(Agent agent) {
        if (agent == null) return null;
        for (GCDPlotItem<Agent> item : agents) {
            if (agent.equals(item.getItem())) {
                return item;
            }
        }
        return null;
    }

    public boolean hasAgent(Agent agent) {
        if (agent == null) return false;
        for (GCDPlotItem<Agent> item : agents) {
            if (agent.equals(item.getItem())) {
                return true;
            }
        }
        return false;
    }

    public PlotVariable getVariable(Variable variable) {
        if (variable == null) return null;
        for (PlotVariable plotVariable : variables) {
            if (variable.equals(plotVariable.variable)) {
                return plotVariable;
            }
        }
        return null;
    }

    public boolean hasVariable(Variable variable) {
        if (variable == null) return false;
        for (PlotVariable v : variables) {
            if (variable.equals(v.variable)) {
                return true;
            }
        }
        return false;
    }

    public GCDPlotItem<AlgebraicVariable> addAlgebraicVariable(boolean independent, AlgebraicVariable algVar) {
        if (algVar == null || hasAlgebraicVariable(algVar)) {
            return getAlgebraicVariable(algVar);
        }
        GCDPlotItem<AlgebraicVariable> newItem = new GCDPlotItem<>(independent, algVar);
        newItem.addDependedProperty().addListener((observable, wasSelected, isSelected) -> {
            if (isSelected) {
                addVariablesFor(algVar);
            } else {
                removeVariablesFor(algVar);
            }
        });
        algebraicVariables.add(newItem);
        return newItem;
    }

    public void removeAlgebraicVariable(GCDPlotItem<AlgebraicVariable> algVar) {
        if (algVar == null || !hasAlgebraicVariable(algVar.getItem())) {
            return;
        }
        if (algebraicVariables.remove(algVar) && algVar.isAddDepended()) {
            removeVariablesFor(algVar.getItem());
        }
    }

    private void removeAlgebraicVariable(AlgebraicVariable algebraicVariable, boolean automaticRemove) {
        GCDPlotItem<AlgebraicVariable> algVar = getAlgebraicVariable(algebraicVariable);
        if (algVar == null) {
            return;
        }
        boolean removed = false;
        if (automaticRemove) {
            if (!algVar.independent) {
                removed = algebraicVariables.remove(algVar);
            }
        } else {
            removed = algebraicVariables.remove(algVar);
        }
        if (removed && algVar.isAddDepended()) {
            removeVariablesFor(algVar.getItem());
        }
    }

    public void removeAgent(GCDPlotItem<Agent> agent) {
        if (agent == null || !hasAgent(agent.getItem())) {
            return;
        }
        if (agents.remove(agent) && agent.isAddDepended()) {
            removeVariablesFor(agent.getItem());
        }
    }


    public GCDPlotItem<Agent> addAgent(Agent agent) {
        if (agent == null || hasAgent(agent)) {
            return getAgent(agent);
        }
        GCDPlotItem<Agent> newItem = new GCDPlotItem<>(true, agent);
        newItem.addDependedProperty().addListener((observable, wasSelected, isSelected) -> {
            if (isSelected) {
                addVariablesFor(agent);
            } else {
                removeVariablesFor(agent);
            }
        });
        agents.add(newItem);
        return newItem;
    }

    public void addVariable(boolean independent, Variable variable) {
        if (variable == null) {
            return;
        }
        if (hasVariable(variable)) {
            if (!independent) {
                getVariable(variable).dependentOn++;
            }
        } else {
            variables.add(new PlotVariable(independent, variable));
        }
    }

    public void removeVariable(Variable variable, boolean automaticRemove) {
        PlotVariable v = getVariable(variable);
        if (v == null) {
            return;
        }
        removeVariable(v, automaticRemove);
    }

    public void removeVariable(PlotVariable variable, boolean automaticRemove) {
        if (variable == null) {
            return;
        }
        boolean shouldRemove = false;
        if (automaticRemove) {
            if (!variable.independent) {
                variable.dependentOn--;
                shouldRemove = variable.dependentOn <= 0;
            }
        } else {
            shouldRemove = true;
        }
        if (shouldRemove) {
            variables.remove(variable);
        }
    }

    private void addVariablesFor(HasVariableStringList item) {
        for (String name : item.getVariables()) {
            if (model.hasVariable(name)) {
                Variable var = model.getVariable(name);
                addVariable(false, var);
            } else if (model.hasAlgebraicVariable(name)) {
                AlgebraicVariable algVar = model.getAlgebraicVariable(name);
                addAlgebraicVariable(false, algVar);
            }
        }
    }

    private void removeVariablesFor(HasVariableStringList item) {
        for (String name : item.getVariables()) {
            if (model.hasVariable(name)) {
                Variable variable = model.getVariable(name);
                removeVariable(variable, true);
            } else if (model.hasAlgebraicVariable(name)) {
                AlgebraicVariable algVar = model.getAlgebraicVariable(name);
                removeAlgebraicVariable(algVar, true);
            }
        }
    }
}