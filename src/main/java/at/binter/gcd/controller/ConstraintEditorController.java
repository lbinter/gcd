package at.binter.gcd.controller;

import at.binter.gcd.model.elements.Constraint;
import at.binter.gcd.util.ParsedFunction;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.apache.commons.lang3.StringUtils;

import java.net.URL;
import java.util.ResourceBundle;

import static at.binter.gcd.util.Tools.setLabelTextFormatted;

public class ConstraintEditorController extends BaseEditorController<Constraint> implements Initializable {

    @FXML
    private Label editorLabelId;
    @FXML
    private TextField editorCondition;
    @FXML
    private TextField editorDescription;
    @FXML
    private Label editorLabelVariables;
    @FXML
    private Label editorLabelParameter;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);
        i18nAddTitle = "editor.constraint.add.title";
        i18nEditTitle = "editor.constraint.edit.title";
        registerEventHandlers();
    }

    private void registerEventHandlers() {
        editorCondition.textProperty().addListener(this::conditionChanged);
    }

    private void conditionChanged(ObservableValue<? extends String> observable, String oldValue, String newValue) {
        ParsedFunction f = new ParsedFunction(newValue);
        setLabelTextFormatted(editorLabelVariables, f.sortedVariables);
        setLabelTextFormatted(editorLabelParameter, f.sortedParameters);
    }

    @Override
    public Constraint createDataObject() {
        Constraint constraint = new Constraint();
        if (StringUtils.isBlank(editorLabelId.getText())) {
            // id will be set via gcd model constraint observable list listener
            constraint.setId(-1);
        } else {
            constraint.setId(Integer.parseInt(editorLabelId.getText()));
        }
        constraint.setCondition(editorCondition.getText());
        constraint.setDescription(editorDescription.getText());
        return constraint;
    }

    public void clearData() {
        editorLabelId.setText("");
        editorCondition.setText("");
        editorDescription.setText("");
        editorLabelVariables.setText("");
        editorLabelParameter.setText("");
    }

    @Override
    protected void fillDataFrom(Constraint data) {
        editorLabelVariables.setText("");
        editorLabelParameter.setText("");
        editorLabelId.setText(String.valueOf(data.getId()));
        editorCondition.setText(data.getCondition());
        editorDescription.setText(data.getDescription());
    }
}
