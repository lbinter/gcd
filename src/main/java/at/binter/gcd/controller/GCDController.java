package at.binter.gcd.controller;

import at.binter.gcd.model.GCDModel;
import at.binter.gcd.model.GCDPlot;
import at.binter.gcd.model.elements.*;
import at.binter.gcd.model.xml.XmlModel;
import at.binter.gcd.xml.XmlReader;
import at.binter.gcd.xml.XmlWriter;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.FileChooser;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;

import static at.binter.gcd.util.FileUtils.isValidGCDFile;
import static at.binter.gcd.util.GuiUtils.sanitizeString;
import static at.binter.gcd.util.GuiUtils.showInvalidFileError;
import static at.binter.gcd.util.Tools.isMousePrimaryDoubleClicked;

public class GCDController extends BaseController implements Initializable {
    private static final Logger log = LoggerFactory.getLogger(GCDController.class);
    public static FileChooser.ExtensionFilter gcdFileExt = new FileChooser.ExtensionFilter("GCD", "*.gcd");

    @FXML
    private Button buttonUndo;
    @FXML
    private Button buttonRedo;
    @FXML
    private Button buttonGCD;
    @FXML
    private TextField addPlotTextField;

    @FXML
    private TabPane mainTabPane;
    private final List<Tab> plotTabs = new ArrayList<>();

    @FXML
    private ListView<AlgebraicVariable> algVarListView;
    @FXML
    private ListView<Agent> agentListView;
    @FXML
    private ListView<Constraint> constraintListView;
    @FXML
    private ListView<Variable> variableListView;
    @FXML
    private ListView<Parameter> parameterListView;
    @FXML
    private Button changeMuButtonEdit;
    @FXML
    private ListView<ChangeMu> changeMuListView;
    @FXML
    private Label filePath;

    protected GCDModel model = new GCDModel();

