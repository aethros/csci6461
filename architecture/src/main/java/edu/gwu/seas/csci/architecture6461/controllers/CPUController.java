package edu.gwu.seas.csci.architecture6461.controllers;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Level;
import java.util.logging.Logger;

import edu.gwu.seas.csci.architecture6461.managers.SessionManager;
import edu.gwu.seas.csci.architecture6461.models.CPU;
import edu.gwu.seas.csci.architecture6461.views.RegisterView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

public class CPUController implements Initializable {
    private static final Logger LOGGER = Logger.getLogger(CPUController.class.getName());

    @FXML
    private RegisterView programCounterView;
    @FXML
    private RegisterView memoryAddressRegisterView;
    @FXML
    private RegisterView conditionCodeView;
    @FXML
    private RegisterView machineFaultRegisterView;
    @FXML
    private RegisterView memoryBufferRegisterView;
    @FXML
    private RegisterView instructionRegisterView;
    @FXML
    private RegisterView indexRegister1View;
    @FXML
    private RegisterView indexRegister2View;
    @FXML
    private RegisterView indexRegister3View;
    @FXML
    private RegisterView gpRegister0View;
    @FXML
    private RegisterView gpRegister1View;
    @FXML
    private RegisterView gpRegister2View;
    @FXML
    private RegisterView gpRegister3View;
    @FXML
    private Button loadButton;
    @FXML
    private Button loadPlusButton;
    @FXML
    private Button storeButton;
    @FXML
    private Button storePlusButton;
    @FXML
    private Button startButton;
    @FXML
    private Button stepButton;
    @FXML
    private Button haltButton;

    private CPU cpu;

    public CPUController(CPU cpu) {
        this.cpu = cpu;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        programCounterView.setRegister("programCounter", this.cpu.getProgramCounter());
        memoryAddressRegisterView.setRegister("memoryAddressRegister", this.cpu.getMemoryAddressRegister());
        conditionCodeView.setRegister("conditionCode", this.cpu.getConditionCode());
        machineFaultRegisterView.setRegister("machineFaultRegister", this.cpu.getMachineFaultRegister());
        memoryBufferRegisterView.setRegister("memoryBufferRegister", this.cpu.getMemoryBufferRegister());
        instructionRegisterView.setRegister("instructionRegister", this.cpu.getInstructionRegister());
        indexRegister1View.setRegister("indexRegister1", this.cpu.getIndexRegister1());
        indexRegister2View.setRegister("indexRegister2", this.cpu.getIndexRegister2());
        indexRegister3View.setRegister("indexRegister3", this.cpu.getIndexRegister3());
        gpRegister0View.setRegister("gpRegister0", this.cpu.getGpRegister0());
        gpRegister1View.setRegister("gpRegister1", this.cpu.getGpRegister1());
        gpRegister2View.setRegister("gpRegister2", this.cpu.getGpRegister2());
        gpRegister3View.setRegister("gpRegister3", this.cpu.getGpRegister3());

        CompletableFuture.runAsync(() -> SessionManager.getInstance().start(false));
    }

    public void loadButtonEvent(ActionEvent event) {
        CompletableFuture.runAsync(() -> {
            SessionManager.getInstance().getControlUnit().HLT(true);
            int addr = this.cpu.getMemoryAddressRegister().getValue();
            int data = SessionManager.getInstance().getControlUnit().getDataInterface().getValue(addr);
            this.cpu.getMemoryBufferRegister().setValue(data);

            LOGGER.log(Level.INFO, String.format("Loading '%d' from Memory Address: %d", data, addr));
        });
    }

    public void loadPlusButtonEvent(ActionEvent event) {
        CompletableFuture.runAsync(() -> {
            SessionManager.getInstance().getControlUnit().HLT(true);
            int addr = this.cpu.getMemoryAddressRegister().getValue();
            int data = SessionManager.getInstance().getControlUnit().getDataInterface().getValue(addr);
            this.cpu.getMemoryBufferRegister().setValue(data);

            LOGGER.log(Level.INFO, String.format("Loading '%d' from Memory Address: %d", data, addr));
            this.cpu.getMemoryAddressRegister().setValue(addr + 1);
        });
    }

    public void storeButtonEvent(ActionEvent event) {
        CompletableFuture.runAsync(() -> {
            SessionManager.getInstance().getControlUnit().HLT(true);
            int addr = this.cpu.getMemoryAddressRegister().getValue();
            int data = this.cpu.getMemoryBufferRegister().getValue();
            SessionManager.getInstance().getControlUnit().getDataInterface().setValue(addr, data);

            LOGGER.log(Level.INFO, String.format("Storing '%d' to Memory Address: %d", data, addr));
        });
    }

    public void storePlusButtonEvent(ActionEvent event) {
        CompletableFuture.runAsync(() -> {
            SessionManager.getInstance().getControlUnit().HLT(true);
            int addr = this.cpu.getMemoryAddressRegister().getValue();
            int data = this.cpu.getMemoryBufferRegister().getValue();
            SessionManager.getInstance().getControlUnit().getDataInterface().setValue(addr, data);

            LOGGER.log(Level.INFO, String.format("Storing '%d' to Memory Address: %d", data, addr));
            this.cpu.getMemoryAddressRegister().setValue(addr + 1);
        });
    }

    public void startButtonEvent(ActionEvent event) {
        LOGGER.info("Starting Program...");
        CompletableFuture.runAsync(() -> SessionManager.getInstance().start(true));
    }

    public void stepButtonEvent(ActionEvent event) {
        LOGGER.info("Single Step");
        CompletableFuture.runAsync(() -> SessionManager.getInstance().getControlUnit().singleStep());
    }

    public void haltButtonEvent(ActionEvent event) {
        LOGGER.info("Halting Program");
        CompletableFuture.runAsync(() -> SessionManager.getInstance().getControlUnit().HLT(true));
    }
}
