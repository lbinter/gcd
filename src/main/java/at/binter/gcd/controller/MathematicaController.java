package at.binter.gcd.controller;

import at.binter.gcd.gui.GCDErrorHTML;
import at.binter.gcd.mathematica.GCDMode;
import at.binter.gcd.mathematica.GCDWriterNotebook;
import at.binter.gcd.model.GCDModel;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebView;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static at.binter.gcd.util.FileUtils.nbFileExt;
import static at.binter.gcd.util.GuiUtils.addStageCloseOnEscapeKey;


public class MathematicaController extends BaseController implements Initializable {
    private static final Logger log = LoggerFactory.getLogger(MathematicaController.class);
    private GCDErrorHTML errorWriter;
    private GCDModel model;

    @FXML
    private TabPane tabPane;
    @FXML
    private TextField gcdFilePath;
    @FXML
    private TextField ndsolveFilePath;
    @FXML
    private TextField modelicaFilePath;
    @FXML
    private TextField controlFilePath;
    @FXML
    private WebView errorView;

    @FXML
    private VBox progressIndicator;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);
    }

    public void initializeGCDDepended() {
        model = gcd.gcdController.model;
        errorWriter = new GCDErrorHTML(model, resources);
    }

    public void clear() {
        errorView.getEngine().loadContent("<html></html>");
    }

    public void fillData() {
        errorView.getEngine().setUserStyleSheetLocation(gcd.errorViewCss);
        errorView.getEngine().loadContent(errorWriter.generate());

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
    void gcdSaveAs() {
        gcd.gcdController.saveAs();
        gcdFilePath.setText(model.getFilePath());
        ndsolveFilePath.setText(model.getFileMathematicaNDSolvePath());
        modelicaFilePath.setText(model.getFileMathematicaModelicaPath());
        controlFilePath.setText(model.getFileMathematicaControlPath());
    }

    @FXML
    void chooseNDSolveFile() {
        File f = showFileChooser(model.getMathematicaNDSolveFile());
        if (f != null) {
            model.setMathematicaNDSolveFile(f);
        }
    }

    @FXML
    void chooseModelicaFile() {
        File f = showFileChooser(model.getMathematicaModelicaFile());
        if (f != null) {
            model.setMathematicaModelicaFile(f);
        }
    }

    @FXML
    void chooseControlFile() {
        File f = showFileChooser(model.getMathematicaControlFile());
        if (f != null) {
            model.setMathematicaControlFile(f);
        }
    }

    @FXML
    synchronized void generateNDSolveFile() {
        if (model.getMathematicaNDSolveFile() != null) {
            new Thread(createFileGenerationTask(GCDMode.NDSOLVE, model.getMathematicaNDSolveFile())).start();
        }
    }

    @FXML
    synchronized void generateModelicaFile() {
        if (model.getMathematicaModelicaFile() != null) {
            new Thread(createFileGenerationTask(GCDMode.MODELICA, model.getMathematicaModelicaFile())).start();
        }
    }

    @FXML
    synchronized void generateControlFile() {
        if (model.getMathematicaControlFile() != null) {
            new Thread(createFileGenerationTask(GCDMode.CONTROL, model.getMathematicaControlFile())).start();
        }
    }

    @FXML
    void openNDSolveFile() {
        openFile(model.getMathematicaNDSolveFile());
    }

    @FXML
    void openModelicaFile() {
        openFile(model.getMathematicaModelicaFile());
    }

    @FXML
    void openControlFile() {
        openFile(model.getMathematicaControlFile());
    }

    @FXML
    void generateAllNBFiles() {
        generateNDSolveFile();
        generateModelicaFile();
        generateControlFile();
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

    private void openFile(File file) {
        if (file == null || !file.exists() || file.isDirectory()) {
            return;
        }
        try {
            Desktop.getDesktop().open(file);
        } catch (IOException | IllegalArgumentException e) {
            log.error("Could not open file {}", file, e);
        }
    }

    private void showProgress(boolean showProgressIndicator) {
        progressIndicator.setVisible(showProgressIndicator);
        tabPane.setDisable(showProgressIndicator);
    }

    private Task<Boolean> createFileGenerationTask(GCDMode mode, File file) {
        Task<Boolean> task = new Task<>() {
            @Override
            protected Boolean call() throws Exception {
                return new GCDWriterNotebook(model, mode, gcd.utils).writeToFile();
            }
        };
        task.setOnRunning(t -> showProgress(true));
        task.setOnSucceeded(t -> {
            showProgress(false);
            if (task.getValue()) {
                openFile(file);
            }
        });
        task.setOnCancelled(t -> showProgress(false));
        task.setOnFailed(t -> {
            showProgress(false);
            Throwable e = t.getSource().getException();
            if (e.getMessage().contains("The process cannot access the file because it is being used by another process")) {
                log.error("File already opened");
            }
            log.error("Could not create file {}", file.getName(), t.getSource().getException());
        });
        return task;
    }
}