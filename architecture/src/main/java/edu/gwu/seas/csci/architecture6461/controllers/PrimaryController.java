package edu.gwu.seas.csci.architecture6461.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import edu.gwu.seas.csci.architecture6461.App;
import edu.gwu.seas.csci.architecture6461.views.SecondaryView;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

public class PrimaryController implements Initializable {

    @FXML
    private void switchToSecondary() throws IOException {
        App.setView(new SecondaryView());
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // No initialization needed.
    }
}
