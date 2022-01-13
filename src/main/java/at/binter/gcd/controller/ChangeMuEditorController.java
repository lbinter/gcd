package at.binter.gcd.controller;

import at.binter.gcd.model.elements.Agent;
import at.binter.gcd.model.elements.ChangeMu;
import at.binter.gcd.model.elements.Variable;
import at.binter.gcd.util.Tools;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

import static at.binter.gcd.util.GuiUtils.*;

public class ChangeMuEditorController extends BaseEditorController<ChangeMu> implements Initializable {

    @FXML
    private Label editorLabelIndex;
    @FXML
    private Label editorLabelName;

    @FXML
    private TextField editorValueStart;

    @FXML
    private TextField editorValueMinimum;

    @FXML
    private TextField editorValueMaximum;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);
        i18nAddTitle = "editor.changemu.edit.title";
        i18nEditTitle = "editor.changemu.edit.title";
        editorValueStart.setTextFormatter(createDoubleTextFormatter());
        editorValueMinimum.setTextFormatter(createDoubleTextFormatter());
        editorValueMaximum.setTextFormatter(createDoubleTextFormatter());
        registerEventHandlers();
    }

    private void registerEventHandlers() {
        addValuesValidationListener(editorValueStart, editorValueMinimum, editorValueMaximum);
    }

    @Override
    public ChangeMu createDataObject() {
        String indexName = Tools.transformUnicodeToMathematicaGreekLetters(editorLabelIndex.getText());
        String identifier = Tools.transformUnicodeToMathematicaGreekLetters(editorLabelName.getText());
        String[] split = indexName.split(",");
        String agentName = split[0].replace("\\[Mu][", "");
        int index = Integer.parseInt(split[1].replace("]", ""));
        String variableName = identifier.replace("\\[Mu]" + agentName, "");
        Agent agent = gcd.gcdController.model.getAgent(agentName);
        Variable variable = gcd.gcdController.model.getVariable(variableName);
        ChangeMu changeMu = new ChangeMu(agent, variable, index);
        changeMu.setStartValue(readDoubleValueFrom(editorValueStart));
        changeMu.setMinValue(readDoubleValueFrom(editorValueMinimum));
        changeMu.setMaxValue(readDoubleValueFrom(editorValueMaximum));
        return changeMu;
    }

    @Override
    protected void clearData() {
        editorLabelIndex.setText("");
        editorLabelName.setText("");
        editorValueStart.setText("");
        editorValueMinimum.setText("");
        editorValueMaximum.setText("");
    }

    @Override
    protected void fillDataFrom(ChangeMu data) {
        editorLabelIndex.setText(Tools.transformMathematicaGreekToUnicodeLetters(data.getIndexName()));
        editorLabelName.setText(Tools.transformMathematicaGreekToUnicodeLetters(data.getIdentifier()));
        editorValueStart.setText(doubleToString(data.getStartValue()));
        editorValueMinimum.setText(doubleToString(data.getMinValue()));
        editorValueMaximum.setText(doubleToString(data.getMaxValue()));
    }

    @Override
    boolean closeDependingOnValidation() {
        return true;
    }
}
