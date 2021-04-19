package jasmin.translation;

import jasmin.InstSingleton;
import jasmin.UtilsJasmin;
import org.specs.comp.ollir.*;
import visitor.Utils;

import java.util.ArrayList;
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
                return invokevirtual(callInstruction, table);

            case invokestatic:
                return invokestatic(callInstruction, table);
            default:
                return "";
        }

    }

    private static String invokevirtual(CallInstruction callInstruction, HashMap<String, Descriptor> table){
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
    }

    private static String invokestatic(CallInstruction callInstruction, HashMap<String, Descriptor> table){
        ArrayList<Element> parameters = callInstruction.getListOfOperands();
        StringBuilder stringBuilder = new StringBuilder();

        for (Element parameter: parameters){
            stringBuilder.append(TranslateElement.getJasminInst(parameter, table));
        }

        stringBuilder.append("invokestatic ");
        stringBuilder.append(UtilsJasmin.getObjectName(callInstruction.getFirstArg())).append(".");

        StringBuilder methodName = new StringBuilder(((LiteralElement)callInstruction.getSecondArg()).getLiteral());
        methodName.deleteCharAt(methodName.length() -1);
        methodName.deleteCharAt(0);
        stringBuilder.append(methodName.toString()).append("(");

        for (Element parameter: parameters){
            stringBuilder.append(TranslateType.getJasminType(parameter.getType(), (Operand)parameter));
        }
        stringBuilder.append(")");
        stringBuilder.append(TranslateType.getJasminType(callInstruction.getReturnType())).append("\n\n");

        return stringBuilder.toString();
    }
}
