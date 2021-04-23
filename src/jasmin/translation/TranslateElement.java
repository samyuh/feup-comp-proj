package jasmin.translation;

import jasmin.*;
import jasmin.UtilsJasmin;
import org.specs.comp.ollir.*;
import visitor.Utils;

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
        else if (elementType == ElementType.OBJECTREF || elementType == ElementType.THIS){
            int register = UtilsJasmin.getVirtualReg(element, table);
            return InstSingleton.aload(register);
        }
        return "";
    }

    public static String getJasminStore(Element element, HashMap<String, Descriptor> table){
        ElementType elementType = element.getType().getTypeOfElement();
        if (elementType == ElementType.ARRAYREF) {
            return getStoreArray(element, table);
        } else if (elementType == ElementType.INT32 || elementType == ElementType.STRING){
            ElementType typeVar = table.get(((Operand) element).getName()).getVarType().getTypeOfElement();
            if (typeVar == ElementType.ARRAYREF)
                return getStoreArray(element, table);
            else {
                int register = UtilsJasmin.getVirtualReg(element, table);
                return InstSingleton.istore(register);
            }
        }
        else if (elementType == ElementType.OBJECTREF || elementType == ElementType.THIS){
            int register = UtilsJasmin.getVirtualReg(element, table);
            return InstSingleton.astore(register);
        }
        return "bla";
    }

    public static String getAccessArray(Element arrayElement, HashMap<String, Descriptor> table){
        int virtualRegIndex = UtilsJasmin.getVirtualRegIndex(arrayElement, table);
        int virtualRegArray = UtilsJasmin.getVirtualReg(arrayElement, table);

        return InstSingleton.getAccessArrayVar(virtualRegArray, virtualRegIndex);
    }

    public static String getStoreArray(Element arrayElement, HashMap<String, Descriptor> table){
        int arrayReg = UtilsJasmin.getVirtualReg(arrayElement, table);
        int indexReg = UtilsJasmin.getVirtualRegIndex(arrayElement, table);
        return InstSingleton.getStoreArrayVar(arrayReg, indexReg);
    }





}
