package at.binter.gcd.controller;

import at.binter.gcd.model.GCDModel;
import at.binter.gcd.model.elements.Agent;
import at.binter.gcd.model.elements.AlgebraicVariable;
import at.binter.gcd.model.xml.XmlFunction;
import at.binter.gcd.model.xml.XmlModel;
import at.binter.gcd.xml.XmlReader;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
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
    private ListView<AlgebraicVariable> algVarListView;

    @FXML
    private Button agentButtonAdd;

    @FXML
    private Button agentButtonRemove;

    @FXML
    private Button agentButtonEdit;

    @FXML
    private ListView<Agent> agentListView;

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


    private GCDModel model = new GCDModel();

    private EditDialog<AlgebraicVariable> algebraicVariableEditDialog;
    private EditDialog<Agent> agentEditDialog;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);
        this.model = new GCDModel();
        algVarListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        algVarListView.setItems(model.getAlgebraicVariables().sorted());
        agentListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        agentListView.setItems(model.getAgents().sorted());
        constraintListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        constraintListView.setItems(model.getConstraints().sorted());
        variableListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        variableListView.setItems(model.getVariables().sorted());
        parameterListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        parameterListView.setItems(model.getParameters().sorted());
        changeMuListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        changeMuListView.setItems(model.getChangeMus().sorted());

        registerEventHandlers();
    }

    public void initializeGcdDepended() {
        algebraicVariableEditDialog = new EditDialog<>(algVarListView, model.getAlgebraicVariables(), gcd.algebraicVariableEditorController, gcd);
        agentEditDialog = new EditDialog<>(agentListView, model.getAgents(), gcd.agentEditorController, gcd);
    }

    private void registerEventHandlers() {

        constraintButtonAdd.setOnAction(event -> gcd.constraintEditorController.createEditor());
        constraintButtonEdit.setOnAction(event -> {
            gcd.constraintEditorController.createEditor(event);
            // TODO: fill with data from selected value
        });
        constraintButtonRemove.setOnAction(event -> gcd.askUserRemoveAlert(constraintListView, model.getConstraints(), "constraint.name"));


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

        algVarListView.setOnMouseClicked(this::editSelectedAlgebraicVariable);
    }

    @FXML
    protected void addNewAlgebraicVariable() {
        algebraicVariableEditDialog.addNewItem();
    }

    @FXML
    protected void removeSelectedAlgebraicVariable() {
        algebraicVariableEditDialog.askUserRemoveAlert("algebraicVariables.name");
    }

    @FXML
    protected void editSelectedAlgebraicVariable() {
        algebraicVariableEditDialog.editSelectedValue();
    }

    @FXML
    protected void editSelectedAlgebraicVariable(MouseEvent event) {
        if (event != null && event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
            editSelectedAlgebraicVariable();
        }
    }

    @FXML
    protected void addNewAgent() {
        agentEditDialog.addNewItem();
    }


    @FXML
    protected void removeSelectedAgent() {
        agentEditDialog.askUserRemoveAlert("agent.name");
    }

    @FXML
    protected void editSelectedAgent() {
        agentEditDialog.editSelectedValue();
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
                for (XmlFunction algVar : xmlModel.algebraicVariables) {
                    AlgebraicVariable newAlgVar = new AlgebraicVariable();
                    newAlgVar.setFunction(algVar.function);
                    model.getAlgebraicVariables().add(newAlgVar);
                }
                for (XmlFunction agent : xmlModel.agents) {
                    Agent newAgent = new Agent();
                    newAgent.setFunction(agent.function);
                    model.getAgents().add(newAgent);
                }
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