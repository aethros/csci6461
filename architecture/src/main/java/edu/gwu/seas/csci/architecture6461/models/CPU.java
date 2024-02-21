package edu.gwu.seas.csci.architecture6461.models;

import lombok.Getter;
import lombok.Setter;

public final class CPU {
    private static CPU instance;

    @Getter
    @Setter
    private Register programCounter = new Register(12, "PC");
    @Getter
    @Setter
    private Register memoryAddressRegister = new Register(12, "MAR");
    @Getter
    @Setter
    private Register conditionCode = new Register(4, "CC");
    @Getter
    @Setter
    private Register machineFaultRegister = new Register(4, "MFR");
    @Getter
    @Setter
    private Register memoryBufferRegister = new Register(Register.MAX_SIZE, "MBR");
    @Getter
    @Setter
    private Register instructionRegister = new Register(Register.MAX_SIZE, "IR");
    @Getter
    @Setter
    private Register indexRegister1 = new Register(Register.MAX_SIZE, "X1");
    @Getter
    @Setter
    private Register indexRegister2 = new Register(Register.MAX_SIZE, "X2");
    @Getter
    @Setter
    private Register indexRegister3 = new Register(Register.MAX_SIZE, "X3");
    @Getter
    @Setter
    private Register gpRegister0 = new Register(Register.MAX_SIZE, "R0");
    @Getter
    @Setter
    private Register gpRegister1 = new Register(Register.MAX_SIZE, "R1");
    @Getter
    @Setter
    private Register gpRegister2 = new Register(Register.MAX_SIZE, "R2");
    @Getter
    @Setter
    private Register gpRegister3 = new Register(Register.MAX_SIZE, "R3");

    private CPU() {
    }

    public static CPU getInstance() {
        if (instance == null) {
            instance = new CPU();
        }

        return instance;
    }

    public void reset() {
        this.programCounter.setValue(0);
        this.memoryAddressRegister.setValue(0);
        this.conditionCode.setValue(0);
        this.machineFaultRegister.setValue(0);
        this.memoryBufferRegister.setValue(0);
        this.instructionRegister.setValue(0);
        this.indexRegister1.setValue(0);
        this.indexRegister2.setValue(0);
        this.indexRegister3.setValue(0);
        this.gpRegister0.setValue(0);
        this.gpRegister1.setValue(0);
        this.gpRegister2.setValue(0);
        this.gpRegister3.setValue(0);
    }
}
