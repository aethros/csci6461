package edu.gwu.seas.csci.architecture6461.controllers;

import java.net.URL;
import java.util.BitSet;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import edu.gwu.seas.csci.architecture6461.models.Register;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

public class RegisterController implements Initializable {


    private static final Logger LOGGER = Logger.getLogger(RegisterController.class.getName());
    private String name;
    private BitSet bitSet;

    public RegisterController() {
        // No initialization needed.
    }

    public void setRegister(String name, Register register) {
        this.name = name;
        this.bitSet = new BitSet(register.getSize());
        this.bitSet.toString();
    }

    @FXML
    private void switchToPrimary() {
        LOGGER.log(Level.INFO, "Register: {0} clicked.", name);
        this.bitSet.toString();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // No initialization needed.
    }
}