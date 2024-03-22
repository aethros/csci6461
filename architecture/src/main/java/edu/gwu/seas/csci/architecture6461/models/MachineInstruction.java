package edu.gwu.seas.csci.architecture6461.models;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import edu.gwu.seas.csci.architecture6461.managers.Assembler;
import edu.gwu.seas.csci.architecture6461.util.InstructionUtils;
import edu.gwu.seas.csci.architecture6461.util.InstructionUtils.Opcode;
import lombok.val;

public class MachineInstruction {
    public enum InstructionType {
        ADDRESS_REGISTER, SHIFT_ROTATE, TWO_OPERAND, THREE_OPERAND, FOUR_OPERAND, SPECIAL_PURPOSE
    }
    public enum Operand {
        R, RX, RY, IX, I, ADDRESS, IMMED, COUNT, AL, LR
    }

    private Opcode opcode;
    private Map<Operand, Integer> operands;
    private int instruction;
    private InstructionType type;

    public MachineInstruction(Opcode code, int instruction) {
        this.operands = new HashMap<Operand, Integer>();
        this.opcode = code;
        this.instruction = instruction;
        this.type = this.getType();
    }

    private InstructionType getType() {
        switch (this.opcode) {
            case AIR:
            case SIR:
            case IN:
            case OUT:
            case CHK:
                return InstructionType.ADDRESS_REGISTER;
            case SRC:
            case RRC:
                return InstructionType.SHIFT_ROTATE;
            case MLT:
            case DVD:
            case TRR:
            case AND:
            case ORR:
                return InstructionType.TWO_OPERAND;
            case LDX:
            case STX:
            case JZ:
            case JNE:
            case JMA:
            case JSR:
                return InstructionType.THREE_OPERAND;
            case LDR:
            case STR:
            case LDA:
            case JCC:
            case SOB:
            case JGE:
            case AMR:
            case SMR:
                return InstructionType.FOUR_OPERAND;
            case HLT:
            case RFS:
            case NOT:
            case SETCCE:
            case TRAP:
                return InstructionType.SPECIAL_PURPOSE;
            default:
                return null;
        }
    }

    public void setOperands() {
        switch (this.type) {
            case ADDRESS_REGISTER:
                this.setAddressRegisterOperands();
                break;
            case SHIFT_ROTATE:
                this.setShiftRotateOperands();
                break;
            case TWO_OPERAND:
                this.setTwoOperandOperands();
                break;
            case THREE_OPERAND:
                this.setThreeOperandOperands();
                break;
            case FOUR_OPERAND:
                this.setFourOperandOperands();
                break;
            case SPECIAL_PURPOSE:
                this.setSpecialPurposeOperands();
                break;
            default:
                break;
        }
    }
    private void setAddressRegisterOperands() {
        this.operands.put(Operand.R, (this.instruction & InstructionUtils.GP_REGISTER_MASK) >>> Assembler.REGISTER_BIT_POSITION);
        this.operands.put(Operand.ADDRESS, this.instruction & InstructionUtils.ADDRESS_MASK);
    }

    private void setShiftRotateOperands() {
        this.operands.put(Operand.R, (this.instruction & InstructionUtils.GP_REGISTER_MASK) >>> Assembler.REGISTER_BIT_POSITION);
        this.operands.put(Operand.AL,(this.instruction & InstructionUtils.AL_MASK) >>> Assembler.AL_BIT_POSITION);
        this.operands.put(Operand.LR,(this.instruction & InstructionUtils.LR_MASK) >>> Assembler.LR_BIT_POSITION);
        this.operands.put(Operand.COUNT, this.instruction & InstructionUtils.COUNT_BITS);
    }

    private void setTwoOperandOperands() {
        this.operands.put(Operand.RX, (this.instruction & InstructionUtils.GP_REGISTER_MASK) >>> Assembler.REGISTER_BIT_POSITION);
        this.operands.put(Operand.RY, (this.instruction & InstructionUtils.INDEX_REGISTER_MASK) >>> Assembler.INDEX_BIT_POSITION);
    }

    private void setThreeOperandOperands() {
        this.operands.put(Operand.IX, (this.instruction & InstructionUtils.INDEX_REGISTER_MASK) >>> Assembler.INDEX_BIT_POSITION);
        this.operands.put(Operand.I, (this.instruction & InstructionUtils.INDIRECT_MASK) >>> Assembler.INDIRECT_BIT_POSITION);
        this.operands.put(Operand.ADDRESS, this.instruction & InstructionUtils.ADDRESS_MASK);
    }

    private void setFourOperandOperands() {
        this.operands.put(Operand.R, (this.instruction & InstructionUtils.GP_REGISTER_MASK) >>> Assembler.REGISTER_BIT_POSITION);
        this.operands.put(Operand.IX, (this.instruction & InstructionUtils.INDEX_REGISTER_MASK) >>> Assembler.INDEX_BIT_POSITION);
        this.operands.put(Operand.I, (this.instruction & InstructionUtils.INDIRECT_MASK) >>> Assembler.INDIRECT_BIT_POSITION);
        this.operands.put(Operand.ADDRESS, this.instruction & InstructionUtils.ADDRESS_MASK);
    }

    private void setSpecialPurposeOperands() {
        switch (this.opcode) {
            case RFS:
                this.operands.put(Operand.IMMED, this.instruction & InstructionUtils.COUNT_BITS);
                break;
            case NOT:
                this.operands.put(Operand.RX, (this.instruction & InstructionUtils.GP_REGISTER_MASK) >>> Assembler.REGISTER_BIT_POSITION);
                break;
            case SETCCE:
                this.operands.put(Operand.R, (this.instruction & InstructionUtils.GP_REGISTER_MASK) >>> Assembler.REGISTER_BIT_POSITION);
                break;
            case TRAP:
                this.operands.put(Operand.COUNT, this.instruction & InstructionUtils.COUNT_BITS);
                break;
            default:
                // Halt is a special case function
                break;
        }
    }

    public Integer getOperand(Operand operand) {
        return this.operands.get(operand);
    }

    public Integer getAddress() {
        return (this.operands.get(Operand.ADDRESS) != null)
                    ? this.operands.get(Operand.ADDRESS)
                    : (this.operands.get(Operand.COUNT) != null)
                        ? this.operands.get(Operand.COUNT)
                        : (this.operands.get(Operand.IMMED) != null)
                            ? this.operands.get(Operand.IMMED)
                            : null;
    }

    public int getValue() {
        return this.instruction;
    }

    public Opcode getOpcode() {
        return this.opcode;
    }

    @Override
    public String toString() {
        String text = "";
        val nonNullEntries = operands.entrySet().stream()
        .filter(entry -> entry.getKey() != null)
        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        for (var e : nonNullEntries.entrySet()) {
            text += e.getKey().toString() + ": " + e.getValue() + ", ";
        }

        return this.opcode.toString() + " " + text;
    }
}
