package registerAllocation.dataflow;

import org.specs.comp.ollir.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.function.BinaryOperator;

/**
 * Given an instruction, this class is responsible for
 * returning the used variables for this instruction.
 */
public class UsedVariables {

    Instruction instruction;

    public UsedVariables(Instruction instruction) {
        this.instruction = instruction;
    }

    public String[] getUsed() {
        switch (instruction.getInstType()) {
            case ASSIGN:
                return getUsedAssign((AssignInstruction) instruction);
            case CALL:
                return getUsedCall((CallInstruction) instruction);
            case PUTFIELD:
                return getUsedPutField((PutFieldInstruction) instruction);
            case BINARYOPER:
                return getUsedBinaryOperator((BinaryOpInstruction) instruction);
            case NOPER:
                return getUsedNoper((SingleOpInstruction) instruction);
            case BRANCH:
                return getUsedBranch((CondBranchInstruction) instruction);
            case RETURN:
                return getUsedReturn((ReturnInstruction) instruction);
        }
        return new String[]{};
    }

    public String[] getUsedAssign(AssignInstruction instruction) {
        List<String> used = new ArrayList<>();
        var rsh = instruction.getRhs();

        // Left side is an array operation.
        if (instruction.getDest() instanceof ArrayOperand)
            used.addAll(getOperandUses(instruction.getDest()));

        used.addAll(Arrays.asList(new UsedVariables(rsh).getUsed()));
        return used.toArray(new String[0]);
    }

    public String[] getUsedBranch(CondBranchInstruction instruction) {
        List<String> used = new ArrayList<>();

        if (instruction.getCondOperation().getOpType() == OperationType.NOTB) {
            used.addAll(getOperandUses(instruction.getRightOperand()));
        } else {
            used.addAll(getOperandUses(instruction.getLeftOperand()));
            used.addAll(getOperandUses(instruction.getRightOperand()));
        }
        return used.toArray(new String[0]);
    }

    public String[] getUsedReturn(ReturnInstruction instruction){
        if (instruction.hasReturnValue())
            return getOperandUses(instruction.getOperand()).toArray(new String[0]);
        return new String[]{};
    }

    public String[] getUsedCall(CallInstruction instruction) {
        List<String> used = new ArrayList<>();
        ArrayList<Element> elements = instruction.getListOfOperands();
        for (var element : elements) {
            used.addAll(getOperandUses(element));
        }
        return used.toArray(new String[0]);
    }


    public String[] getUsedPutField(PutFieldInstruction instruction) {
        List<String> used = new ArrayList<>();
        used.addAll(getOperandUses(instruction.getThirdOperand()));
        return used.toArray(new String[0]);
    }

    public String[] getUsedBinaryOperator(BinaryOpInstruction instruction) {
        OperationType instType = instruction.getUnaryOperation().getOpType();
        List<String> used = new ArrayList<>();

        if (instType == OperationType.NOTB) {
            used.addAll(getOperandUses(instruction.getRightOperand()));
        } else {
            used.addAll(getOperandUses(instruction.getLeftOperand()));
            used.addAll(getOperandUses(instruction.getRightOperand()));
        }

        return used.toArray(new String[0]);
    }


    public String[] getUsedNoper(SingleOpInstruction instruction) {
        Element element = instruction.getSingleOperand();
        return getOperandUses(element).toArray(new String[0]);
    }

    public List<String> getOperandUses(Element element) {
        List<String> elementsName = new ArrayList<>();
        if (element.isLiteral()) return elementsName;
        else if (element instanceof ArrayOperand) {
            ArrayOperand arrayOperand = (ArrayOperand) element;
            elementsName.add(getArrayIndexName(arrayOperand));
        }
        elementsName.add(((Operand) element).getName());

        return elementsName;
    }

    public String getArrayIndexName(Element arrayElement) {
        ArrayOperand arrayOperand = (ArrayOperand) arrayElement;
        ArrayList<Element> indexOperand = arrayOperand.getIndexOperands();
        return ((Operand) indexOperand.get(0)).getName();
    }
}
