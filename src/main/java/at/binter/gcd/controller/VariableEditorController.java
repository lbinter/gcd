package at.binter.gcd.controller;

import at.binter.gcd.model.elements.Variable;
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
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import org.apache.commons.lang3.StringUtils;

import java.net.URL;
import java.util.ResourceBundle;

import static at.binter.gcd.util.GuiUtils.*;
import static at.binter.gcd.util.Tools.setLabelTextFormatted;

public class VariableEditorController extends BaseEditorController<Variable> implements Initializable {
    @FXML
    private Label editorLabelName;
    @FXML
    private TextField editorDescription;
    @FXML
    private TextArea editorInitialCondition;
    @FXML
    protected Button transformButton;
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

    @FXML
    private GridPane grid;
    @FXML
    private Label editorLabelVariable;
    @FXML
    private Label editorLabelVariables;
    @FXML
    private Label editorLabelParameter;
    @FXML
    private Label editorLabelParameters;
    private RowConstraints rowVariables;
    private RowConstraints rowParameters;
    private RowConstraints rowAlgebraicVariables;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);
        i18nAddTitle = "editor.variable.edit.title";
        i18nEditTitle = "editor.variable.edit.title";
        editorValueStart.setTextFormatter(createDoubleTextFormatter());
        editorValueMinimum.setTextFormatter(createDoubleTextFormatter());
        editorValueMaximum.setTextFormatter(createDoubleTextFormatter());
        editorPlotThickness.setTextFormatter(createDoubleTextFormatter());
        rowVariables = grid.getRowConstraints().get(4);
        rowParameters = grid.getRowConstraints().get(5);
        rowAlgebraicVariables = grid.getRowConstraints().get(9);
        transformButton.setStyle("-fx-background-color:-fx-shadow-highlight-color, -fx-outer-border, -fx-inner-border,lime;");
    }

    public void initializeGCDDepended() {
        registerEventHandlers();
        new PlotStyleIndicator(gcd.plotStyles, null, editorLabelName, editorPlotColor, editorPlotThickness, editorPlotLineArt);
    }

    private void registerEventHandlers() {
        editorValueStart.textProperty().addListener((observable, oldValue, newValue) -> {
            editorValueStart.pseudoClassStateChanged(errorClass, needsCheckValue(!isValueStartValid(newValue, editorValueMinimum, editorValueMaximum), 1));
        });
        editorValueMinimum.textProperty().addListener((observable, oldValue, newValue) -> {
            editorValueMinimum.pseudoClassStateChanged(errorClass, needsCheckValue(!isValueMinValid(newValue, editorValueStart, editorValueMaximum), 2));
        });
        editorValueMaximum.textProperty().addListener((observable, oldValue, newValue) -> {
            editorValueMaximum.pseudoClassStateChanged(errorClass, needsCheckValue(!isValueMaxValid(newValue, editorValueStart, editorValueMinimum), 3));
        });
        editorInitialCondition.textProperty().addListener(this::initialConditionChanged);
    }

    private void initialConditionChanged(ObservableValue<? extends String> observable, String oldValue, String newValue) {
        editorValueStart.pseudoClassStateChanged(errorClass, needsCheckValue(!isValueStartValid(editorValueStart.getText(), editorValueMinimum, editorValueMaximum), 1));
        editorValueMinimum.pseudoClassStateChanged(errorClass, needsCheckValue(!isValueMinValid(editorValueMinimum.getText(), editorValueStart, editorValueMaximum), 2));
        editorValueMaximum.pseudoClassStateChanged(errorClass, needsCheckValue(!isValueMaxValid(editorValueMaximum.getText(), editorValueStart, editorValueMinimum), 3));

        String name = editorLabelName.getText();
        String text = sanitizeString(newValue);
        if (text != null) {
            if ((name + "0").equals(text)) {
                setVariableParameterListVisible(false);
                showGreenTransformButton(transformButton);
                return;
            }
            showRedTransformButton(transformButton);
            ParsedFunction f = new ParsedFunction(text);
            setVariableParameterListVisible(true);
            setLabelTextFormatted(editorLabelVariables, f.sortedVariables);
            setLabelTextFormatted(editorLabelParameters, f.sortedParameters);
        } else {
            setVariableParameterListVisible(false);
            showGreenTransformButton(transformButton);
        }
    }

    private void setVariableParameterListVisible(boolean visible) {
        if (visible) {
            rowVariables.setMinHeight(rowAlgebraicVariables.getMinHeight());
            rowVariables.setPrefHeight(rowAlgebraicVariables.getPrefHeight());
            rowVariables.setMaxHeight(rowAlgebraicVariables.getMaxHeight());
            rowParameters.setMinHeight(rowAlgebraicVariables.getMinHeight());
            rowParameters.setPrefHeight(rowAlgebraicVariables.getPrefHeight());
            rowParameters.setMaxHeight(rowAlgebraicVariables.getMaxHeight());
        } else {
            rowVariables.setMinHeight(0);
            rowVariables.setPrefHeight(0);
            rowVariables.setMaxHeight(0);
            rowParameters.setMinHeight(0);
            rowParameters.setPrefHeight(0);
            rowParameters.setMaxHeight(0);
        }
        editorLabelVariable.setVisible(visible);
        editorLabelVariables.setVisible(visible);
        editorLabelParameter.setVisible(visible);
        editorLabelParameters.setVisible(visible);
        popup.sizeToScene();
    }

    private boolean needsCheckValue(boolean valueCheck, int pos) {
        String name = editorLabelName.getText();
        String initialCondition = editorInitialCondition.getText();
        Double start = readDoubleValueFrom(editorValueStart);
        Double min = readDoubleValueFrom(editorValueStart);
        Double max = readDoubleValueFrom(editorValueStart);

        if (StringUtils.isBlank(initialCondition)) {
            // VALID_AUTOMATIC ==> initialConditions should be automatically calculated by mathematica => no double value allowed
            switch (pos) {
                case 1 -> {
                    return start != null;
                }
                case 2 -> {
                    return min != null;
                }
                case 3 -> {
                    return max != null;
                }
            }
            return (start == null && min == null && max == null);
        } else if ((name + "0").equals(initialCondition)) {
            // VALID_HAS_VALUES: initialConditions == <name>0 => requires all double values
            return valueCheck;
        } else {
            // VALID_HAS_FUNCTION: initialConditions == function => no double value allowed
            switch (pos) {
                case 1 -> {
                    return start != null;
                }
                case 2 -> {
                    return min != null;
                }
                case 3 -> {
                    return max != null;
                }
            }
        }
        return false;
    }

    @FXML
    protected void transformFunction() {
        GuiUtils.transformFunction(editorInitialCondition, transformButton);
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
        editorValueStart.pseudoClassStateChanged(errorClass, false);
        editorValueMinimum.pseudoClassStateChanged(errorClass, false);
        editorValueMaximum.pseudoClassStateChanged(errorClass, false);
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

        showGreenTransformButton(transformButton);
    }

    @Override
    boolean closeDependingOnValidation() {
        // TODO validate and display errors
        return true;
    }
}