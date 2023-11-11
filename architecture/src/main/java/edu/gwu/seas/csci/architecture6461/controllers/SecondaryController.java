package edu.gwu.seas.csci.architecture6461.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import edu.gwu.seas.csci.architecture6461.App;
import edu.gwu.seas.csci.architecture6461.views.PrimaryView;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

public class SecondaryController implements Initializable  {

    @FXML
    private void switchToPrimary() throws IOException {
        App.setView(new PrimaryView());
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // No initialization needed.
    }
}