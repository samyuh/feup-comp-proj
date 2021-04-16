package jasmin;

import jasmin.translation.TranslateType;
import org.specs.comp.ollir.*;

import java.util.ArrayList;
import java.util.HashMap;

public class UtilsJasmin {

    public static String getObjectName(Element element){
        ClassType classType = (ClassType) element.getType();
        return classType.getName();
    }
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

    public static String getArguments(ArrayList<Element> params){

        StringBuilder stringBuilder = new StringBuilder("(");
        for (Element param: params) {
            Type type = param.getType();
            stringBuilder.append(TranslateType.getJasminType(type));
            stringBuilder.append(",");
        }
        if (params.size() > 0 )
            stringBuilder.deleteCharAt(stringBuilder.length() - 1);

        stringBuilder.append(")");
        return stringBuilder.toString();
    }

}
