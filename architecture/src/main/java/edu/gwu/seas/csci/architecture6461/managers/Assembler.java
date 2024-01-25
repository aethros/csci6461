package edu.gwu.seas.csci.architecture6461.managers;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import edu.gwu.seas.csci.architecture6461.models.Instruction;
import lombok.val;

public final class Assembler {
    private static final Logger LOGGER = Logger.getLogger(Assembler.class.getName());
    private final List<Instruction> instructions = new ArrayList<>();

    public int[] assemble(String path) {
        try {
            val filePath = Paths.get(path);
            val lines = getLines(filePath);
            val table = this.buildSymbolTable(lines);
            val listing = this.translate(table, this.instructions);
            this.writeProgram(listing);

            // take program/listing and convert to machine code
            val octal = this.decimalToOctal(listing.size()); // fix this
            return new int[] { octal }; // fix this
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "File IO operation failed.\n");
            e.printStackTrace();
        }
        return new int[0];
    }

    private String[] getLines(final Path filePath) throws IOException {
        val fileBytes = Files.readAllBytes(filePath);
        val fileContent = new String(fileBytes, StandardCharsets.UTF_8);
        return fileContent.split("\n");
    }

    private HashMap<String, Integer> buildSymbolTable(final String[] lines) {
        val symbolTable = new HashMap<String, Integer>();
        var address = 0;

        for (var line : lines) {
            val instruction = new Instruction(line);
            this.instructions.add(instruction); // parse instruction and add to list

            if (instruction.hasLabel()) { // load label with address if present
                symbolTable.put(instruction.getLabel(), address);
            }
            if (instruction.operandSymbol() // load symbol with null address if operand
            && symbolTable.get(instruction.getOperandSymbol()) == null) {
                symbolTable.put(instruction.getOperandSymbol(), null);
            }

            val location = this.hasLocation(instruction); // if opcode is LOC, set address to operand
            if (location != -1) {
                address = location;
            } else { address++; }
        }

        return symbolTable;
    }

    private int hasLocation(Instruction instruction) {
        if (instruction.getOpcode() == "LOC") {
            return instruction.getOperand();
        }
        else { return -1; }
    }

    private List<String> translate(HashMap<String, Integer> table, List<Instruction> instructions) {


        return new ArrayList<>();
    }

    private void writeProgram(final List<String> assembled) throws IOException {
        val outputPath = Files.createDirectories(Paths.get(System.getProperty("user.dir") + "/assembled"));
        val outputFile = outputPath.resolve("program.lst");
        val writer = Files.newBufferedWriter(outputFile, StandardCharsets.UTF_8);
        for (var line : assembled) {
            writer.write(line);
            writer.newLine();
        }
    }

    public int[] loadProgram(int[] octal) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'loadProgram'");
    }

    /**
     * Converts a decimal number to a octal number.
     * 
     * @param decimal The decimal number to convert.
     * @return The octal value of the number.
     */
    public int decimalToOctal(int decimal) {
        int octal = 0;
        int i = 1;
        while (decimal != 0) {
            octal += (decimal % 8) * i; // Add modulo remainder to `octal`
            decimal /= 8;               // Divide `decimal` by 8
            i *= 10;                    // Multiply `i` by 10's place
        }
        return octal;
    }
}