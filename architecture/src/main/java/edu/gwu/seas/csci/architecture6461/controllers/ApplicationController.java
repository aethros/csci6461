package edu.gwu.seas.csci.architecture6461.controllers;

import java.net.URL;
import java.util.ResourceBundle;

import edu.gwu.seas.csci.architecture6461.models.CPU;
import edu.gwu.seas.csci.architecture6461.views.RegisterView;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

public class ApplicationController implements Initializable {
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

    private CPU cpu;

    public ApplicationController(CPU cpu) {
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
    }
}
