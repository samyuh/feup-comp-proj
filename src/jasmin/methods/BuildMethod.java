package jasmin.methods;

import jasmin.InstSingleton;
import jasmin.translation.TranslateBranch;
import jasmin.translation.TranslateCall;
import jasmin.translation.TranslatePutField;
import jasmin.translation.TranslateReturn;
import org.specs.comp.ollir.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Class responsible for translating a specific method.
 * It will call all the necessary method to translate all the instructions.
 */
public class BuildMethod extends JasminMethod {

    public static int currentIndex = 0;
    public static Method currentMethod;
    public static int maxStackSize = 0;
    public static int stackSizeUntilNow = 0;

    public BuildMethod(ClassUnit ollir) {
        super(ollir);
    }

    /**
     * Get the instructions in the body of a method.
     *
     * @return Returns the body instructions as a string.
     */
    public String getMethod(int index) {
        maxStackSize = 0;
        stackSizeUntilNow = 0;
        currentMethod = ollir.getMethod(index);

        StringBuilder methodScope = new StringBuilder();
        StringBuilder methodBody = new StringBuilder();

        ArrayList<Instruction> instructions = currentMethod.getInstructions();
        Instruction inst = null;

        for (currentIndex = 0; currentIndex < instructions.size(); currentIndex++) {
            methodBody.append(getLabels(instructions.get(currentIndex)));
            inst = instructions.get(currentIndex);
            methodBody.append(getInstruction(inst, currentMethod));
            methodBody.append("\n");
        }


        if (inst.getInstType() != InstructionType.RETURN && currentMethod.getReturnType().getTypeOfElement() == ElementType.VOID) {
            methodBody.append("return\n");
        }

        methodScope.append(new BuildMethodScope(ollir, currentMethod, maxStackSize).getScope());

        methodString.append(methodScope);
        methodString.append(methodBody);

        methodString.append(".end method\n");
        return this.toString();
    }


    /**
     * Calls the right method to translate the instruction
     *
     * @return Return the instruction as a string.
     */
    public String getInstruction(Instruction inst, Method method) {
        var table = OllirAccesser.getVarTable(method);

        switch (inst.getInstType()) {
            case ASSIGN:
                return new BuildMethodAssigment(ollir, method).getInstructionAssign((AssignInstruction) inst);
            case CALL:
                return TranslateCall.getJasminInst((CallInstruction) inst, table, false) + "\n";
            case PUTFIELD:
                return TranslatePutField.getJasminInst((PutFieldInstruction) inst, table) + "\n";
            case RETURN:
                return TranslateReturn.getJasminInst((ReturnInstruction) inst, table) + "\n";
            case BRANCH:
                return TranslateBranch.getJasminInst((CondBranchInstruction) inst, table, method) + "\n";
            case GOTO:
                return InstSingleton.gotoInst(((GotoInstruction) inst).getLabel());
            default:
                return inst.getInstType().toString();
        }
    }

    public String getLabels(Instruction instruction) {
        StringBuilder stringBuilder = new StringBuilder();
        var labels = currentMethod.getLabels(instruction);
        labels.forEach((label) -> {
            stringBuilder.append(label).append(":\n");
        });

        return stringBuilder.toString();

    }

    /**
     * Sometimes the current instruction needs the next to write the correct logic for the program.
     * This function "looks" to the next, but don't consumes it.
     *
     * @return returns the next instruction.
     */
    public static Instruction getNextInstruction() {
        if (currentMethod.getInstructions().size() > currentIndex + 1)
            return currentMethod.getInstructions().get(currentIndex + 1);
        return null;
    }

    /**
     * This method is responsible for returning the label of the next instruction.
     *
     * @return The name of the next label instruction.
     */
    public static List<String> getNextInstructionLabels() {
        Instruction nextInstruction = getNextInstruction();
        return currentMethod.getLabels(nextInstruction);
    }

    /**
     * Consumes the next instruction.
     */
    public static void skipNextInstruction() {
        currentIndex++;
    }

    public static String getCurrentIndex() {
        return String.valueOf(currentIndex);
    }

    public static void updateMaxStack(int popSize, int pushSize) {
        stackSizeUntilNow -= popSize;
        stackSizeUntilNow += pushSize;
        maxStackSize = Math.max(maxStackSize, stackSizeUntilNow);
    }
}
