package at.binter.gcd.controller;

import at.binter.gcd.model.elements.Parameter;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

import static at.binter.gcd.util.GuiUtils.*;

public class ParameterEditorController extends BaseEditorController<Parameter> implements Initializable {
    @FXML
    private Label editorLabelName;
    @FXML
    private TextField editorDescription;
    @FXML
    private TextField editorValueStart;
    @FXML
    private TextField editorValueMinimum;
    @FXML
    private TextField editorValueMaximum;
    @FXML
    private Label editorLabelAlgebraicVariables;
    @FXML
    private Label editorLabelAgents;
    @FXML
    private Label editorLabelConstraints;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);
        i18nAddTitle = "editor.parameter.edit.title";
        i18nEditTitle = "editor.parameter.edit.title";
        editorValueStart.setTextFormatter(createDoubleTextFormatter());
        editorValueMinimum.setTextFormatter(createDoubleTextFormatter());
        editorValueMaximum.setTextFormatter(createDoubleTextFormatter());
        registerEventHandlers();
    }

    private void registerEventHandlers() {
        addValuesValidationListener(editorValueStart, editorValueMinimum, editorValueMaximum);
    }

    @Override
    public Parameter createDataObject() {
        Parameter parameter = new Parameter(editorLabelName.getText());
        parameter.setDescription(editorDescription.getText());
        parameter.setStartValue(readDoubleValueFrom(editorValueStart));
        parameter.setMinValue(readDoubleValueFrom(editorValueMinimum));
        parameter.setMaxValue(readDoubleValueFrom(editorValueMaximum));
        return parameter;
    }

    @Override
    protected void clearData() {
        editorLabelName.setText("");
        editorDescription.setText("");
        editorValueStart.setText("");
        editorValueMinimum.setText("");
        editorValueMaximum.setText("");
        editorLabelAlgebraicVariables.setText("");
        editorLabelAgents.setText("");
        editorLabelConstraints.setText("");
    }

    @Override
    protected void fillDataFrom(Parameter data) {
        editorLabelName.setText(data.getName());
        editorDescription.setText(data.getDescription());
        editorValueStart.setText(doubleToString(data.getStartValue()));
        editorValueMinimum.setText(doubleToString(data.getMinValue()));
        editorValueMaximum.setText(doubleToString(data.getMaxValue()));
        editorLabelAlgebraicVariables.setText(data.getAlgebraicVariablesAsString());
        editorLabelAgents.setText(data.getAgentsAsString());
        editorLabelConstraints.setText(data.getConstraintsAsString());
    }
}
