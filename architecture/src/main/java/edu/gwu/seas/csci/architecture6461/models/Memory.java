package edu.gwu.seas.csci.architecture6461.models;

public final class Memory {
    public static final int MEMORY_MAX = 4096;
    private static Memory instance;
    private int[] data;

    private Memory() { }

    /**
     * Initializes memory with a given size.
     * @param size The size of application memory.
     */
    public void initialize(int size)
    {
        this.data = (size < MEMORY_MAX) 
            ? new int[size]
            : new int[MEMORY_MAX];
    }

    /**
     * Returns the current instance of the Memory model.
     * @return The application memory.
     */
    public static Memory getInstance() {
        if (instance == null) {
            instance = new Memory();
        }

        return instance;
    }

    /**
     * Sets a memory address with a given value.
     * @param address The address which will be set.
     * @param value The value to set memory to.
     */
    public void setValue(int address, int value)
    {
        this.data[address] = value;
    }

    /**
     * Returns the value stored at a given address.
     * @param address The address from which to pull a value.
     * @return The value at a given address.
     */
    public int getValue(int address)
    {
        return this.data[address];
    }
}
