package at.binter.gcd.controller;

import at.binter.gcd.model.elements.Variable;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

import static at.binter.gcd.util.GuiUtils.*;

public class VariableEditorController extends BaseEditorController<Variable> implements Initializable {
    @FXML
    private Label editorLabelName;
    @FXML
    private TextField editorDescription;
    @FXML
    private TextField editorInitialCondition;
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
    @FXML
    private TextField editorPlotColor;
    @FXML
    private TextField editorPlotThickness;
    @FXML
    private TextField editorPlotLineArt;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);
        i18nAddTitle = "editor.variable.edit.title";
        i18nEditTitle = "editor.variable.edit.title";
        editorValueStart.setTextFormatter(createDoubleTextFormatter());
        editorValueMinimum.setTextFormatter(createDoubleTextFormatter());
        editorValueMaximum.setTextFormatter(createDoubleTextFormatter());
        editorPlotThickness.setTextFormatter(createDoubleTextFormatter());
        registerEventHandlers();
    }

    private void registerEventHandlers() {
    }

    @Override
    public Variable createDataObject() {
        Variable variable = new Variable(editorLabelName.getText());
        variable.setDescription(editorDescription.getText());
        variable.setInitialCondition(editorInitialCondition.getText());
        variable.setStartValue(readDoubleValueFrom(editorValueStart));
        variable.setMinValue(readDoubleValueFrom(editorValueMinimum));
        variable.setMaxValue(readDoubleValueFrom(editorValueMaximum));
        variable.setPlotColor(editorPlotColor.getText());
        variable.setPlotThickness(readDoubleValueFrom(editorPlotThickness));
        variable.setPlotLineStyle(editorPlotLineArt.getText());
        return variable;
    }

    protected void clearData() {
        editorLabelName.setText("");
        editorDescription.setText("");
        editorInitialCondition.setText("");
        editorValueStart.setText("");
        editorValueMinimum.setText("");
        editorValueMaximum.setText("");
        editorLabelAlgebraicVariables.setText("");
        editorLabelAgents.setText("");
        editorLabelConstraints.setText("");
        editorPlotColor.setText("");
        editorPlotThickness.setText("");
        editorPlotLineArt.setText("");
    }

    @Override
    protected void fillDataFrom(Variable data) {
        editorLabelName.setText(data.getName());
        editorDescription.setText(data.getDescription());
        editorInitialCondition.setText(data.getInitialCondition());
        editorValueStart.setText(doubleToString(data.getStartValue()));
        editorValueMinimum.setText(doubleToString(data.getMinValue()));
        editorValueMaximum.setText(doubleToString(data.getMaxValue()));
        editorLabelAlgebraicVariables.setText(data.getAlgebraicVariablesAsString());
        editorLabelAgents.setText(data.getAgentsAsString());
        editorLabelConstraints.setText(data.getConstraintsAsString());
        editorPlotColor.setText(data.getPlotColor());
        editorPlotThickness.setText(doubleToString(data.getPlotThickness()));
        editorPlotLineArt.setText(data.getPlotLineStyle());
    }
}