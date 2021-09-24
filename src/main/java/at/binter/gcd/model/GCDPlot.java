package at.binter.gcd.model;

import at.binter.gcd.model.elements.Agent;
import at.binter.gcd.model.elements.AlgebraicVariable;
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
    private final ObservableList<Variable> variables = FXCollections.observableArrayList();
    private final SortedList<Variable> variablesSorted = variables.sorted();

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
            return "PLOTSTYLE" + sanitizeString(getName()).replace(" ", "");
        }
        return plotStyle;
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

    public SortedList<Variable> getVariablesSorted() {
        return variablesSorted;
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

    public boolean hasAgent(Agent agent) {
        if (agent == null) return false;
        for (GCDPlotItem<Agent> item : agents) {
            if (agent.equals(item.getItem())) {
                return true;
            }
        }
        return false;
    }

    public boolean hasVariable(Variable variable) {
        if (variable == null) return false;
        for (Variable v : variables) {
            if (variable.equals(v)) {
                return true;
            }
        }
        return false;
    }

    public void addAlgebraicVariable(AlgebraicVariable algVar) {
        if (algVar == null || hasAlgebraicVariable(algVar)) {
            return;
        }
        GCDPlotItem<AlgebraicVariable> newItem = new GCDPlotItem<>(algVar);
        newItem.addDependedProperty().addListener((observable, wasSelected, isSelected) -> {
            if (isSelected) {
                addVariablesFor(algVar);
            } else {
                removeVariablesFor(algVar);
            }
        });
        algebraicVariables.add(newItem);
    }

    public void removeAlgebraicVariable(GCDPlotItem<AlgebraicVariable> algVar) {
        if (algVar == null || !hasAlgebraicVariable(algVar.getItem())) {
            return;
        }
        if (algebraicVariables.remove(algVar)) {
            // TODO remove depended
        }
    }

    private void removeAlgebraicVariable(AlgebraicVariable algVar) {
        // TODO implement me
    }

    public void removeAgent(GCDPlotItem<Agent> agent) {
        if (agent == null || !hasAgent(agent.getItem())) {
            return;
        }
        if (agents.remove(agent)) {
            // TODO remove depended
        }
    }


    public void addAgent(Agent agent) {
        if (agent == null || hasAgent(agent)) {
            return;
        }
        GCDPlotItem<Agent> newItem = new GCDPlotItem<>(agent);
        newItem.addDependedProperty().addListener((observable, wasSelected, isSelected) -> {
            if (isSelected) {
                addVariablesFor(agent);
            } else {
                removeVariablesFor(agent);
            }
        });
        agents.add(newItem);
    }

    public void addVariable(Variable variable) {
        if (variable == null || variables.contains(variable)) {
            return;
        }
        variables.add(variable);
    }

    public void removeVariable(Variable variable) {
    }

    private void addVariablesFor(HasVariableStringList item) {
        for (String name : item.getVariables()) {
            if (model.hasVariable(name)) {
                Variable var = model.getVariable(name);
                addVariable(var);
            } else if (model.hasAlgebraicVariable(name)) {
                AlgebraicVariable algVar = model.getAlgebraicVariable(name);
                addAlgebraicVariable(algVar);
            }
        }
    }

    private void removeVariablesFor(HasVariableStringList item) {
        for (String name : item.getVariables()) {
            if (model.hasVariable(name)) {
                Variable variable = model.getVariable(name);
                removeVariable(variable);
                // TODO remove variable after check
            } else if (model.hasAlgebraicVariable(name)) {
                AlgebraicVariable algVar = model.getAlgebraicVariable(name);
                removeAlgebraicVariable(algVar);
                // TODO remove algvar and children
            }
        }
    }
}