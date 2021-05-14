package jasmin.translation;

import jasmin.InstSingleton;
import org.specs.comp.ollir.*;

import java.util.HashMap;

public class TranslateGetField {
    public static String getJasminInst(GetFieldInstruction getFieldInstruction, HashMap<String, Descriptor> table) {
        StringBuilder stringBuilder = new StringBuilder();
        Element classElement = getFieldInstruction.getFirstOperand();
        Element fieldElement = getFieldInstruction.getSecondOperand();

        String className = TranslateType.getJasminType(classElement.getType());
        String fieldName = ((Operand)fieldElement).getName();
        String type = TranslateType.getJasminType(fieldElement.getType());

        stringBuilder.append(TranslateLoadStore.getLoadInst(classElement, table));
        stringBuilder.append(InstSingleton.getfield(className, fieldName, type));

        return stringBuilder.toString();
    }


}
