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

import static at.binter.gcd.util.Tools.readDoubleValueFrom;
import static at.binter.gcd.util.Tools.setLabelTextFormatted;

public class AlgebraicVariableEditorController extends BaseEditorController<AlgebraicVariable> implements Initializable {
    @FXML
    private Label editorLabelDefinition;
    @FXML
    private TextField editorName;
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
    private TextField editorLineArt;

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
        editorFunction.textProperty().addListener(this::functionChanged);
    }

    private void registerValidators() {

    }

    private void nameChanged(ObservableValue<? extends String> observable, String oldValue, String newValue) {
        setLabelTextFormatted(editorLabelDefinition, newValue + AlgebraicVariable.assignmentSymbol + editorFunction.getText());
    }

    private void functionChanged(ObservableValue<? extends String> observable, String oldValue, String newValue) {
        ParsedFunction f = new ParsedFunction(editorName.getText(), newValue, AlgebraicVariable.assignmentSymbol);
        setLabelTextFormatted(editorLabelVariables, f.sortedVariables);
        setLabelTextFormatted(editorLabelParameter, f.sortedParameters);
        setLabelTextFormatted(editorLabelDefinition, editorName.getText() + AlgebraicVariable.assignmentSymbol + newValue);
    }

    @Override
    protected void clearData() {
        editorName.setText("");
        editorFunction.setText("");
        editorDescription.setText("");
        editorLabelVariables.setText("");
        editorLabelParameter.setText("");
        editorPlotColor.setText("");
        editorPlotThickness.setText("");
        editorLineArt.setText("");
    }

    @Override
    public AlgebraicVariable createDataObject() {
        AlgebraicVariable algVar = new AlgebraicVariable();
        algVar.setName(editorName.getText());
        algVar.setFunction(editorFunction.getText());
        algVar.setDescription(editorDescription.getText());
        algVar.setPlotColor(editorPlotColor.getText());
        algVar.setPlotThickness(readDoubleValueFrom(editorPlotThickness));
        algVar.setPlotLineStyle(editorLineArt.getText());
        return algVar;
    }

    @Override
    protected void fillDataFrom(AlgebraicVariable data) {
        editorLabelVariables.setText("");
        editorLabelParameter.setText("");
        editorName.setText(data.getName());
        editorFunction.setText(data.getFunction());
        editorDescription.setText(data.getDescription());
        editorPlotColor.setText(data.getPlotColor());
        if (data.getPlotThickness() != null) {
            editorPlotThickness.setText(Double.toString(data.getPlotThickness()));
        }
        editorLineArt.setText(data.getPlotLineStyle());
    }
}
