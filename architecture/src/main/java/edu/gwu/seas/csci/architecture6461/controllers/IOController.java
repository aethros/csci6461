package edu.gwu.seas.csci.architecture6461.controllers;

import java.net.URL;
import java.util.ResourceBundle;

import edu.gwu.seas.csci.architecture6461.managers.SessionManager;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

public class IOController implements Initializable {
    @FXML
    private TextField inputField;
    @FXML
    private Text outputText;

    public IOController() {
        this.setupHandlers();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // No initialization needed.
    }

    private void setupHandlers() {
        SessionManager.getInstance().getControlUnit().getInput = this::getInput;
        SessionManager.getInstance().getControlUnit().sendOutput = this::sendOutput;
        SessionManager.getInstance().getControlUnit().checkStatus = this::checkStatus;
    }

    private char getInput() {
        char input = inputField.getText().charAt(0);
        inputField.setText(inputField.getText().substring(1));
        return input;
    }

    private void sendOutput(char output) {
        this.outputText.setText(this.outputText.getText() + output);
    }

    private int checkStatus() {
        return (this.inputField.getText().length() > 0) ? 1 : 0;
    }
}
