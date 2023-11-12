package edu.gwu.seas.csci.architecture6461.util;

public abstract class InstructionUtils {
    private static final int INSTRUCTION_MASK = 0x0000FFFF;

    public enum Opcode {
        LDR, STR, LDA, JZ, JNE, JCC, JMA, JSR, RFS, SOB, JGE, LDX, STX, AMR, SMR,
        AIR, SIR, MLT, DVD, TRR, AND, ORR, NOT, SRC, RRC, FADD, VADD, LDFR, FSUB,
        VSUB, CNVRT, STFR, IN, OUT, CHK, HLT, TRAP;

        public static Opcode fromInteger(int x) {
            switch(x) {
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
        IllegalMemoryAddressToReservedLocations,
        IllegalTrapCode,
        IllegalOperationCode,
        IllegalMemoryAddressBeyond2048;
        
        public static MachineFaultCode fromInteger(int x) {
            switch (x) {
                case 1:
                    return IllegalMemoryAddressToReservedLocations;
                case 2:
                    return IllegalTrapCode;
                case 4:
                    return IllegalOperationCode;
                case 8:
                    return IllegalMemoryAddressBeyond2048;
                default:
                    return null;
            }
        }

        public static int toInteger(MachineFaultCode code) {
            switch (code) {
                case IllegalMemoryAddressToReservedLocations:
                    return 1;
                case IllegalTrapCode:
                    return 2;
                case IllegalOperationCode:
                    return 4;
                case IllegalMemoryAddressBeyond2048:
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

    public static Opcode opcodeFromInstruction(int instruction)
    {
        if (instruction > INSTRUCTION_MASK) {
            throw new IllegalArgumentException("Instructions must be an unsigned integer value between 0 and 65535.\n");
        }

        int maskedInstruction = INSTRUCTION_MASK & instruction;
        int opcodeBytes = maskedInstruction >>> 10;
        return Opcode.fromInteger(opcodeBytes);
    }

    public static int addressFromInstruction(int instruction)
    {
        if (instruction > INSTRUCTION_MASK) {
            throw new IllegalArgumentException("Instructions must be an unsigned integer value between 0 and 65535.\n");
        }

        int maskedInstruction = INSTRUCTION_MASK & instruction;
        /**
         * Effective Address (EA) = 
         *          if I field = 0:
                            // NO indirect addressing
                            If IX = 00
                                EA = contents of the Address field  c(Address Field)
                            else if IX = 1..3,
                                EA = c(IX) + c(Address Field) // that is, the IX field has an
                                                              // index register number, the contents of that register are
                                                              // added to the contents of the address field
                    if I field = 1:
                            // indirect addressing, but NO indexing
                            if IX = 00,
                                EA = c(c(Address Field)) // It helps to think in terms of a
                                                         // pointer where the address field has the location of the EA
                                                         // in memory

                            // both indirect addressing and indexing
                            else if IX = 1..3
                                c(c(IX) + c(Address Field))
                        
                        // another way to think of this is take the EA computed without
                        // indirections and use that as the location of where the EA is stored.
         */
        return maskedInstruction;
    }
}
