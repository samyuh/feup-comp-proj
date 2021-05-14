package jasmin.translation;

import org.specs.comp.ollir.Descriptor;
import org.specs.comp.ollir.Element;
import org.specs.comp.ollir.Operand;
import org.specs.comp.ollir.PutFieldInstruction;
import jasmin.*;
import java.util.HashMap;

public class TranslatePutField {

    public static String getJasminInst(PutFieldInstruction putFieldInstruction, HashMap<String, Descriptor> table){
        StringBuilder stringBuilder = new StringBuilder();
        Element classElement = putFieldInstruction.getFirstOperand();
        Element fieldElement = putFieldInstruction.getSecondOperand();
        Element valueElement = putFieldInstruction.getThirdOperand();

        String className = TranslateType.getJasminType(classElement.getType());
        String fieldName = ((Operand)fieldElement).getName();
        String type = TranslateType.getJasminType(fieldElement.getType());

        stringBuilder.append(TranslateLoadStore.getLoadInst(classElement, table));
        stringBuilder.append(TranslateLoadStore.getLoadInst(valueElement, table));
        stringBuilder.append(InstSingleton.putfield(className, fieldName, type));

        return stringBuilder.toString();
    }
}