    private EditDialog<AlgebraicVariable> algebraicVariableEditDialog;
    private EditDialog<Agent> agentEditDialog;
    private EditDialog<Constraint> constraintEditDialog;
    private EditDialog<Variable> variableEditDialog;
    private EditDialog<Parameter> parameterEditDialog;
    private EditDialog<ChangeMu> changeMuEditDialog;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);
        this.model = new GCDModel();
        mainTabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.SELECTED_TAB);
        algVarListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        algVarListView.setItems(model.getAlgebraicVariablesSorted());
        agentListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        agentListView.setItems(model.getAgentsSorted());
        constraintListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        constraintListView.setItems(model.getConstraints());
        variableListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        variableListView.setItems(model.getVariablesSorted());
        parameterListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        parameterListView.setItems(model.getParametersSorted());
        changeMuListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        changeMuListView.setItems(model.getChangeMus().sorted(Comparator.comparing(ChangeMu::getIdentifier)));

        registerEventHandlers();
    }

    public void initializeGCDDepended() {
        algebraicVariableEditDialog = new EditDialog<>(algVarListView, model.getAlgebraicVariables(), gcd.algebraicVariableEditorController, gcd);
        agentEditDialog = new EditDialog<>(agentListView, model.getAgents(), gcd.agentEditorController, gcd);
        constraintEditDialog = new EditDialog<>(constraintListView, model.getConstraints(), gcd.constraintEditorController, gcd);
        variableEditDialog = new EditDialog<>(variableListView, model.getVariables(), gcd.variableEditorController, gcd);
        parameterEditDialog = new EditDialog<>(parameterListView, model.getParameters(), gcd.parameterEditorController, gcd);
        changeMuEditDialog = new EditDialog<>(changeMuListView, model.getChangeMus(), gcd.changeMuEditorController, gcd);
    }

    private void registerEventHandlers() {
        algVarListView.setOnMouseClicked(event -> {
            if (isMousePrimaryDoubleClicked(event)) {
                editSelectedAlgebraicVariable();
            }
        });
        agentListView.setOnMouseClicked(event -> {
            if (isMousePrimaryDoubleClicked(event)) {
                editSelectedAgent();
            }
        });
        constraintListView.setOnMouseClicked(event -> {
            if (isMousePrimaryDoubleClicked(event)) {
                editSelectedConstraint();
            }
        });
        variableListView.setOnMouseClicked(event -> {
            if (isMousePrimaryDoubleClicked(event)) {
                editSelectedVariable();
            }
        });
        parameterListView.setOnMouseClicked(event -> {
            if (isMousePrimaryDoubleClicked(event)) {
                editSelectedParameter();
            }
        });
        changeMuListView.setOnMouseClicked(event -> {
            if (isMousePrimaryDoubleClicked(event)) {
                editSelectedChangeMu();
            }
        });

        addPlotTextField.setOnKeyPressed((KeyEvent event) -> {
            if (KeyCode.ENTER == event.getCode()) {
                addPlot();
            }
        });
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
    protected void addNewConstraint() {
        constraintEditDialog.addNewItem();
    }

    @FXML
    protected void removeSelectedConstraint() {
        constraintEditDialog.askUserRemoveAlert("constraint.name");
    }

    @FXML
    protected void editSelectedConstraint() {
        constraintEditDialog.editSelectedValue();
    }

    @FXML
    protected void editSelectedVariable() {
        variableEditDialog.editSelectedValue();
    }

    @FXML
    protected void editSelectedParameter() {
        parameterEditDialog.editSelectedValue();
    }

    @FXML
    protected void editSelectedChangeMu() {
        changeMuEditDialog.editSelectedValue();
    }

    @FXML
    protected void loadFromFile() {
        FileChooser fc = new FileChooser();
        fc.getExtensionFilters().add(gcdFileExt);
        fc.setSelectedExtensionFilter(gcdFileExt);
        // TODO fc.setInitialDirectory(new File("path"));
        File file = fc.showOpenDialog(gcd.primaryStage);
        loadModel(file);
    }

    @FXML
    protected void save() {
        if (model.getFile() == null) {
            saveAs();
        } else {
            saveModel();
        }
    }

    @FXML
    protected void saveAs() {
        File file = showSaveFileDialog();
        if (file != null) {
            if (!isValidGCDFile(file)) {
                showInvalidFileError(file);
                return;
            }
            saveModelToFile(file);
        }
    }

    @FXML
    public void revert() {
        loadModel(model.getFile());
    }

    @FXML
    public void clearModel() {
        model.clearModel();
    }

    @FXML
    protected void undo() {
        System.out.println("undo called");
        model.generateChangeMu();
    }

    @FXML
    protected void redo() {
        System.out.println("redo called");
    }

    @FXML
    protected void openMathematicaWindow() {
        gcd.mathematicaController.showMathematicaWindow();
    }

    @FXML
    protected void showHelpWindow() {
        gcd.helpController.showHelpWindow();
    }

    @FXML
    protected void addPlot() {
        String plotName = sanitizeString(addPlotTextField.getText());
        if (StringUtils.isBlank(plotName)) {
            return;
        }
        if (log.isTraceEnabled()) {
            log.trace("adding new plot with name {}", plotName);
        }
        GCDPlot newPlot = new GCDPlot(model, plotName);
        if (model.addPlot(newPlot)) {
            addPlot(newPlot);
        }
    }

    private void addPlot(GCDPlot plot) {
        FXMLLoader loader = new FXMLLoader();
        loader.setController(new PlotController());
        loader.setLocation(gcd.getClass().getResource("plot.fxml"));
        loader.setResources(resources);

        Tab tab = new Tab("Plot: " + plot.getName());

        try {
            tab.setContent(loader.load());
        } catch (IOException e) {
            log.error("could not load plot.fxml", e);
            throw new RuntimeException(e);
        }
        tab.setClosable(true);
        tab.setOnCloseRequest((Event t) -> {
            String title = gcd.getString("plot.remove.question.title");
            String message = gcd.getString("plot.remove.question.message", plot.getName());
            Alert alert = new Alert(Alert.AlertType.NONE, message, ButtonType.YES, ButtonType.NO);
            alert.setTitle(title);
            ((Button) alert.getDialogPane().lookupButton(ButtonType.YES)).setText(gcd.getString("button.yes"));
            ((Button) alert.getDialogPane().lookupButton(ButtonType.NO)).setText(gcd.getString("button.no"));
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isEmpty() || result.get() != ButtonType.YES) {
                t.consume();
            }
        });

        PlotController controller = loader.getController();
        controller.setApplication(gcd);
        controller.setParentTab(tab);
        controller.setPlotModel(plot);

        mainTabPane.getTabs().add(tab);
        plotTabs.add(tab);
        gcd.primaryStage.sizeToScene();
    }

    private File showSaveFileDialog() {
        FileChooser fc = new FileChooser();
        fc.getExtensionFilters().add(gcdFileExt);
        fc.setSelectedExtensionFilter(gcdFileExt);
        return fc.showSaveDialog(gcd.primaryStage);
    }

    private void saveModelToFile(File file) {
        if (model.getFile() != file) {
            if (log.isInfoEnabled()) {
                String old = "";
                if (model.getFile() != null) {
                    old = model.getFile().getAbsolutePath();
                }
                log.info("Setting model.file from \"{}\" to \"{}\"", old, file.getAbsolutePath());
            }
            model.setFile(file);
            setDataFromFilePath(file);
        }
        saveModel();
    }

    private void saveModel() {
        if (XmlWriter.write(model)) {
            model.setSavedToFile(true);
            setDataFromFilePath(model.getFile());
        }
    }

    private void loadModel(File file) {
        if (file != null) {
            if (!isValidGCDFile(file)) {
                showInvalidFileError(file);
                return;
            }
            if (log.isInfoEnabled()) {
                log.info("Loading gcd model from file {}", file.getAbsolutePath());
            }
            XmlModel xmlModel = XmlReader.read(file);
            clearPlots();
            model.loadXmlModel(xmlModel);
            setDataFromFilePath(file);
            for (GCDPlot p : model.getPlots()) {
                addPlot(p);
            }
        }
    }

    private void clearPlots() {
        for (Tab tab : plotTabs) {
            mainTabPane.getTabs().remove(tab);
        }
        plotTabs.clear();
    }

    private void setDataFromFilePath(File gcdFile) {
        gcd.primaryStage.setTitle(gcdFile.getName() + " - " + resources.getString("main.title"));
        filePath.setText(gcdFile.getParentFile().getAbsolutePath());
    }
}