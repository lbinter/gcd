package at.binter.gcd.controller;

import at.binter.gcd.model.elements.AlgebraicVariable;
import at.binter.gcd.util.ParsedFunction;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

import static at.binter.gcd.util.GuiUtils.doubleToString;
import static at.binter.gcd.util.GuiUtils.readDoubleValueFrom;
import static at.binter.gcd.util.Tools.setLabelTextFormatted;

public class AlgebraicVariableEditorController extends BaseEditorController<AlgebraicVariable> implements Initializable {
    @FXML
    private Label editorLabelDefinition;
    @FXML
    private TextField editorName;
    @FXML
    private TextField editorParameter;
    @FXML
    private TextField editorFunction;
    @FXML
    private TextField editorDescription;
    @FXML
    private Label editorLabelVariables;
    @FXML
    private Label editorLabelParameter;
    @FXML
    private TextField editorPlotColor;
    @FXML
    private TextField editorPlotThickness;
    @FXML
    private TextField editorPlotLineArt;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);
        i18nAddTitle = "editor.algebraicVariable.add.title";
        i18nEditTitle = "editor.algebraicVariable.edit.title";
        registerEventHandlers();
        registerValidators();
    }

    private void registerEventHandlers() {
        editorName.textProperty().addListener(this::nameChanged);
        editorParameter.textProperty().addListener(this::parameterChanged);
        editorFunction.textProperty().addListener(this::functionChanged);
    }

    private void registerValidators() {

    }

    private void nameChanged(ObservableValue<? extends String> observable, String oldValue, String newValue) {
        setLabelTextFormatted(editorLabelDefinition, newValue + "[" + editorParameter.getText() + "]" + AlgebraicVariable.assignmentSymbol + editorFunction.getText());
        setDefinition(newValue, editorParameter.getText(), editorFunction.getText());
    }


    private void parameterChanged(ObservableValue<? extends String> observable, String oldValue, String newValue) {
        setDefinition(editorName.getText(), newValue, editorFunction.getText());
    }

    private void functionChanged(ObservableValue<? extends String> observable, String oldValue, String newValue) {
        ParsedFunction f = new ParsedFunction(newValue);
        setLabelTextFormatted(editorLabelVariables, f.sortedVariables);
        setLabelTextFormatted(editorLabelParameter, f.sortedParameters);
        setDefinition(editorName.getText(), editorParameter.getText(), newValue);
    }

    private void setDefinition(String name, String parameter, String function) {
        setLabelTextFormatted(editorLabelDefinition, name + "[" + parameter + "]" + AlgebraicVariable.assignmentSymbol + function);
    }

    @Override
    protected void clearData() {
        editorName.setText("");
        editorParameter.setText("t");
        editorFunction.setText("");
        editorDescription.setText("");
        editorLabelVariables.setText("");
        editorLabelParameter.setText("");
        editorPlotColor.setText("");
        editorPlotThickness.setText("");
        editorPlotLineArt.setText("");
    }

    @Override
    public AlgebraicVariable createDataObject() {
        AlgebraicVariable algVar = new AlgebraicVariable();
        algVar.setName(editorName.getText());
        algVar.setParameter(editorParameter.getText());
        algVar.setFunction(editorFunction.getText());
        algVar.setDescription(editorDescription.getText());
        algVar.setPlotColor(editorPlotColor.getText());
        algVar.setPlotThickness(readDoubleValueFrom(editorPlotThickness));
        algVar.setPlotLineStyle(editorPlotLineArt.getText());
        return algVar;
    }

    @Override
    protected void fillDataFrom(AlgebraicVariable data) {
        editorLabelVariables.setText("");
        editorLabelParameter.setText("");
        editorName.setText(data.getName());
        editorParameter.setText(data.getParameter());
        editorFunction.setText(data.getFunction());
        editorDescription.setText(data.getDescription());
        editorPlotColor.setText(data.getPlotColor());
        editorPlotThickness.setText(doubleToString(data.getPlotThickness()));
        editorPlotLineArt.setText(data.getPlotLineStyle());
    }
}
