package at.binter.gcd.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.util.ResourceBundle;

public class SettingsController extends BaseController implements Initializable {
    private static final Logger log = LoggerFactory.getLogger(SettingsController.class);

    @FXML
    private TextField jLink;
    @FXML
    private TextField mathKernel;
    @FXML
    private TextField defaultFolder;
    @FXML
    private TextField lastOpened;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        super.initialize(location, resources);

    }
}