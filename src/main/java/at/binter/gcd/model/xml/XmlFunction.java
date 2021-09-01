package at.binter.gcd.model.xml;

import at.binter.gcd.model.HasPlotStyle;
import at.binter.gcd.model.PlotStyle;
import at.binter.gcd.model.elements.Agent;
import at.binter.gcd.model.elements.AlgebraicVariable;
import at.binter.gcd.model.elements.Constraint;
import at.binter.gcd.model.elements.Function;
import jakarta.xml.bind.annotation.XmlElement;

/**
 * maps and its subclasses
 */
public class XmlFunction implements HasPlotStyle {
    @XmlElement
    public String name;
    @XmlElement
    public String parameter;
    @XmlElement
    public String function;

    @XmlElement
    public String description;

    private final PlotStyle plotStyle = new PlotStyle();

    public XmlFunction() {
    }

    public XmlFunction(Function f) {
        name = f.getName();
        function = f.getFunction();
        parameter = f.getParameter();
        if (f instanceof Agent) {
            initPlotValues((Agent) f);
            description = ((Agent) f).getDescription();
        } else if (f instanceof AlgebraicVariable) {
            initPlotValues((AlgebraicVariable) f);
            description = ((AlgebraicVariable) f).getDescription();
        }
    }

    public XmlFunction(Constraint c) {
        function = c.getCondition();
        description = c.getDescription();
    }

    private void initPlotValues(HasPlotStyle p) {
        setPlotColor(p.getPlotColor());
        setPlotThickness(p.getPlotThickness());
        setPlotLineStyle(p.getPlotLineStyle());
    }

    @Override
    @XmlElement
    public String getPlotColor() {
        return plotStyle.getPlotColor();
    }

    @Override
    public void setPlotColor(String plotColor) {
        plotStyle.setPlotColor(plotColor);
    }

    @Override
    @XmlElement
    public Double getPlotThickness() {
        return plotStyle.getPlotThickness();
    }

    @Override
    public void setPlotThickness(Double plotThickness) {
        plotStyle.setPlotThickness(plotThickness);
    }

    @Override
    @XmlElement
    public String getPlotLineStyle() {
        return plotStyle.getPlotLineStyle();
    }

    @Override
    public void setPlotLineStyle(String plotLineStyle) {
        plotStyle.setPlotLineStyle(plotLineStyle);
    }

    @Override
    public boolean hasValidPlotStyle() {
        return plotStyle.hasValidPlotStyle();
    }

    public AlgebraicVariable createAlgebraicVariable() {
        AlgebraicVariable algVar = new AlgebraicVariable();
        algVar.setName(name);
        algVar.setFunction(function);
        algVar.setDescription(description);
        algVar.getPlotStyle().update(this);
        return algVar;
    }

    public Agent createAgent() {
        Agent agent = new Agent();
        agent.setName(name);
        agent.setParameter(parameter);
        agent.setFunction(function);
        agent.setDescription(description);
        agent.getPlotStyle().update(this);
        return agent;
    }

    public Constraint createConstraint() {
        Constraint constraint = new Constraint();
        constraint.setCondition(function);
        constraint.setDescription(description);
        return constraint;
    }
}