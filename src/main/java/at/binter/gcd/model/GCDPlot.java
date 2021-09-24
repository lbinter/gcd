package at.binter.gcd.model;

import at.binter.gcd.model.elements.Agent;
import at.binter.gcd.model.elements.AlgebraicVariable;
import at.binter.gcd.model.elements.Variable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    public SortedList<GCDPlotItem<Agent>> getAgentsSorted() {
        return agentsSorted;
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
        algebraicVariables.add(new GCDPlotItem<>(algVar));
    }

    public void addAgent(Agent agent) {
        if (agent == null || hasAgent(agent)) {
            return;
        }
        agents.add(new GCDPlotItem<>(agent));
    }

    public void addVariable(Variable variable) {
        if (variable == null || variables.contains(variable)) {
            return;
        }
        variables.add(variable);
    }

    private void addVariables(List<String> variables) {
        for (String name : variables) {
            if (model.hasVariable(name)) {
                Variable var = model.getVariable(name);
                //addVariable(var);
            } else if (model.hasAlgebraicVariable(name)) {
                AlgebraicVariable algVar = model.getAlgebraicVariable(name);
                //addAlgebraicVariable(algVar);
            }
        }
    }

    /**
     * @param variables                  variable names to remove
     * @param selectedAlgebraicVariables variables to skip from remove
     * @param selectedVariables          selected variables to skip from remove
     */
    private void removeVariables(List<String> variables,
                                 AlgebraicVariable[] selectedAlgebraicVariables,
                                 Agent[] selectedAgents,
                                 Variable[] selectedVariables) {
        for (String name : variables) {
            if (model.hasVariable(name)) {
                Variable variable = model.getVariable(name);
                if (!ArrayUtils.contains(selectedVariables, variable)) {
                    // removeVariable(variable, selectedAlgebraicVariables, selectedAgents, selectedVariables);
                }
            } else if (model.hasAlgebraicVariable(name)) {
                AlgebraicVariable algVar = model.getAlgebraicVariable(name);
                if (!ArrayUtils.contains(selectedAlgebraicVariables, algVar)) {
                    // removeAlgebraicVariable(algVar, selectedAlgebraicVariables, selectedAgents, selectedVariables);
                }
            }
        }
    }
}