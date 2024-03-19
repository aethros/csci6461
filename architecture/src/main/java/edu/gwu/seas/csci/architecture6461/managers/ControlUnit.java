package edu.gwu.seas.csci.architecture6461.managers;

import java.util.logging.Level;
import java.util.logging.Logger;

import edu.gwu.seas.csci.architecture6461.models.CPU;
import edu.gwu.seas.csci.architecture6461.models.MachineInstruction;
import edu.gwu.seas.csci.architecture6461.models.Memory;
import edu.gwu.seas.csci.architecture6461.models.Register;
import edu.gwu.seas.csci.architecture6461.models.MachineInstruction.Operand;
import edu.gwu.seas.csci.architecture6461.util.InstructionUtils;
import edu.gwu.seas.csci.architecture6461.util.InstructionUtils.ConditionCode;
import edu.gwu.seas.csci.architecture6461.util.InstructionUtils.MachineFaultCode;
import edu.gwu.seas.csci.architecture6461.util.InstructionUtils.Opcode;
import lombok.Getter;
import lombok.val;

public final class ControlUnit {
    private static final Logger LOGGER = Logger.getLogger(ControlUnit.class.getName());
    private static final String LOG_JUMP = "Jumped to memory address {0}.";

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
        // Check Instruction Cache
        // TODO: Implement Cache

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
        } else if (Opcode.toInteger(code) == 63) {
            LOGGER.log(Level.INFO, "Decoded illegal instruction: {0}.", maskedInstruction);
            this.cpu.getMachineFaultRegister().setValue(MachineFaultCode.toInteger(MachineFaultCode.ILLEGALOPERATIONCODE));
            return new MachineInstruction(null, maskedInstruction);
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
            case LDR: this.LDR(instruction);
                break;
            case STR: this.STR(instruction);
                break;
            case LDA: this.LDA(instruction);
                break;
            case LDX: this.LDX(instruction);
                break;
            case STX: this.STX(instruction);
                break;
            case SETCCE: this.SETCCE(instruction);
                break;
            case JZ: this.JZ(instruction);
                break;
            case JNE: this.JNE(instruction);
                break;
            case JCC: this.JCC(instruction);
                break;
            case JMA: this.JMA(instruction);
                break;
            case JSR: this.JSR(instruction);
                break;
            case RFS: this.RFS(instruction);
                break;
            case SOB: this.SOB(instruction);
                break;
            case JGE: this.JGE(instruction);
                break;
            case AMR: this.AMR(instruction);
                break;
            case SMR: this.SMR(instruction);
                break;
            case AIR: this.AIR(instruction);
                break;
            case SIR: this.SIR(instruction);
                break;
            case MLT: this.MLT(instruction);
                break;
            case DVD: this.DVD(instruction);
                break;
            case TRR: this.TRR(instruction);
                break;
            case AND: this.AND(instruction);
                break;
            case ORR: this.ORR(instruction);
                break;
            case NOT: this.NOT(instruction);
                break;
            case SRC: this.SRC(instruction);
                break;
            case RRC: this.RRC(instruction);
                break;
            case IN: this.IN(instruction);
                break;
            case OUT: this.OUT(instruction);
                break;
            case HLT: this.HLT(false);
                break;
            default:
                break;
        }
    }

    public void HLT(boolean silent) {
        if (!silent) {
            LOGGER.info("Halting the CPU.");
        }
        this.isRunning = false;
    }

    private void OUT(MachineInstruction instruction) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'OUT'");
    }

    private void IN(MachineInstruction instruction) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'IN'");
    }

    private void RRC(MachineInstruction instruction) {
        Register r = this.getGpRegister(instruction, Operand.R);
        int count = instruction.getOperand(Operand.COUNT);
        int left = instruction.getOperand(Operand.LR);
        boolean logicalRotate = instruction.getOperand(Operand.AL) > 0; // Otherwise, arithmetic rotate
        int value = r.getValue();
        if (logicalRotate) {
            if (left > 0) {
                // TODO: Left rotate logical
            } else {
                // TODO: Right rotate logical
            }
        } else {
            if (left > 0) {
                // Left rotate arithmetic
            } else {
                // Right rotate arithmetic
            }
        }
        r.setValue(value);
        LOGGER.log(Level.INFO, String.format("Rotated value %d in register %s by %d bits to the %s.", r.getValue(), r.getName(), count, (left > 0) ? "left" : "right"));
    }

    private void SRC(MachineInstruction instruction) {
        Register r = this.getGpRegister(instruction, Operand.R);
        int count = instruction.getOperand(Operand.COUNT);
        int left = instruction.getOperand(Operand.LR);
        boolean logicalShift = instruction.getOperand(Operand.AL) > 0; // Otherwise, arithmetic shift
        int value = r.getValue();
        if (logicalShift) {
            if (left > 0) {
                // TODO: Left shift logical
            } else {
                // TODO: Right shift logical
            }
        } else {
            if (left > 0) {
                // TODO: Left shift arithmetic
            } else {
                // TODO: Right shift arithmetic
            }
        }
        r.setValue(value);
        LOGGER.log(Level.INFO, String.format("Shifted value %d in register %s by %d bits to the %s.", r.getValue(), r.getName(), count, (left > 0) ? "left" : "right"));
    }

    private void NOT(MachineInstruction instruction) {
        Register rx = this.getGpRegister(instruction, Operand.RX);
        rx.setValue(~rx.getValue());
        LOGGER.log(Level.INFO, String.format("Bitwise NOTed value %d and stored the result in register %s.", rx.getValue(), rx.getName()));
    }

    private void ORR(MachineInstruction instruction) {
        Register rx = this.getGpRegister(instruction, Operand.RX);
        Register ry = this.getGpRegister(instruction, Operand.RY);
        rx.setValue(rx.getValue() | ry.getValue());
        LOGGER.log(Level.INFO, String.format("Bitwise ORed values %d and %d and stored the result in register %s.", rx.getValue(), ry.getValue(), rx.getName()));
    }

    private void AND(MachineInstruction instruction) {
        Register rx = this.getGpRegister(instruction, Operand.RX);
        Register ry = this.getGpRegister(instruction, Operand.RY);
        rx.setValue(rx.getValue() & ry.getValue());
        LOGGER.log(Level.INFO, String.format("Bitwise ANDed values %d and %d and stored the result in register %s.", rx.getValue(), ry.getValue(), rx.getName()));
    }

    private void TRR(MachineInstruction instruction) {
        Register rx = this.getGpRegister(instruction, Operand.RX);
        Register ry = this.getGpRegister(instruction, Operand.RY);
        if (rx.getValue() == ry.getValue()) {
            this.cpu.getConditionCode().setValue(this.cpu.getConditionCode().getValue() | ConditionCode.toInteger(ConditionCode.EQUALORNOT));
        } else {
            this.cpu.getConditionCode().setValue(this.cpu.getConditionCode().getValue() & ~ConditionCode.toInteger(ConditionCode.EQUALORNOT));
        }
        LOGGER.log(Level.INFO, String.format("Compared values %d and %d and set the condition code 'EQUALORNOT' to %b.", rx.getValue(), ry.getValue(), (rx.getValue() == ry.getValue())));
    }

    private void DVD(MachineInstruction instruction) {
        Register rx = this.getGpRegister(instruction, Operand.RX);
        Register ry = this.getGpRegister(instruction, Operand.RY);
        Register rxP1 = this.getrxP1Register(rx); // RX + 1
        int quotient = rx.getValue() / ry.getValue();
        int remainder = rx.getValue() % ry.getValue();
        rx.setValue(quotient);
        rxP1.setValue(remainder);
        if (ry.getValue() == 0) {
            this.cpu.getConditionCode().setValue(this.cpu.getConditionCode().getValue() | ConditionCode.toInteger(ConditionCode.DIVZERO));
        }
        LOGGER.log(Level.INFO, String.format("Divided values %d by %d and stored the quotient in register %s and the remainder in register %s.", rx.getValue(), ry.getValue(), rx.getName(), rxP1.getName()));
    }

    private void MLT(MachineInstruction instruction) {
        Register rx = this.getGpRegister(instruction, Operand.RX);
        Register ry = this.getGpRegister(instruction, Operand.RY);
        Register rxP1 = this.getrxP1Register(rx); // RX + 1
        int value = rx.getValue() * ry.getValue();
        int low = value & 0xFFFF;
        int high = (value & 0xFFFF0000) >> 16;
        rx.setValue(high);
        rxP1.setValue(low);
        if (value < rx.getValue() || value < ry.getValue()) {
            this.cpu.getConditionCode().setValue(this.cpu.getConditionCode().getValue() | ConditionCode.toInteger(ConditionCode.OVERFLOW));
        }
        LOGGER.log(Level.INFO, String.format("Multiplied values %d and %d and stored the result in registers %s and %s.", rx.getValue(), ry.getValue(), rx.getName(), rxP1.getName()));
    }

    private void SIR(MachineInstruction instruction) {
        Register register = this.getGpRegister(instruction, Operand.R);
        int immediate = instruction.getAddress();
        register.setValue(register.getValue() - immediate);
        LOGGER.log(Level.INFO, String.format("Subtracted immediate value %d from register %s.", immediate, register.getName()));
    }

    private void AIR(MachineInstruction instruction) {
        Register register = this.getGpRegister(instruction, Operand.R);
        int immediate = instruction.getAddress();
        register.setValue(register.getValue() + immediate);
        LOGGER.log(Level.INFO, String.format("Added immediate value %d to register %s.", immediate, register.getName()));
    }

    private void SMR(MachineInstruction instruction) {
        Register register = this.getGpRegister(instruction, Operand.R);
        int address = this.getEffectiveAddress(instruction);
        register.setValue(register.getValue() - this.memory.getValue(address));
        LOGGER.log(Level.INFO, String.format("Subtracted value %d from memory address %d from register %s.", this.memory.getValue(address), address, register.getName()));
    }

    private void AMR(MachineInstruction instruction) {
        Register register = this.getGpRegister(instruction, Operand.R);
        int address = this.getEffectiveAddress(instruction);
        register.setValue(register.getValue() + this.memory.getValue(address));
        LOGGER.log(Level.INFO, String.format("Added value %d from memory address %d to register %s.", this.memory.getValue(address), address, register.getName()));
    }

    private void JGE(MachineInstruction instruction) {
        Register register = this.getGpRegister(instruction, Operand.R);
        int address = this.getEffectiveAddress(instruction);
        if (register.getValue() >= 0) {
            this.cpu.getProgramCounter().setValue(address);
            LOGGER.log(Level.INFO, LOG_JUMP, address);
        }
    }

    private void SOB(MachineInstruction instruction) {
        Register register = this.getGpRegister(instruction, Operand.R);
        int address = this.getEffectiveAddress(instruction);
        register.setValue(register.getValue() - 1);
        if (register.getValue() > 0) {
            this.cpu.getProgramCounter().setValue(address);
            LOGGER.log(Level.INFO, LOG_JUMP, address);
        }
    }

    private void RFS(MachineInstruction instruction) {
        int returnCode = instruction.getOperand(Operand.IMMED);
        this.cpu.getProgramCounter().setValue(this.cpu.getGpRegister3().getValue());
        this.cpu.getGpRegister0().setValue(returnCode);
        LOGGER.log(Level.INFO, "Returned from subroutine to memory address {0}.", this.cpu.getProgramCounter().getValue());
    }

    private void JSR(MachineInstruction instruction) {
        int pc = this.cpu.getProgramCounter().getValue();
        int address = this.getEffectiveAddress(instruction);
        this.cpu.getGpRegister3().setValue(pc + 1);
        this.cpu.getProgramCounter().setValue(address);
        LOGGER.log(Level.INFO, LOG_JUMP, address);
    }

    private void JMA(MachineInstruction instruction) {
        int address = this.getEffectiveAddress(instruction);
        this.cpu.getProgramCounter().setValue(address);
        LOGGER.log(Level.INFO, LOG_JUMP, address);
    }

    private void JCC(MachineInstruction instruction) {
        Register cc = this.cpu.getConditionCode();
        int address = this.getEffectiveAddress(instruction);
        // R is the bit position of the condition code to check
        if ((cc.getValue() & (1 << instruction.getOperand(Operand.R))) > 0) {
            this.cpu.getProgramCounter().setValue(address);
            LOGGER.log(Level.INFO, LOG_JUMP, address);
        }
    }

    private void JNE(MachineInstruction instruction) {
        Register register = this.cpu.getConditionCode();
        int address = this.getEffectiveAddress(instruction);
        if ((register.getValue() & ConditionCode.toInteger(ConditionCode.EQUALORNOT)) == 0) {
            this.cpu.getProgramCounter().setValue(address);
            LOGGER.log(Level.INFO, LOG_JUMP, address);
        }
    }

    private void JZ(MachineInstruction instruction) {
        Register register = this.cpu.getConditionCode();
        int address = this.getEffectiveAddress(instruction);
        if ((register.getValue() & ConditionCode.toInteger(ConditionCode.EQUALORNOT)) > 0) {
            this.cpu.getProgramCounter().setValue(address);
            LOGGER.log(Level.INFO, LOG_JUMP, address);
        }
    }

    private void SETCCE(MachineInstruction instruction) {
        Register register = this.getGpRegister(instruction, Operand.R);
        int cc = this.cpu.getConditionCode().getValue();
        if (register.getValue() == 0) {
            this.cpu.getConditionCode().setValue(cc | ConditionCode.toInteger(ConditionCode.EQUALORNOT));
        } else {
            this.cpu.getConditionCode().setValue(cc & ~ConditionCode.toInteger(ConditionCode.EQUALORNOT));
        }
        LOGGER.log(Level.INFO, "Set condition code 'EQUALORNOT' to {0}.", (register.getValue() == 0));
    }

    private void STX(MachineInstruction instruction) {
        Register register = this.getIndexRegister(instruction);
        int address = this.getEffectiveAddress(instruction);
        this.memory.setValue(address, register.getValue());
        LOGGER.log(Level.INFO, String.format("Stored value %d from index register %s into memory address %d.", register.getValue(), register.getName(), address));
    }

    private void LDX(MachineInstruction instruction) {
        Register register = this.getIndexRegister(instruction);
        int address = this.getEffectiveAddress(instruction);
        register.setValue(this.memory.getValue(address));
        LOGGER.log(Level.INFO, String.format("Loaded value %d from memory address %d into index register %s.", register.getValue(), address, register.getName()));
    }

    private void LDA(MachineInstruction instruction) {
        Register register = this.getGpRegister(instruction, Operand.R);
        int address = this.getEffectiveAddress(instruction);
        register.setValue(address);
        LOGGER.log(Level.INFO, String.format("Loaded address %d into register %s.", address, register.getName()));
    }

    private void STR(MachineInstruction instruction) {
        Register register = this.getGpRegister(instruction, Operand.R);
        int address = this.getEffectiveAddress(instruction);
        this.memory.setValue(address, register.getValue());
        LOGGER.log(Level.INFO, String.format("Stored value %d from register %s into memory address %d.", register.getValue(), register.getName(), address));
    }

    private void LDR(MachineInstruction instruction) {
        Register register = this.getGpRegister(instruction, Operand.R);
        int address = this.getEffectiveAddress(instruction);
        register.setValue(this.memory.getValue(address));
        LOGGER.log(Level.INFO, String.format("Loaded value %d from memory address %d into register %s.", register.getValue(), address, register.getName()));
    }

    private Register getrxP1Register(Register rx) {
        switch (rx.getName()) {
            case "R0":
                return this.cpu.getGpRegister1();
            case "R2":
                return this.cpu.getGpRegister3();
            case "R1":
            case "R3":
            default:
                return null;
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
        val indexRegister = this.getIndexRegister(instruction);

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

    private Register getGpRegister(MachineInstruction instruction, Operand operand) {
        val num = instruction.getOperand(operand);
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

    private Register getIndexRegister(MachineInstruction instruction) {
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
