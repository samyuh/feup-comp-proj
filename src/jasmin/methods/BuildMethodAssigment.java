package jasmin.methods;

import jasmin.translation.TranslateLoadStore;
import org.specs.comp.ollir.*;

public class BuildMethodAssigment extends JasminMethod{
    Method method;
    public BuildMethodAssigment(ClassUnit ollir, Method method) {
        super(ollir);
        this.method = method;
    }

    public String getInstructionAssign(AssignInstruction assignInstruction){
        Instruction rhs = assignInstruction.getRhs();
        Element lhs = assignInstruction.getDest();
        var table = OllirAccesser.getVarTable(method);

        methodString.append(new BuildOperand(ollir, table, lhs).getOperand(rhs));
        methodString.append(TranslateLoadStore.getJasminStore(lhs, table));

        return this.toString();
    }


}
