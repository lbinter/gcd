package at.binter.gcd.controller;

import at.binter.gcd.GCDApplication;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

public abstract class BaseController implements Initializable {
    protected URL location;
    protected ResourceBundle resources;
    protected GCDApplication gcd;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.location = location;
        this.resources = resources;
    }

    public void setApplication(GCDApplication gcd) {
        this.gcd = gcd;
    }
}
