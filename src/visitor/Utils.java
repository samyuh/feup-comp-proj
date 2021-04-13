package visitor;

import analysis.Analysis;
import analysis.MySymbolTable;
import pt.up.fe.comp.jmm.JmmNode;
import pt.up.fe.comp.jmm.analysis.table.Symbol;

import java.util.List;

public class Utils {
    /**
     * Checks the type of the node by searching in fields, imports, parameter, declaration.
     */
    public static String getVariableType(JmmNode node, Analysis analysis, String parentMethodName){
        if (node.getKind().equals("Number")) return "int";
        else if (node.getKind().equals("True") || node.getKind().equals("False")) return "boolean";
        else if (node.getKind().equals("This")) return analysis.getSymbolTable().getClassName();

        List<Symbol> localVariables = analysis.getSymbolTable().getLocalVariables(parentMethodName);
        List<Symbol> fields = analysis.getSymbolTable().getFields();
        List<Symbol> parameters = analysis.getSymbolTable().getParameters(parentMethodName);

        // Verifies if the element is in the symbol and table. And if it is, return the type.
        for (Symbol symb: localVariables){
            String varName = symb.getName();
            if (varName.equals(node.get("name")))
                return symb.getType().getName();
        }

        for (Symbol symb: fields){
            String varName = symb.getName();
            if (varName.equals(node.get("name")))
                return symb.getType().getName();
        }

        for (Symbol symb: parameters){
            String varName = symb.getName();
            if (varName.equals(node.get("name")))
                return symb.getType().getName();
        }

        return "int";
    }



    public static String getParentMethodName(JmmNode node) {
        JmmNode currentNode = node;
        while (!currentNode.getKind().equals("MethodGeneric") && !currentNode.getKind().equals("MethodMain")) {
            currentNode = currentNode.getParent();
        }
            if (currentNode.getKind().equals("MethodGeneric"))
                return currentNode.getChildren().get(1).get("name");
            return "main";
        }

    public static Boolean isBoolean(JmmNode node){
        if (node.getKind().equals("True") || node.getKind().equals("False")) return true;
        return false;
    }
}
