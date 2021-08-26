package at.binter.gcd.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.net.URL;
import java.util.ResourceBundle;

import static at.binter.gcd.util.GuiUtils.addStageCloseOnEscapeKey;

public class VariableEditorController extends BaseController implements Initializable {
    private Stage popup;

    @FXML
    private TextField editorDescription;

    @FXML
    private Label editorLabelName;

    @FXML
    private Label editorLabelAlgebraicVariables;

    @FXML
    private Label editorLabelAgents;

    @FXML
    private Label editorLabelConstraints;

    @FXML
    private TextField editorPlotColor;

    @FXML
    private TextField editorPlotThickness;

    @FXML
    private TextField editorLineArt;

    @FXML
    private TextField editorInitialCondition;

    @FXML
    private TextField editorValueStart;

    @FXML
    private TextField editorValueMinimum;

    @FXML
    private TextField editorValueMaximum;

    @FXML
    private Button editorButtonConfirm;

    @FXML
    private Button editorButtonCancel;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);
        registerEventHandlers();
    }

    private void registerEventHandlers() {
        editorButtonCancel.setOnAction(event -> popup.close());
    }

    public void createEditor() {
        createEditor(null);
    }

    private void clearData() {
        editorDescription.setText("");
        editorLabelName.setText("");
        editorLabelAlgebraicVariables.setText("");
        editorLabelAgents.setText("");
        editorLabelConstraints.setText("");
        editorPlotColor.setText("");
        editorPlotThickness.setText("");
        editorLineArt.setText("");
        editorInitialCondition.setText("");
        editorValueStart.setText("");
        editorValueMinimum.setText("");
        editorValueMaximum.setText("");
    }

    public void createEditor(Object dataObject) {
        popup = new Stage();
        popup.initStyle(StageStyle.UNDECORATED);
        popup.setScene(gcd.variableEditorScene);
        popup.initOwner(gcd.primaryStage);
        popup.initModality(Modality.WINDOW_MODAL);
        clearData();
        if (dataObject != null) {
            // TODO: fillData();
        }
        addStageCloseOnEscapeKey(popup, gcd.variableEditorScene);
        popup.showAndWait();
    }
}
