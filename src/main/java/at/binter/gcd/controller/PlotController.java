package at.binter.gcd.controller;

import at.binter.gcd.model.GCDModel;
import at.binter.gcd.model.GCDPlot;
import at.binter.gcd.model.elements.*;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.util.ResourceBundle;

import static at.binter.gcd.util.Tools.isMousePrimaryDoubleClicked;

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
    private ListView<Variable> variableListView;
    @FXML
    private ListView<Parameter> parameterListView;
    @FXML
    private ListView<ChangeMu> changeMuListView;
    @FXML
    private ListView<AlgebraicVariable> algVarListSelected;
    @FXML
    private ListView<Agent> agentListSelected;
    @FXML
    private ListView<Variable> variableListSelected;
    @FXML
    private ListView<Parameter> parameterListSelected;
    @FXML
    private ListView<ChangeMu> changeMuListSelected;
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
    private EditDialog<Constraint> constraintEditDialog;
    private EditDialog<Variable> variableEditDialog;
    private EditDialog<Parameter> parameterEditDialog;
    private EditDialog<ChangeMu> changeMuEditDialog;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);
    }

    public void initializeGCDDepended() {
        model = gcd.gcdController.model;

        algebraicVariableEditDialog = new EditDialog<>(algVarListSelected, plot.getAlgebraicVariables(), gcd.algebraicVariableEditorController, gcd);
        agentEditDialog = new EditDialog<>(agentListSelected, plot.getAgents(), gcd.agentEditorController, gcd);
        variableEditDialog = new EditDialog<>(variableListSelected, plot.getVariables(), gcd.variableEditorController, gcd);
        parameterEditDialog = new EditDialog<>(parameterListSelected, plot.getParameters(), gcd.parameterEditorController, gcd);
        changeMuEditDialog = new EditDialog<>(changeMuListSelected, plot.getChangeMus(), gcd.changeMuEditorController, gcd);

        algVarListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        algVarListView.setCellFactory(factory -> {
            ListCell<AlgebraicVariable> cell = new ListCell<>() {
                @Override
                protected void updateItem(AlgebraicVariable item, boolean empty) {
                    super.updateItem(item, empty);
                    setText(empty ? null : item.toString());
                }
            };
            cell.addEventFilter(MouseEvent.MOUSE_PRESSED, event -> {
                algVarListView.requestFocus();
                if (!cell.isEmpty()) {
                    int index = cell.getIndex();
                    AlgebraicVariable algebraicVariable = cell.getItem();
                    if (algVarListView.getSelectionModel().getSelectedIndices().contains(index)) {
                        algVarListView.getSelectionModel().clearSelection(index);
                        plot.removeAlgebraicVariable(algebraicVariable,
                                algVarListView.getSelectionModel().getSelectedItems().toArray(new AlgebraicVariable[0]),
                                agentListSelected.getSelectionModel().getSelectedItems().toArray(new Agent[0]),
                                variableListView.getSelectionModel().getSelectedItems().toArray(new Variable[0]),
                                parameterListView.getSelectionModel().getSelectedItems().toArray(new Parameter[0]));
                    } else {
                        algVarListView.getSelectionModel().select(index);
                        plot.addAlgebraicVariable(algebraicVariable);
                    }
                    event.consume();
                }
            });
            return cell;
        });
        algVarListView.setItems(model.getAlgebraicVariablesSorted());

        agentListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        agentListView.setCellFactory(factory -> {
            ListCell<Agent> cell = new ListCell<>() {
                @Override
                protected void updateItem(Agent item, boolean empty) {
                    super.updateItem(item, empty);
                    setText(empty ? null : item.toString());
                }
            };
            cell.addEventFilter(MouseEvent.MOUSE_PRESSED, event -> {
                agentListView.requestFocus();
                if (!cell.isEmpty()) {
                    int index = cell.getIndex();
                    Agent agent = cell.getItem();
                    if (agentListView.getSelectionModel().getSelectedIndices().contains(index)) {
                        agentListView.getSelectionModel().clearSelection(index);
                        plot.removeAgent(agent,
                                algVarListView.getSelectionModel().getSelectedItems().toArray(new AlgebraicVariable[0]),
                                agentListSelected.getSelectionModel().getSelectedItems().toArray(new Agent[0]),
                                variableListView.getSelectionModel().getSelectedItems().toArray(new Variable[0]),
                                parameterListView.getSelectionModel().getSelectedItems().toArray(new Parameter[0]));
                    } else {
                        agentListView.getSelectionModel().select(index);
                        plot.addAgent(agent);
                    }
                    event.consume();
                }
            });
            return cell;
        });
        agentListView.setItems(model.getAgentsSorted());

        variableListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        variableListView.setCellFactory(factory -> {
            ListCell<Variable> cell = new ListCell<>() {
                @Override
                protected void updateItem(Variable item, boolean empty) {
                    super.updateItem(item, empty);
                    setText(empty ? null : item.toString());
                }
            };
            cell.addEventFilter(MouseEvent.MOUSE_PRESSED, event -> {
                variableListView.requestFocus();
                if (!cell.isEmpty()) {
                    int index = cell.getIndex();
                    Variable variable = cell.getItem();
                    if (variableListView.getSelectionModel().getSelectedIndices().contains(index)) {
                        variableListView.getSelectionModel().clearSelection(index);
                        plot.removeVariable(variable,
                                algVarListView.getSelectionModel().getSelectedItems().toArray(new AlgebraicVariable[0]),
                                agentListSelected.getSelectionModel().getSelectedItems().toArray(new Agent[0]),
                                variableListView.getSelectionModel().getSelectedItems().toArray(new Variable[0]));
                    } else {
                        variableListView.getSelectionModel().select(index);
                        plot.addVariable(variable);
                    }
                    event.consume();
                }
            });
            return cell;
        });
        variableListView.setItems(model.getVariablesSorted());

        parameterListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        parameterListView.setCellFactory(factory -> {
            ListCell<Parameter> cell = new ListCell<>() {
                @Override
                protected void updateItem(Parameter item, boolean empty) {
                    super.updateItem(item, empty);
                    setText(empty ? null : item.toString());
                }
            };
            cell.addEventFilter(MouseEvent.MOUSE_PRESSED, event -> {
                parameterListView.requestFocus();
                if (!cell.isEmpty()) {
                    int index = cell.getIndex();
                    Parameter parameter = cell.getItem();
                    if (parameterListView.getSelectionModel().getSelectedIndices().contains(index)) {
                        parameterListView.getSelectionModel().clearSelection(index);
                        plot.removeParameter(parameter,
                                algVarListView.getSelectionModel().getSelectedItems().toArray(new AlgebraicVariable[0]),
                                agentListSelected.getSelectionModel().getSelectedItems().toArray(new Agent[0]),
                                parameterListView.getSelectionModel().getSelectedItems().toArray(new Parameter[0]));
                    } else {
                        parameterListView.getSelectionModel().select(index);
                        plot.addParameter(parameter);
                    }
                    event.consume();
                }
            });
            return cell;
        });
        parameterListView.setItems(model.getParametersSorted());

        changeMuListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        changeMuListView.setCellFactory(factory -> {
            ListCell<ChangeMu> cell = new ListCell<>() {
                @Override
                protected void updateItem(ChangeMu item, boolean empty) {
                    super.updateItem(item, empty);
                    setText(empty ? null : item.toString());
                }
            };
            cell.addEventFilter(MouseEvent.MOUSE_PRESSED, event -> {
                changeMuListView.requestFocus();
                if (!cell.isEmpty()) {
                    int index = cell.getIndex();
                    ChangeMu changeMu = cell.getItem();
                    if (changeMuListView.getSelectionModel().getSelectedIndices().contains(index)) {
                        changeMuListView.getSelectionModel().clearSelection(index);
                        plot.removeChangeMu(changeMu);
                    } else {
                        changeMuListView.getSelectionModel().select(index);
                        plot.addChangeMu(changeMu);
                    }
                    event.consume();
                }
            });
            return cell;
        });
        changeMuListView.setItems(model.getChangeMus());

        algVarListSelected.setItems(plot.getAlgebraicVariablesSorted());
        agentListSelected.setItems(plot.getAgentsSorted());
        variableListSelected.setItems(plot.getVariablesSorted());
        parameterListSelected.setItems(plot.getParametersSorted());
        changeMuListSelected.setItems(plot.getChangeMus());

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

    private void registerEventHandlers() {
        algVarListSelected.setOnMouseClicked(event -> {
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
        });
        parameterListSelected.setOnMouseClicked(event -> {
            if (isMousePrimaryDoubleClicked(event)) {
                editSelectedParameter();
            }
        });
        changeMuListSelected.setOnMouseClicked(event -> {
            if (isMousePrimaryDoubleClicked(event)) {
                editSelectedChangeMu();
            }
        });
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

    protected void editSelectedParameter() {
        parameterEditDialog.editSelectedValue();
    }

    protected void editSelectedChangeMu() {
        changeMuEditDialog.editSelectedValue();
    }
}