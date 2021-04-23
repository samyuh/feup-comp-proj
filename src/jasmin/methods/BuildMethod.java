package jasmin.methods;

import jasmin.translation.TranslateCall;
import org.specs.comp.ollir.*;

import java.util.ArrayList;


public class BuildMethod extends JasminMethod {

    public static int currentIndex = 0;
    public static Method currentMethod;
    public BuildMethod(ClassUnit ollir) {
        super(ollir);
    }

    /**
     * Get the body of a method.
     * @return Returns the body as a string.
     */
    public String getMethod(int index) {
        currentMethod = ollir.getMethod(index);
        methodString.append(new BuildMethodScope(ollir, currentMethod).getScope());
        ArrayList<Instruction> instructions = currentMethod.getInstructions();
        for (currentIndex = 0; currentIndex < instructions.size(); currentIndex++) {
            Instruction inst = instructions.get(currentIndex);
            methodString.append(getInstruction(inst, currentMethod));
            addEndLine();
        }

        addEnd();
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
        methodString.append(".end method\n");
    }

    public static Instruction getNextInstruction(){
        if (currentMethod.getInstructions().size() >= currentIndex + 1)
            return currentMethod.getInstructions().get(currentIndex+1);
        return null;
    }

    public static void skipNextInstruction(){
        currentIndex ++;
    }


}
