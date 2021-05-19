package jasmin.translation;

import jasmin.*;
import jasmin.UtilsJasmin;
import jasmin.methods.BuildMethod;
import org.specs.comp.ollir.*;

import java.io.CharArrayReader;
import java.util.ArrayList;
import java.util.HashMap;

import static jasmin.UtilsJasmin.getVirtualReg;
import static jasmin.UtilsJasmin.loadElements;

public class TranslateCall {

    public static String getJasminInst(CallInstruction callInstruction, HashMap<String, Descriptor> table, boolean isAssign) {
        CallType callType = OllirAccesser.getCallInvocation(callInstruction);
        switch (callType) {
            case arraylength:
                Element arrayElement = callInstruction.getFirstArg();
                int arrayReg = getVirtualReg(arrayElement, table);
                return InstSingleton.getArrayLength(arrayReg);
            case NEW:
                return newCall(callInstruction, table);
            case invokevirtual:
                return invokevirtual(callInstruction, table, isAssign);
            case invokestatic:
                return invokestatic(callInstruction, table, isAssign);
            case invokespecial:
                return invokespecial(callInstruction, table);
            default:
                return "";
        }

    }

    private static String invokevirtual(CallInstruction callInstruction, HashMap<String, Descriptor> table, boolean isAssign) {
        StringBuilder stringBuilder = new StringBuilder();
        ArrayList<Element> operandList = callInstruction.getListOfOperands();
        Type returnType = callInstruction.getReturnType();
        Element firstArg = callInstruction.getFirstArg();
        String className = ((ClassType) firstArg.getType()).getName();
        String methodCall = ((LiteralElement) callInstruction.getSecondArg()).getLiteral();

        stringBuilder.append(TranslateLoadStore.getLoadInst(firstArg, table));
        stringBuilder.append(loadElements(operandList, table));

        updateStackCall(operandList.size() + 1, 0, returnType.getTypeOfElement());

        stringBuilder.append("invokevirtual ");
        stringBuilder.append(className).append(".");
        stringBuilder.append(UtilsJasmin.getRemovedQuotes(methodCall));
        stringBuilder.append(UtilsJasmin.getArguments(callInstruction.getListOfOperands()));
        stringBuilder.append(TranslateType.getJasminType(returnType));
        stringBuilder.append("\n");

        if (!isAssign && returnType.getTypeOfElement() != ElementType.VOID)
            stringBuilder.append(InstSingleton.pop());

        return stringBuilder.toString() + "\n";
    }


    private static String invokestatic(CallInstruction callInstruction, HashMap<String, Descriptor> table, boolean isAssign) {
        ArrayList<Element> parameters = callInstruction.getListOfOperands();
        StringBuilder stringBuilder = new StringBuilder();
        Type returnType = callInstruction.getReturnType();

        updateStackCall(parameters.size(), 0, returnType.getTypeOfElement());

        stringBuilder.append(loadElements(parameters, table));

        stringBuilder.append("invokestatic ");
        stringBuilder.append(UtilsJasmin.getOperandName(callInstruction.getFirstArg())).append(".");

        String methodName = ((LiteralElement) callInstruction.getSecondArg()).getLiteral();
        stringBuilder.append(UtilsJasmin.getRemovedQuotes(methodName));
        stringBuilder.append(UtilsJasmin.getArguments(parameters));
        stringBuilder.append(TranslateType.getJasminType(returnType));
        stringBuilder.append("\n");

        if (!isAssign && returnType.getTypeOfElement() != ElementType.VOID)
            stringBuilder.append(InstSingleton.pop());

        return stringBuilder.toString();
    }

    private static String invokespecial(CallInstruction callInstruction, HashMap<String, Descriptor> table) {
        StringBuilder stringBuilder = new StringBuilder();
        ArrayList<Element> parameters = callInstruction.getListOfOperands();
        String methodName = UtilsJasmin.getRemovedQuotes(((LiteralElement) callInstruction.getSecondArg()).getLiteral());
        Element classElement = callInstruction.getFirstArg();
        Type returnType = callInstruction.getReturnType();


        if (classElement.getType().getTypeOfElement() != ElementType.THIS) {
            String className = ((ClassType) classElement.getType()).getName();
            stringBuilder.append(loadElements(parameters, table));
            updateStackCall(parameters.size(), 0, returnType.getTypeOfElement());
            stringBuilder.append("invokespecial ").append(className).append(".");
        } else {
            stringBuilder.append(TranslateLoadStore.getLoadInst(classElement, table));
            stringBuilder.append("invokespecial ");
            updateStackCall(1,0, returnType.getTypeOfElement());
            stringBuilder.append(InstSingleton.extend).append(".");
        }

        stringBuilder.append(methodName);
        stringBuilder.append(UtilsJasmin.getArguments(parameters));
        stringBuilder.append(TranslateType.getJasminType(callInstruction.getReturnType()));

        return stringBuilder.toString();
    }

    private static String newCall(CallInstruction callInstruction, HashMap<String, Descriptor> table) {
        StringBuilder instruction = new StringBuilder();
        Type returnType = callInstruction.getReturnType();
        String returnTypeString = TranslateType.getJasminType(returnType);

        // It can be new array or new Class.
        if (returnType.getTypeOfElement() == ElementType.OBJECTREF) {
            instruction.append(InstSingleton.newCall(returnTypeString));
            Instruction nextInstruction = BuildMethod.getNextInstruction();
            if (nextInstruction.getInstType() == InstructionType.CALL && OllirAccesser.getCallInvocation(((CallInstruction) nextInstruction)) == CallType.invokespecial)
                instruction.append(InstSingleton.dup());
            instruction.append(invokespecial((CallInstruction) nextInstruction, table));
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


    /**
     * If the return type is not VOID, so the number of pushed elements must be summed by 1.
     *
     * @param popSize    Number of elements being consumed in the stack.
     * @param pushSize   Number of elements being added to the stack.
     * @param returnType
     */
    public static void updateStackCall(int popSize, int pushSize, ElementType returnType) {
        if (ElementType.VOID == returnType)
            pushSize += 1;
        BuildMethod.updateMaxStack(popSize, pushSize);
    }



}
