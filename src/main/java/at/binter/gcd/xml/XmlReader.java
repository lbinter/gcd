package at.binter.gcd.xml;

import at.binter.gcd.model.xml.XmlModel;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

public class XmlReader {
    private static final Logger log = LoggerFactory.getLogger(XmlReader.class);

    public static XmlModel read(File file) {
        if (file == null) {
            return null;
        }
        JAXBContext jaxbContext;
        try {
            jaxbContext = JAXBContext.newInstance(XmlModel.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

            Object obj = jaxbUnmarshaller.unmarshal(file);
            if (obj instanceof XmlModel xml) {
                xml.file = file;
                return xml;
            }
        } catch (JAXBException e) {
            log.error("Could not read model from file {}", file.getAbsolutePath(), e);
        }
        return null;
    }
}
