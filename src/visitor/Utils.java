package visitor;

import analysis.Analysis;
import analysis.MySymbolTable;
import pt.up.fe.comp.jmm.JmmNode;
import pt.up.fe.comp.jmm.analysis.table.Symbol;
import pt.up.fe.comp.jmm.analysis.table.Type;

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

        for (Symbol symb: localVariables){
            String varName = symb.getName();
            if (varName.equals(node.get("name")))
                return symb.getType().getName() + (symb.getType().isArray() ? "[]" : "");
        }

        for (Symbol symb: fields){
            String varName = symb.getName();
            if (varName.equals(node.get("name")))
                return symb.getType().getName() + (symb.getType().isArray() ? "[]" : "");
        }

        for (Symbol symb: parameters){
            String varName = symb.getName();
            if (varName.equals(node.get("name")))
                return symb.getType().getName() + (symb.getType().isArray() ? "[]" : "");
        }

        return "undefined";
    }

    public static String getNodeType(JmmNode node, Analysis analysis){
        String kind = node.getKind();

        if(isMathExpression(kind)) return "int";
        if(isBooleanExpression(kind)) return "boolean";

        switch (kind){
            case "Dot":
                return getReturnValueMethod(node,analysis);
            case "ArrayAccess":
                return "int";
            case "NewObject":
                return node.getChildren().get(0).get("name");
            case "NewIntArray":
                return "int[]";
            default:
                String parentMethodName = getParentMethodName(node);
                return getVariableType(node,analysis,parentMethodName);
        }

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

    public static boolean isMathExpression(String kind) {
        return kind.equals("Mult") || kind.equals("Add") || kind.equals("Sub") || kind.equals("Div");
    }

    public static boolean isBooleanExpression(String kind) {
        return kind.equals("Less") || kind.equals("And") || kind.equals("Not");
    }

    public static Boolean isOperator(String kind) {
        return kind.equals("Add") ||
                kind.equals("Mult") ||
                kind.equals("Sub") ||
                kind.equals("Div") ||
                kind.equals("Less") ||
                kind.equals("And") ||
                kind.equals("ArrayAccess")||
                kind.equals("ArrayExpression")||
                kind.equals("ArrayAssignment");
    }

    public static String getReturnValueMethod(JmmNode dotNode, Analysis analysis) {
        JmmNode leftNode = dotNode.getChildren().get(0);
        JmmNode rigthNode = dotNode.getChildren().get(1);

        String parentMethodName = Utils.getParentMethodName(dotNode);
        String typeName = Utils.getVariableType(leftNode, analysis, parentMethodName);
        String className = analysis.getSymbolTable().getClassName();

        if(rigthNode.getKind().equals("Length")) return "int";

        String methodName = dotNode.getChildren().get(1).getChildren().get(0).get("name");
        boolean containsMethodName = analysis.getSymbolTable().getMethods().contains(methodName);

        if (containsMethodName && (typeName.equals(className) || dotNode.getKind().equals("This"))) {
            Type returnType = analysis.getSymbolTable().getReturnType(methodName);
            return returnType.getName() + (returnType.isArray() ? "[]" : "");
        }

        return "undefined";
    }
}
