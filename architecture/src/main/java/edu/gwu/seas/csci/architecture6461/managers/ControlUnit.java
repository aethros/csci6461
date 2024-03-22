package edu.gwu.seas.csci.architecture6461.managers;

import java.util.logging.Level;
import java.util.logging.Logger;

import edu.gwu.seas.csci.architecture6461.models.CPU;
import edu.gwu.seas.csci.architecture6461.models.MachineInstruction;
import edu.gwu.seas.csci.architecture6461.models.Memory;
import edu.gwu.seas.csci.architecture6461.models.Register;
import edu.gwu.seas.csci.architecture6461.models.MachineInstruction.Operand;
import edu.gwu.seas.csci.architecture6461.util.InstructionUtils;
import edu.gwu.seas.csci.architecture6461.util.InstructionUtils.MachineFaultCode;
import edu.gwu.seas.csci.architecture6461.util.InstructionUtils.Opcode;
import lombok.Getter;
import lombok.val;

public final class ControlUnit {
    private static final Logger LOGGER = Logger.getLogger(ControlUnit.class.getName());

    @Getter
    private Memory memory = Memory.getInstance();
    @Getter
    private CPU cpu = CPU.getInstance();

    private boolean isRunning = false;

    public ControlUnit() {
        // No need to initialize anything here
    }

    /**
     * Resets the Control Unit.
     */
    public void reset() {
        LOGGER.info("Resetting the Control Unit.");
        this.cpu.reset();
        this.memory.initialize(2048);
    }

    /**
     * Starts the Control Unit.
     */
    public void start() {
        LOGGER.info("Starting the Control Unit.");
        this.isRunning = true;
        while (this.isRunning) {
            this.singleStep();
        }
    }

    /**
     * Executes a single step of the Control Unit.
     */
    public void singleStep() {
        // Fetch
        this.fetch(
            this.cpu.getMemoryAddressRegister(),
            this.cpu.getProgramCounter(),
            this.cpu.getMemoryBufferRegister(),
            this.cpu.getInstructionRegister());

        // Decode
        var instruction = this.decode(this.cpu.getInstructionRegister());

        // Execute Instruction
        this.executeInstruction(instruction);
    }

    /**
     * Sets the running state of the Control Unit.
     *
     * @param running The running state of the Control Unit.
     */
    public void setRunning(boolean running) {
        this.isRunning = running;
    }

    /**
     * Fetches an instruction from memory and updates the necessary registers.
     *
     * @param MAR The Memory Address Register.
     * @param PC The Program Counter.
     * @param MBR The Memory Buffer Register.
     * @param IR The Instruction Register.
     * @return The value of the Instruction Register.
     */
    public int fetch(final Register MAR, final Register PC, final Register MBR, final Register IR) {
        LOGGER.log(Level.INFO, "Fetching instruction from address: {0}.", PC.getValue());
        // Set the Memory Address Register to the Program Counter
        MAR.setValue(PC.getValue());

        // Increment the Program Counter
        PC.setValue(PC.getValue() + 1);

        // Set the Memory Buffer Register to the value at the Memory Address Register
        var memoryLine = this.memory.getValue(MAR.getValue());
        this.checkAddress(memoryLine);
        MBR.setValue(memoryLine);

        // Set the Instruction Register to the Memory Buffer Register
        IR.setValue(MBR.getValue());


        LOGGER.log(Level.INFO, "Instruction: {0} fetched from memory.", IR.getValue());
        return IR.getValue();
    }

    /**
     * Decodes an instruction and returns the Machine Instruction.
     *
     * @param IR The Instruction Register.
     * @return The Machine Instruction.
     */
    public MachineInstruction decode(final Register IR) {
        val maskedInstruction = InstructionUtils.maskInstruction(IR.getValue());
        val code = InstructionUtils.opcodeFromInstruction(maskedInstruction);

        if (code == Opcode.HLT) {
            return this.decodeHaltInstruction();
        } else {
            return this.decodeInstr(maskedInstruction, code);
        }
    }

