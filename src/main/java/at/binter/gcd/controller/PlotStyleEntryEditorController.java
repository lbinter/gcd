package at.binter.gcd.controller;

import at.binter.gcd.model.plotstyle.PlotStyleEntry;
import at.binter.gcd.util.ValidationError;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import org.apache.commons.lang3.StringUtils;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import static at.binter.gcd.util.GuiUtils.*;

public class PlotStyleEntryEditorController extends BaseEditorController<PlotStyleEntry> implements Initializable {
    @FXML
    private TextField editorName;
    @FXML
    private TextField editorPlotColor;
    @FXML
    private TextField editorPlotThickness;
    @FXML
    private TextField editorPlotLineArt;
    @FXML
    private TextField editorDescription;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);
        i18nAddTitle = "editor.plot.style.add.title";
        i18nEditTitle = "editor.plot.style.edit.title";
        editorPlotThickness.setTextFormatter(createDoubleTextFormatter());
        registerEventHandlers();
    }

    private void registerEventHandlers() {
    }

    @Override
    public PlotStyleEntry createDataObject() {
        PlotStyleEntry entry = new PlotStyleEntry();
        entry.setName(editorName.getText());
        entry.setPlotColor(editorPlotColor.getText());
        entry.setPlotThickness(readDoubleValueFrom(editorPlotThickness));
        entry.setPlotLineStyle(editorPlotLineArt.getText());
        entry.setDescription(editorDescription.getText());
        return entry;
    }

    @Override
    public void clearData() {
        editorName.setText("");
        editorPlotColor.setText("");
        editorPlotThickness.setText("");
        editorPlotLineArt.setText("");
        editorDescription.setText("");
    }

    @Override
    protected void fillDataFrom(PlotStyleEntry data) {
        editorName.setText(data.getName());
        editorPlotColor.setText(data.getPlotColor());
        editorPlotThickness.setText(doubleToString(data.getPlotThickness()));
        editorPlotLineArt.setText(data.getPlotLineStyle());
        editorDescription.setText(data.getDescription());
    }

    @Override
    boolean closeDependingOnValidation() {
        List<ValidationError> errorList = new ArrayList<>();
        if (StringUtils.isBlank(editorName.getText())) {
            errorList.add(new ValidationError(false, "error.plot.style.name.missing"));
        }
        return showValidationAlert("error.warning", errorList);
    }
}
