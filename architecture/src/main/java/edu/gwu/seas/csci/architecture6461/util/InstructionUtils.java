package edu.gwu.seas.csci.architecture6461.util;

import java.util.HashMap;
import java.util.Map;

import edu.gwu.seas.csci.architecture6461.models.CPU;
import edu.gwu.seas.csci.architecture6461.models.Memory;
import edu.gwu.seas.csci.architecture6461.models.Register;
import lombok.val;

public abstract class InstructionUtils {
    //// 0b  000000 00 00 0 00000
    ////       ^    ^   ^ ^   ^
    ////     OPCODE R  IX I ADDRS
    public static final int INSTRUCTION_MASK       = 0xFFFF; // 111111 11 11 1 11111
    public static final int OPCODE_MASK            = 0xFC00; // 111111 00 00 0 00000
    public static final int GP_REGISTER_MASK       = 0x0300; // 000000 11 00 0 00000
    public static final int INDEX_REGISTER_MASK    = 0x00C0; // 000000 00 11 0 00000
    public static final int INDIRECT_MASK          = 0x0020; // 000000 00 00 1 00000
    public static final int ADDRESS_MASK           = 0x001F; // 000000 00 00 0 11111

    public enum Opcode {
        LDR, STR, LDA, JZ, JNE, JCC, JMA, JSR, RFS, SOB, JGE, LDX, STX, AMR, SMR,
        AIR, SIR, MLT, DVD, TRR, AND, ORR, NOT, SRC, RRC, FADD, VADD, LDFR, FSUB,
        VSUB, CNVRT, STFR, IN, OUT, CHK, HLT, TRAP, SETCCE;

        private static final Map<Integer, Opcode> opcodeMap = new HashMap<>();
        static {
            opcodeMap.put(0, HLT);
            opcodeMap.put(1, LDR);
            opcodeMap.put(2, STR);
            opcodeMap.put(3, LDA);
            opcodeMap.put(4, LDX);
            opcodeMap.put(5, STX);
            opcodeMap.put(6, JZ);
            opcodeMap.put(7, JNE);
            opcodeMap.put(8, JCC);
            opcodeMap.put(9, JMA);
            opcodeMap.put(10, JSR);
            opcodeMap.put(11, RFS);
            opcodeMap.put(12, SOB);
            opcodeMap.put(13, JGE);
            opcodeMap.put(14, AMR);
            opcodeMap.put(15, SMR);
            opcodeMap.put(16, AIR);
            opcodeMap.put(17, SIR);
            opcodeMap.put(18, MLT);
            opcodeMap.put(19, DVD);
            opcodeMap.put(20, TRR);
            opcodeMap.put(21, AND);
            opcodeMap.put(22, ORR);
            opcodeMap.put(23, NOT);
            opcodeMap.put(24, SRC);
            opcodeMap.put(25, RRC);
            opcodeMap.put(26, IN);
            opcodeMap.put(27, OUT);
            opcodeMap.put(28, CHK);
            opcodeMap.put(29, FADD);
            opcodeMap.put(30, FSUB);
            opcodeMap.put(31, VADD);
            opcodeMap.put(32, VSUB);
            opcodeMap.put(33, CNVRT);
            opcodeMap.put(34, LDFR);
            opcodeMap.put(35, STFR);
            opcodeMap.put(36, SETCCE);
            opcodeMap.put(37, TRAP);
        }

        public static Opcode fromInteger(int x) {
            return opcodeMap.get(x);
        }

        public static int toInteger(Opcode x) {
            return opcodeMap.entrySet().stream()
                    .filter(entry -> entry.getValue().equals(x))
                    .map(Map.Entry::getKey)
                    .findFirst()
                    .orElse(63);
        }
    }

    public enum MachineFaultCode {
        ILLEGALMEMORYADDRESSTORESERVEDLOCATIONS,
        ILLEGALTRAPCODE,
        ILLEGALOPERATIONCODE,
        ILLEGALMEMORYADDRESSBEYOND2048;

        public static MachineFaultCode fromInteger(int x) {
            switch (x) {
                case 1:
                    return ILLEGALMEMORYADDRESSTORESERVEDLOCATIONS;
                case 2:
                    return ILLEGALTRAPCODE;
                case 4:
                    return ILLEGALOPERATIONCODE;
                case 8:
                    return ILLEGALMEMORYADDRESSBEYOND2048;
                default:
                    return null;
            }
        }

