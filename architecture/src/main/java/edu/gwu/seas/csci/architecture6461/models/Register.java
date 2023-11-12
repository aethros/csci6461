package edu.gwu.seas.csci.architecture6461.models;

public class Register {
    private int value;
    private int size;

    protected Register(int size) {
        this.size = size;
        this.value = 0;
    }

    public void setValue(int value)
    {
        if (value < 0) {
            throw new IllegalArgumentException("Registers can only be set with positive values.\n");
        }

        int maskedValue = this.getMask() & value;
        this.value = maskedValue;
    }

    public int getValue()
    {
        return value;
    }

    private int getMask()
    {
        int mask = 1;
        for (int i = 1; i < this.size; i++) {
            mask = (mask << 1) + 1;
        }
        return mask;
    }
}
