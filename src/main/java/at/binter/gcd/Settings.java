package at.binter.gcd;

import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.io.File;

@XmlRootElement(name = "gcd-settings")
public class Settings {
    @XmlElement(name = "jLink")
    public String jLink;
    @XmlElement(name = "mathKernel")
    public String mathKernel;
    @XmlElement
    public String defaultFolder;
    @XmlElement
    public String lastOpened;

    public Settings() {
    }

    public void loadDefaultValues() {
        jLink = "C:/Program Files/Wolfram Research/Mathematica/12.1/SystemFiles/Links/JLink";
        mathKernel = "C:/Program Files/Wolfram Research/Mathematica/12.1/MathKernel.exe";
    }

    public boolean verifyMathematicaPaths() {
        return new File(jLink).exists() && new File(mathKernel).exists();
    }
}