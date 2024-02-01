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
import lombok.val;

/**
 * Class for assembling programs into machine code.
 */
public final class Assembler {
    private static final Logger LOGGER = Logger.getLogger(Assembler.class.getName());
    private static final int INDEX_REGISTER_BITS = 2;
    private static final int ADDRESS_BITS = 5;
    private static final int INDIRECT_BITS = 1;
    private final List<Instruction> instructions = new ArrayList<>();
    private Map<Integer, Integer> programMemory;

    /**
     * Assembles a program from a file.
     * 
     * @param path The path to the file.
     * @return The assembled program.
     */
    public Map<Integer, Integer> assemble(String path) {
        try {
            val filePath = Paths.get(path);
            val lines = this.getLines(filePath);
            val table = this.buildSymbolTable(lines);
            val listing = this.translate(table, this.instructions);
            this.writeProgram(listing);
            return this.programMemory;
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "File IO operation failed.\n");
            e.printStackTrace();
            return new HashMap<>();
        }
    }

    /**
     * Reads the lines of a file into a string array.
     * 
     * @param filePath The path to the file.
     * @return The lines of the file.
     * @throws IOException If the file cannot be read.
     */
    private String[] getLines(final Path filePath) throws IOException {
        val fileBytes = Files.readAllBytes(filePath);
        val fileContent = new String(fileBytes, StandardCharsets.UTF_8);
        return fileContent.split("\n");
    }

    /**
     * Builds a symbol table from the lines of a file.
     * 
     * @param lines The lines of the file.
     * @return The symbol table.
     */
    private Map<String, Integer> buildSymbolTable(final String[] lines) {
        val symbolTable = new HashMap<String, Integer>();
        var address = 0;

        for (var line : lines) {
            LOGGER.log(Level.INFO, "Parsing line: {}", line);
            val instruction = new Instruction(line);
            this.instructions.add(instruction); // parse instruction and add to list

            if (instruction.hasLabel()) { // load label with address if present
                symbolTable.put(instruction.getLabel(), address);
            }
            if (instruction.operandSymbol() // load symbol with null address if operand
                    && symbolTable.get(instruction.getOperandSymbol()) == null) {
                symbolTable.put(instruction.getOperandSymbol(), null);
            }

            address = setLocation(address, instruction);
        }

        return symbolTable;
    }

    /**
     * Checks if an instruction is a location directive.
     * 
     * @param instruction The instruction to check.
     * @return The address of the location if present, otherwise -1.
     */
    public int isLocation(Instruction instruction) {
        if (instruction.getOpcode().equals("LOC")) {
            return instruction.getOperand(0);
        } else {
            return -1;
        }
    }

    /**
     * Translates a list of instructions into a program.
     * 
     * @param table        The symbol table.
     * @param instructions The list of instructions.
     * @return The assembled program.
     */
    private Map<Integer, Integer> translate(Map<String, Integer> table, List<Instruction> instructions) {
        val assembled = new HashMap<Integer, Integer>();
        var address = 0;
        var value = 0;

        for (var instruction : instructions) {
            var message = String.format("Parsing Instruction: %s: %s %s,%s,%s,%s", instruction.getLabel(), instruction.getOpcode(),
            instruction.getOperand(0).toString(), instruction.getOperand(1).toString(), instruction.getOperand(2).toString(), instruction.getOperand(3).toString());
            LOGGER.log(Level.INFO, message);
            
            Integer symbolValue = null;
            switch (instruction.getOpcode()) {
                case "Data":
                    symbolValue = this.calculateSymbol(table, instruction);
                    value = this.setAddressValue(value, instruction.getOperand(0), symbolValue);
                    break;
                case "AIR":
                    // code for AIR opcode
                    break;
                case "AMR":
                    // code for AMR opcode
                    break;
                case "JZ":
                    // code for JZ opcode
                    break;
                case "LDA":
                    // code for LDA opcode
                    break;
                case "LDX":
                    symbolValue = this.calculateSymbol(table, instruction);
                    this.checkOperandBits(instruction.getOperand(0), INDEX_REGISTER_BITS);
                    this.checkOperandBits(instruction.getOperand(2), INDIRECT_BITS);
                    this.checkOperandBits(instruction.getOperand(1), ADDRESS_BITS);

                    value = this.setOpcodeValue(Opcode.LDX);
                    value |= this.setIndexValue(value, instruction.getOperand(0));
                    value |= this.setIndirectValue(value, instruction.getOperand(2));
                    value |= this.setAddressValue(value, instruction.getOperand(1), symbolValue);
                    break;
                case "LDR":
                    symbolValue = this.calculateSymbol(table, instruction);
                    this.checkOperandBits(instruction.getOperand(0), INDEX_REGISTER_BITS);
                    this.checkOperandBits(instruction.getOperand(1), INDEX_REGISTER_BITS);
                    this.checkOperandBits(instruction.getOperand(3), INDIRECT_BITS);
                    this.checkOperandBits(instruction.getOperand(2), ADDRESS_BITS);

                    value = this.setOpcodeValue(Opcode.LDR);
                    value |= this.setRegisterValue(value, instruction.getOperand(0));
                    value |= this.setIndexValue(value, instruction.getOperand(1));
                    value |= this.setIndirectValue(value, instruction.getOperand(3));
                    value |= this.setAddressValue(value, instruction.getOperand(2), symbolValue);
                    break;
                case "STR":
                    // code for STR opcode
                    break;
                case "STX":
                    // code for STX opcode
                    break;
                case "HLT":
                    // code for HLT opcode
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
     * Sets the opcode value of an instruction.
     * 
     * @param code The opcode to set.
     * @return The new value of the instruction.
     */
    private int setOpcodeValue(Opcode code) {
        return Opcode.toInteger(code) << 10;
    }

    /**
     * Sets the register value of an instruction.
     * 
     * @param value   The value of the instruction.
     * @param operand The register operand.
     * @return The new value of the instruction.
     */
    private int setRegisterValue(int value, Integer operand) {
        return (operand != null) ? (operand << 8) : value;
    }

    /**
     * Sets the index value of an instruction.
     * 
     * @param value   The value of the instruction.
     * @param operand The index operand.
     * @return The new value of the instruction.
     */
    private int setIndexValue(int value, Integer operand) {
        return (operand != null) ? (operand << 6) : value;
    }

    /**
     * Sets the indirect value of an instruction.
     * 
     * @param value   The value of the instruction.
     * @param operand The indirect operand.
     * @return The new value of the instruction.
     */
    private int setIndirectValue(int value, Integer operand) {
        return (operand != null) ? (operand << 5) : value;
    }

    /**
     * Sets the address value of an instruction.
     * 
     * @param value          The value of the instruction.
     * @param addressOperand The address operand.
     * @param symbolValue    The value of the symbol.
     * @return The new value of the instruction.
     */
    private int setAddressValue(int value, Integer addressOperand, Integer symbolValue) {
        if (symbolValue != null) {              // symbol is address
            value |= symbolValue;
        } else if (addressOperand != null) {    // operand is adress
            value |= addressOperand;
        }
        return value;                           // if no address, just return value
    }

    /**
     * Sets the location of an instruction.
     * 
     * @param address     The current address.
     * @param instruction The instruction to set the location of.
     * @return The new address.
     */
    private int setLocation(int address, Instruction instruction) {
        val location = this.isLocation(instruction); // if opcode is LOC, set address to operand
        if (location != -1) {
            address = location;
        } else {
            address++;
        }
        return address;
    }

    /**
     * Calculates the value of a symbol.
     * 
     * @param table       The symbol table.
     * @param instruction The instruction to calculate.
     * @return The value of the symbol.
     */
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

    /**
     * Checks if an operand is within the bounds of its field.
     * 
     * @param operand     The operand to check.
     * @param operandSize The size of the operand field.
     */
    private void checkOperandBits(Integer operand, int operandSize) {
        try {
            switch (operandSize) {
                case 1:
                    if (operand > 1) {
                        throw new IllegalStateException("Operand too large for 1-bit field.");                    
                    }
                    break;
                case 2:
                    if (operand > 3) {
                        throw new IllegalStateException("Operand too large for 2-bit field.");                    
                    }
                    break;
            
                case 5:
                    if (operand > 31) {
                        throw new IllegalStateException("Operand too large for 5-bit field.");                    
                    }
                    break;
                default:
                    throw new IllegalArgumentException("Invalid operand size.");
            }
        } catch (NullPointerException e) {
            // No operand, skip check
        }
    }

    /**
     * Writes a program to a file.
     * 
     * @param assembled The assembled program.
     * @throws IOException If the file cannot be written.
     */
    private void writeProgram(final Map<Integer, Integer> assembled) throws IOException {
        val outputPath = Files.createDirectories(Paths.get(System.getProperty("user.dir") + "/assembled"));
        val outputFile = outputPath.resolve("program.lst");
        val writer = Files.newBufferedWriter(outputFile, StandardCharsets.UTF_8);
        for (var entry : assembled.entrySet()) {
            val address = entry.getKey();
            val value = entry.getValue();
            var string = String.format("%d %d%n", this.decimalToOctal(address), this.decimalToOctal(value));
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
    public int decimalToOctal(int decimal) {
        var octal = 0;
        var i = 1;
        while (decimal != 0) {
            octal += (decimal % 8) * i; // Add modulo remainder to `octal`
            decimal /= 8; // Divide `decimal` by 8
            i *= 10; // Multiply `i` by 10's place
        }
        return octal;
    }
}