package jasmin.translation;

import jasmin.*;
import jasmin.UtilsJasmin;
import org.specs.comp.ollir.*;

import java.util.ArrayList;
import java.util.HashMap;

import static jasmin.UtilsJasmin.getVirtualReg;
import static jasmin.UtilsJasmin.loadElements;

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
                    return InstSingleton.anewarray(getVirtualReg(element, table), TranslateType.getJasminType(type));
                }else {
                    Element element = callInstruction.getFirstArg();
                    Type type = element.getType();
                    return InstSingleton.anewarray(getVirtualReg(element, table), TranslateType.getJasminType(type, (Operand) element));
                }
            case invokevirtual:
                return invokevirtual(callInstruction, table);

            case invokestatic:
                return invokestatic(callInstruction, table);
            case invokespecial:
                return invokespecial(callInstruction, table);
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
        methodCall = UtilsJasmin.getRemovedQuotes(methodCall);


        for (Element operand : callInstruction.getListOfOperands())
            stringBuilder.append(TranslateElement.getJasminInst(operand, table));


        stringBuilder.append("invokevirtual ").append(objectName).append(".").append(methodCall);

        stringBuilder.append(UtilsJasmin.getArgumentsNoComma(callInstruction.getListOfOperands()));
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

        String methodName = ((LiteralElement)callInstruction.getSecondArg()).getLiteral();
        stringBuilder.append(UtilsJasmin.getRemovedQuotes(methodName));
        stringBuilder.append(UtilsJasmin.getArgumentsNoComma(parameters));
        stringBuilder.append(TranslateType.getJasminType(callInstruction.getReturnType())).append("\n\n");

        return stringBuilder.toString();
    }

    private static String invokespecial(CallInstruction callInstruction, HashMap<String, Descriptor> table){
        StringBuilder stringBuilder = new StringBuilder();
        ArrayList<Element> parameters = callInstruction.getListOfOperands();

        String methodName = UtilsJasmin.getRemovedQuotes(((LiteralElement)callInstruction.getSecondArg()).getLiteral());
        if (callInstruction.getFirstArg().getType().getTypeOfElement() != ElementType.THIS){
            String load = TranslateElement.getJasminInst(callInstruction.getFirstArg(), table);
            stringBuilder.append(load);
            stringBuilder.append(loadElements(parameters, table));
            stringBuilder.append("invokespecial ");
            stringBuilder.append(methodName);
            stringBuilder.append(UtilsJasmin.getArgumentsNoComma(parameters));
            stringBuilder.append(TranslateType.getJasminType(callInstruction.getReturnType())).append(";");
        }
        else {
            stringBuilder.append("invokespecial ");
            stringBuilder.append("java/lang/Object.").append(methodName);
            stringBuilder.append(UtilsJasmin.getArgumentsNoComma(parameters));
            stringBuilder.append(TranslateType.getJasminType(callInstruction.getReturnType())).append(";");
        }



        return stringBuilder.toString();
    }



}
