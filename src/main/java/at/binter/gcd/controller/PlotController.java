package at.binter.gcd.controller;

import at.binter.gcd.gui.VariableMatcher;
import at.binter.gcd.model.GCDModel;
import at.binter.gcd.model.GCDPlot;
import at.binter.gcd.model.GCDPlotItem;
import at.binter.gcd.model.elements.Agent;
import at.binter.gcd.model.elements.AlgebraicVariable;
import at.binter.gcd.model.elements.Variable;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.Tab;
import javafx.scene.control.TextField;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.util.ResourceBundle;

import static at.binter.gcd.util.GuiUtils.sanitizeString;

public class PlotController extends BaseController implements Initializable {
    private static final Logger log = LoggerFactory.getLogger(PlotController.class);

    private Tab parentTab;
    protected GCDModel model;
    private GCDPlot plot;

    @FXML
    private ListView<AlgebraicVariable> algVarListView;
    @FXML
    private ListView<Agent> agentListView;
    @FXML
    private TextField variableListFilter;
    @FXML
    private ListView<Variable> variableListView;
    @FXML
    private ListView<GCDPlotItem<AlgebraicVariable>> algVarListSelected;
    @FXML
    private ListView<GCDPlotItem<Agent>> agentListSelected;
    @FXML
    private ListView<Variable> variableListSelected;
    @FXML
    private TextField plotName;
    @FXML
    private TextField plotLegendLabel;
    @FXML
    private TextField plotStyle;
    @FXML
    private TextField plotRange;

    private EditDialog<AlgebraicVariable> algebraicVariableEditDialog;
    private EditDialog<Agent> agentEditDialog;
    private EditDialog<Variable> variableEditDialog;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);
    }

    public void initializeGCDDepended() {
        model = gcd.gcdController.model;

        //algebraicVariableEditDialog = new EditDialog<>(algVarListSelected, plot.getAlgebraicVariables(), gcd.algebraicVariableEditorController, gcd);
        //agentEditDialog = new EditDialog<>(agentListSelected, plot.getAgents(), gcd.agentEditorController, gcd);
        //variableEditDialog = new EditDialog<>(variableListSelected, plot.getVariables(), gcd.variableEditorController, gcd);

        algVarListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        algVarListView.setItems(model.getAlgebraicVariablesSorted());

        agentListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        agentListView.setItems(model.getAgentsSorted());

        variableListFilter.textProperty().addListener(this::filterVariable);
        variableListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        variableListView.itemsProperty().bind(model.viewableVariableProperty());

        algVarListSelected.setItems(plot.getAlgebraicVariablesSorted());
        agentListSelected.setItems(plot.getAgentsSorted());
        variableListSelected.setItems(plot.getVariablesSorted());

        plotName.setText(parentTab.getText());
        plotLegendLabel.setText(plot.getLegendLabel());
        plotStyle.setText(plot.getPlotStyle());
        plotRange.setText(plot.getPlotRange());

        registerEventHandlers();
    }

    public void setParentTab(Tab tab) {
        parentTab = tab;
    }

    public void setPlotModel(GCDPlot plot) {
        this.plot = plot;
        initializeGCDDepended();
        // TODO select values based on plot
    }

    public void saveSettings() {
        String defaultPlotStyle = plot.getPlotStyle();
        parentTab.setText(plotName.getText());
        plot.setName(plotName.getText());
        plot.setLegendLabel(plotLegendLabel.getText());
        if (!plotStyle.getText().equals(defaultPlotStyle)) {
            plot.setPlotStyle(plotStyle.getText());
        } else {
            plotStyle.setText(plot.getPlotStyle());
        }
        plot.setPlotRange(plotRange.getText());
    }

    void filterVariable(ObservableValue<? extends String> observable, String oldValue, String newValue) {
        String filter = sanitizeString(newValue);
        if (StringUtils.isBlank(newValue)) {
            model.variableFilterProperty().set(new VariableMatcher("*"));
        } else {
            model.variableFilterProperty().set(new VariableMatcher(filter));
        }
    }

    private void registerEventHandlers() {
        /*algVarListSelected.setOnMouseClicked(event -> {
            if (isMousePrimaryDoubleClicked(event)) {
                editSelectedAlgebraicVariable();
            }
        });
        agentListSelected.setOnMouseClicked(event -> {
            if (isMousePrimaryDoubleClicked(event)) {
                editSelectedAgent();
            }
        });
        variableListSelected.setOnMouseClicked(event -> {
            if (isMousePrimaryDoubleClicked(event)) {
                editSelectedVariable();
            }
        });*/
    }


    protected void editSelectedAlgebraicVariable() {
        algebraicVariableEditDialog.editSelectedValue();
    }

    protected void editSelectedAgent() {
        agentEditDialog.editSelectedValue();
    }

    protected void editSelectedVariable() {
        variableEditDialog.editSelectedValue();
    }
}