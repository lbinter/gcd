package at.binter.gcd.controller;

import at.binter.gcd.gui.CheckedListViewCheckObserver;
import at.binter.gcd.model.GCDModel;
import at.binter.gcd.model.GCDPlot;
import at.binter.gcd.model.elements.*;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.cell.CheckBoxListCell;
import javafx.scene.input.MouseEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.util.ResourceBundle;

public class PlotController extends BaseController implements Initializable {
    private static final Logger log = LoggerFactory.getLogger(PlotController.class);

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

    CheckedListViewCheckObserver<AlgebraicVariable> algVarObserver = new CheckedListViewCheckObserver<>();
    CheckedListViewCheckObserver<Agent> agentObserver = new CheckedListViewCheckObserver<>();
    CheckedListViewCheckObserver<Variable> variableObserver = new CheckedListViewCheckObserver<>();
    CheckedListViewCheckObserver<Parameter> parameterObserver = new CheckedListViewCheckObserver<>();
    CheckedListViewCheckObserver<ChangeMu> changeMuObserver = new CheckedListViewCheckObserver<>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);
    }

    public void initializeGCDDepended() {
        model = gcd.gcdController.model;

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
                                algVarListView.getSelectionModel().getSelectedItems(),
                                agentListSelected.getSelectionModel().getSelectedItems(),
                                variableListView.getSelectionModel().getSelectedItems(),
                                parameterListView.getSelectionModel().getSelectedItems());
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
                                algVarListView.getSelectionModel().getSelectedItems(),
                                agentListSelected.getSelectionModel().getSelectedItems(),
                                variableListView.getSelectionModel().getSelectedItems(),
                                parameterListView.getSelectionModel().getSelectedItems());
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

        variableListView.setCellFactory(CheckBoxListCell.forListView(variableObserver::getObserverForObject));
        variableListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        variableListView.setItems(model.getVariablesSorted());

        parameterListView.setCellFactory(CheckBoxListCell.forListView(parameterObserver::getObserverForObject));
        parameterListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        parameterListView.setItems(model.getParametersSorted());

        changeMuListView.setCellFactory(CheckBoxListCell.forListView(changeMuObserver::getObserverForObject));
        changeMuListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        changeMuListView.setItems(model.getChangeMus());

        algVarListSelected.setItems(plot.getAlgebraicVariablesSorted());
        agentListSelected.setItems(plot.getAgentsSorted());
        variableListSelected.setItems(plot.getVariablesSorted());
        parameterListSelected.setItems(plot.getParametersSorted());
        changeMuListSelected.setItems(plot.getChangeMus());

        registerEventHandlers();
    }

    public void setPlotModel(GCDPlot plot) {
        this.plot = plot;
    }

    private void registerEventHandlers() {
        variableListView.getSelectionModel().getSelectedItems().addListener((ListChangeListener<? super Variable>) c -> {
            while (c.next()) {
                if (c.wasAdded()) {
                    for (Variable v : c.getAddedSubList()) {
                        plot.addVariable(v);
                    }
                }
                if (c.wasRemoved()) {
                    for (Variable v : c.getRemoved()) {
                        plot.removeVariable(v,
                                algVarListView.getSelectionModel().getSelectedItems(),
                                agentListView.getSelectionModel().getSelectedItems(),
                                variableListSelected.getSelectionModel().getSelectedItems());
                    }
                }
                if (c.wasUpdated()) {
                    log.info("was updated");
                }
            }
        });
        parameterListView.getSelectionModel().getSelectedItems().addListener((ListChangeListener<? super Parameter>) c -> {
            while (c.next()) {
                if (c.wasAdded()) {
                    for (Parameter p : c.getAddedSubList()) {
                        plot.addParameter(p);
                    }
                }
                if (c.wasRemoved()) {
                    for (Parameter p : c.getRemoved()) {
                        plot.removeParameter(p,
                                agentListView.getSelectionModel().getSelectedItems(),
                                algVarListView.getSelectionModel().getSelectedItems(),
                                parameterListSelected.getSelectionModel().getSelectedItems());
                    }
                }
            }
        });
        changeMuListView.getSelectionModel().getSelectedItems().addListener((ListChangeListener<? super ChangeMu>) c -> {
            while (c.next()) {
                if (c.wasAdded()) {
                    for (ChangeMu mu : c.getAddedSubList()) {
                        plot.addChangeMu(mu);
                    }
                }
                if (c.wasRemoved()) {
                    for (ChangeMu mu : c.getRemoved()) {
                        plot.removeChangeMu(mu);
                    }
                }
            }
        });
    }
}