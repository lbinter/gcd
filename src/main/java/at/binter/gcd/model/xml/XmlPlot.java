package at.binter.gcd.model.xml;

import at.binter.gcd.model.GCDPlot;
import at.binter.gcd.model.GCDPlotItem;
import at.binter.gcd.model.elements.Agent;
import at.binter.gcd.model.elements.AlgebraicVariable;
import at.binter.gcd.model.elements.Variable;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElementWrapper;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.util.ArrayList;
import java.util.List;

@XmlRootElement
public class XmlPlot {
    @XmlElement
    public String name;
    @XmlElement
    public String legendLabel;
    @XmlElement
    public String plotStyle;
    @XmlElement
    public String plotRange;
    @XmlElementWrapper(name = "algebraic-variables")
    @XmlElement(name = "algVar")
    public List<GCDPlotItem<AlgebraicVariable>> algebraicVariables = new ArrayList<>();
    @XmlElementWrapper(name = "agents")
    @XmlElement(name = "agent")
    public List<GCDPlotItem<Agent>> agents = new ArrayList<>();
    @XmlElementWrapper(name = "variables")
    @XmlElement(name = "variable")
    public List<String> variables = new ArrayList<>();

    public XmlPlot() {
    }

    public XmlPlot(GCDPlot plot) {
        name = plot.getName();
        legendLabel = plot.getLegendLabel();
        plotStyle = plot.getPlotStyle();
        plotRange = plot.getPlotRange();
        algebraicVariables.addAll(plot.getAlgebraicVariablesSorted());
        agents.addAll(plot.getAgentsSorted());
        for (Variable v : plot.getVariablesSorted()) {
            variables.add(v.getName());
        }
    }
}