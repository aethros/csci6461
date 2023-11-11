package edu.gwu.seas.csci.architecture6461.controllers;

import java.io.IOException;
import javafx.fxml.FXML;

import edu.gwu.seas.csci.architecture6461.App;

public class PrimaryController {

    @FXML
    private void switchToSecondary() throws IOException {
        App.setRoot("secondary");
    }
}
