package edu.gwu.seas.csci.architecture6461.managers;

import edu.gwu.seas.csci.architecture6461.models.CPU;
import edu.gwu.seas.csci.architecture6461.models.Memory;
import lombok.Getter;
import lombok.Setter;

public final class ProgramManager {
    private static ProgramManager instance;
    @Getter @Setter private Memory memory = Memory.getInstance();
    @Getter @Setter private CPU cpu = CPU.getInstance();

    private ProgramManager() {
        this.cpu.reset(memory);
    }

    public void start() {
        // .
    }

    public static ProgramManager getInstance() {
        if (instance == null) {
            instance = new ProgramManager();
        }

        return instance;
    }
}
