package jasmin;

import org.specs.comp.ollir.ArrayOperand;
import org.specs.comp.ollir.Descriptor;
import org.specs.comp.ollir.Element;
import org.specs.comp.ollir.Operand;

import java.util.ArrayList;
import java.util.HashMap;

public class UtilsJasmin {

    public static int getVirtualRegArray(Element arrayElement, HashMap<String, Descriptor> table){
        Descriptor descriptor = table.get(((Operand)arrayElement).getName());
        return descriptor.getVirtualReg();
    }

    public static int getVirtualRegIndex(Element arrayElement, HashMap<String, Descriptor> table){
        ArrayOperand arrayOperand = (ArrayOperand) arrayElement;
        ArrayList<Element> indexOperand = arrayOperand.getIndexOperands();
        Element indexElement = indexOperand.get(0);
        Descriptor desc = table.get(((Operand) indexElement).getName());
        return desc.getVirtualReg();
    }
}
