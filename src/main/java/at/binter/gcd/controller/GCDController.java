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
    private Button buttonSave;

    @FXML
    private Button buttonUndo;

    @FXML
    private Button buttonRedo;

    @FXML
    private Button buttonGCD;

    @FXML
    private Button buttonHelp;

    @FXML
    private Button algVarButtonAdd;

    @FXML
    private Button algVarButtonRemove;

    @FXML
    private Button algVarButtonEdit;

    @FXML
    private ListView<String> algVarListView;

    @FXML
    private Button agentButtonAdd;

    @FXML
    private Button agentButtonRemove;

    @FXML
    private Button agentButtonEdit;

    @FXML
    private ListView<String> agentListView;

    @FXML
    private Button constraintButtonAdd;

    @FXML
    private Button constraintButtonRemove;

    @FXML
    private Button constraintButtonEdit;

    @FXML
    private ListView<String> constraintListView;

    @FXML
    private Button variableButtonEdit;

    @FXML
    private ListView<String> variableListView;

    @FXML
    private Button parameterButtonEdit;

    @FXML
    private ListView<String> parameterListView;

    @FXML
    private Button changeMuButtonEdit;

    @FXML
    private ListView<String> changeMuListView;


    private final ObservableList<String> algVarList = FXCollections.observableArrayList();
    private final ObservableList<String> agentList = FXCollections.observableArrayList();
    private final ObservableList<String> constraintList = FXCollections.observableArrayList();
    private final ObservableList<String> variableList = FXCollections.observableArrayList();
    private final ObservableList<String> parameterList = FXCollections.observableArrayList();
    private final ObservableList<String> changeMuList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);
        fillListWithItems(algVarList, 30);
        fillListWithItems(agentList, 30);
        fillListWithItems(constraintList, 30);
        fillListWithItems(variableList, 30);
        fillListWithItems(parameterList, 30);
        fillListWithItems(changeMuList, 30);
        algVarListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        algVarListView.setItems(algVarList);
        agentListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        agentListView.setItems(agentList);
        constraintListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        constraintListView.setItems(constraintList);
        variableListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        variableListView.setItems(variableList);
        parameterListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        parameterListView.setItems(parameterList);
        changeMuListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        changeMuListView.setItems(changeMuList);

        registerEventHandlers();

    }

    private void registerEventHandlers() {
        algVarButtonAdd.setOnAction(event -> gcd.algebraicVariableEditorController.createEditor());
        algVarButtonEdit.setOnAction(event -> {
            gcd.algebraicVariableEditorController.createEditor(event);
            // TODO: fill with data from selected value
        });
        algVarButtonRemove.setOnAction(event -> gcd.askUserRemoveAlert(algVarListView, algVarList, "algebraicVariables.name"));

        agentButtonAdd.setOnAction(event -> gcd.agentEditorController.createEditor());
        agentButtonEdit.setOnAction(event -> {
            gcd.agentEditorController.createEditor(event);
            // TODO: fill with data from selected value
        });
        agentButtonRemove.setOnAction(event -> gcd.askUserRemoveAlert(agentListView, agentList, "agent.name"));

        constraintButtonAdd.setOnAction(event -> gcd.constraintEditorController.createEditor());
        constraintButtonEdit.setOnAction(event -> {
            gcd.constraintEditorController.createEditor(event);
            // TODO: fill with data from selected value
        });
        constraintButtonRemove.setOnAction(event -> gcd.askUserRemoveAlert(constraintListView, constraintList, "constraint.name"));


        variableButtonEdit.setOnAction(event -> {
            gcd.variableEditorController.createEditor(event);
            // TODO: fill with data from selected value
        });

        parameterButtonEdit.setOnAction(event -> {
            gcd.parameterEditorController.createEditor(event);
            // TODO: fill with data from selected value
        });

        changeMuButtonEdit.setOnAction(event -> {
            gcd.changeMuEditorController.createEditor(event);
            // TODO: fill with data from selected value
        });
    }

    private void fillListWithItems(ObservableList<String> list, int count) {
        for (int i = 0; i < count; i++) {
            list.add("Item_" + i);
        }
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