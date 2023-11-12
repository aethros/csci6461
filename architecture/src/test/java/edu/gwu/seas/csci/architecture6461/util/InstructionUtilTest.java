package edu.gwu.seas.csci.architecture6461.util;

import java.util.Arrays;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;

import edu.gwu.seas.csci.architecture6461.util.InstructionUtils.Opcode;

@RunWith(value = Parameterized.class)
public class InstructionUtilTest {

    @Parameter(0)
    public Opcode code;

    @Parameter(1)
    public int instruction;


    @Parameterized.Parameters()
    public static Iterable<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {Opcode.LDR,  0b0000010000000000},
                {Opcode.CHK,  0b1111110000000000},
                {Opcode.JCC,  0b0011000000000000},
                {Opcode.FADD, 0b1000010000000000},
                {Opcode.SRC,  0b0111110000000000}
        });
    }

    @Test
    public void testGetOpcodeFromInstruction(){
        Assert.assertEquals(code, InstructionUtils.opcodeFromInstruction(instruction));
    }
}