package jasmin.methods;

import jasmin.translation.TranslateCall;
import jasmin.translation.TranslatePutField;
import jasmin.translation.TranslateReturn;
import org.specs.comp.ollir.*;

import java.io.ObjectOutputStream;
import java.util.ArrayList;

/**
 * Class responsible for translating a specific method.
 * It will call all the necessary method to translate all the instructions.
 */
public class BuildMethod extends JasminMethod {

    public static int currentIndex = 0;
    public static Method currentMethod;
    public BuildMethod(ClassUnit ollir) {
        super(ollir);
    }

    /**
     * Get the instructions in the body of a method.
     * @return Returns the body instructions as a string.
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

    /**
     * Calls the right method to translate the instruction
     * @return Return the instruction as a string.
     */
    public String getInstruction(Instruction inst, Method method){
        var table = OllirAccesser.getVarTable(method);
        return switch (inst.getInstType()) {
            case ASSIGN -> new BuildMethodAssigment(ollir, method).getInstructionAssign((AssignInstruction) inst);
            case CALL -> TranslateCall.getJasminInst((CallInstruction) inst, table) + "\n";
            case PUTFIELD -> TranslatePutField.getJasminInst((PutFieldInstruction) inst, table) + "\n";
            case RETURN -> TranslateReturn.getJasminInst((ReturnInstruction) inst, table) + "\n";
            default -> inst.getInstType().toString();
        };
    }

    public void addEnd() {
        methodString.append(".end method\n");
    }

    /**
     * Sometimes the current instruction needs the next to write the correct logic for the program.
     * This function "looks" to the next, but don't consumes it.
     * @return returns the next instruction.
     */
    public static Instruction getNextInstruction(){
        if (currentMethod.getInstructions().size() >= currentIndex + 1)
            return currentMethod.getInstructions().get(currentIndex+1);
        return null;
    }

    /**
     * Consumes the next instruction.
     */
    public static void skipNextInstruction(){
        currentIndex ++;
    }


}
