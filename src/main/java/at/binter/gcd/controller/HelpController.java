package at.binter.gcd.controller;

import javafx.css.PseudoClass;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

import static at.binter.gcd.util.GuiUtils.addStageCloseOnEscapeKey;


public class HelpController extends BaseController implements Initializable {
    private static final PseudoClass selected = PseudoClass.getPseudoClass("selected");

    @FXML
    private Label correct;

    @FXML
    private Label correctSelected;

    @FXML
    private Label error;

    @FXML
    private Label errorSelected;

    @FXML
    private Label textValidDefValues;

    @FXML
    private Label selectedValidDefValues;

    @FXML
    private Label textValidFunction;

    @FXML
    private Label selectedValidFunction;

    @FXML
    private Label textAutoPlot;

    @FXML
    private Label selectedAutoPlot;

    @FXML
    private Label textInvalid;

    @FXML
    private Label selectedInvalid;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);

        /* correct.setStyle("-fx-border-color:black;");
        error.setStyle("-fx-border-color:black;");
        textValidDefValues.setStyle("-fx-border-color:black;");
        textValidFunction.setStyle("-fx-border-color:black;");
        textAutoPlot.setStyle("-fx-border-color:black;");
        textInvalid.setStyle("-fx-border-color:black;");

        correctSelected.setStyle("-fx-border-color:black;");
        errorSelected.setStyle("-fx-border-color:black;");
        selectedValidDefValues.setStyle("-fx-border-color:black;");
        selectedValidFunction.setStyle("-fx-border-color:black;");
        selectedAutoPlot.setStyle("-fx-border-color:black;");
        selectedInvalid.setStyle("-fx-border-color:black;"); */

        correctSelected.pseudoClassStateChanged(selected, true);
        errorSelected.pseudoClassStateChanged(selected, true);
        selectedValidDefValues.pseudoClassStateChanged(selected, true);
        selectedValidFunction.pseudoClassStateChanged(selected, true);
        selectedAutoPlot.pseudoClassStateChanged(selected, true);
        selectedInvalid.pseudoClassStateChanged(selected, true);
    }

    public void showHelpWindow() {
        Stage popup = new Stage();
        popup.setScene(gcd.helpScene);
        popup.setTitle(resources.getString("help.frame.title"));
        popup.initOwner(gcd.primaryStage);
        popup.initModality(Modality.WINDOW_MODAL);
        addStageCloseOnEscapeKey(popup, gcd.helpScene);
        popup.showAndWait();
    }
}
