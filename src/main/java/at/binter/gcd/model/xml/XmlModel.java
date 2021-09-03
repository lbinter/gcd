package at.binter.gcd.model.xml;


import at.binter.gcd.model.GCDModel;
import at.binter.gcd.model.elements.*;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElementWrapper;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "gcd-model")
public class XmlModel {
    public File file;

    @XmlElementWrapper(name = "algebraic-variables")
    @XmlElement(name = "algVar")
    public List<XmlFunction> algebraicVariables = new ArrayList<>();
    @XmlElementWrapper(name = "agents")
    @XmlElement(name = "agent")
    public List<XmlFunction> agents = new ArrayList<>();
    @XmlElementWrapper(name = "constraints")
    @XmlElement(name = "constraint")
    public List<XmlFunction> constraints = new ArrayList<>();

    @XmlElementWrapper(name = "variables")
    @XmlElement(name = "variable")
    public List<XmlVariable> variables = new ArrayList<>();
    @XmlElementWrapper(name = "parameters")
    @XmlElement(name = "parameter")
    public List<XmlBasicVariable> parameters = new ArrayList<>();
    @XmlElementWrapper(name = "change-mu")
    @XmlElement(name = "mu")
    public List<XmlBasicVariable> changeMu = new ArrayList<>();

    public XmlModel() {
    }

    public XmlModel(GCDModel gcdModel) {
        for (AlgebraicVariable algVar : gcdModel.getAlgebraicVariables().sorted()) {
            algebraicVariables.add(new XmlFunction(algVar));
        }
        for (Agent agent : gcdModel.getAgents()) {
            agents.add(new XmlFunction(agent));
        }
        for (Constraint constraint : gcdModel.getConstraints().sorted()) {
            constraints.add(new XmlFunction(constraint));
        }
        for (Variable variable : gcdModel.getVariables().sorted()) {
            variables.add(new XmlVariable(variable));
        }
        for (Parameter parameter : gcdModel.getParameters().sorted()) {
            parameters.add(new XmlBasicVariable(parameter));
        }
        for (ChangeMu mu : gcdModel.getChangeMus()) {
            if (!mu.hasNoValues()) {
                changeMu.add(new XmlBasicVariable(mu));
            }
        }
    }
}