package jasmin.translation;


import jasmin.methods.BuildMethod;

import jasmin.*;
import org.specs.comp.ollir.OperationType;


// This class is responsible for translating boolean operations.
public class TranslateBooleanOp {

    public static  String getJasminInst(String leftInst, String rightInst, OperationType opType){
        StringBuilder stringBuilder = new StringBuilder();
        if (opType == OperationType.LTH) stringBuilder.append(lthInst(leftInst, rightInst));
        else if (opType == OperationType.ANDB) stringBuilder.append(andInst(leftInst, rightInst));
        // TODO: do we also have to support the NOT?
        else if (opType == OperationType.NOTB ) stringBuilder.append(notInst(rightInst));
        return stringBuilder.toString();
    }

    public static String notInst(String rightInst){
        StringBuilder stringBuilder = new StringBuilder();

        // Since there are two labels for this instruction, we must multiply it by two to avoid repetitions.
        String label_1 = "IFNE"+ BuildMethod.currentIndex*2;
        String label_2 = "IFNE_"+(BuildMethod.currentIndex*2+1);

        // Check if the element is 0.
        stringBuilder.append(rightInst);
        stringBuilder.append("ifne ").append(label_1).append("\n");
        stringBuilder.append(InstSingleton.iconst("1"));
        stringBuilder.append("goto ").append(label_2).append("\n");
        stringBuilder.append(InstSingleton.label(label_1));
        stringBuilder.append(InstSingleton.iconst("0"));
        stringBuilder.append(InstSingleton.label(label_2));
        return stringBuilder.toString();
    }
    public static String andInst(String leftInst, String rightInst){
        StringBuilder stringBuilder = new StringBuilder();

        // Since there are two labels for this instruction, we must multiply it by two to avoid repetitions.
        String label_1 = "IFEQ_"+ BuildMethod.currentIndex*2;
        String label_2 = "IFEQ_"+(BuildMethod.currentIndex*2+1);

        // Check if the first is 0.
        stringBuilder.append(leftInst);
        stringBuilder.append("ifeq ").append(label_1).append("\n");

        // Check if the second is 0.
        stringBuilder.append(rightInst);
        stringBuilder.append("ifeq ").append(label_1).append("\n");

        // Case 1.
        stringBuilder.append(InstSingleton.iconst("1"));
        stringBuilder.append("goto ").append(label_2).append("\n");

        // Case it was 0.
        stringBuilder.append(InstSingleton.label(label_1));
        stringBuilder.append(InstSingleton.iconst("0"));
        // Case 1.
        stringBuilder.append(InstSingleton.label(label_2));

        return stringBuilder.toString();
    }
    public static String lthInst(String leftInst, String rightInst){
        StringBuilder stringBuilder = new StringBuilder();

        // Load elements
        stringBuilder.append(leftInst);
        stringBuilder.append(rightInst);

        // Since there are two labels for this instruction, we must multiply it by two to avoid repetitions.
        String label_1 = "IFICMP_"+ BuildMethod.currentIndex*2;
        String label_2 = "IFICMP_"+(BuildMethod.currentIndex*2+1);
        // In case of success jump.
        stringBuilder.append("if_icmplt ").append(label_1).append("\n");
        stringBuilder.append(InstSingleton.iconst("0"));
        stringBuilder.append("goto ").append(label_2).append("\n");
        stringBuilder.append(InstSingleton.label(label_1));
        stringBuilder.append(InstSingleton.iconst("1"));
        stringBuilder.append(InstSingleton.label(label_2));

        return stringBuilder.toString();
    }
}
