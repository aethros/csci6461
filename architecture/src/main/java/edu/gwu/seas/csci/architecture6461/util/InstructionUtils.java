package edu.gwu.seas.csci.architecture6461.util;

import edu.gwu.seas.csci.architecture6461.models.CPU;
import edu.gwu.seas.csci.architecture6461.models.Memory;
import edu.gwu.seas.csci.architecture6461.models.Register;
import lombok.val;
import java.util.HashMap;
import java.util.Map;

public abstract class InstructionUtils {
    //// 0b  000000 00 00 0 00000
    ////       ^    ^   ^ ^   ^
    ////     OPCODE R  IX I ADDRS
    private static final int INSTRUCTION_MASK       = 0xFFFF; // 111111 11 11 1 11111
    private static final int INDIRECT_REGISTER_MASK = 0x00C0; // 000000 00 11 0 00000
    private static final int INDIRECT_MASK          = 0x0020; // 000000 00 00 1 00000
    private static final int ADDRESS_MASK           = 0x001F; // 000000 00 00 0 11111

    public enum Opcode {
        LDR, STR, LDA, JZ, JNE, JCC, JMA, JSR, RFS, SOB, JGE, LDX, STX, AMR, SMR,
        AIR, SIR, MLT, DVD, TRR, AND, ORR, NOT, SRC, RRC, FADD, VADD, LDFR, FSUB,
        VSUB, CNVRT, STFR, IN, OUT, CHK, HLT, TRAP;

        private static final Map<Integer, Opcode> opcodeMap = new HashMap<>();
        static {
            opcodeMap.put(0, HLT);
            opcodeMap.put(1, LDR);
            opcodeMap.put(2, STR);
            opcodeMap.put(3, LDA);
            opcodeMap.put(4, AMR);
            opcodeMap.put(5, SMR);
            opcodeMap.put(6, AIR);
            opcodeMap.put(7, SIR);
            opcodeMap.put(10, JZ);
            opcodeMap.put(11, JNE);
            opcodeMap.put(12, JCC);
            opcodeMap.put(13, JMA);
            opcodeMap.put(14, JSR);
            opcodeMap.put(15, RFS);
            opcodeMap.put(16, SOB);
            opcodeMap.put(17, JGE);
            opcodeMap.put(20, MLT);
            opcodeMap.put(21, DVD);
            opcodeMap.put(22, TRR);
            opcodeMap.put(23, AND);
            opcodeMap.put(24, ORR);
            opcodeMap.put(25, NOT);
            opcodeMap.put(30, TRAP);
            opcodeMap.put(31, SRC);
            opcodeMap.put(32, RRC);
            opcodeMap.put(33, FADD);
            opcodeMap.put(34, VADD);
            opcodeMap.put(35, FSUB);
            opcodeMap.put(36, VSUB);
            opcodeMap.put(37, CNVRT);
            opcodeMap.put(41, LDX);
            opcodeMap.put(42, STX);
            opcodeMap.put(50, LDFR);
            opcodeMap.put(51, STFR);
            opcodeMap.put(61, IN);
            opcodeMap.put(62, OUT);
            opcodeMap.put(63, CHK);
        }

        public static Opcode fromInteger(int x) {
            return opcodeMap.get(x);
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

    public static Register registerFromInstruction(CPU cpu, int instruction) {
        int maskedInstruction = maskInstruction(instruction);
        int registerBytes = (maskedInstruction >>> 6) & 0b01111;
        // the register bytes need to do an unsigned right shift (>>>)
        // 6 places (1 place past i bit and 5 places past address bits)
        // and then they need an and (&) with lower 4 bits to get their 
        // binary value.
        switch (registerBytes) {
            case 0b0000:
                return cpu.getGpRegister0();
            case 0b0100:
                return cpu.getGpRegister1();
            case 0b1000:
                return cpu.getGpRegister2();
            case 0b1100:
                return cpu.getGpRegister3();
            case 0b0001:
                return cpu.getIndexRegister1();
            case 0b0010:
                return cpu.getIndexRegister2();
            case 0b0011:
                return cpu.getIndexRegister3();
            default:
                return null;
        }
    }

    public static int addressFromInstruction(CPU cpu, Memory memory, int instruction) {
        int ea;
        val maskedInstruction = maskInstruction(instruction);
        val i = (maskedInstruction & INDIRECT_MASK) > 0;
        val ix = (maskedInstruction & INDIRECT_REGISTER_MASK) > 0;
        val address = InstructionUtils.getAddressBits(maskedInstruction);
        val register = InstructionUtils.registerFromInstruction(cpu, maskedInstruction);

        if (!i) {
            // NO indirect addressing
            if (!ix) {
                ea = address;
            } else {
                ea = register.getValue() + address;
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
                ea = memory.getValue(register.getValue() + address);
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
     * Masks an integer representing an instruction from memory down to a 16-bit number.
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
