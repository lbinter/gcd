package at.binter.gcd.model.xml;

import at.binter.gcd.model.GCDPlotItem;
import at.binter.gcd.model.elements.Function;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class XmlPlotItem {
    @XmlElement
    public boolean addDepended;
    @XmlElement
    public String name;

    public XmlPlotItem() {
    }

    public XmlPlotItem(GCDPlotItem<?> item) {
        addDepended = item.isAddDepended();
        if (item.getItem() instanceof Function) {
            name = ((Function) item.getItem()).getName();
        }
    }
}
