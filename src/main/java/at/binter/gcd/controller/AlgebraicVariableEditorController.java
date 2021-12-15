package at.binter.gcd.controller;

import at.binter.gcd.model.elements.AlgebraicVariable;
import at.binter.gcd.util.GuiUtils;
import at.binter.gcd.util.ParsedFunction;
import at.binter.gcd.util.PlotStyleIndicator;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import org.apache.commons.lang3.StringUtils;

import java.net.URL;
import java.util.ResourceBundle;

import static at.binter.gcd.util.GuiUtils.*;
import static at.binter.gcd.util.Tools.setLabelTextFormatted;

public class AlgebraicVariableEditorController extends BaseEditorController<AlgebraicVariable> implements Initializable {
    @FXML
    private Label editorLabelDefinition;
    @FXML
    private TextField editorName;
    @FXML
    private TextField editorParameter;
    @FXML
    private TextArea editorFunction;
    @FXML
    protected Button transformButton;
    @FXML
    private TextField editorDescription;
    @FXML
    private Label editorLabelVariables;
    @FXML
    private Label editorLabelParameters;
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
        editorPlotThickness.setTextFormatter(createDoubleTextFormatter());
    }

    private void registerEventHandlers() {
        editorName.textProperty().addListener(this::nameChanged);
        editorParameter.textProperty().addListener(this::parameterChanged);
        editorFunction.textProperty().addListener(this::functionChanged);
    }

    public void initializeGCDDepended() {
        registerEventHandlers();
        new PlotStyleIndicator(gcd.plotStyles, editorName, null, editorPlotColor, editorPlotThickness, editorPlotLineArt);
    }

    private void nameChanged(ObservableValue<? extends String> observable, String oldValue, String newValue) {
        setLabelTextFormatted(editorLabelDefinition, newValue + "[" + editorParameter.getText() + "]" + AlgebraicVariable.assignmentSymbol + editorFunction.getText());
        setDefinition(newValue, editorParameter.getText(), editorFunction.getText());
    }


    private void parameterChanged(ObservableValue<? extends String> observable, String oldValue, String newValue) {
        setDefinition(editorName.getText(), newValue, editorFunction.getText());
    }

    private void functionChanged(ObservableValue<? extends String> observable, String oldValue, String newValue) {
        if (StringUtils.isBlank(newValue)) {
            editorLabelVariables.setText("");
            editorLabelParameters.setText("");
            editorLabelDefinition.setText("");
            return;
        }
        ParsedFunction f = new ParsedFunction(newValue);
        setLabelTextFormatted(editorLabelVariables, f.sortedVariables);
        setLabelTextFormatted(editorLabelParameters, f.sortedParameters);
        setDefinition(editorName.getText(), editorParameter.getText(), newValue);
    }

    private void setDefinition(String name, String parameter, String function) {
        setLabelTextFormatted(editorLabelDefinition, name + "[" + parameter + "]" + AlgebraicVariable.assignmentSymbol + function);
    }

    @FXML
    protected void transformFunction() {
        GuiUtils.transformFunction(editorFunction, transformButton);
    }

    @Override
    protected void clearData() {
        editorName.setText("");
        editorParameter.setText("t");
        editorFunction.setText("");
        editorDescription.setText("");
        editorLabelVariables.setText("");
        editorLabelParameters.setText("");
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
        editorLabelParameters.setText("");
        editorName.setText(data.getName());
        editorParameter.setText(data.getParameter());
        editorFunction.setText(data.getFunction());
        editorDescription.setText(data.getDescription());
        editorPlotColor.setText(data.getPlotColor());
        editorPlotThickness.setText(doubleToString(data.getPlotThickness()));
        editorPlotLineArt.setText(data.getPlotLineStyle());
    }
}
