package at.binter.gcd.model.xml;

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
}