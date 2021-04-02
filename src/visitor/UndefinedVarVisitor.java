package visitor;
import analysis.MySymbolTable;
import pt.up.fe.comp.jmm.JmmNode;
import pt.up.fe.comp.jmm.analysis.table.Symbol;
import pt.up.fe.comp.jmm.ast.PreorderJmmVisitor;

import java.util.List;

public class UndefinedVarVisitor extends PreorderJmmVisitor<MySymbolTable, Boolean>{

    public UndefinedVarVisitor(){
        addVisit("MethodDeclaration", this::visitMethodDeclaration);
    }

    public Boolean visitMethodDeclaration(JmmNode MethodDeclarationNode, MySymbolTable symbolTable){
        JmmNode methodScope = MethodDeclarationNode.getChildren().get(0); // MethodMain or MethodGeneric
        String methodName = getMethodName(methodScope);
        JmmNode methodBody = getMethodBody(methodScope);

        validateAssignments(methodName, methodBody, symbolTable);

        return true;
    }

    public String getMethodName(JmmNode methodScope){
        if(methodScope.getKind().equals("MethodGeneric")){
            return methodScope.getChildren().get(1).get("name");
        }
        return "main";
    }

    public JmmNode getMethodBody(JmmNode methodScope){
        for (JmmNode child: methodScope.getChildren()){
            if (child.getKind().equals("MethodBody")) return child;
        }
        return null;
    }

    public void validateAssignments(String methodName, JmmNode methodBody, MySymbolTable symbolTable){
        for (JmmNode node: methodBody.getChildren()){
            if(node.getKind().equals("Assignment")){
                validateAssignment(methodName, node, symbolTable);
            }
        }
    }

    public Boolean validateAssignment(String methodName, JmmNode assignment, MySymbolTable symbolTable){
        JmmNode left = assignment.getChildren().get(0);
        JmmNode right = assignment.getChildren().get(1);
        List<Symbol> localVariables = symbolTable.getLocalVariables(methodName);
        if (left.getKind().equals("Identifier")){
            containsSymbol(localVariables, left.get("name"));
        }

        if (right.getKind().equals("Identifier")){
            if (right.getKind().equals("Identifier")){
                containsSymbol(localVariables, right.get("name"));
            }
        }
        return true;
    }

    public Boolean containsSymbol(List<Symbol> localVariables, String nameVariable){
        for(Symbol symbol: localVariables){
            if(symbol.getName().equals(nameVariable)){

                return true;
            }
        }
        // TODO: report and checkfields
        System.out.println("Variable \"" + nameVariable + "\" is undefined");
        return false;
    }
}
