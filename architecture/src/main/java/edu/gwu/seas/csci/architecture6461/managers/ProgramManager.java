package edu.gwu.seas.csci.architecture6461.managers;

import edu.gwu.seas.csci.architecture6461.models.CPU;
import edu.gwu.seas.csci.architecture6461.models.Memory;
import edu.gwu.seas.csci.architecture6461.util.InstructionUtils;
import lombok.Getter;
import lombok.Setter;
import lombok.val;

public final class ProgramManager {
    private static ProgramManager instance;
    @Getter @Setter private Memory memory = Memory.getInstance();
    @Getter @Setter private CPU cpu = CPU.getInstance();

    private ProgramManager() {
        this.cpu.reset(memory);
    }

    public void start(int[] instructions) {
        // .

        for (var instruction : instructions) {
            executeInstruction(instruction);
        }
    }

    public void executeInstruction(int instruction)
    {
        val maskedInstruction = InstructionUtils.maskInstruction(instruction);
        val code = InstructionUtils.opcodeFromInstruction(maskedInstruction);
        val address = InstructionUtils.addressFromInstruction(cpu, memory, maskedInstruction);
        val register = InstructionUtils.registerFromInstruction(this.cpu, maskedInstruction);

        switch (code) {
            case LDR:
                {
                    register.setValue(this.memory.getValue(address));
                }
                break;
            case STR:
                {
                    this.memory.setValue(address, register.getValue());
                }
                break;
            default:
                break;
        }
    }

    public static ProgramManager getInstance() {
        if (instance == null) {
            instance = new ProgramManager();
        }

        return instance;
    }
}
