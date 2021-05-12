package jasmin.translation;

import jasmin.InstSingleton;
import jasmin.methods.BuildMethod;
import org.specs.comp.ollir.*;

import java.util.HashMap;

public class TranslateBranch {

    public static String getJasminInst(CondBranchInstruction inst, HashMap<String, Descriptor> table, Method method) {
        Element leftElement = inst.getLeftOperand();
        Element rightElement = inst.getRightOperand();

        String leftInst = TranslateLoadStore.getLoadInst(leftElement, table);
        String rightInst = TranslateLoadStore.getLoadInst(rightElement, table);
        Operation condOperation = inst.getCondOperation();

        String label = inst.getLabel();
        return TranslateBooleanCond.getJasminInst(leftInst, rightInst, condOperation.getOpType(), label);
    }

/*
    public static String translate(String leftInst, String rightInst, OperationType opType, String label){
        StringBuilder stringBuilder = new StringBuilder();
        if (opType == OperationType.LTH) stringBuilder.append(lthInst(leftInst, rightInst, label));
        else if (opType == OperationType.ANDB) stringBuilder.append(andInst(leftInst, rightInst, label));
        else if (opType == OperationType.NOTB ) stringBuilder.append(notInst(rightInst, label));
        return stringBuilder.toString();
    }*/


}
