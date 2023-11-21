package edu.gwu.seas.csci.architecture6461.models;

import lombok.val;

public class Register {
    public static final int MAX_SIZE = 16;

    private int value;
    private int size;

    protected Register(int size) {
        this.size = size;
        this.value = 0;
    }

    /**
     * Sets the value stored in the register.
     * @param value The value.
     */
    public void setValue(int value)
    {
        valueCheck(value);
        val maskedValue = this.getMask() & value;
        this.value = maskedValue;
    }

    /**
     * Gets the value stored in the register.
     * @return The value.
     */
    public int getValue()
    {
        return value;
    }

    /**
     * Gets the size of register bits.
     * @return The register size.
     */
    public int getSize()
    {
        return size;
    }

    private void valueCheck(int value) {
        if (value < 0 || value > this.getMask()) { // consider register overflow or exception thrown
            throw new IllegalArgumentException(
                String.format(
                    "Register can only be set with positive values less than %d.%n",
                    this.getMask()));
        }
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
