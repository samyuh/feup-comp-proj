package jasmin.translation;

import jasmin.InstSingleton;
import jasmin.UtilsJasmin;
import org.specs.comp.ollir.*;

import java.util.HashMap;


public class TranslateElement {

    public static String getJasminInst(Element element, HashMap<String, Descriptor> table){
        ElementType elementType = element.getType().getTypeOfElement();

        // generates iconst_
        if (element.isLiteral()){
            return InstSingleton.iconst(((LiteralElement) element).getLiteral());
        }
        // generates aload, iload
        else if (elementType == ElementType.ARRAYREF){
           return getAccessArray(element, table);
        }
        // generates iload
        else if (elementType == ElementType.INT32 || elementType == ElementType.STRING ) {
            ElementType typeVar = table.get(((Operand) element).getName()).getVarType().getTypeOfElement();
            if (typeVar == ElementType.ARRAYREF)
                return getAccessArray(element, table);
            else {
                int register = UtilsJasmin.getVirtualReg(element, table);
                return InstSingleton.iload(register);
            }
        }
        else if (elementType == ElementType.OBJECTREF){
            int register = UtilsJasmin.getVirtualReg(element, table);
            return InstSingleton.aload(register);
        }
        return "";
    }

    public static String getAccessArray(Element arrayElement, HashMap<String, Descriptor> table){
        int virtualRegIndex = UtilsJasmin.getVirtualRegIndex(arrayElement, table);
        int virtualRegArray = UtilsJasmin.getVirtualReg(arrayElement, table);

        return InstSingleton.getAccessArrayVar(virtualRegArray, virtualRegIndex);
    }



}
