package at.binter.gcd.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.input.InputMethodEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.net.URL;
import java.util.ResourceBundle;

public class AlgebraicVariableEditorController extends BaseController implements Initializable {
    private Stage popup;
    @FXML
    private TitledPane algebraicVariableEditorTitle;

    @FXML
    private Button editorAlgVarButtonConfirm;

    @FXML
    private Button editorAlgVarButtonCancel;

    @FXML
    private TextField editorAlgVarDefinition;

    @FXML
    private TextField editorAlgVarDescription;

    @FXML
    private Label editorAlgVarLabelName;

    @FXML
    private Label editorAlgVarLabelFunction;

    @FXML
    private Label editorAlgVarLabelVariables;

    @FXML
    private Label editorAlgVarLabelParameter;

    @FXML
    private TextField editorAlgVarPlotColor;

    @FXML
    private TextField editorAlgVarPlotThickness;

    @FXML
    private TextField editorAlgVarLineArt;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        registerEventHandlers();
    }

    public void definitionChanged(InputMethodEvent inputMethodEvent) {
        System.out.println("test: " + inputMethodEvent.getCommitted());
    }

    private void registerEventHandlers() {
        editorAlgVarButtonCancel.setOnAction(event -> popup.close());
        editorAlgVarDefinition.textProperty().addListener((observable, oldValue, newValue) -> {
            // TODO: parse and fill other fields from input
        });
    }

    public void createEditor() {
        createEditor(null);
    }

    private void clearData() {
        editorAlgVarDefinition.setText("");
        editorAlgVarDescription.setText("");
        editorAlgVarLabelName.setText("");
        editorAlgVarLabelFunction.setText("");
        editorAlgVarLabelVariables.setText("");
        editorAlgVarLabelParameter.setText("");
        editorAlgVarPlotColor.setText("");
        editorAlgVarPlotThickness.setText("");
        editorAlgVarLineArt.setText("");
    }

    public void createEditor(Object dataObject) {
        popup = new Stage();
        popup.initStyle(StageStyle.UNDECORATED);
        popup.setScene(gcd.algebraicVariableEditorScene);
        String i18nTitle;
        String i18nConfirm;
        if (dataObject == null) {
            i18nTitle = "editor.algebraicVariable.add.title";
            i18nConfirm = "editor.button.add";
        } else {
            i18nTitle = "editor.algebraicVariable.edit.title";
            i18nConfirm = "editor.button.edit";
        }
        algebraicVariableEditorTitle.setText(gcd.resources.getString(i18nTitle));
        editorAlgVarButtonConfirm.setText(gcd.resources.getString(i18nConfirm));
        popup.initOwner(gcd.primaryStage);
        popup.initModality(Modality.WINDOW_MODAL);
        clearData();
        if (dataObject != null) {
            // TODO: fillData();
        }
        gcd.algebraicVariableEditorScene.setOnKeyPressed((KeyEvent event) -> {
            if (KeyCode.ESCAPE == event.getCode()) {
                popup.close();
            }
        });
        popup.showAndWait();
    }
}