    /**
     * Executes an instruction.
     *
     * @param instruction The Machine Instruction to execute.
     */
    public void executeInstruction(MachineInstruction instruction) {
        LOGGER.log(Level.INFO, "Executing instruction: {0}.", instruction.toString());
        Opcode code = instruction.getOpcode();

        switch (code) {
            case LDR: {
                Register register = this.getRegister(instruction);
                int address = this.getEffectiveAddress(instruction);

                register.setValue(this.memory.getValue(address));
                LOGGER.log(Level.INFO, String.format("Loaded value %d from memory address %d into register %s.", register.getValue(), address, register.getName()));
            }
                break;
            case STR: {
                Register register = this.getRegister(instruction);
                int address = this.getEffectiveAddress(instruction);

                this.memory.setValue(address, register.getValue());
                LOGGER.log(Level.INFO, String.format("Stored value %d from register %s into memory address %d.", register.getValue(), register.getName(), address));
            }
                break;
            case HLT: {
                LOGGER.info("Halting the CPU.");
                this.isRunning = false;
            }
                break;
            default:
                break;
        }
    }

    private void checkAddress(int instruction) {
        if (instruction == -1) {
            LOGGER.severe("Illegal memory address used beyond 2048.");
            var code = MachineFaultCode.ILLEGALMEMORYADDRESSBEYOND2048;
            int mfrCode = MachineFaultCode.toInteger(code);
            this.cpu.getMachineFaultRegister().setValue(mfrCode);
        }
    }

    private MachineInstruction decodeInstr(final int instruction, final Opcode code) {
        val mInstr = new MachineInstruction(code, instruction);
        mInstr.setOperands();

        LOGGER.log(Level.INFO, "Decoded instruction: {0}.", mInstr.toString());
        return mInstr;
    }

    private MachineInstruction decodeHaltInstruction() {
        int bytes = Opcode.toInteger(Opcode.HLT) << 10;
        LOGGER.log(Level.INFO, "Decoded HLT instruction.");
        return new MachineInstruction(Opcode.HLT, bytes);
    }

    private int getEffectiveAddress(MachineInstruction instruction) {
        int ea;
        val i = instruction.getOperand(Operand.I) > 0;
        val ix = instruction.getOperand(Operand.IX) > 0;
        val address = instruction.getAddress();
        val indexRegister = this.indexRegisterFromInstruction(instruction);

        if (!i) {
            // NO indirect addressing
            if (!ix) {
                ea = address;
            } else {
                ea = indexRegister.getValue() + address;
                // that is, the IX field has an
                // index register number, the contents of that register are
                // added to the contents of the address field
            }
        } else {
            if (!ix) {
                // indirect addressing, but NO indexing
                ea = memory.getValue(address);
                // It helps to think in terms of a
                // pointer where the address field has the location of the EA
                // in memory
            } else {
                // both indirect addressing and indexing
                ea = memory.getValue(indexRegister.getValue() + address);
                // another way to think of this is take the EA computed without
                // indirections and use that as the location of where the EA is stored.
            }
        }
        return ea;
    }

    private Register getRegister(MachineInstruction instruction) {
        val num = instruction.getOperand(Operand.R);
        switch (num) {
            case 0:
                return this.cpu.getGpRegister0();
            case 1:
                return this.cpu.getGpRegister1();
            case 2:
                return this.cpu.getGpRegister2();
            case 3:
                return this.cpu.getGpRegister3();
            default:
                return null;
        }
    }

    private Register indexRegisterFromInstruction(MachineInstruction instruction) {
        val num = instruction.getOperand(Operand.IX);
        switch (num) {
            case 1:
                return this.cpu.getIndexRegister1();
            case 2:
                return this.cpu.getIndexRegister2();
            case 3:
                return this.cpu.getIndexRegister3();
            default:
                return null;
        }
    }
}
