package edu.gwu.seas.csci.architecture6461.models;

import java.util.logging.Logger;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import lombok.Getter;
import lombok.val;

public class Register {
    private static final Logger LOGGER = Logger.getLogger(Register.class.getName());

    public static final int MAX_SIZE = 16;
    private IntegerProperty value;
    private int maskCache;
    private int size;

    @Getter
    private String name;

    protected Register(int size, String name) {
        this.maskCache = 0;
        this.name = name;
        this.size = size;
        value = new SimpleIntegerProperty(0);
    }

    public IntegerProperty valueProperty() {
        return value;
    }

    /**
     * Sets the value stored in the register.
     * 
     * @param value The value.
     */
    public void setValue(int value) throws IllegalArgumentException {
        valueCheck(value);
        val maskedValue = this.getMask() & value;
        this.value.set(maskedValue);

        LOGGER.info(String.format("Register %s set to %d.", this.name, maskedValue));
    }

    /**
     * Gets the value stored in the register.
     * 
     * @return The value.
     */
    public int getValue() {
        return value.get();
    }

    /**
     * Gets the size of register bits.
     * 
     * @return The register size.
     */
    public int getSize() {
        return size;
    }

    private void valueCheck(int value) {
        if (value < 0 || value > this.getMask()) {
            throw new IllegalArgumentException(
                    String.format(
                            "Register can only be set with positive values less than %d.%n",
                            this.getMask()));
        }
    }

    private int getMask() {
        if (this.maskCache == 0) {
            int mask = 1;
            for (int i = 1; i < this.size; i++) {
                mask = (mask << 1) + 1;
            }
            // Compute the mask once and cache it.
            this.maskCache = mask;
        }

        return this.maskCache;
    }
}
