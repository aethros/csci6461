package edu.gwu.seas.csci.architecture6461.managers;

import edu.gwu.seas.csci.architecture6461.models.CPU;
import edu.gwu.seas.csci.architecture6461.models.Memory;
import edu.gwu.seas.csci.architecture6461.util.InstructionUtils;
import lombok.Getter;
import lombok.Setter;
import lombok.val;

public final class ControlUnit {
    @Getter
    @Setter
    private Memory memory = Memory.getInstance();
    @Getter
    @Setter
    private CPU cpu = CPU.getInstance();

    public ControlUnit() {
        this.cpu.reset(this.memory);
    }

    public void start(int[] instructions) {
        for (var instruction : instructions) { // TODO: implement program start and end
            executeInstruction(instruction);
        }
    }

    public void executeInstruction(int instruction) {
        val maskedInstruction = InstructionUtils.maskInstruction(instruction);
        val code = InstructionUtils.opcodeFromInstruction(maskedInstruction);
        val address = InstructionUtils.addressFromInstruction(this.cpu, this.memory, maskedInstruction);
        val register = InstructionUtils.gpRegisterFromInstruction(this.cpu, maskedInstruction);

        switch (code) {
            // TODO: implement all cases
            case LDR: {
                register.setValue(this.memory.getValue(address));
            }
                break;
            case STR: {
                this.memory.setValue(address, register.getValue());
            }
                break;
            default:
                break;
        }
    }
}