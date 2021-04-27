package jasmin.translation;

import org.specs.comp.ollir.*;

import java.util.HashMap;

public class TranslateGetField {
    public static String getJasminInst(GetFieldInstruction putFieldInstruction, HashMap<String, Descriptor> table) {
        StringBuilder stringBuilder = new StringBuilder();
        Element firstOperand = putFieldInstruction.getFirstOperand();
        Element secondOperand = putFieldInstruction.getSecondOperand();
        String secondOperandName = ((Operand)secondOperand).getName();
        String returnType = TranslateType.getJasminType(secondOperand.getType());

        // load the first element
        stringBuilder.append(TranslateLoadStore.getLoadInst(firstOperand, table));

        // getField <returnType> <elementName>;
        stringBuilder.append("getField ").append(returnType).append(" ");
        stringBuilder.append(secondOperandName).append("\n");

        return stringBuilder.toString();
    }


}
