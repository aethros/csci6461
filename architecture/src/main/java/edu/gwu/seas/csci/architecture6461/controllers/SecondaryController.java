package edu.gwu.seas.csci.architecture6461.controllers;

import java.io.IOException;
import javafx.fxml.FXML;

import edu.gwu.seas.csci.architecture6461.App;

public class SecondaryController {

    @FXML
    private void switchToPrimary() throws IOException {
        App.setRoot("primary");
    }
}