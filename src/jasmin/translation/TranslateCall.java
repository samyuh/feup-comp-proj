package jasmin.translation;

import jasmin.InstSingleton;
import jasmin.UtilsJasmin;
import org.specs.comp.ollir.*;

import java.util.HashMap;

import static jasmin.InstSingleton.anewarray;
import static jasmin.UtilsJasmin.getVirtualRegArray;

public class TranslateCall {

    public static String getJasminInst(CallInstruction callInstruction, HashMap<String, Descriptor> table){
        CallType callType = OllirAccesser.getCallInvocation(callInstruction);
        System.out.println(callType);
        switch(callType){
            case arraylength:
                Element arrayElement = callInstruction.getFirstArg();
                int arrayReg = getVirtualRegArray(arrayElement, table);
                return InstSingleton.getArrayLength(arrayReg);
            case NEW:
                System.out.println(callInstruction.getFirstArg());
                if (callInstruction.getListOfOperands().size() != 0) {
                    Element element = callInstruction.getListOfOperands().get(0);
                    Type type = element.getType();
                    return anewarray(getVirtualRegArray(element, table), TranslateType.getJasminType(type));
                }else {
                    Element element = callInstruction.getFirstArg();
                    Type type = element.getType();
                    return anewarray(getVirtualRegArray(element, table), TranslateType.getJasminType(type, (Operand) element));
                }

            default:
                return "";
        }

    }
}
