package edu.gwu.seas.csci.architecture6461.models;

import java.util.Arrays;
import java.util.stream.Collectors;

import lombok.Getter;
import lombok.val;

public class Instruction {

    @Getter
    private String label;
    @Getter
    private String opcode;
    private String[] operands;

    public Instruction(String instruction) {
        instruction = instruction.split(";")[0]; // remove comments
        val tokens = Arrays.stream(instruction.split(" ")).filter(s -> s.length() > 0).collect(Collectors.toList());

        if ( tokens.size() == 0                                 // token count too low
        ||   tokens.size() > 3                                  // token count too high
        || (!tokens.get(0).endsWith(":") && tokens.size() > 2)) // token count too high for unlabeled instructions
        {
            throw new IllegalArgumentException("Invalid token count for instruction: " + instruction);
        }
        else if (tokens.get(0).endsWith(":")) { // instruction has label
            this.label    = tokens.get(0).split(":")[0].trim();
            this.opcode   = tokens.get(1);
            val operand   = tokens.get(2);
            this.operands = operand.split(",");
        }
        else { // instruction has no label
            this.opcode   = tokens.get(0);
            val operand   = tokens.get(1);
            this.operands = operand.split(",");
        }
    }

    public boolean hasLabel() {
        return this.label != null && this.label.length() > 0;
    }

    public boolean operandSymbol() {
        return this.operands.length > 0 && Integer.parseInt(this.operands[0]) != -1;
    }

    public String getOperandSymbol() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getSymbol'");
    }

    public int getOperand() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getOperand'");
    }
}