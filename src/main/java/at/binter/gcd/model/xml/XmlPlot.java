package at.binter.gcd.model.xml;

import at.binter.gcd.model.GCDPlot;
import at.binter.gcd.model.GCDPlotItem;
import at.binter.gcd.model.elements.Agent;
import at.binter.gcd.model.elements.AlgebraicVariable;
import at.binter.gcd.model.elements.PlotVariable;
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
    public String plotParameter;
    @XmlElement
    public String plotRange;
    @XmlElement
    public boolean showPlotLabels;
    @XmlElement
    public boolean showLegendLabels;
    @XmlElementWrapper(name = "algebraic-variables")
    @XmlElement(name = "algVar")
    public List<XmlPlotItem> algebraicVariables = new ArrayList<>();
    @XmlElementWrapper(name = "agents")
    @XmlElement(name = "agent")
    public List<XmlPlotItem> agents = new ArrayList<>();
    @XmlElementWrapper(name = "variables")
    @XmlElement(name = "variable")
    public List<String> variables = new ArrayList<>();

    public XmlPlot() {
    }

    public XmlPlot(GCDPlot plot) {
        name = plot.getName();
        legendLabel = plot.getLegendLabel();
        plotStyle = plot.getPlotStyle();
        plotParameter = plot.getPlotParameter();
        plotRange = plot.getPlotRange();
        showPlotLabels = plot.isShowPlotLabels();
        showLegendLabels = plot.isShowLegendLabels();
        for (GCDPlotItem<AlgebraicVariable> algVar : plot.getAlgebraicVariablesSorted()) {
            if (algVar.independent || algVar.isAddDepended()) {
                algebraicVariables.add(new XmlPlotItem(algVar));
            }
        }
        for (GCDPlotItem<Agent> agent : plot.getAgentsSorted()) {
            agents.add(new XmlPlotItem(agent));
        }
        for (PlotVariable v : plot.getVariablesSorted()) {
            if (v.independent) {
                variables.add(v.variable.getName());
            }
        }
    }
}