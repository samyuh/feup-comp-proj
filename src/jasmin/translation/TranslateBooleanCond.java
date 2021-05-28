package jasmin.translation;

import jasmin.InstSingleton;
import jasmin.methods.BuildMethod;
import org.specs.comp.ollir.OperationType;

import java.util.List;

/**
 * Translates a boolean expression if the given label.
 */
public class TranslateBooleanCond {

    public static String getJasminInst(String leftInst, String rightInst, OperationType opType, String label) {
        StringBuilder stringBuilder = new StringBuilder();

        if (opType == OperationType.LTH) stringBuilder.append(lthInst(leftInst, rightInst, label));
        else if (opType == OperationType.ANDB) stringBuilder.append(andInst(leftInst, rightInst, label));
        else if (opType == OperationType.NOTB) stringBuilder.append(notInst(rightInst, label));
        else if (opType == OperationType.GTE) stringBuilder.append(geInst(leftInst, rightInst, label));

        return stringBuilder.toString();
    }

    public static String notInst(String rightInst, String label) {
        StringBuilder stringBuilder = new StringBuilder();
        String nextInstLabel = getNextLabel();

        // Check if the element is 0.
        stringBuilder.append(rightInst);
        stringBuilder.append("ifne ").append(nextInstLabel).append("\n");
        // Case not 0.
        stringBuilder.append(InstSingleton.gotoInst(label));

        if (needGenLabel())
            stringBuilder.append(nextInstLabel).append(":\n");

        return stringBuilder.toString();
    }

    public static String andInst(String leftInst, String rightInst, String label) {
        StringBuilder stringBuilder = new StringBuilder();
        String nextInstLabel = getNextLabel();


        // Check if the first is 0.
        stringBuilder.append(leftInst);
        stringBuilder.append("ifeq ").append(nextInstLabel).append("\n");

        // Check if the second is 0.
        stringBuilder.append(rightInst);
        stringBuilder.append("ifeq ").append(nextInstLabel).append("\n");

        // Case not zero.
        stringBuilder.append(InstSingleton.gotoInst(label));

        if (needGenLabel())
            stringBuilder.append(nextInstLabel).append(":\n");

        return stringBuilder.toString();
    }

    public static String lthInst(String leftInst, String rightInst, String label) {
        StringBuilder stringBuilder = new StringBuilder();

        // Load elements
        stringBuilder.append(leftInst);
        stringBuilder.append(rightInst);

        // In case of success jump.
        stringBuilder.append("if_icmplt ").append(label).append("\n");


        return stringBuilder.toString();
    }

    public static String geInst(String leftInst, String rightInst, String label){
        StringBuilder stringBuilder = new StringBuilder();

        // Load elements
        stringBuilder.append(leftInst);
        stringBuilder.append(rightInst);

        // In case of success jump.
        stringBuilder.append("if_icmpge ").append(label).append("\n");


        return stringBuilder.toString();
    }

    public static String getNextLabel() {
        List<String> nextInstLabels = BuildMethod.getNextInstructionLabels();
        if (nextInstLabels.isEmpty())
            return "CMP_" + BuildMethod.getCurrentIndex();
        return nextInstLabels.get(0);
    }

    public static boolean needGenLabel(){
        List<String> nextInstLabels = BuildMethod.getNextInstructionLabels();
        return nextInstLabels.isEmpty();
    }

}
