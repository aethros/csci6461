package edu.gwu.seas.csci.architecture6461.models;

import lombok.Getter;
import lombok.Setter;

public final class CPU {
    private static CPU instance;

    @Getter @Setter private Register programCounter = new Register(12);
    @Getter @Setter private Register memoryAddressRegister = new Register(12);
    @Getter @Setter private Register conditionCode = new Register(4);
    @Getter @Setter private Register machineFaultRegister = new Register(4);
    @Getter @Setter private Register memoryBufferRegister = new Register(Register.MAX_SIZE);
    @Getter @Setter private Register instructionRegister = new Register(Register.MAX_SIZE);
    @Getter @Setter private Register indexRegister1 = new Register(Register.MAX_SIZE);
    @Getter @Setter private Register indexRegister2 = new Register(Register.MAX_SIZE);
    @Getter @Setter private Register indexRegister3 = new Register(Register.MAX_SIZE);
    @Getter @Setter private Register gpRegister0 = new Register(Register.MAX_SIZE);
    @Getter @Setter private Register gpRegister1 = new Register(Register.MAX_SIZE);
    @Getter @Setter private Register gpRegister2 = new Register(Register.MAX_SIZE);
    @Getter @Setter private Register gpRegister3 = new Register(Register.MAX_SIZE);

    private CPU() { }

    public static CPU getInstance() {
        if (instance == null) {
            instance = new CPU();
        }

        return instance;
    }

    public void reset(Memory memory)
    {
        memory.initialize(2047); // TODO: consider memory size
    }
}
