package edu.gwu.seas.csci.architecture6461.controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Level;
import java.util.logging.Logger;

import edu.gwu.seas.csci.architecture6461.managers.SessionManager;
import edu.gwu.seas.csci.architecture6461.models.CPU;
import edu.gwu.seas.csci.architecture6461.views.CacheView;
import edu.gwu.seas.csci.architecture6461.views.IOView;
import edu.gwu.seas.csci.architecture6461.views.RegisterView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class CPUController implements Initializable {
    private static final Logger LOGGER = Logger.getLogger("CPUController");

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
    @FXML
    private Button cacheButton;
    @FXML
    private Button ioButton;

    private CPU cpu;

    private boolean cacheVisible = false;
    private boolean ioVisible = false;
    private Stage cacheStage;
    private Stage ioStage;

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

        try {
            this.cacheStage = new Stage();
            this.cacheStage.setTitle("Cache");
            this.cacheStage.setScene(new Scene(new CacheView()));
            this.cacheStage.hide();

            this.ioStage = new Stage();
            this.ioStage.setTitle("I/O");
            this.ioStage.setScene(new Scene(new IOView()));
            this.ioStage.hide();
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Failed to open a secondary window.", e);
        }

        CompletableFuture.runAsync(() -> SessionManager.getInstance().start(false));
    }

    @FXML
    private void loadButtonEvent(ActionEvent event) {
        CompletableFuture.runAsync(() -> {
            SessionManager.getInstance().getControlUnit().HLT(true);
            int addr = this.cpu.getMemoryAddressRegister().getValue();
            int data = SessionManager.getInstance().getControlUnit().getMemoryInterface().getValue(addr);
            this.cpu.getMemoryBufferRegister().setValue(data);

            LOGGER.log(Level.INFO, String.format("Loading '%d' from Memory Address: %d", data, addr));
        });
    }

    @FXML
    private void loadPlusButtonEvent(ActionEvent event) {
        CompletableFuture.runAsync(() -> {
            SessionManager.getInstance().getControlUnit().HLT(true);
            int addr = this.cpu.getMemoryAddressRegister().getValue();
            int data = SessionManager.getInstance().getControlUnit().getMemoryInterface().getValue(addr);
            this.cpu.getMemoryBufferRegister().setValue(data);

            LOGGER.log(Level.INFO, String.format("Loading '%d' from Memory Address: %d", data, addr));
            this.cpu.getMemoryAddressRegister().setValue(addr + 1);
        });
    }

    @FXML
    private void storeButtonEvent(ActionEvent event) {
        CompletableFuture.runAsync(() -> {
            SessionManager.getInstance().getControlUnit().HLT(true);
            int addr = this.cpu.getMemoryAddressRegister().getValue();
            int data = this.cpu.getMemoryBufferRegister().getValue();
            SessionManager.getInstance().getControlUnit().getMemoryInterface().setValue(addr, data);

            LOGGER.log(Level.INFO, String.format("Storing '%d' to Memory Address: %d", data, addr));
        });
    }

    @FXML
    private void storePlusButtonEvent(ActionEvent event) {
        CompletableFuture.runAsync(() -> {
            SessionManager.getInstance().getControlUnit().HLT(true);
            int addr = this.cpu.getMemoryAddressRegister().getValue();
            int data = this.cpu.getMemoryBufferRegister().getValue();
            SessionManager.getInstance().getControlUnit().getMemoryInterface().setValue(addr, data);

            LOGGER.log(Level.INFO, String.format("Storing '%d' to Memory Address: %d", data, addr));
            this.cpu.getMemoryAddressRegister().setValue(addr + 1);
        });
    }

    @FXML
    private void startButtonEvent(ActionEvent event) {
        LOGGER.info("Starting Program...");
        CompletableFuture.runAsync(() -> SessionManager.getInstance().start(true));
    }

    @FXML
    private void stepButtonEvent(ActionEvent event) {
        LOGGER.info("Single Step");
        CompletableFuture.runAsync(() -> SessionManager.getInstance().getControlUnit().singleStep());
    }

    @FXML
    private void haltButtonEvent(ActionEvent event) {
        LOGGER.info("Halting Program");
        CompletableFuture.runAsync(() -> SessionManager.getInstance().getControlUnit().HLT(true));
    }

    @FXML
    private void cacheButtonEvent(ActionEvent event) {
        this.cacheVisible = !this.cacheVisible;
        if (this.cacheVisible) {
            this.cacheStage.show();
        } else {
            this.cacheStage.hide();
        }
    }

    @FXML
    private void ioButtonEvent(ActionEvent event) {
        this.ioVisible = !this.ioVisible;
        if (this.ioVisible) {
            this.ioStage.show();
        } else {
            this.ioStage.hide();
        }
    }
}
