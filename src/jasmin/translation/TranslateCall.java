package jasmin.translation;

import jasmin.*;
import jasmin.UtilsJasmin;
import jasmin.methods.BuildMethod;
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
                return newCall(callInstruction, table);
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
        String methodCall;
        String className;
        StringBuilder stringBuilder = new StringBuilder();

        Element firstArg = callInstruction.getFirstArg();
        stringBuilder.append(TranslateLoadStore.getLoadInst(firstArg, table));
        className = ((ClassType)firstArg.getType()).getName();

        methodCall = ((LiteralElement) callInstruction.getSecondArg()).getLiteral();
        methodCall = UtilsJasmin.getRemovedQuotes(methodCall);


        for (Element operand : callInstruction.getListOfOperands())
            stringBuilder.append(TranslateLoadStore.getLoadInst(operand, table));


        stringBuilder.append("invokevirtual ").append(className).append(".").append(methodCall);

        stringBuilder.append(UtilsJasmin.getArguments(callInstruction.getListOfOperands()));
        stringBuilder.append(TranslateType.getJasminType(callInstruction.getReturnType()));
        return stringBuilder.toString() + "\n";
    }

    private static String invokestatic(CallInstruction callInstruction, HashMap<String, Descriptor> table){
        ArrayList<Element> parameters = callInstruction.getListOfOperands();
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append(loadElements(parameters, table));

        stringBuilder.append("invokestatic ");
        stringBuilder.append(UtilsJasmin.getOperandName(callInstruction.getFirstArg())).append(".");

        String methodName = ((LiteralElement)callInstruction.getSecondArg()).getLiteral();
        stringBuilder.append(UtilsJasmin.getRemovedQuotes(methodName));
        stringBuilder.append(UtilsJasmin.getArguments(parameters));
        stringBuilder.append(TranslateType.getJasminType(callInstruction.getReturnType()));

        return stringBuilder.toString();
    }

    private static String invokespecial(CallInstruction callInstruction, HashMap<String, Descriptor> table){
        StringBuilder stringBuilder = new StringBuilder();
        ArrayList<Element> parameters = callInstruction.getListOfOperands();
        String methodName = UtilsJasmin.getRemovedQuotes(((LiteralElement)callInstruction.getSecondArg()).getLiteral());
        Element classElement =  callInstruction.getFirstArg();

        if ( classElement.getType().getTypeOfElement() != ElementType.THIS){

            String className = ((ClassType)classElement.getType()).getName();
            stringBuilder.append(loadElements(parameters, table));
            stringBuilder.append("invokespecial ").append(className).append(".");
        }
        else {
            stringBuilder.append(TranslateLoadStore.getLoadInst(classElement, table));
            stringBuilder.append("invokespecial ");
            stringBuilder.append(InstSingleton.extend).append(".");
        }
        stringBuilder.append(methodName);
        stringBuilder.append(UtilsJasmin.getArguments(parameters));
        stringBuilder.append(TranslateType.getJasminType(callInstruction.getReturnType()));

        return stringBuilder.toString();
    }

    private static String newCall(CallInstruction callInstruction, HashMap<String, Descriptor> table){
        StringBuilder instruction = new StringBuilder();
        Type returnType = callInstruction.getReturnType();
        String returnTypeString = TranslateType.getJasminType(returnType);

        // It can be new array or new Class.
        if (returnType.getTypeOfElement()  == ElementType.OBJECTREF){
            instruction.append(InstSingleton.newCall(returnTypeString));
            Instruction nextInstruction = BuildMethod.getNextInstruction();
            if (nextInstruction.getInstType() == InstructionType.CALL && OllirAccesser.getCallInvocation(((CallInstruction) nextInstruction)) == CallType.invokespecial)
                instruction.append("dup\n");
            instruction.append(invokespecial((CallInstruction)nextInstruction, table));
            BuildMethod.skipNextInstruction();
            instruction.append("\n");
            return instruction.toString();

        } else {
            if (callInstruction.getListOfOperands().size() != 0) {
                Element element = callInstruction.getListOfOperands().get(0);
                return InstSingleton.anewarray(getVirtualReg(element, table), returnTypeString);
            } else {
                Element element = callInstruction.getFirstArg();
                return InstSingleton.anewarray(getVirtualReg(element, table), returnTypeString);
            }
        }
    }




}
