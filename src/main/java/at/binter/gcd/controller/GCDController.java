package at.binter.gcd.controller;

import at.binter.gcd.model.GCDModel;
import at.binter.gcd.model.GCDPlot;
import at.binter.gcd.model.Status;
import at.binter.gcd.model.elements.*;
import at.binter.gcd.model.xml.XmlModel;
import at.binter.gcd.xml.XmlReader;
import at.binter.gcd.xml.XmlWriter;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import static at.binter.gcd.util.FileUtils.*;
import static at.binter.gcd.util.GuiUtils.*;
import static at.binter.gcd.util.Tools.isMousePrimaryDoubleClicked;

public class GCDController extends BaseController implements Initializable {
    private static final Logger log = LoggerFactory.getLogger(GCDController.class);

    @FXML
    private Menu recentlyOpened;
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
    @FXML
    private Circle modelStatusIndicator;

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

        algVarListView.setCellFactory(f -> new ListCell<>() {
            @Override
            protected void updateItem(AlgebraicVariable item, boolean empty) {
                super.updateItem(item, empty);
                getStyleClass().remove(Status.VALID.cssClass);
                getStyleClass().remove(Status.VALID_HAS_VALUES.cssClass);
                getStyleClass().remove(Status.VALID_HAS_FUNCTION.cssClass);
                getStyleClass().remove(Status.VALID_AUTOMATIC.cssClass);
                getStyleClass().remove(Status.INVALID.cssClass);
                if (item == null || empty) {
                    setText(null);
                } else {
                    if (log.isTraceEnabled()) {
                        log.trace("Setting algVar status: {}", item.getStatus().pseudoClass.getPseudoClassName());
                    }
                    setText(item.toString());
                    getStyleClass().add(item.getStatus().cssClass);
                }
            }
        });

        agentListView.setCellFactory(f -> new ListCell<>() {
            @Override
            protected void updateItem(Agent item, boolean empty) {
                super.updateItem(item, empty);
                getStyleClass().remove(Status.VALID.cssClass);
                getStyleClass().remove(Status.VALID_HAS_VALUES.cssClass);
                getStyleClass().remove(Status.VALID_HAS_FUNCTION.cssClass);
                getStyleClass().remove(Status.VALID_AUTOMATIC.cssClass);
                getStyleClass().remove(Status.INVALID.cssClass);
                if (item == null || empty) {
                    setText(null);
                } else {
                    if (log.isTraceEnabled()) {
                        log.trace("Setting agent status: {} -> {}", item.getName(), item.getStatus().cssClass);
                    }
                    setText(item.toString());
                    getStyleClass().add(item.getStatus().cssClass);
                }
            }
        });

        constraintListView.setCellFactory(f -> new ListCell<>() {
            @Override
            protected void updateItem(Constraint item, boolean empty) {
                super.updateItem(item, empty);
                getStyleClass().remove(Status.VALID.cssClass);
                getStyleClass().remove(Status.VALID_HAS_VALUES.cssClass);
                getStyleClass().remove(Status.VALID_HAS_FUNCTION.cssClass);
                getStyleClass().remove(Status.VALID_AUTOMATIC.cssClass);
                getStyleClass().remove(Status.INVALID.cssClass);
                if (item == null || empty) {
                    setText(null);
                } else {
                    if (log.isTraceEnabled()) {
                        log.trace("Setting constraint status: {} -> {}", item.getName(), item.getStatus().cssClass);
                    }
                    setText(item.toString());
                    getStyleClass().add(item.getStatus().cssClass);
                }
            }
        });

        variableListView.setCellFactory(f -> new ListCell<>() {
            @Override
            protected void updateItem(Variable item, boolean empty) {
                super.updateItem(item, empty);
                getStyleClass().remove(Status.VALID.cssClass);
                getStyleClass().remove(Status.VALID_HAS_VALUES.cssClass);
                getStyleClass().remove(Status.VALID_HAS_FUNCTION.cssClass);
                getStyleClass().remove(Status.VALID_AUTOMATIC.cssClass);
                getStyleClass().remove(Status.INVALID.cssClass);
                if (item == null || empty) {
                    setText(null);
                } else {
                    if (log.isTraceEnabled()) {
                        log.trace("Setting variable status: {} -> {}", item.getName(), item.getStatus().cssClass);
                    }
                    setText(item.toString());
                    getStyleClass().add(item.getStatus().cssClass);
                }
            }
        });

        parameterListView.setCellFactory(f -> new ListCell<>() {
            @Override
            protected void updateItem(Parameter item, boolean empty) {
                super.updateItem(item, empty);
                getStyleClass().remove(Status.VALID.cssClass);
                getStyleClass().remove(Status.VALID_HAS_VALUES.cssClass);
                getStyleClass().remove(Status.VALID_HAS_FUNCTION.cssClass);
                getStyleClass().remove(Status.VALID_AUTOMATIC.cssClass);
                getStyleClass().remove(Status.INVALID.cssClass);
                if (item == null || empty) {
                    setText(null);
                } else {
                    if (log.isTraceEnabled()) {
                        log.trace("Setting parameter status: {} -> {}", item.getName(), item.getStatus().cssClass);
                    }
                    setText(item.toString());
                    getStyleClass().add(item.getStatus().cssClass);
                }
            }
        });
        changeMuListView.setCellFactory(f -> new ListCell<>() {
            @Override
            protected void updateItem(ChangeMu item, boolean empty) {
                super.updateItem(item, empty);
                getStyleClass().remove(Status.VALID.cssClass);
                getStyleClass().remove(Status.VALID_HAS_VALUES.cssClass);
                getStyleClass().remove(Status.VALID_HAS_FUNCTION.cssClass);
                getStyleClass().remove(Status.VALID_AUTOMATIC.cssClass);
                getStyleClass().remove(Status.INVALID.cssClass);
                if (item == null || empty) {
                    setText(null);
                } else {
                    if (log.isTraceEnabled()) {
                        log.trace("Setting changemu status: {} -> {}", item.getIdentifier(), item.getStatus().cssClass);
                    }
                    setText(item.toString());
                    getStyleClass().add(item.getStatus().cssClass);
                }
            }
        });

