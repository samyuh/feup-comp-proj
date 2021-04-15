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

        switch(callType){
            case arraylength:
                Element arrayElement = callInstruction.getFirstArg();
                int arrayReg = getVirtualRegArray(arrayElement, table);
                return InstSingleton.getArrayLength(arrayReg);
            case NEW:
                Element element = callInstruction.getListOfOperands().get(0);
                return anewarray(getVirtualRegArray(element, table), element.getType());
            default:
                return "";
        }

    }
}
