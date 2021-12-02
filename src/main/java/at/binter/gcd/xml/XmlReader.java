package at.binter.gcd.xml;

import at.binter.gcd.Settings;
import at.binter.gcd.model.plotstyle.GCDPlotStyles;
import at.binter.gcd.model.xml.XmlModel;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.InputStream;

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

    public static Settings readSettings(File file) {
        if (file == null) {
            return null;
        }
        JAXBContext jaxbContext;
        try {
            jaxbContext = JAXBContext.newInstance(Settings.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

            Object obj = jaxbUnmarshaller.unmarshal(file);
            if (obj instanceof Settings xml) {
                log.info("Loaded gcd settings from {}", file.getAbsolutePath());
                return xml;
            }
        } catch (JAXBException e) {
            log.error("Could not read gcd settings from file {}", file.getAbsolutePath(), e);
        }
        return null;
    }

    public static GCDPlotStyles readGCDPlotStyles(File file) {
        if (file == null) {
            return null;
        }
        JAXBContext jaxbContext;
        try {
            jaxbContext = JAXBContext.newInstance(GCDPlotStyles.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

            Object obj = jaxbUnmarshaller.unmarshal(file);
            if (obj instanceof GCDPlotStyles plotStyles) {
                log.info("Loaded gcd plot styles from {}", file.getAbsolutePath());
                return plotStyles;
            }
        } catch (JAXBException e) {
            log.error("Could not read gcd plot styles from file {}", file.getAbsolutePath(), e);
        }
        return null;
    }

    public static GCDPlotStyles readGCDPlotStyles(InputStream resourceAsStream) {
        if (resourceAsStream == null) {
            return null;
        }
        JAXBContext jaxbContext;
        try {
            jaxbContext = JAXBContext.newInstance(GCDPlotStyles.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();

            Object obj = jaxbUnmarshaller.unmarshal(resourceAsStream);
            if (obj instanceof GCDPlotStyles plotStyles) {
                log.info("Loaded gcd default plot styles");
                return plotStyles;
            }
        } catch (JAXBException e) {
            log.error("Could not read gcd plot styles from default file", e);
        }
        return null;
    }
}
