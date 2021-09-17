package at.binter.gcd.controller;

import at.binter.gcd.mathematica.GCDMathematica;
import at.binter.gcd.mathematica.GCDWriterHTML;
import at.binter.gcd.mathematica.GCDWriterNotebook;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.web.WebView;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

import static at.binter.gcd.util.GuiUtils.addStageCloseOnEscapeKey;


public class MathematicaController extends BaseController implements Initializable {
    private GCDMathematica htmlWriter;
    private GCDMathematica notebookWriter;

    @FXML
    private WebView mathematicaCode;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);
    }

    public void initializeGCDDepended() {
        htmlWriter = new GCDWriterHTML(gcd.gcdController.model);
        notebookWriter = new GCDWriterNotebook(gcd.gcdController.model);
    }

    public void clear() {
        mathematicaCode.getEngine().loadContent("<html></html>");
    }

    public void fillData() {
        mathematicaCode.getEngine().loadContent(htmlWriter.generate());
        // TODO notebook output
    }

    public void showMathematicaWindow() {
        clear();
        Stage popup = new Stage();
        popup.setScene(gcd.mathematicaScene);
        popup.setTitle(resources.getString("mathematica.window.title"));
        popup.initOwner(gcd.primaryStage);
        popup.initModality(Modality.WINDOW_MODAL);
        addStageCloseOnEscapeKey(popup, gcd.mathematicaScene);
        fillData();
        popup.showAndWait();
    }
}
