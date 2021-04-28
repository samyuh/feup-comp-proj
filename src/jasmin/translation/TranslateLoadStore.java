package jasmin.translation;

import jasmin.*;
import jasmin.UtilsJasmin;
import org.specs.comp.ollir.*;

import java.util.HashMap;

/**
 * Class responsible for translate load and store instructions.
 */
public class TranslateLoadStore {

    /**
     * Get the correct instruction to load an element.
     *
     * @return Returns the instruction to load the element as a string.
     */
    public static String getLoadInst(Element element, HashMap<String, Descriptor> table) {
        ElementType elementType = element.getType().getTypeOfElement();

        // generates iconst_
        if (element.isLiteral()) {
            return InstSingleton.iconst(((LiteralElement) element).getLiteral());
        }
        // generates aload, iload
        else if (elementType == ElementType.ARRAYREF) {
            return getLoadArrayAccess(element, table);
        }
        // generates iload
        else if (elementType == ElementType.INT32 || elementType == ElementType.STRING || elementType == ElementType.BOOLEAN) {
            System.out.println(table.get(((Operand)element).getName()));
            ElementType typeVar = table.get(((Operand) element).getName()).getVarType().getTypeOfElement();

            // Array accesses are treated as integers. Thus, this verification is necessary.
            if (typeVar == ElementType.ARRAYREF)
                return getLoadArrayAccess(element, table);
            else {
                int register = UtilsJasmin.getVirtualReg(element, table);
                return InstSingleton.iload(register);
            }
        } else if (elementType == ElementType.OBJECTREF || elementType == ElementType.THIS) {
            int register = UtilsJasmin.getVirtualReg(element, table);
            return InstSingleton.aload(register);
        }
        return "";
    }

    /**
     * Get the correct instruction to store an element.
     *
     * @return Get the store instruction as a string.
     */
    public static String getJasminStore(Element element, HashMap<String, Descriptor> table) {
        ElementType elementType = element.getType().getTypeOfElement();

        if (elementType == ElementType.ARRAYREF) {
            return getStoreArrayAccess(element, table);
        } else if (elementType == ElementType.INT32 || elementType == ElementType.STRING || elementType == ElementType.BOOLEAN) {
            ElementType typeVar = table.get(((Operand) element).getName()).getVarType().getTypeOfElement();
            if (typeVar == ElementType.ARRAYREF)
                return getStoreArrayAccess(element, table);
            else {
                int register = UtilsJasmin.getVirtualReg(element, table);
                return InstSingleton.istore(register);
            }
        } else if (elementType == ElementType.OBJECTREF || elementType == ElementType.THIS) {
            int register = UtilsJasmin.getVirtualReg(element, table);
            return InstSingleton.astore(register);
        }
        return element.toString();
    }

    /**
     * Get the load instruction for an array access.
     */
    public static String getLoadArrayAccess(Element arrayElement, HashMap<String, Descriptor> table) {
        int virtualRegIndex = UtilsJasmin.getVirtualRegIndex(arrayElement, table);
        int virtualRegArray = UtilsJasmin.getVirtualReg(arrayElement, table);

        return InstSingleton.getAccessArrayVar(virtualRegArray, virtualRegIndex);
    }

    /**
     * Get the store instruction for an array access.
     */
    public static String getStoreArrayAccess(Element arrayElement, HashMap<String, Descriptor> table) {
        int arrayReg = UtilsJasmin.getVirtualReg(arrayElement, table);
        int indexReg = UtilsJasmin.getVirtualRegIndex(arrayElement, table);
        return InstSingleton.getStoreArrayVar(arrayReg, indexReg);
    }


}
