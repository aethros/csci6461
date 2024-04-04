package edu.gwu.seas.csci.architecture6461.models;

import java.util.logging.Level;
import java.util.logging.Logger;

import lombok.Getter;

public final class MemoryInterface {
    private static final Logger LOGGER = Logger.getLogger("DataInterface");
    private static MemoryInterface instance;
    private Memory memory = Memory.getInstance();

    @Getter
    private Cache cache = Cache.getInstance();

    private MemoryInterface() {
    }

    /**
     * Returns the current instance of the Data Interface (Cache+Memory).
     * 
     * @return The application data.
     */
    public static MemoryInterface getInstance() {
        if (instance == null) {
            instance = new MemoryInterface();
        }

        return instance;
    }

    public void initialize(int memSize, int cacheSize) {
        this.memory.initialize(memSize);
        this.cache.initialize(cacheSize);
    }


    public int getValue(int address) {
        int offset = address % 8;
        int tag = (address - offset) & 0x00000FFF;
        if (this.cache.getValue(tag) != null) {
            LOGGER.log(Level.INFO, String.format("Cache hit at address: %d, offset: %d", (address - offset), offset));
            return this.cache.getValue(tag)[offset];
        } else {
            LOGGER.log(Level.INFO, "Cache miss at address: {0}", address);
            int value = this.memory.getValue(address);
            this.cache.setValue(tag, this.getCacheLine(address - offset));
            return value;
        }
    }

    private int[] getCacheLine(int baseAddress) {
        int[] values = new int[8];
        for (int i = 0; i < 8; i++) {
            values[i] = this.memory.getValue(baseAddress + i);
        }
        return values;
    }

    public void setValue(int address, int value) {
        int offset = address % 8;
        int tag = (address - offset) & 0x00000FFF;
        int[] values = this.cache.getValue(tag);
        if (values != null) {
            values[offset] = value;
            this.cache.setValue(tag, values);
            // write through to memory
            this.memory.setValue(address, value);
        } else {
            values = new int[8];
            values[offset] = value;
            this.cache.setValue(tag, values);
            // write through to memory
            this.memory.setValue(address, value);
        }
    }
}
