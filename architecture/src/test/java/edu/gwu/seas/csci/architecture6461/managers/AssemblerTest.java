package edu.gwu.seas.csci.architecture6461.managers;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.junit.Assert;
import org.junit.Test;

public class AssemblerTest {

    @Test
    public void testOctalToDecimal() {
        Assert.assertEquals(0, Assembler.octalToDecimal(0));
        Assert.assertEquals(0, Assembler.decimalToOctal(0));
        Assert.assertEquals(1, Assembler.octalToDecimal(1));
        Assert.assertEquals(1, Assembler.decimalToOctal(1));
        Assert.assertEquals(7, Assembler.octalToDecimal(7));
        Assert.assertEquals(7, Assembler.decimalToOctal(7));
        Assert.assertEquals(8, Assembler.octalToDecimal(10));
        Assert.assertEquals(10, Assembler.decimalToOctal(8));
        Assert.assertEquals(17, Assembler.octalToDecimal(21));
        Assert.assertEquals(21, Assembler.decimalToOctal(17));
        Assert.assertEquals(15, Assembler.octalToDecimal(17));
        Assert.assertEquals(17, Assembler.decimalToOctal(15));
        Assert.assertEquals(21, Assembler.octalToDecimal(25));
        Assert.assertEquals(25, Assembler.decimalToOctal(21));
    }

    @Test
    public void testAssembler() throws Exception {
        System.out.println("Working Directory = " + System.getProperty("user.dir"));

        // Arrange Expected
        Assembler assembler = new Assembler();
        Map<Integer, Integer> assembled = assembler.assemble("./src/test/fixtures/example1.S",
                "./assembled/program.lst");
        List<Entry<Integer, Integer>> computedEntries = new ArrayList<>(assembled.entrySet());
        computedEntries.sort(Map.Entry.comparingByKey());

        // Arrange Actual
        List<String> fileLines = Files.readAllLines(Paths.get("./src/test/fixtures/program.lst"));
        Map<Integer, Integer> expectedMap = new HashMap<Integer, Integer>();
        for (String line : fileLines) {
            String[] parts = line.split(" ");
            expectedMap.put(Assembler.octalToDecimal(Integer.parseInt(parts[0])),
                    Assembler.octalToDecimal(Integer.parseInt(parts[1])));
        }
        List<Entry<Integer, Integer>> expectedEntries = new ArrayList<>(expectedMap.entrySet());
        expectedEntries.sort(Map.Entry.comparingByKey());

        // Assert
        for (Entry<Integer, Integer> entry : computedEntries) {
            Assert.assertTrue(expectedMap.containsKey(entry.getKey()));
            Assert.assertEquals(expectedMap.get(entry.getKey()), entry.getValue());
        }
    }
}
