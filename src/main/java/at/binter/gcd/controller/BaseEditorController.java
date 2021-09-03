package at.binter.gcd.controller;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TitledPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.net.URL;
import java.util.ResourceBundle;

import static at.binter.gcd.util.GuiUtils.addStageCloseOnEscapeKey;

public abstract class BaseEditorController<T> extends BaseController implements Editor<T> {
    protected Stage popup = new Stage();
    protected Scene scene;
    protected String i18nAddTitle;
    protected String i18nEditTitle;

    @FXML
    protected TitledPane editorTitle;
    @FXML
    protected Button editorButtonConfirm;
    @FXML
    protected Button editorButtonCancel;

    private boolean hasData = false;

    protected abstract void clearData();

    protected abstract void fillDataFrom(T data);

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);
        registerEventHandlers();
    }

    private void registerEventHandlers() {
        editorButtonConfirm.setOnAction(event -> {
            // TODO: validate formdata
            hasData = true;
            popup.close();
        });
        editorButtonCancel.setOnAction(event -> popup.close());
    }

    public void setScene(Scene scene) {
        this.scene = scene;
    }

    public void showEditor(T dataObject) {
        popup = new Stage();
        popup.initStyle(StageStyle.UNDECORATED);
        popup.setScene(scene);
        String i18nTitle;
        String i18nConfirm;
        if (dataObject == null) {
            i18nTitle = i18nAddTitle;
            i18nConfirm = "editor.button.add";
        } else {
            i18nTitle = i18nEditTitle;
            i18nConfirm = "editor.button.edit";
        }
        editorTitle.setText(resources.getString(i18nTitle));
        editorButtonConfirm.setText(resources.getString(i18nConfirm));
        popup.initOwner(gcd.primaryStage);
        popup.initModality(Modality.WINDOW_MODAL);
        clearData();
        hasData = false;
        if (dataObject != null) {
            fillDataFrom(dataObject);
        }
        addStageCloseOnEscapeKey(popup, scene);
        popup.showAndWait();
    }

    public boolean hasData() {
        return hasData;
    }
}
