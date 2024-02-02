package edu.gwu.seas.csci.architecture6461.managers;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import edu.gwu.seas.csci.architecture6461.models.Instruction;
import edu.gwu.seas.csci.architecture6461.util.InstructionUtils.Opcode;
import javafx.util.Pair;
import lombok.val;

/**
 * Class for assembling programs into machine code.
 */
public final class Assembler {
    private static final Logger LOGGER = Logger.getLogger(Assembler.class.getName());
    private static final int REGISTER_BIT_POSITION = 8;
    private static final int INDEX_BIT_POSITION = 6;
    private static final int INDIRECT_BIT_POSITION = 5;
    private static final int INDEX_REGISTER_BITS = 2;
    private static final int ADDRESS_BITS = 5;
    private static final int INDIRECT_BITS = 1;
    private Map<Integer, Integer> programMemory;

    /**
     * Assembles a program from a file.
     * 
     * @param inputPath  The input path for the assembly file.
     * @param outputPath The output path for the listing file.
     * @return The assembled program.
     */
    public Map<Integer, Integer> assemble(String inputPath, String outputPath) {
        try {
            val filePath = Paths.get(inputPath);
            val lines = this.getLines(filePath);
            val pair = this.buildSymbolTable(lines);
            val instructions = pair.getKey();
            val symbolTable = pair.getValue();

            val listing = this.translate(symbolTable, instructions);
            this.writeProgram(listing, outputPath);
            this.programMemory = listing;
            return this.programMemory;
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "File IO operation failed.\n");
            e.printStackTrace();
            return new HashMap<>();
        }
    }

    /**
     * Builds a symbol table from the lines of a file.
     * 
     * @param lines The lines of the file.
     * @return A pair of the instructions and the symbol table.
     */
    public Pair<List<Instruction>, Map<String, Integer>> buildSymbolTable(final String[] lines) {
        val symbolTable = new HashMap<String, Integer>();
        val instructions = new ArrayList<Instruction>();
        var address = 0;

        for (var line : lines) {
            LOGGER.log(Level.INFO, "Parsing line: {0}", line);
            val instruction = new Instruction(line);
            instructions.add(instruction); // parse instruction and add to list

            if (instruction.hasLabel()) { // load label with address if present
                symbolTable.put(instruction.getLabel(), address);
            }
            if (instruction.operandSymbol() // load symbol with null address if operand
                    && symbolTable.get(instruction.getOperandSymbol()) == null) {
                symbolTable.put(instruction.getOperandSymbol(), null);
            }

            address = setLocation(address, instruction);
        }

        return new Pair<>(instructions, symbolTable);
    }

    /**
     * Translates a list of instructions into a program.
     * 
     * @param symbolTable  The symbol table.
     * @param instructions The list of instructions.
     * @return The assembled program.
     */
    public Map<Integer, Integer> translate(Map<String, Integer> symbolTable, List<Instruction> instructions) {
        val assembled = new HashMap<Integer, Integer>();
        var address = 0;

        for (var instruction : instructions) {
            LOGGER.log(Level.INFO, "Parsing Instruction: {0}", instruction);

            var value = 0;
            Integer symbolValue = null;
            switch (instruction.getOpcode()) {
                case "Data":
                    symbolValue = this.calculateSymbol(symbolTable, instruction);
                    value = this.setAddressBits(value, instruction.getOperand(0), symbolValue);
                    break;
                case "JZ":
                    value = this.setThreeOperandValue(symbolTable, instruction, Opcode.JZ);
                    break;
                case "LDA":
                    value = this.setFourOperandValue(symbolTable, instruction, Opcode.LDA);
                    break;
                case "LDX":
                    value = this.setThreeOperandValue(symbolTable, instruction, Opcode.LDX);
                    break;
                case "LDR":
                    value = this.setFourOperandValue(symbolTable, instruction, Opcode.LDR);
                    break;
                case "SETCCE":
                    this.checkOperandBits(instruction.getOperand(0), INDEX_REGISTER_BITS);

                    value = this.setOpcodeBits(Opcode.SETCCE);
                    value |= this.setOperandBits(value, instruction.getOperand(0), REGISTER_BIT_POSITION);
                    break;
                case "HLT":
                    value = this.setOpcodeBits(Opcode.HLT);
                    break;
                default:
                    break;
            }

            if (!instruction.getOpcode().equals("LOC")) {
                assembled.put(address, value); // skip LOC instructions
            }
            address = this.setLocation(address, instruction);
        }

        return assembled;
    }

    /**
     * Writes a program to a file.
     * 
     * @param assembled The assembled program.
     * @param path      The path to write the program to.
     * @throws IOException If the file cannot be written.
     */
    public void writeProgram(final Map<Integer, Integer> assembled, String path) throws IOException {
        val outputPath = Files.createDirectories(Paths.get(path).getParent());
        val outputFile = outputPath.resolve(Paths.get(path).getFileName());
        val writer = Files.newBufferedWriter(outputFile, StandardCharsets.UTF_8);
        val entries = new ArrayList<>(assembled.entrySet());
        entries.sort(Map.Entry.comparingByKey());
        for (var entry : entries) {
            val address = entry.getKey();
            val value = entry.getValue();
            var string = String.format("%06d %06d%n", Assembler.decimalToOctal(address),
                    Assembler.decimalToOctal(value));
            writer.write(string);
        }
        writer.close();
    }

    /**
     * Loads the program memory.
     * 
     * @return The program memory.
     */
    public Map<Integer, Integer> loadProgram() {
        if (this.programMemory != null) {
            return this.programMemory;
        } else {
            throw new IllegalStateException("Program memory not loaded.");
        }
    }

    /**
     * Converts a decimal number to a octal number.
     * 
     * @param decimal The decimal number to convert.
     * @return The octal value of the number.
     */
    public static int decimalToOctal(int decimal) {
        var octal = 0;
        var i = 1;
        while (decimal != 0) {
            octal += (decimal % 8) * i; // Add modulo remainder to `octal`
            decimal /= 8; // Divide `decimal` by 8
            i *= 10; // Multiply `i` by 10's place
        }
        return octal;
    }

    /**
     * Converts a octal number to a decimal number.
     * 
     * @param octal The octal number to convert.
     * @return The decimal value of the number.
     */
    public static int octalToDecimal(int octal) {
        int decimal = 0;
        int i = 1;
        while (octal != 0) {
            decimal += (octal % 10) * i; // Add modulo remainder to `decimal`
            octal /= 10; // Divide `octal` by 10's place
            i *= 8; // Multiply `i` by 8
        }
        return decimal;
    }

    private String[] getLines(final Path filePath) throws IOException {
        val fileBytes = Files.readAllBytes(filePath);
        val fileContent = new String(fileBytes, StandardCharsets.UTF_8);
        return fileContent.split("\n");
    }

    private int setThreeOperandValue(Map<String, Integer> table, Instruction instruction, final Opcode opcode) {
        int value;
        Integer symbolValue = this.calculateSymbol(table, instruction);
        this.checkOperandBits(instruction.getOperand(0), INDEX_REGISTER_BITS);
        this.checkOperandBits(instruction.getOperand(2), INDIRECT_BITS);
        this.checkOperandBits(instruction.getOperand(1), ADDRESS_BITS);

        value = this.setOpcodeBits(opcode);
        value |= this.setOperandBits(value, instruction.getOperand(0), INDEX_BIT_POSITION);
        value |= this.setOperandBits(value, instruction.getOperand(2), INDIRECT_BIT_POSITION);
        value |= this.setAddressBits(value, instruction.getOperand(1), symbolValue);
        return value;
    }

    private int setFourOperandValue(Map<String, Integer> table, Instruction instruction, final Opcode opcode) {
        int value;
        Integer symbolValue = this.calculateSymbol(table, instruction);
        this.checkOperandBits(instruction.getOperand(0), INDEX_REGISTER_BITS);
        this.checkOperandBits(instruction.getOperand(1), INDEX_REGISTER_BITS);
        this.checkOperandBits(instruction.getOperand(3), INDIRECT_BITS);
        this.checkOperandBits(instruction.getOperand(2), ADDRESS_BITS);

        value = this.setOpcodeBits(opcode);
        value |= this.setOperandBits(value, instruction.getOperand(0), REGISTER_BIT_POSITION);
        value |= this.setOperandBits(value, instruction.getOperand(1), INDEX_BIT_POSITION);
        value |= this.setOperandBits(value, instruction.getOperand(3), INDIRECT_BIT_POSITION);
        value |= this.setAddressBits(value, instruction.getOperand(2), symbolValue);
        return value;
    }

    private int setOpcodeBits(Opcode code) {
        return Opcode.toInteger(code) << 10;
    }

    private int setOperandBits(int value, Integer operand, int bitPosition) {
        return (operand != null) ? (operand << bitPosition) : value;
    }

    private int setAddressBits(int value, Integer addressOperand, Integer symbolValue) {
        if (symbolValue != null) { // symbol is address
            value |= symbolValue;
        } else if (addressOperand != null) { // operand is adress
            value |= addressOperand;
        }
        return value; // if no address, just return value
    }

    private int setLocation(int address, Instruction instruction) {
        if (instruction.getOpcode().equals("LOC")) {
            address = instruction.getOperand(0);
        } else {
            address++;
        }
        return address;
    }

    private Integer calculateSymbol(Map<String, Integer> table, Instruction instruction) {
        Integer symbolValue = null;
        if (instruction.operandSymbol()) {
            var symbol = table.get(instruction.getOperandSymbol());
            if (symbol != null) {
                symbolValue = symbol;
            } else {
                throw new IllegalStateException("Symbol not found in symbol table.");
            }
        }
        return symbolValue;
    }

    private void checkOperandBits(Integer operand, int operandSize) {
        try {
            val maxBits = Math.pow(2, operandSize) - 1;
            if (operand > maxBits) {
                throw new IllegalStateException("Operand too large for " + operandSize + "-bit field.");
            }
        } catch (NullPointerException e) {
            // No operand, skip check
        }
    }
}