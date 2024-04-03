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

import edu.gwu.seas.csci.architecture6461.models.AssemblyInstruction;
import edu.gwu.seas.csci.architecture6461.util.InstructionUtils.Opcode;
import javafx.util.Pair;
import lombok.val;

/**
 * Class for assembling programs into machine code.
 */
public final class Assembler {
    private static final Logger LOGGER = Logger.getLogger("Assembler");

    public static final int REGISTER_BIT_POSITION = 8;
    public static final int INDEX_BIT_POSITION = 6;
    public static final int INDIRECT_BIT_POSITION = 5;
    public static final int AL_BIT_POSITION = 7;
    public static final int LR_BIT_POSITION = 6;
    public static final int ADDRESS_BITS = 5;
    public static final int COUNT_BITS = 4;
    public static final int INDEX_REGISTER_BITS = 2;
    public static final int INDIRECT_BITS = 1;

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
            val lines = Files.readAllLines(filePath).toArray();
            val pair = this.buildSymbolTable(lines);
            val instructions = pair.getKey();
            val symbolTable = pair.getValue();

            val listing = this.translate(symbolTable, instructions);
            this.writeProgram(listing, outputPath);
            return listing;
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
    public Pair<List<AssemblyInstruction>, Map<String, Integer>> buildSymbolTable(final Object[] lines) {
        val symbolTable = new HashMap<String, Integer>();
        val instructions = new ArrayList<AssemblyInstruction>();
        var address = 0;

        for (var line : lines) {
            LOGGER.log(Level.INFO, "Parsing line: {0}", line);

            AssemblyInstruction instruction = null;
            try {
                instruction = new AssemblyInstruction((String)line);
            } catch (IllegalArgumentException e) {
                LOGGER.log(Level.WARNING, "Skipping line: {0}", line);
                continue;
            }

            if (instruction != null) {
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
    public Map<Integer, Integer> translate(Map<String, Integer> symbolTable, List<AssemblyInstruction> instructions) {
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
                case "HLT":
                    value = this.setOpcodeBits(Opcode.HLT);
                    break;
                case "LDR":
                    value = this.setFourOperandValue(symbolTable, instruction, Opcode.LDR);
                    break;
                case "STR":
                    value = this.setFourOperandValue(symbolTable, instruction, Opcode.STR);
                    break;
                case "LDA":
                    value = this.setFourOperandValue(symbolTable, instruction, Opcode.LDA);
                    break;
                case "LDX":
                    value = this.setThreeOperandValue(symbolTable, instruction, Opcode.LDX);
                    break;
                case "STX":
                    value = this.setThreeOperandValue(symbolTable, instruction, Opcode.STX);
                    break;
                case "JZ":
                    value = this.setThreeOperandValue(symbolTable, instruction, Opcode.JZ);
                    break;
                case "JNE":
                    value = this.setThreeOperandValue(symbolTable, instruction, Opcode.JNE);
                    break;
                case "JCC":
                    value = this.setFourOperandValue(symbolTable, instruction, Opcode.JCC);
                    break;
                case "JMA":
                    value = this.setThreeOperandValue(symbolTable, instruction, Opcode.JMA);
                    break;
                case "JSR":
                    value = this.setThreeOperandValue(symbolTable, instruction, Opcode.JSR);
                    break;
                case "RFS":
                    symbolValue = this.calculateSymbol(symbolTable, instruction);
                    this.checkOperandBits(instruction.getOperand(0), COUNT_BITS);

                    value = this.setOpcodeBits(Opcode.RFS);
                    value |= this.setAddressBits(value, instruction.getOperand(0), symbolValue);
                    break;
                case "SOB":
                    value = this.setFourOperandValue(symbolTable, instruction, Opcode.SOB);
                    break;
                case "JGE":
                    value = this.setFourOperandValue(symbolTable, instruction, Opcode.JGE);
                    break;
                case "AMR":
                    value = this.setFourOperandValue(symbolTable, instruction, Opcode.AMR);
                    break;
                case "SMR":
                    value = this.setFourOperandValue(symbolTable, instruction, Opcode.SMR);
                    break;
                case "AIR":
                    value = this.setAddressRegisterOperands(symbolTable, instruction, Opcode.AIR);
                    break;
                case "SIR":
                    value = this.setAddressRegisterOperands(symbolTable, instruction, Opcode.SIR);
                    break;
                case "MLT":
                    value = this.setTwoOperandValue(instruction, Opcode.MLT);
                    break;
                case "DVD":
                    value = this.setTwoOperandValue(instruction, Opcode.DVD);
                    break;
                case "TRR":
                    value = this.setTwoOperandValue(instruction, Opcode.TRR);
                    break;
                case "AND":
                    value = this.setTwoOperandValue(instruction, Opcode.AND);
                    break;
                case "ORR":
                    value = this.setTwoOperandValue(instruction, Opcode.ORR);
                    break;
                case "NOT":
                    this.checkOperandBits(instruction.getOperand(0), INDEX_REGISTER_BITS);
            
                    value = this.setOpcodeBits(Opcode.NOT);
                    value |= this.setOperandBits(value, instruction.getOperand(0), REGISTER_BIT_POSITION);
                    break;
                case "SRC":
                    value = this.setShiftRotateOperands(symbolTable, instruction, Opcode.SRC);
                    break;
                case "RRC":
                    value = this.setShiftRotateOperands(symbolTable, instruction, Opcode.RRC);
                    break;
                case "IN":
                    value = this.setAddressRegisterOperands(symbolTable, instruction, Opcode.IN);
                    break;
                case "OUT":
                    value = this.setAddressRegisterOperands(symbolTable, instruction, Opcode.OUT);
                    break;
                case "CHK":
                    value = this.setAddressRegisterOperands(symbolTable, instruction, Opcode.CHK);
                    break;
                // Floating Point Instructions
                // TODO: case "FADD":
                // TODO: case "FSUB":
                // TODO: case "VADD":
                // TODO: case "VSUB":
                // TODO: case "CNVRT":
                // TODO: case "LDFR":
                // TODO: case "STFR":
                case "SETCCE":
                    this.checkOperandBits(instruction.getOperand(0), INDEX_REGISTER_BITS);

                    value = this.setOpcodeBits(Opcode.SETCCE);
                    value |= this.setOperandBits(value, instruction.getOperand(0), REGISTER_BIT_POSITION);
                    break;
                case "TRAP":
                    symbolValue = this.calculateSymbol(symbolTable, instruction);
                    this.checkOperandBits(instruction.getOperand(0), COUNT_BITS);

                    value = this.setOpcodeBits(Opcode.TRAP);
                    value |= this.setAddressBits(value, instruction.getOperand(0), symbolValue);
                    break;
                case "LOC":
                    break;
                default:
                // No known instruction, decrement the address pointer (it gets incremented later, so this keeps it at +0).
                    address--;
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
        val outputFile = this.getOutputFile(path);
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

    private Path getOutputFile(String path) throws IOException {
        Path outputPath;
        Path outputFile;
        if (Paths.get(path).toFile().isDirectory()) {
            outputPath = Files.createDirectories(Paths.get(path));
            outputFile = outputPath.resolve(Paths.get(path, "assembled/program.lst"));
            outputFile.getParent().toFile().mkdirs();
            outputFile.toFile().createNewFile();

            // **IGNORE: Do something with the "boolean" value returned. sonarlint(java:S899)**
            // There is a try/catch around the overall statement, and the return value
            // simply tells us if a new file was created or not. We don't need to do anything in
            // cases where file was simply overwritten.

        }
        else {
            outputPath = Files.createDirectories(Paths.get(path).getParent());
            outputFile = outputPath.resolve(Paths.get(path).getFileName());
        }
        return outputFile;
    }

    private int setAddressRegisterOperands(Map<String, Integer> symbolTable, AssemblyInstruction instruction, Opcode opcode) {
        int value;
        Integer symbolValue = this.calculateSymbol(symbolTable, instruction);
        this.checkOperandBits(instruction.getOperand(0), INDEX_REGISTER_BITS);
        this.checkOperandBits(instruction.getOperand(1), ADDRESS_BITS);
         
        value = this.setOpcodeBits(opcode);
        value |= this.setOperandBits(value, instruction.getOperand(0), REGISTER_BIT_POSITION);
        value |= this.setAddressBits(value, instruction.getOperand(1), symbolValue);
        return value;
    }

    private int setShiftRotateOperands(Map<String, Integer> table, AssemblyInstruction instruction, Opcode opcode) {
        int value;
        Integer symbolValue = this.calculateSymbol(table, instruction);
        this.checkOperandBits(instruction.getOperand(0), INDEX_REGISTER_BITS);
        this.checkOperandBits(instruction.getOperand(2), INDIRECT_BITS);
        this.checkOperandBits(instruction.getOperand(3), INDIRECT_BITS);
        this.checkOperandBits(instruction.getOperand(1), COUNT_BITS);

        value = this.setOpcodeBits(opcode);
        value |= this.setOperandBits(value, instruction.getOperand(0), REGISTER_BIT_POSITION);
        value |= this.setOperandBits(value, instruction.getOperand(3), AL_BIT_POSITION);
        value |= this.setOperandBits(value, instruction.getOperand(2), LR_BIT_POSITION);
        value |= this.setAddressBits(value, instruction.getOperand(1), symbolValue);
        return value;
    }

    private int setTwoOperandValue(AssemblyInstruction instruction, final Opcode opcode) {
        int value;
        this.checkOperandBits(instruction.getOperand(0), INDEX_REGISTER_BITS);
        this.checkOperandBits(instruction.getOperand(1), INDEX_REGISTER_BITS);

        value = this.setOpcodeBits(opcode);
        value |= this.setOperandBits(value, instruction.getOperand(0), REGISTER_BIT_POSITION);
        value |= this.setOperandBits(value, instruction.getOperand(1), INDEX_BIT_POSITION);
        return value;
    }

    private int setThreeOperandValue(Map<String, Integer> table, AssemblyInstruction instruction, final Opcode opcode) {
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

    private int setFourOperandValue(Map<String, Integer> table, AssemblyInstruction instruction, final Opcode opcode) {
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

    private int setLocation(int address, AssemblyInstruction instruction) {
        if (instruction.getOpcode().equals("LOC")) {
            address = instruction.getOperand(0);
        } else {
            address++;
        }
        return address;
    }

    private Integer calculateSymbol(Map<String, Integer> table, AssemblyInstruction instruction) {
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