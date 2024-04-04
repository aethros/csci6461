package edu.gwu.seas.csci.architecture6461.models;

import java.util.Arrays;
import java.util.stream.Collectors;

import lombok.Getter;
import lombok.val;

public class AssemblyInstruction {

    @Getter
    private String label;
    @Getter
    private String opcode;
    @Getter
    private String operandSymbol;
    private String[] operands;

    public AssemblyInstruction(String instruction) {
        instruction = instruction.split(";")[0]; // remove comments
        val tokens = Arrays.stream(instruction.split(" ")).filter(s -> !s.isEmpty()).collect(Collectors.toList());

        if (tokens.isEmpty() // token count too low
                || tokens.size() > 3 // token count too high
                || (!tokens.get(0).endsWith(":") && tokens.size() > 2)) // token count too high for unlabeled
                                                                        // instructions
        {
            throw new IllegalArgumentException("Invalid token count for instruction: " + instruction);
        } else if (tokens.get(0).endsWith(":")) { // instruction has label
            this.label = tokens.get(0).split(":")[0].trim();
            this.opcode = tokens.get(1);
            try {
                val operand = tokens.get(2);
                this.operands = operand.split(",");
                this.operandSymbol = this.setOperandSymbol();

                if (this.operands.length > 4) {
                    throw new IllegalArgumentException("Too many operands for instruction: " + instruction);
                }
            } catch (IndexOutOfBoundsException e) {
                // no operands
            }
        } else { // instruction has no label
            this.opcode = tokens.get(0);
            try {
                val operand = tokens.get(1);
                this.operands = operand.split(",");
                this.operandSymbol = this.setOperandSymbol();

                if (this.operands.length > 4) {
                    throw new IllegalArgumentException("Too many operands for instruction: " + instruction);
                }
            } catch (IndexOutOfBoundsException e) {
                // no operands
            }
        }
    }

    private String setOperandSymbol() {
        for (String operand : operands) {
            if (operand.matches("([a-zA-Z]+)")) {
                return operand;
            } else if (operand.matches("(\\d+)")) {
                return null;
            }
        }
        return null;
    }

    public boolean hasLabel() {
        return this.label != null && !this.label.isEmpty();
    }

    public boolean operandSymbol() {
        return this.operandSymbol != null && !this.operandSymbol.isEmpty();
    }

    public Integer getOperand(int operand) {
        try {
            return Integer.parseInt(this.operands[operand]);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public String toString() {
        return this.label + ": " + this.opcode + ' ' + Arrays.toString(this.operands);
    }
}