        public static int toInteger(MachineFaultCode code) {
            switch (code) {
                case ILLEGALMEMORYADDRESSTORESERVEDLOCATIONS:
                    return 1;
                case ILLEGALTRAPCODE:
                    return 2;
                case ILLEGALOPERATIONCODE:
                    return 4;
                case ILLEGALMEMORYADDRESSBEYOND2048:
                    return 8;
                default:
                    return 0;
            }
        }
    }

    public enum ConditionCode {
        OVERFLOW, UNDERFLOW, DIVZERO, EQUALORNOT;

        public static ConditionCode fromInteger(int x) {
            switch (x) {
                case 1:
                    return OVERFLOW;
                case 2:
                    return UNDERFLOW;
                case 4:
                    return DIVZERO;
                case 8:
                    return EQUALORNOT;
                default:
                    return null;
            }
        }

        public static int toInteger(ConditionCode code) {
            switch (code) {
                case OVERFLOW:
                    return 1;
                case UNDERFLOW:
                    return 2;
                case DIVZERO:
                    return 4;
                case EQUALORNOT:
                    return 8;
                default:
                    return 0;
            }
        }
    }

    public static Opcode opcodeFromInstruction(int instruction) {
        int maskedInstruction = maskInstruction(instruction);
        int opcodeBytes = maskedInstruction >>> 10;
        // the opcode bytes need to do an unsigned right shift (>>>)
        // 10 places (highest 6 bits of 16 [16 - 6 = 10])
        return Opcode.fromInteger(opcodeBytes);
    }

    public static Register indexRegisterFromInstruction(CPU cpu, int instruction) {
        int maskedInstruction = maskInstruction(instruction);
        int registerBytes = (maskedInstruction >>> 6) & 0b011;
        // the register bytes need to do an unsigned right shift (>>>)
        // 6 places (1 place past i bit and 5 places past address bits)
        // and then they need an and (&) with lower 4 bits to get their
        // binary value.
        switch (registerBytes) {
            case 0b01:
                return cpu.getIndexRegister1();
            case 0b10:
                return cpu.getIndexRegister2();
            case 0b11:
                return cpu.getIndexRegister3();
            case 0b00:
            default:
                return null;
        }
    }

    public static Register gpRegisterFromInstruction(CPU cpu, int instruction) {
        int maskedInstruction = maskInstruction(instruction);
        int registerBytes = (maskedInstruction >>> 8) & 0b011;
        // the register bytes need to do an unsigned right shift (>>>)
        // 6 places (1 place past i bit and 5 places past address bits)
        // and then they need an and (&) with lower 4 bits to get their
        // binary value.
        switch (registerBytes) {
            case 0b00:
                return cpu.getGpRegister0();
            case 0b01:
                return cpu.getGpRegister1();
            case 0b10:
                return cpu.getGpRegister2();
            case 0b11:
                return cpu.getGpRegister3();
            default:
                return null;
        }
    }

    public static int addressFromInstruction(CPU cpu, Memory memory, int instruction) {
        int ea;
        val maskedInstruction = maskInstruction(instruction);
        val i = (maskedInstruction & INDIRECT_MASK) > 0;
        val ix = (maskedInstruction & INDEX_REGISTER_MASK) > 0;
        val address = InstructionUtils.getAddressBits(maskedInstruction);
        val indexRegister = InstructionUtils.indexRegisterFromInstruction(cpu, maskedInstruction);

        if (!i) {
            // NO indirect addressing
            if (!ix) {
                ea = address;
            } else {
                ea = indexRegister.getValue() + address;
                // that is, the IX field has an
                // index register number, the contents of that register are
                // added to the contents of the address field
            }
        } else {
            if (!ix) {
                // indirect addressing, but NO indexing
                ea = memory.getValue(address);
                // It helps to think in terms of a
                // pointer where the address field has the location of the EA
                // in memory
            } else {
                // both indirect addressing and indexing
                ea = memory.getValue(indexRegister.getValue() + address);
                // another way to think of this is take the EA computed without
                // indirections and use that as the location of where the EA is stored.
            }
        }
        return ea;
    }

    public static int getAddressBits(int instruction) {
        return ADDRESS_MASK & maskInstruction(instruction);
    }

    /**
     * Masks an integer representing an instruction from memory down to a 16-bit
     * number.
     * 
     * @param instruction The instruction from memory.
     * @return An instruction masked to a value no greater than 65535.
     */
    public static int maskInstruction(int instruction) throws IllegalArgumentException {
        if (instruction > INSTRUCTION_MASK) {
            throw new IllegalArgumentException(
                    "Instructions must be an unsigned integer value between 0 and 65535.\n");
        }
        return INSTRUCTION_MASK & instruction;
    }
}
