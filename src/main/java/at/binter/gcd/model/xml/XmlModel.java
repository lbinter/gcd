package at.binter.gcd.model.xml;


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
}