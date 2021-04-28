package jasmin.translation;

import org.specs.comp.ollir.*;

import java.util.HashMap;

public class TranslateReturn {

    public static String getJasminInst(ReturnInstruction returnInst, HashMap<String, Descriptor> table){
        StringBuilder stringBuilder = new StringBuilder();
        System.out.println("return value " + returnInst.hasReturnValue());
        if (returnInst.hasReturnValue()){
            Element operand = returnInst.getOperand();
            stringBuilder.append(TranslateLoadStore.getLoadInst(operand, table));
            ElementType elementType = returnInst.getOperand().getType().getTypeOfElement();
            if (elementType == ElementType.INT32 || elementType == ElementType.BOOLEAN){
                System.out.println("ireturn " + elementType);
                stringBuilder.append("ireturn");
            } else {

                System.out.println("areturn " + elementType);
                stringBuilder.append("areturn");
            }
            return stringBuilder.toString();
        }

        return "return";
    }
}
