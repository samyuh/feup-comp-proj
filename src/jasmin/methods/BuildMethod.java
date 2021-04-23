package jasmin.methods;

import jasmin.translation.TranslateCall;
import org.specs.comp.ollir.*;


public class BuildMethod extends JasminMethod {


    public BuildMethod(ClassUnit ollir) {
        super(ollir);
    }

    public String getMethod(int index) {
        Method method = ollir.getMethod(index);
        methodString.append(new BuildMethodScope(ollir, method).getScope());
        for (Instruction inst: method.getInstructions())
            methodString.append(getInstruction(inst, method));
        addEnd();
        addEndLine();
        addEndLine();

        return this.toString();
    }

    public String getInstruction(Instruction inst, Method method){
        return switch (inst.getInstType()) {
            case ASSIGN -> new BuildMethodAssigment(ollir, method).getInstructionAssign((AssignInstruction) inst);
            case CALL -> TranslateCall.getJasminInst((CallInstruction) inst, OllirAccesser.getVarTable(method)) + "\n";
            default -> "";
        };
    }

    public void addEnd() {
        methodString.append(".end method");
    }


}
