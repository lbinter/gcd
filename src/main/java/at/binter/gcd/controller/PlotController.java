package at.binter.gcd.controller;

import at.binter.gcd.gui.SelectedMatcher;
import at.binter.gcd.gui.VariableMatcher;
import at.binter.gcd.model.GCDModel;
import at.binter.gcd.model.GCDPlot;
import at.binter.gcd.model.GCDPlotItem;
import at.binter.gcd.model.elements.Agent;
import at.binter.gcd.model.elements.AlgebraicVariable;
import at.binter.gcd.model.elements.Variable;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxListCell;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Predicate;

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

    private ObjectProperty<Predicate<? super Variable>> variableFilter;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);
    }

    public void initializeGCDDepended() {
        model = gcd.gcdController.model;

        final FilteredList<AlgebraicVariable> viewableAlgebraicVariables = new FilteredList<>(model.getAlgebraicVariablesSorted());
        ReadOnlyObjectProperty<ObservableList<AlgebraicVariable>> viewableAlgebraicVariablesProperty = new SimpleObjectProperty<>(viewableAlgebraicVariables);
        ObjectProperty<Predicate<? super AlgebraicVariable>> algebraicVariablesFilter = viewableAlgebraicVariables.predicateProperty();
        algVarListView.setCellFactory(factory -> {
            ListCell<AlgebraicVariable> cell = new ListCell<>() {
                @Override
                public void updateItem(AlgebraicVariable algVar, boolean empty) {
                    super.updateItem(algVar, empty);
                    setText(empty ? null : algVar.toString());
                }
            };
            cell.addEventFilter(MouseEvent.MOUSE_PRESSED, event -> {
                algVarListView.requestFocus();
                if (!cell.isEmpty()) {
                    int index = cell.getIndex();
                    AlgebraicVariable item = cell.getItem();
                    if (!algVarListView.getSelectionModel().getSelectedIndices().contains(index)) {
                        algVarListView.getSelectionModel().select(index);
                        plot.addAlgebraicVariable(item);
                    }
                    algebraicVariablesFilter.set(new SelectedMatcher<>(plot.getAlgebraicVariables()));
                    event.consume();
                }
            });
            return cell;
        });
        algVarListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        algVarListView.itemsProperty().bind(viewableAlgebraicVariablesProperty);

        final FilteredList<Agent> viewableAgents = new FilteredList<>(model.getAgentsSorted());
        ReadOnlyObjectProperty<ObservableList<Agent>> viewableAgentsProperty = new SimpleObjectProperty<>(viewableAgents);
        ObjectProperty<Predicate<? super Agent>> agentsFilter = viewableAgents.predicateProperty();
        agentListView.setCellFactory(factory -> {
            ListCell<Agent> cell = new ListCell<>() {
                @Override
                public void updateItem(Agent agent, boolean empty) {
                    super.updateItem(agent, empty);
                    setText(empty ? null : agent.toString());
                }
            };
            cell.addEventFilter(MouseEvent.MOUSE_PRESSED, event -> {
                agentListView.requestFocus();
                if (!cell.isEmpty()) {
                    int index = cell.getIndex();
                    Agent item = cell.getItem();
                    if (!agentListView.getSelectionModel().getSelectedIndices().contains(index)) {
                        agentListView.getSelectionModel().select(index);
                        plot.addAgent(item);
                    }
                    agentsFilter.set(new SelectedMatcher<>(plot.getAgents()));
                    event.consume();
                }
            });
            return cell;
        });
        agentListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        agentListView.itemsProperty().bind(viewableAgentsProperty);

        final FilteredList<Variable> viewableVariable = new FilteredList<>(model.getVariablesSorted());
        setVariableFilter(viewableVariable.predicateProperty());
        variableListFilter.textProperty().addListener(this::filterVariable);
        variableListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        variableListView.itemsProperty().bind(new SimpleObjectProperty<>(viewableVariable));

        algVarListSelected.setCellFactory(factory -> {
            CheckBoxListCell<GCDPlotItem<AlgebraicVariable>> cell = new CheckBoxListCell<>(GCDPlotItem::addDependedProperty) {
                @Override
                public void updateItem(GCDPlotItem<AlgebraicVariable> item, boolean empty) {
                    super.updateItem(item, empty);
                    setText(empty ? null : item.getItem().toString());
                }
            };
            cell.addEventFilter(MouseEvent.MOUSE_PRESSED, event -> {
                algVarListSelected.requestFocus();
                if (!(event.getTarget() instanceof StackPane) && !cell.isEmpty()) {
                    int index = cell.getIndex();
                    GCDPlotItem<AlgebraicVariable> item = cell.getItem();
                    if (!algVarListSelected.getSelectionModel().getSelectedIndices().contains(index)) {
                        algVarListSelected.getSelectionModel().select(index);
                        plot.removeAlgebraicVariable(item);
                    }
                    algebraicVariablesFilter.set(new SelectedMatcher<>(plot.getAlgebraicVariables()));
                    event.consume();
                }
            });
            return cell;
        });
        algVarListSelected.setItems(plot.getAlgebraicVariablesSorted());

        agentListSelected.setCellFactory(factory -> {
            CheckBoxListCell<GCDPlotItem<Agent>> cell = new CheckBoxListCell<>(GCDPlotItem::addDependedProperty) {
                @Override
                public void updateItem(GCDPlotItem<Agent> item, boolean empty) {
                    super.updateItem(item, empty);
                    setText(empty ? null : item.getItem().toString());
                }
            };
            cell.addEventFilter(MouseEvent.MOUSE_PRESSED, event -> {
                agentListSelected.requestFocus();
                if (!(event.getTarget() instanceof StackPane) && !cell.isEmpty()) {
                    int index = cell.getIndex();
                    GCDPlotItem<Agent> item = cell.getItem();
                    if (!agentListSelected.getSelectionModel().getSelectedIndices().contains(index)) {
                        agentListSelected.getSelectionModel().select(index);
                        plot.removeAgent(item);
                    }
                    agentsFilter.set(new SelectedMatcher<>(plot.getAgents()));
                    event.consume();
                }
            });
            return cell;
        });
        agentListSelected.setItems(plot.getAgentsSorted());
        variableListSelected.setItems(plot.getVariablesSorted());

        plotName.setText(parentTab.getText());
        plotLegendLabel.setText(plot.getLegendLabel());
        plotStyle.setText(plot.getPlotStyle());
        plotRange.setText(plot.getPlotRange());
    }

    private void setVariableFilter(ObjectProperty<Predicate<? super Variable>> predicateProperty) {
        variableFilter = predicateProperty;
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
        parentTab.setText("Plot: " + plotName.getText());
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
            variableFilter.set(new VariableMatcher("*"));
        } else {
            variableFilter.set(new VariableMatcher(filter));
        }
    }
}