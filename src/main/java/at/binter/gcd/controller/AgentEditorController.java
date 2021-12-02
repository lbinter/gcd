package at.binter.gcd.controller;

import at.binter.gcd.model.elements.Agent;
import at.binter.gcd.util.ParsedFunction;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.apache.commons.lang3.StringUtils;

import java.net.URL;
import java.util.ResourceBundle;

import static at.binter.gcd.util.GuiUtils.*;
import static at.binter.gcd.util.Tools.setLabelTextFormatted;

public class AgentEditorController extends BaseEditorController<Agent> implements Initializable {
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
        i18nAddTitle = "editor.agent.add.title";
        i18nEditTitle = "editor.agent.edit.title";
        editorDescription.setTextFormatter(createStringTextFormatter());
        editorName.setTextFormatter(createStringTextFormatter());
        editorFunction.setTextFormatter(createStringTextFormatter());
        editorPlotColor.setTextFormatter(createStringTextFormatter());
        editorPlotThickness.setTextFormatter(createDoubleTextFormatter());
        editorPlotLineArt.setTextFormatter(createStringTextFormatter());
        registerEventHandlers();
    }

    private void registerEventHandlers() {
        editorName.textProperty().addListener(this::nameChanged);
        editorFunction.textProperty().addListener(this::functionChanged);
    }

    private void nameChanged(ObservableValue<? extends String> observable, String oldValue, String newValue) {
        setLabelTextFormatted(editorLabelDefinition, newValue + Agent.assignmentSymbol + editorFunction.getText());
        editorName.pseudoClassStateChanged(errorClass, !Character.isUpperCase(newValue.charAt(0)));
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
        setLabelTextFormatted(editorLabelDefinition, editorName.getText() + Agent.assignmentSymbol + newValue);

        editorFunction.pseudoClassStateChanged(errorClass, Agent.functionContainsErrors(newValue));
    }

    @Override
    public Agent createDataObject() {
        Agent agent = new Agent();
        agent.setName(editorName.getText());
        agent.setFunction(editorFunction.getText());
        agent.setDescription(editorDescription.getText());
        agent.setPlotColor(editorPlotColor.getText());
        agent.setPlotThickness(readDoubleValueFrom(editorPlotThickness));
        agent.setPlotLineStyle(editorPlotLineArt.getText());
        return agent;
    }

    @Override
    public void clearData() {
        editorLabelDefinition.setText("");
        editorName.setText("");
        editorFunction.setText("");
        editorDescription.setText("");
        editorLabelVariables.setText("");
        editorLabelParameters.setText("");
        editorPlotColor.setText("");
        editorPlotThickness.setText("");
        editorPlotLineArt.setText("");
    }

    @Override
    protected void fillDataFrom(Agent data) {
        editorLabelVariables.setText("");
        editorLabelParameters.setText("");
        editorName.setText(data.getName());
        editorFunction.setText(data.getFunction());
        editorDescription.setText(data.getDescription());
        editorPlotColor.setText(data.getPlotColor());
        editorPlotThickness.setText(doubleToString(data.getPlotThickness()));
        editorPlotLineArt.setText(data.getPlotLineStyle());
    }
}