        registerEventHandlers();
    }

    public void initializeGCDDepended() {
        algebraicVariableEditDialog = new EditDialog<>(algVarListView, model.getAlgebraicVariables(), gcd.algebraicVariableEditorController, gcd);
        agentEditDialog = new EditDialog<>(agentListView, model.getAgents(), gcd.agentEditorController, gcd);
        constraintEditDialog = new EditDialog<>(constraintListView, model.getConstraints(), gcd.constraintEditorController, gcd);
        variableEditDialog = new EditDialog<>(variableListView, model.getVariables(), gcd.variableEditorController, gcd);
        parameterEditDialog = new EditDialog<>(parameterListView, model.getParameters(), gcd.parameterEditorController, gcd);
        changeMuEditDialog = new EditDialog<>(changeMuListView, model.getChangeMus(), gcd.changeMuEditorController, gcd);
        populateRecentlyOpened();
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

        model.savedToFileProperty().addListener((observable, oldValue, newValue) -> {
            if (model.getFile() != null) {
                setDataFromFilePath(model.getFile());
                setModelIndicator(newValue);
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
        if (gcd.settings.lastOpened != null) {
            File lastOpened = new File(gcd.settings.lastOpened);
            if (isValidGCDFile(lastOpened)) {
                fc.setInitialDirectory(lastOpened.getParentFile());
            }
        }
        File file = fc.showOpenDialog(gcd.primaryStage);
        openFile(file);
    }

    private void openFile(File file) {
        if (file == null) {
            return;
        }
        loadModel(file);
        gcd.settings.addRecentlyOpened(file);
        gcd.settings.lastOpened = file.getAbsolutePath();
        gcd.settingsController.saveSettings(null);
        populateRecentlyOpened();
    }

    void populateRecentlyOpened() {
        recentlyOpened.getItems().clear();
        for (String path : gcd.settings.recentlyOpened) {
            File file = new File(path);
            Path filePath = Paths.get(path);
            Path driveLetter = filePath.getRoot();
            Path parent = filePath.getParent();

            StringBuilder displayText = new StringBuilder();
            if (parent != null) {
                if (parent.getParent() != null) {
                    displayText.append(driveLetter.toString());
                    displayText.append("..");
                    displayText.append(fileSeparator);
                    displayText.append(parent.getFileName().toString());
                    displayText.append(fileSeparator);
                } else {
                    displayText.append(driveLetter.toString());
                }
            }
            displayText.append(filePath.getFileName().toString());

            MenuItem item = new MenuItem(displayText.toString());
            item.setOnAction(event -> openFile(file));
            recentlyOpened.getItems().add(item);
        }
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
    protected boolean saveAs() {
        File file = showSaveFileDialog();
        if (file != null) {
            if (!isValidGCDFile(file)) {
                showInvalidFileError(file);
                return false;
            }
            saveModelToFile(file);
            return true;
        }
        return false;
    }

    @FXML
    public void revert() {
        loadModel(model.getFile());
    }

    @FXML
    public void clearModel() {
        model.clearModel();
        clearPlots();
    }

    @FXML
    protected void showSettings() {
        gcd.settingsStage.show();
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
        GCDPlot newPlot = new GCDPlot(model, plotName);
        if (model.addPlot(newPlot)) {
            if (log.isTraceEnabled()) {
                log.trace("adding plot: {}", plotName);
            }
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
            Optional<ButtonType> result = showYesNoDialog(title, message);
            if (result.isEmpty() || result.get() != ButtonType.YES) {
                t.consume();
            } else {
                model.removePlot(plot);
                if (log.isTraceEnabled()) {
                    log.trace("removing plot: {}", plot.getName());
                }
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
                log.info("Changing model.file from \"{}\" to \"{}\"", old, file.getAbsolutePath());
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
            setModelIndicator(true);
        }
    }

    private void clearPlots() {
        for (Tab tab : plotTabs) {
            mainTabPane.getTabs().remove(tab);
        }
        plotTabs.clear();
    }

    private void setDataFromFilePath(File gcdFile) {
        String isSaved = "";
        if (!model.isSavedToFile()) {
            isSaved = "*";
        }
        gcd.primaryStage.setTitle(isSaved + gcdFile.getName() + " - " + resources.getString("main.title"));
        filePath.setText(gcdFile.getParentFile().getAbsolutePath());
    }

    public void showCloseDialog(Event event) {
        String title = gcd.getString("main.close.question.title");
        String message = gcd.getString("main.close.question.message");
        if (model.getFile() != null && !model.isSavedToFile()) {
            message = gcd.getString("main.close.question.without.saving.message");
        }
        Optional<ButtonType> result = showYesNoDialog(title, message);
        if (result.isPresent() && result.get() == ButtonType.YES) {
            gcd.saveSettings();
            Platform.exit();
        } else {
            if (event != null) {
                event.consume();
            }
        }
    }

    private void setModelIndicator(boolean isSaved) {
        if (modelStatusIndicator != null) {
            modelStatusIndicator.setVisible(true);
            if (isSaved) {
                modelStatusIndicator.setFill(Color.LIME);
            } else {
                modelStatusIndicator.setFill(Color.RED);
            }
        }
    }
}