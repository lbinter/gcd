package at.binter.gcd.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.input.InputMethodEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.net.URL;
import java.util.ResourceBundle;

import static at.binter.gcd.util.GuiUtils.addStageCloseOnEscapeKey;

public class ConstraintEditorController extends BaseController implements Initializable {
    private Stage popup;
    @FXML
    private TitledPane editorTitle;

    @FXML
    private Button editorButtonConfirm;

    @FXML
    private Button editorButtonCancel;

    @FXML
    private TextField editorDefinition;

    @FXML
    private TextField editorDescription;

    @FXML
    private Label editorLabelNumber;

    @FXML
    private Label editorLabelCondition;

    @FXML
    private Label editorLabelVariables;

    @FXML
    private Label editorLabelParameter;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);
        registerEventHandlers();
    }

    public void definitionChanged(InputMethodEvent inputMethodEvent) {
        System.out.println("test: " + inputMethodEvent.getCommitted());
    }

    private void registerEventHandlers() {
        editorButtonCancel.setOnAction(event -> popup.close());
        editorDefinition.textProperty().addListener((observable, oldValue, newValue) -> {
            // TODO: parse and fill other fields from input
        });
    }

    public void createEditor() {
        createEditor(null);
    }

    private void clearData() {
        editorDefinition.setText("");
        editorDescription.setText("");
        editorLabelNumber.setText("");
        editorLabelCondition.setText("");
        editorLabelVariables.setText("");
        editorLabelParameter.setText("");
    }

    public void createEditor(Object dataObject) {
        popup = new Stage();
        popup.initStyle(StageStyle.UNDECORATED);
        popup.setScene(gcd.constraintEditorScene);
        String i18nTitle;
        String i18nConfirm;
        if (dataObject == null) {
            i18nTitle = "editor.constraint.add.title";
            i18nConfirm = "editor.button.add";
        } else {
            i18nTitle = "editor.constraint.edit.title";
            i18nConfirm = "editor.button.edit";
        }
        editorTitle.setText(resources.getString(i18nTitle));
        editorButtonConfirm.setText(resources.getString(i18nConfirm));
        popup.initOwner(gcd.primaryStage);
        popup.initModality(Modality.WINDOW_MODAL);
        clearData();
        if (dataObject != null) {
            // TODO: fillData();
        }
        addStageCloseOnEscapeKey(popup, gcd.constraintEditorScene);
        popup.showAndWait();
    }
}
