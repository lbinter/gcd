package at.binter.gcd.controller;

import at.binter.gcd.GCDApplication;
import at.binter.gcd.Settings;
import at.binter.gcd.xml.XmlWriter;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

import static at.binter.gcd.Settings.isValidJLinkPath;
import static at.binter.gcd.Settings.isValidMathKernelPath;
import static at.binter.gcd.util.FileUtils.mathKernelExt;
import static at.binter.gcd.util.GuiUtils.*;

public class SettingsController extends BaseController implements Initializable {
    private static final Logger log = LoggerFactory.getLogger(SettingsController.class);

    private Settings settings;
    @FXML
    private TextField jLink;
    @FXML
    private TextField mathKernel;
    @FXML
    private TextField defaultFolder;
    @FXML
    private TextField lastOpened;

    boolean jLinkChanged = false;
    boolean mathKernelChanged = false;
    boolean defaultFolderChanged = false;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);
    }

    @Override
    public void setApplication(GCDApplication gcd) {
        super.setApplication(gcd);
        settings = gcd.settings;
        addStageCloseOnEscapeKey(gcd.settingsStage, gcd.settingsScene);
        jLink.textProperty().addListener(event -> jLink.pseudoClassStateChanged(errorClass, !isValidJLinkPath(jLink.getText())));
        mathKernel.textProperty().addListener(event -> mathKernel.pseudoClassStateChanged(errorClass, !isValidMathKernelPath(mathKernel.getText())));
    }

    public void loadSettings() {
        jLink.setText(settings.jLink);
        mathKernel.setText(settings.mathKernel);
        defaultFolder.setText(settings.defaultFolder);
        lastOpened.setText(settings.lastOpened);
        jLinkChanged = false;
        mathKernelChanged = false;
        defaultFolderChanged = false;
    }

    @FXML
    void chooseDefaultFolder(ActionEvent event) {
        DirectoryChooser fc = new DirectoryChooser();
        fc.setTitle(gcd.getString("settings.mathematica.jlink.title"));
        fc.setInitialDirectory(new File("C:/Program Files/Wolfram Research/Mathematica"));
        File file = fc.showDialog(gcd.settingsStage);
        if (file != null && file.exists() && file.isDirectory()) {
            jLink.setText(file.getAbsolutePath());
            defaultFolderChanged = true;
        }
    }

    @FXML
    void chooseJLinkFolder(ActionEvent event) {
        DirectoryChooser fc = new DirectoryChooser();
        fc.setTitle(gcd.getString("settings.mathematica.jlink.title"));
        fc.setInitialDirectory(new File("C:/Program Files/Wolfram Research/Mathematica"));
        File file = fc.showDialog(gcd.settingsStage);
        if (file != null && file.exists() && file.isDirectory()) {
            jLink.setText(file.getAbsolutePath());
            jLinkChanged = true;
            if (!"JLink".equals(file.getName())) {
                showWarning("warning.mathematica.jlink.title", "warning.mathematica.jlink.message", file.getName());
            }
        }
    }

    @FXML
    void chooseMathKernel(ActionEvent event) {
        FileChooser fc = new FileChooser();
        fc.setTitle(gcd.getString("settings.mathematica.math.kernel.title"));
        fc.getExtensionFilters().add(mathKernelExt);
        fc.setSelectedExtensionFilter(mathKernelExt);
        fc.setInitialDirectory(new File("C:/Program Files/Wolfram Research/Mathematica"));
        File file = fc.showOpenDialog(gcd.settingsStage);
        if (file != null && file.exists() && "MathKernel.exe".equals(file.getName())) {
            mathKernel.setText(file.getAbsolutePath());
            mathKernelChanged = true;
        }
    }

    @FXML
    void saveSettings(ActionEvent event) {
        String jLinkPath = sanitizeString(jLink.getText());
        String mathKernelPath = sanitizeString(mathKernel.getText());
        String defaultFolderPath = sanitizeString(defaultFolder.getText());
        if (jLinkChanged) {
            settings.jLink = jLinkPath;
            log.info("Setting jLink = {}", settings.jLink);
        }
        if (mathKernelChanged) {
            settings.mathKernel = mathKernelPath;
            log.info("Setting mathKernel = {}", settings.mathKernel);
        }
        if (defaultFolderChanged) {
            settings.defaultFolder = defaultFolderPath;
            log.info("Setting defaultFolder = {}", settings.defaultFolder);
        }
        XmlWriter.write(settings, gcd.settingsLocation.toFile());
        close(event);
    }

    @FXML
    void close(ActionEvent event) {
        gcd.settingsStage.close();
    }
}