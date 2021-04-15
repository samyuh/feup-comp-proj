package jasmin.methods;

import jasmin.InstSingleton;
import org.specs.comp.ollir.*;

import java.util.ArrayList;

public class BuildMethodAssigment extends JasminMethod{
    Method method;
    public BuildMethodAssigment(ClassUnit ollir, Method method) {
        super(ollir);
        this.method = method;
    }

    public String getAssigments(){
        ArrayList<Instruction> instructions = method.getInstructions();
        for (Instruction instruction : instructions) addInstruction(instruction);

        return this.toString();
    }

    public void addInstruction(Instruction instruction){
        InstructionType instructionType = instruction.getInstType();

        switch(instructionType){
            case ASSIGN:
                addInstructionAssign((AssignInstruction) instruction);
        }
    }

    public void addInstructionAssign(AssignInstruction assignInstruction){
        assignInstruction.show();
        String destName = ((Operand)assignInstruction.getDest()).getName();
        Instruction rhs = assignInstruction.getRhs();
        Element lhs = assignInstruction.getDest();
        var table = OllirAccesser.getVarTable(method);
        int reg = table.get(destName).getVirtualReg();

        methodString.append(new BuildOperand(ollir, table, lhs).getOperand(rhs));
        methodString.append(InstSingleton.istore(reg));
        addEndLine();

    }




}
