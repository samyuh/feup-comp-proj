package jasmin.methods;

import jasmin.*;
import org.specs.comp.ollir.*;

import java.util.ArrayList;

public class BuildMethodAssigment extends JasminMethod{
    Method method;
    public BuildMethodAssigment(ClassUnit ollir, Method method) {
        super(ollir);
        this.method = method;
    }

    public String getInstructionAssign(AssignInstruction assignInstruction){
        String destName = ((Operand)assignInstruction.getDest()).getName();
        Instruction rhs = assignInstruction.getRhs();
        Element lhs = assignInstruction.getDest();
        var table = OllirAccesser.getVarTable(method);
        int reg = table.get(destName).getVirtualReg();

        methodString.append(new BuildOperand(ollir, table, lhs).getOperand(rhs));
        methodString.append(InstSingleton.istore(reg));

        return this.toString();
    }




}
