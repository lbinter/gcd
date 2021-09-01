package at.binter.gcd.model.xml;

import at.binter.gcd.model.HasMinMaxValues;
import at.binter.gcd.model.elements.Agent;
import at.binter.gcd.model.elements.ChangeMu;
import at.binter.gcd.model.elements.Parameter;
import at.binter.gcd.model.elements.Variable;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

/**
 * maps and its subclasses
 */
@XmlRootElement
public class XmlBasicVariable {
    @XmlElement
    public String name;
    @XmlElement
    public String description;
    @XmlElement
    public Double startValue;
    @XmlElement
    public Double minValue;
    @XmlElement
    public Double maxValue;

    public XmlBasicVariable() {
    }

    public XmlBasicVariable(Variable variable) {
        name = variable.getName();
        description = variable.getDescription();
        initMinMaxValues(variable);
    }


    public XmlBasicVariable(Parameter parameter) {
        name = parameter.getName();
        description = parameter.getDescription();
        initMinMaxValues(parameter);
    }

    public XmlBasicVariable(ChangeMu mu) {
        name = mu.getIdentifier();
        initMinMaxValues(mu);
    }

    public void initMinMaxValues(HasMinMaxValues v) {
        startValue = v.getStartValue();
        minValue = v.getMinValue();
        maxValue = v.getMaxValue();
    }


    public Parameter createParameter() {
        Parameter p = new Parameter(name);
        p.setDescription(description);
        writeMinMaxValues(p);
        return p;
    }

    public ChangeMu createChangeMu(Agent agent, Variable variable, int index) {
        ChangeMu mu = new ChangeMu(agent, variable, index);
        writeMinMaxValues(mu);
        return mu;
    }

    protected void writeMinMaxValues(HasMinMaxValues v) {
        if (startValue != null) {
            v.setStartValue(startValue);
        }
        if (minValue != null) {
            v.setMinValue(minValue);
        }
        if (maxValue != null) {
            v.setMaxValue(maxValue);
        }
    }
}