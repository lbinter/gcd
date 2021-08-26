package at.binter.gcd.controller;

import at.binter.gcd.model.xml.XmlModel;
import at.binter.gcd.xml.XmlReader;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.stage.FileChooser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

import static at.binter.gcd.util.FileUtils.isValidGCDFile;


public class GCDController extends BaseController implements Initializable {
    private static final Logger log = LoggerFactory.getLogger(GCDController.class);
    public static FileChooser.ExtensionFilter gcdFileExt = new FileChooser.ExtensionFilter("GCD", "*.gcd");

    @FXML
    private Button algVarButtonAdd;
    @FXML
    private Button algVarButtonRemove;
    @FXML
    private Button algVarButtonEdit;
    @FXML
    private ListView<String> algVarList;


    private ObservableList<String> list;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);
        list = FXCollections.observableArrayList();
        genItems();
        algVarList.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        algVarList.setItems(list);

        registerEventHandlers();

    }

    private void registerEventHandlers() {
        algVarButtonAdd.setOnAction(event -> gcd.algebraicVariableEditorController.createEditor());
        algVarButtonEdit.setOnAction(event -> {
            gcd.algebraicVariableEditorController.createEditor(event);
            // TODO: fill with data from selected value
        });
        algVarButtonRemove.setOnAction(event -> gcd.askUserRemoveAlert(algVarList, list, "algebraicVariables.name"));
    }

    private void genItems() {
        for (int i = 0; i < 30; i++) {
            list.add("Item_" + i);
        }
        list.add("Item_öouiafhdgüoahdgfoüäadhfgüäadohfgad+üpfhgpädafjgpd+a#fojgpda+#jfgp+#adfjgp+#");
    }

    @FXML
    protected void openFileChooser() {
        System.out.println("openFileChooser called");
        FileChooser fc = new FileChooser();
        fc.getExtensionFilters().add(gcdFileExt);
        fc.setSelectedExtensionFilter(gcdFileExt);
        // TODO fc.setInitialDirectory(new File("path"));
        File file = fc.showOpenDialog(gcd.primaryStage);
        if (file != null) {
            if (!isValidGCDFile(file)) {
                showInvalidFileError(file);
                return;
            }
            if (log.isInfoEnabled()) {
                log.info("Loading gcd model from file {}", file.getAbsolutePath());
            }
            XmlModel xmlModel = XmlReader.read(file);
            if (xmlModel != null) {
                // TODO load xml to gcd model
                // model.loadXMLModel(xmlModel);
            }
        } else {
            System.out.println("no file selected");
        }
    }

    private void showInvalidFileError(File file) {
        /*GuiError.showError(
                frame,
                file.getAbsolutePath() + ": " + SwingI18n.getString("error.invalid.file.message"),
                SwingI18n.getString("error.invalid.file.title")
        );*/
    }

    @FXML
    protected void save() {
        System.out.println("saveModel called");
    }

    @FXML
    protected void undo() {
        System.out.println("undo called");
    }

    @FXML
    protected void redo() {
        System.out.println("redo called");
    }

    @FXML
    protected void openGCDOutput() {
        System.out.println("openGCDOutput called");
    }

    @FXML
    protected void showHelpWindow() {
        System.out.println("showHelpWindow called");
        gcd.helpController.showHelpWindow();
    }
}