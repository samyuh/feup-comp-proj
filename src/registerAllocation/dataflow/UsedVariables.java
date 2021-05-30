package registerAllocation.dataflow;

import org.specs.comp.ollir.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * Given an instruction, this class is responsible for
 * returning the used variables for this instruction.
 */
public class UsedVariables {

    Instruction instruction;

    public UsedVariables(Instruction instruction){
        this.instruction = instruction;
    }

    public String[] getUsed(){
        switch(instruction.getInstType()){
            case ASSIGN:
                return getUsedAssign((AssignInstruction) instruction);
            case CALL:
                return new String[]{};
            case PUTFIELD:
                return new String[]{};
            case UNARYOPER:
                return new String[]{};
            case BINARYOPER:
                return new String[]{};
            case NOPER:
                return getUsedNoper((SingleOpInstruction) instruction);
        }
        return new String[]{};
    }

    public String[] getUsedAssign(AssignInstruction instruction){
        List<String> used = new ArrayList<>();
        var rsh = instruction.getRhs();

        // Left side is an array operation.
        if (instruction.getDest() instanceof ArrayOperand){
            ArrayOperand arrayOperand =  (ArrayOperand) instruction.getDest();
            used.add(arrayOperand.getName());
            used.add(getArrayIndexName(arrayOperand));
        }

        used.addAll(Arrays.asList(new UsedVariables(rsh).getUsed()));
        return used.toArray(new String[0]);
    }
/*
    public String[] getUsedCall(){

    }

    public String[] getUsedPutField(){

    }

    public String[] getUsedUnaryOperator(){

    }

    public String[] getUsedBinaryOperator(){

    }
*/

    public String[] getUsedNoper(SingleOpInstruction instruction){
        instruction.show();
        List<String> used = new ArrayList<>();
        Element element = instruction.getSingleOperand();

        if (!element.isLiteral()){
            Operand op = (Operand) element;
            if (op instanceof ArrayOperand)
                used.add(getArrayIndexName(element));
            used.add(op.getName());
        }

        return used.toArray(new String[0]);
    }

    public String getArrayIndexName(Element arrayElement){
        ArrayOperand arrayOperand = (ArrayOperand) arrayElement;
        ArrayList<Element> indexOperand = arrayOperand.getIndexOperands();
        return ((Operand)indexOperand.get(0)).getName();
    }
}
