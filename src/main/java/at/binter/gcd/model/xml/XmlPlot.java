package at.binter.gcd.model.xml;

import at.binter.gcd.model.GCDPlot;
import at.binter.gcd.model.elements.*;
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
    public List<String> algebraicVariables = new ArrayList<>();
    @XmlElementWrapper(name = "agents")
    @XmlElement(name = "agent")
    public List<String> agents = new ArrayList<>();
    @XmlElementWrapper(name = "variables")
    @XmlElement(name = "variable")
    public List<String> variables = new ArrayList<>();
    @XmlElementWrapper(name = "parameters")
    @XmlElement(name = "parameter")
    public List<String> parameters = new ArrayList<>();
    @XmlElementWrapper(name = "change-mu")
    @XmlElement(name = "mu")
    public List<String> changeMu = new ArrayList<>();

    public XmlPlot() {
    }

    public XmlPlot(GCDPlot plot) {
        name = plot.getName();
        legendLabel = plot.getLegendLabel();
        plotStyle = plot.getPlotStyle();
        plotRange = plot.getPlotRange();
        for (AlgebraicVariable algVar : plot.getAlgebraicVariablesSorted()) {
            algebraicVariables.add(algVar.getName());
        }
        for (Agent agent : plot.getAgentsSorted()) {
            agents.add(agent.getName());
        }
        for (Variable v : plot.getVariablesSorted()) {
            variables.add(v.getName());
        }
        for (Parameter p : plot.getParametersSorted()) {
            parameters.add(p.getName());
        }
        for (ChangeMu mu : plot.getChangeMus()) {
            changeMu.add(mu.getIdentifier());
        }
    }
}