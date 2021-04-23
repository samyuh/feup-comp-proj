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
        Element firstArg = putFieldInstruction.getFirstOperand();
        Element secondArg = putFieldInstruction.getSecondOperand();
        Element thirdArg = putFieldInstruction.getThirdOperand();

        String className = ((Operand)firstArg).getName();
        String varName = ((Operand)secondArg).getName();
        String type = TranslateType.getJasminType(secondArg.getType());
        stringBuilder.append(TranslateLoadStore.getLoadInst(thirdArg, table));
        stringBuilder.append(InstSingleton.putfield(className, varName, type));

        return stringBuilder.toString();
    }
}
