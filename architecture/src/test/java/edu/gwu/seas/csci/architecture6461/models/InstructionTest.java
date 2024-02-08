package edu.gwu.seas.csci.architecture6461.models;

import java.util.Arrays;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;

@RunWith(value = Parameterized.class)
public class InstructionTest {
    @Parameter(0)
    public String label;
    @Parameter(1)
    public String opcode;
    @Parameter(2)
    public String operandSymbol;
    @Parameter(3)
    public Integer operandA;
    @Parameter(4)
    public Integer operandB;
    @Parameter(5)
    public Integer operandC;
    @Parameter(6)
    public Integer operandD;
    @Parameter(7)
    public String instruction;

    @Parameterized.Parameters(name = "{index}: {7} to {0}: {1} {3},{4},{5},{6}")
    public static Iterable<Object[]> data() {
        return Arrays.asList(new Object[][] {
            /* 0 */  { null, "LOC",    null, 6,    null, null, null,    "LOC     6           ;BEGIN AT LOCATION 6" },
            /* 1 */  { null, "Data",   null, 10,   null, null, null,    "Data    10          ;PUT 10 AT LOCATION 6" },
            /* 2 */  { null, "Data",   null, 3,    null, null, null,    "Data    3           ;PUT 3 AT LOCATION 7" },
            /* 3 */  { null, "Data",  "End", null, null, null, null,    "Data    End         ;PUT 1024 AT LOCATION 8" },
            /* 4 */  { null, "Data",   null, 0,    null, null, null,    "Data    0" },
            /* 5 */  { null, "Data",   null, 12,   null, null, null,    "Data    12" },
            /* 6 */  { null, "Data",   null, 9,    null, null, null,    "Data    9" },
            /* 7 */  { null, "Data",   null, 18,   null, null, null,    "Data    18" },
            /* 8 */  { null, "Data",   null, 12,   null, null, null,    "Data    12" },
            /* 9 */  { null, "LDX",    null, 2, 7, null, null,          "LDX     2,7         ;X2 GETS 3" },
            /* 10 */ { null, "LDR",    null, 3, 0, 10,   null,          "LDR     3,0,10      ;R3 GETS 12" },
            /* 11 */ { null, "LDR",    null, 2, 2, 10,   null,          "LDR     2,2,10      ;R2 GETS 12" },
            /* 12 */ { null, "LDR",    null, 1, 2, 10,   1,             "LDR     1,2,10,1    ;R1 GETS 18" },
            /* 13 */ { null, "LDA",    null, 0, 0, 0,    null,          "LDA     0,0,0       ;R0 GETS 0 to set CONDITION CODE" },
            /* 14 */ { null, "LDX",    null, 1, 8, null, null,          "LDX     1,8         ;X1 GETS 1024" },
            /* 15 */ { null, "SETCCE", null, 0,    null, null, null,    "SETCCE  0           ;SET CONDITION CODE FOR EQUAL" },
            /* 16 */ { null, "JZ",     null, 1, 0, null, null,          "JZ      1,0         ;JUMP TO End if CC is 1" },
            /* 17 */ { null, "LOC",    null, 1024, null, null, null,    "LOC     1024" },
            /* 18 */ { "End", "HLT",   null, null, null, null, null,    "End:    HLT" }
        });
    }

    @Test
    public void testInstruction() {
        Instruction instr = new Instruction(instruction);
        Assert.assertEquals(label, instr.getLabel());
        Assert.assertEquals(opcode, instr.getOpcode());
        Assert.assertEquals(operandSymbol, instr.getOperandSymbol());
        Assert.assertEquals(operandA, instr.getOperand(0));
        Assert.assertEquals(operandB, instr.getOperand(1));
        Assert.assertEquals(operandC, instr.getOperand(2));
        Assert.assertEquals(operandD, instr.getOperand(3));
    }
}
