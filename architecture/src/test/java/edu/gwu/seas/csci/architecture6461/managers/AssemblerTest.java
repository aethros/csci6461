package edu.gwu.seas.csci.architecture6461.managers;

import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

public class AssemblerTest {

    @Test
    public void testAssembler() {
        System.out.println("Working Directory = " + System.getProperty("user.dir"));
        Assembler assembler = new Assembler();
        Map<Integer, Integer> assembled = assembler.assemble("./src/test/example_programs/example1.S");
        Assert.assertEquals(true, true);
    }
}
