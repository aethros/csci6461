package edu.gwu.seas.csci.architecture6461.util;

import edu.gwu.seas.csci.architecture6461.models.CPU;
import edu.gwu.seas.csci.architecture6461.models.Memory;
import edu.gwu.seas.csci.architecture6461.models.Register;
import lombok.val;

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

        public static Opcode fromInteger(int x) {
            switch (x) {
                case 0:
                    return HLT;
                case 1:
                    return LDR;
                case 2:
                    return STR;
                case 3:
                    return LDA;
                case 4:
                    return AMR;
                case 5:
                    return SMR;
                case 6:
                    return AIR;
                case 7:
                    return SIR;
                case 10:
                    return JZ;
                case 11:
                    return JNE;
                case 12:
                    return JCC;
                case 13:
                    return JMA;
                case 14:
                    return JSR;
                case 15:
                    return RFS;
                case 16:
                    return SOB;
                case 17:
                    return JGE;
                case 20:
                    return MLT;
                case 21:
                    return DVD;
                case 22:
                    return TRR;
                case 23:
                    return AND;
                case 24:
                    return ORR;
                case 25:
                    return NOT;
                case 30:
                    return TRAP;
                case 31:
                    return SRC;
                case 32:
                    return RRC;
                case 33:
                    return FADD;
                case 34:
                    return VADD;
                case 35:
                    return FSUB;
                case 36:
                    return VSUB;
                case 37:
                    return CNVRT;
                case 41:
                    return LDX;
                case 42:
                    return STX;
                case 50:
                    return LDFR;
                case 51:
                    return STFR;
                case 61:
                    return IN;
                case 62:
                    return OUT;
                case 63:
                    return CHK;
                default:
                    return null;
            }
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
                ea = register.getValue() + address; // that is, the IX field has an
                                                    // index register number, the contents of that register are
                                                    // added to the contents of the address field
            }
        } else {
            if (!ix) {
                // indirect addressing, but NO indexing
                ea = memory.getValue(address); // It helps to think in terms of a
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
    public static int maskInstruction(int instruction) {
        if (instruction > INSTRUCTION_MASK) {
            throw new IllegalArgumentException(
                    "Instructions must be an unsigned integer value between 0 and 65535.\n");
        }
        return INSTRUCTION_MASK & instruction;
    }
}
