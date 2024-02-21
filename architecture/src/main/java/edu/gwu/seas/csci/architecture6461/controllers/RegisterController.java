package edu.gwu.seas.csci.architecture6461.controllers;

import java.net.URL;
import java.util.BitSet;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import edu.gwu.seas.csci.architecture6461.models.Register;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import lombok.val;

public class RegisterController implements Initializable {
    private static final Logger LOGGER = Logger.getLogger(RegisterController.class.getName());
    @FXML
    private Label label;
    @FXML
    private Rectangle oneBit;
    @FXML
    private Rectangle twoBit;
    @FXML
    private Rectangle threeBit;
    @FXML
    private Rectangle fourBit;
    @FXML
    private Rectangle fiveBit;
    @FXML
    private Rectangle sixBit;
    @FXML
    private Rectangle sevenBit;
    @FXML
    private Rectangle eightBit;
    @FXML
    private Rectangle nineBit;
    @FXML
    private Rectangle tenBit;
    @FXML
    private Rectangle elevenBit;
    @FXML
    private Rectangle twelveBit;
    @FXML
    private Rectangle thirteenBit;
    @FXML
    private Rectangle fourteenBit;
    @FXML
    private Rectangle fifteenBit;
    @FXML
    private Rectangle sixteenBit;
    @FXML
    private Button button;

    private String name; // The name of the register.
    private Register register; // The register that this controller is bound to.
    private BitSet bitSet; // The bit set that represents the register's value.
    private Rectangle[] rectangleArray; // Cached set of rectangles for the register.

    public RegisterController() {
        // No initialization needed.
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // No initialization needed.
    }

    public void setRegister(String name, Register register) {
        this.name = name;
        this.register = register;
        this.bitSet = new BitSet(this.register.getSize());
        this.bitSet.set(0, this.register.getSize(), false);
        this.register.valueProperty().addListener(this::registerChangeEventListener);
        this.initializeView();
    }

    /**
     * This method is called when a bit is clicked.
     * 
     * @param event The mouse event.
     */
    @FXML
    private void handleClicked(MouseEvent event) {
        if (this.name.equals("conditionCode") || this.name.equals("machineFaultRegister")) {
            // Special case: these registers are read-only.
            return;
        }

        val sender = (Rectangle) event.getSource(); // The rectangle that was clicked.
        val senderName = sender.getId();
        if (sender.getFill() == Color.DODGERBLUE) {
            sender.setFill(Color.MEDIUMBLUE);
        } else {
            sender.setFill(Color.DODGERBLUE);
        }

        switch (senderName) {
            case "oneBit":
                this.bitSet.flip(0);
                break;
            case "twoBit":
                this.bitSet.flip(1);
                break;
            case "threeBit":
                this.bitSet.flip(2);
                break;
            case "fourBit":
                this.bitSet.flip(3);
                break;
            case "fiveBit":
                this.bitSet.flip(4);
                break;
            case "sixBit":
                this.bitSet.flip(5);
                break;
            case "sevenBit":
                this.bitSet.flip(6);
                break;
            case "eightBit":
                this.bitSet.flip(7);
                break;
            case "nineBit":
                this.bitSet.flip(8);
                break;
            case "tenBit":
                this.bitSet.flip(9);
                break;
            case "elevenBit":
                this.bitSet.flip(10);
                break;
            case "twelveBit":
                this.bitSet.flip(11);
                break;
            case "thirteenBit":
                this.bitSet.flip(12);
                break;
            case "fourteenBit":
                this.bitSet.flip(13);
                break;
            case "fifteenBit":
                this.bitSet.flip(14);
                break;
            case "sixteenBit":
                this.bitSet.flip(15);
                break;
        }
    }

    /**
     * This method is called when the "Load" button is clicked.
     */
    @FXML
    private void loadValue() {
        val value = this.bitSet.isEmpty() ? 0 : this.bitSet.toLongArray()[0];
        this.register.setValue((int) value);
    }

    /**
     * This method is called when the value of the register changes.
     * 
     * @param observable The register's value property.
     * @param oldValue   The old value of the register.
     * @param newValue   The new value of the register.
     */
    private void registerChangeEventListener(ObservableValue<? extends Number> observable, Number oldValue,
            Number newValue) {
        if (Thread.currentThread().getStackTrace()[8].getClassName().equals(this.getClass().getName())) {
            // This listener can get called as a result of the loadValue() method, which
            // will be 8 frames up the stack.
            // If that's the case, we don't want to update the view.
            return;
        }

        this.bitSet = BitSet.valueOf(new long[] { newValue.longValue() });
        for (int i = 0; i < this.register.getSize(); i++) {
            if (this.bitSet.get(i)) {
                this.rectangleArray[i].setFill(Color.DODGERBLUE);
            } else {
                this.rectangleArray[i].setFill(Color.MEDIUMBLUE);
            }
        }
    }

    /**
     * Initializes the fxml view-based components of the register.
     * This method is only called once, when the register is first created.
     */
    private void initializeView() {
        LOGGER.log(Level.INFO, "RegisterView: {0} loaded.", name);
        this.rectangleArray = new Rectangle[] { oneBit, twoBit, threeBit, fourBit, fiveBit, sixBit, sevenBit, eightBit,
                nineBit, tenBit, elevenBit, twelveBit, thirteenBit, fourteenBit, fifteenBit, sixteenBit };
        for (Rectangle rectangle : this.rectangleArray) {
            rectangle.setVisible(false);
        }

        for (int i = 0; i < this.register.getSize(); i++) {
            this.rectangleArray[i].setVisible(true);
        }

        switch (this.name) {
            case "programCounter":
                this.label.setText("PC");
                break;
            case "memoryAddressRegister":
                this.label.setText("MAR");
                break;
            case "conditionCode":
                this.label.setText("CC");
                this.button.setVisible(false);
                break;
            case "machineFaultRegister":
                this.label.setText("MFR");
                this.button.setVisible(false);
                break;
            case "memoryBufferRegister":
                this.label.setText("MBR");
                break;
            case "instructionRegister":
                this.label.setText("IR");
                break;
            case "indexRegister1":
                this.label.setText("IX1");
                break;
            case "indexRegister2":
                this.label.setText("IX2");
                break;
            case "indexRegister3":
                this.label.setText("IX3");
                break;
            case "gpRegister0":
                this.label.setText("GP0");
                break;
            case "gpRegister1":
                this.label.setText("GP1");
                break;
            case "gpRegister2":
                this.label.setText("GP2");
                break;
            case "gpRegister3":
                this.label.setText("GP3");
                break;
            default:
                break;
        }
    }
}