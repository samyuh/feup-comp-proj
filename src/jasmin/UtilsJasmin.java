package jasmin;

import jasmin.translation.TranslateLoadStore;
import jasmin.translation.TranslateType;
import org.specs.comp.ollir.*;

import java.util.ArrayList;
import java.util.HashMap;

public class UtilsJasmin {

    public static String getObjectName(Element element){
        ClassType classType = (ClassType) element.getType();
        return classType.getName();
    }

    public static String getOperandName(Element element){
        Operand operand = (Operand) element;
        return operand.getName();
    }
    // int a = b;
    public static Descriptor getDescriptor(Element element, HashMap<String, Descriptor> table){
        return table.get(((Operand) element).getName());
    }

    public static int getVirtualReg(Element element, HashMap<String, Descriptor> table){
        return getDescriptor(element, table).getVirtualReg();
    }

    public static int getVirtualRegIndex(Element arrayElement, HashMap<String, Descriptor> table){
        ArrayOperand arrayOperand = (ArrayOperand) arrayElement;
        ArrayList<Element> indexOperand = arrayOperand.getIndexOperands();
        Element indexElement = indexOperand.get(0);
        Descriptor desc = table.get(((Operand) indexElement).getName());
        return desc.getVirtualReg();
    }


    public static String getArguments(ArrayList<Element> parameters){
        StringBuilder stringBuilder = new StringBuilder("(");
        for (Element parameter: parameters){
            stringBuilder.append(TranslateType.getJasminType(parameter.getType()));
        }
        stringBuilder.append(")");
        return stringBuilder.toString();
    }

    public static String getRemovedQuotes(String s){
        return s.substring(1, s.length()-1);
    }

    public static String loadElements(ArrayList<Element> parameters, HashMap<String, Descriptor> table){
        StringBuilder stringBuilder = new StringBuilder();
        for(Element param : parameters){
            stringBuilder.append(TranslateLoadStore.getLoadInst(param, table));
        }
        return stringBuilder.toString();
    }

    public static boolean isBooleanOp(OperationType opType){
        return opType == OperationType.ANDB || opType == OperationType.NOTB || opType == OperationType.LTH ;
    }
}
