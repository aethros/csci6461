package edu.gwu.seas.csci.architecture6461.util;

import java.util.Arrays;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;

import edu.gwu.seas.csci.architecture6461.util.InstructionUtils.Opcode;

@RunWith(value = Parameterized.class)
public class OpcodeToFromIntTest {

    @Parameter(0)
    public Opcode code;

    @Parameter(1)
    public int value;

    @Parameterized.Parameters(name = "{index}: {0} from {1}")
    public static Iterable<Object[]> data() {
        return Arrays.asList(new Object[][] {
            /*0*/   {Opcode.LDR,  1},
            /*1*/   {Opcode.CHK,  28},
            /*2*/   {Opcode.JCC,  8},
            /*3*/   {Opcode.FADD, 29},
            /*4*/   {Opcode.SRC,  24}
        });
    }

    @Test
    public void testOpcodeToFromInt() {
        Assert.assertEquals(value, Opcode.toInteger(code));
        Assert.assertEquals(code, Opcode.fromInteger(value));
    }
}