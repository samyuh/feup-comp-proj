package jasmin.translation;

import jasmin.InstSingleton;
import jasmin.UtilsJasmin;
import org.specs.comp.ollir.*;
import visitor.Utils;

import java.util.HashMap;

import static jasmin.InstSingleton.anewarray;
import static jasmin.UtilsJasmin.getDescriptor;
import static jasmin.UtilsJasmin.getVirtualReg;

public class TranslateCall {

    public static String getJasminInst(CallInstruction callInstruction, HashMap<String, Descriptor> table){
        CallType callType = OllirAccesser.getCallInvocation(callInstruction);
        switch(callType){
            case arraylength:
                Element arrayElement = callInstruction.getFirstArg();
                int arrayReg = getVirtualReg(arrayElement, table);
                return InstSingleton.getArrayLength(arrayReg);
            case NEW:
                if (callInstruction.getListOfOperands().size() != 0) {
                    Element element = callInstruction.getListOfOperands().get(0);
                    Type type = element.getType();
                    return anewarray(getVirtualReg(element, table), TranslateType.getJasminType(type));
                }else {
                    Element element = callInstruction.getFirstArg();
                    Type type = element.getType();
                    return anewarray(getVirtualReg(element, table), TranslateType.getJasminType(type, (Operand) element));
                }
            case invokevirtual:
                String objectName;
                String methodCall;
                StringBuilder stringBuilder = new StringBuilder();

                Element firstArg = callInstruction.getFirstArg();
                stringBuilder.append(TranslateElement.getJasminInst(firstArg, table));
                objectName = UtilsJasmin.getObjectName(firstArg);

                methodCall = ((LiteralElement) callInstruction.getSecondArg()).getLiteral();
                methodCall = methodCall.substring(1, methodCall.length()-1);


                for (Element operand : callInstruction.getListOfOperands())
                    stringBuilder.append(TranslateElement.getJasminInst(operand, table));


                stringBuilder.append("invokevirtual " + objectName + "." + methodCall);

                stringBuilder.append(UtilsJasmin.getArguments(callInstruction.getListOfOperands()));
                stringBuilder.append(TranslateType.getJasminType(callInstruction.getReturnType()));
                return stringBuilder.toString() + "\n";
            default:
                return "";
        }

    }
}
