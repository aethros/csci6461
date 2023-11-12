package edu.gwu.seas.csci.architecture6461.models;

import lombok.Getter;
import lombok.Setter;

public final class CPU {
    private static CPU instance;

    @Getter @Setter private Register programCounter = new Register(12);
    @Getter @Setter private Register memoryAddressRegister = new Register(12);
    @Getter @Setter private Register conditionCode = new Register(4);
    @Getter @Setter private Register machineFaultRegister = new Register(4);
    @Getter @Setter private Register memoryBufferRegister = new Register(16);
    @Getter @Setter private Register instructionRegister = new Register(16);
    @Getter @Setter private Register indexRegister1 = new Register(16);
    @Getter @Setter private Register indexRegister2 = new Register(16);
    @Getter @Setter private Register indexRegister3 = new Register(16);

    private CPU() { }

    public static CPU getInstance() {
        if (instance == null) {
            instance = new CPU();
        }

        return instance;
    }

    public void reset(Memory memory)
    {
        memory.initialize();
    }
}
