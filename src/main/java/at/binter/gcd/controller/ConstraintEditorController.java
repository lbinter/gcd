package at.binter.gcd.controller;

import at.binter.gcd.model.elements.Constraint;
import at.binter.gcd.util.GuiUtils;
import at.binter.gcd.util.ParsedFunction;
import at.binter.gcd.util.ValidationError;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import org.apache.commons.lang3.StringUtils;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import static at.binter.gcd.util.GuiUtils.showValidationAlert;
import static at.binter.gcd.util.Tools.setLabelTextFormatted;

public class ConstraintEditorController extends BaseEditorController<Constraint> implements Initializable {

    @FXML
    private Label editorLabelId;
    @FXML
    private TextField editorName;
    @FXML
    private TextArea editorCondition;
    @FXML
    protected Button transformButton;
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
        transformButton.setStyle("-fx-background-color:-fx-shadow-highlight-color, -fx-outer-border, -fx-inner-border,lime;");
        registerEventHandlers();
    }

    private void registerEventHandlers() {
        editorCondition.textProperty().addListener(this::conditionChanged);
    }

    private void conditionChanged(ObservableValue<? extends String> observable, String oldValue, String newValue) {
        if (StringUtils.isBlank(newValue)) {
            editorLabelVariables.setText("");
            editorLabelParameter.setText("");
            return;
        }
        ParsedFunction f = new ParsedFunction(newValue);
        setLabelTextFormatted(editorLabelVariables, f.sortedVariables);
        setLabelTextFormatted(editorLabelParameter, f.sortedParameters);
    }

    @FXML
    protected void transformFunction() {
        GuiUtils.transformFunction(editorCondition, transformButton);
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
        constraint.setName(editorName.getText());
        constraint.setCondition(editorCondition.getText());
        constraint.setDescription(editorDescription.getText());
        return constraint;
    }

    public void clearData() {
        editorLabelId.setText("");
        editorName.setText("");
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
        editorName.setText(data.getName());
        editorCondition.setText(data.getCondition());
        editorDescription.setText(data.getDescription());
    }

    @Override
    boolean closeDependingOnValidation() {
        List<ValidationError> errorList = new ArrayList<>();
        if (StringUtils.isBlank(editorCondition.getText())) {
            errorList.add(new ValidationError(false, "error.constraint.condition.missing"));
        }
        return showValidationAlert("error.warning", errorList);
    }
}
