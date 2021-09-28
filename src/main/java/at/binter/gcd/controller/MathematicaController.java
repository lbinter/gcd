package at.binter.gcd.controller;

import at.binter.gcd.mathematica.GCDMode;
import at.binter.gcd.mathematica.GCDWriterHTML;
import at.binter.gcd.mathematica.GCDWriterNotebook;
import at.binter.gcd.model.GCDModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.web.WebView;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

import static at.binter.gcd.util.GuiUtils.addStageCloseOnEscapeKey;


public class MathematicaController extends BaseController implements Initializable {
    private static final Logger log = LoggerFactory.getLogger(MathematicaController.class);
    public static FileChooser.ExtensionFilter nbFileExt = new FileChooser.ExtensionFilter("Mathematica", "*.nb");
    private GCDWriterHTML htmlWriter;
    private GCDModel model;

    @FXML
    private TextField gcdFilePath;
    @FXML
    private TextField ndsolveFilePath;
    @FXML
    private TextField modelicaFilePath;
    @FXML
    private TextField controlFilePath;
    @FXML
    private WebView mathematicaCode;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);
    }

    public void initializeGCDDepended() {
        model = gcd.gcdController.model;
        htmlWriter = new GCDWriterHTML(model, GCDMode.NDSOLVE);
    }

    public void clear() {
        mathematicaCode.getEngine().loadContent("<html></html>");
    }

    public void fillData() {
        mathematicaCode.getEngine().setUserStyleSheetLocation(gcd.mathematicaCss);
        mathematicaCode.getEngine().loadContent(htmlWriter.toString());

        gcdFilePath.setText(model.getFilePath());
        ndsolveFilePath.setText(model.getFileMathematicaNDSolvePath());
        modelicaFilePath.setText(model.getFileMathematicaModelicaPath());
        controlFilePath.setText(model.getFileMathematicaControlPath());
    }

    public void showMathematicaWindow() {
        clear();
        Stage popup = new Stage();
        popup.setScene(gcd.mathematicaScene);
        popup.setTitle(resources.getString("mathematica.window.title"));
        popup.initOwner(gcd.primaryStage);
        popup.initModality(Modality.WINDOW_MODAL);
        addStageCloseOnEscapeKey(popup, gcd.mathematicaScene);
        fillData();
        popup.showAndWait();
    }

    @FXML
    void chooseNDSolveFile(ActionEvent event) {
        File f = showFileChooser(model.getMathematicaNDSolveFile());
        if (f != null) {
            model.setMathematicaNDSolveFile(f);
        }
    }

    @FXML
    void chooseModelicaFile(ActionEvent event) {
        File f = showFileChooser(model.getMathematicaModelicaFile());
        if (f != null) {
            model.setMathematicaModelicaFile(f);
        }
    }

    @FXML
    void chooseControlFile(ActionEvent event) {
        File f = showFileChooser(model.getMathematicaControlFile());
        if (f != null) {
            model.setMathematicaControlFile(f);
        }
    }

    @FXML
    void generateNDSolveFile(ActionEvent event) {
        GCDWriterNotebook ndSolveWriter = new GCDWriterNotebook(model, GCDMode.NDSOLVE);
        ndSolveWriter.writeToFile();
    }

    @FXML
    void generateModelicaFile(ActionEvent event) {
        GCDWriterNotebook modelicaWriter = new GCDWriterNotebook(model, GCDMode.MODELICA);
        modelicaWriter.writeToFile();
    }

    @FXML
    void generateControlFile(ActionEvent event) {
        GCDWriterNotebook controlWriter = new GCDWriterNotebook(model, GCDMode.CONTROL);
        controlWriter.writeToFile();
    }

    @FXML
    void generateAllNBFiles(ActionEvent event) {
        generateNDSolveFile(event);
        generateModelicaFile(event);
        generateControlFile(event);
    }

    private File showFileChooser(File currentFile) {
        if (currentFile != null) {
            return showFileChooser(currentFile.getParentFile(), currentFile.getName());
        } else {
            return showFileChooser(null, null);
        }
    }

    private File showFileChooser(File initialDirectory, String initialFileName) {
        FileChooser fc = new FileChooser();
        if (initialDirectory != null) {
            fc.setInitialDirectory(initialDirectory);
        }
        if (StringUtils.isNotBlank(initialFileName)) {
            fc.setInitialFileName(initialFileName);
        }
        fc.getExtensionFilters().add(nbFileExt);
        fc.setSelectedExtensionFilter(nbFileExt);
        return fc.showSaveDialog(gcd.primaryStage);
    }
}