package at.binter.gcd.xml;

import at.binter.gcd.Settings;
import at.binter.gcd.model.GCDModel;
import at.binter.gcd.model.xml.XmlModel;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

import static at.binter.gcd.util.FileUtils.isValidGCDFile;
import static at.binter.gcd.util.FileUtils.isValidSettingsFile;

public class XmlWriter {
    private static final Logger log = LoggerFactory.getLogger(XmlWriter.class);

    public static boolean write(GCDModel gcdModel) {
        return write(new XmlModel(gcdModel), gcdModel.getFile());
    }

    public static boolean write(XmlModel xmlModel, File file) {
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(XmlModel.class);
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            if (file == null) {
                jaxbMarshaller.marshal(xmlModel, System.out);
                return false;
            } else if (file.isDirectory()) {
                log.error("Could not save gcd model to directory {} - must be a file", file.getAbsolutePath());
                return false;
            } else {
                if (!file.exists()) {
                    if (!file.getName().endsWith(".gcd")) {
                        file = new File(file.getAbsolutePath() + ".gcd");
                    }
                }
                if (isValidGCDFile(file)) {
                    log.info("Saving model to file {}", file.getAbsolutePath());
                    jaxbMarshaller.marshal(xmlModel, file);
                    log.info("GCD Model saved!");
                    return true;
                } else {
                    return false;
                }
            }
        } catch (JAXBException e) {
            if (file != null) {
                log.error("Could not save gcd model to file {}", file.getAbsolutePath(), e);
            } else {
                log.error("Could not create xml model string", e);
            }
            return false;
        }
    }

    public static boolean write(Settings settings, File file) {
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(Settings.class);
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            if (file == null) {
                jaxbMarshaller.marshal(settings, System.out);
                return false;
            } else if (file.isDirectory()) {
                log.error("Could not save gcd model to directory {} - must be a file", file.getAbsolutePath());
                return false;
            } else {
                if (isValidSettingsFile(file)) {
                    log.info("Saving gcd settings to file {}", file.getAbsolutePath());
                    jaxbMarshaller.marshal(settings, file);
                    log.info("Settings saved!");
                    return true;
                } else {
                    return false;
                }
            }
        } catch (JAXBException e) {
            if (file != null) {
                log.error("Could not save gcd settings to file {}", file, e);
            } else {
                log.error("Could not create settings string", e);
            }
            return false;
        }
    }
}