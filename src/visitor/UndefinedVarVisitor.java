package visitor;
import analysis.Analysis;
import pt.up.fe.comp.jmm.JmmNode;
import pt.up.fe.comp.jmm.analysis.table.Symbol;
import pt.up.fe.comp.jmm.ast.PreorderJmmVisitor;

import java.util.List;


public class UndefinedVarVisitor extends PreorderJmmVisitor<Analysis, Boolean>{
    public UndefinedVarVisitor(){
        addVisit("MethodDeclaration", this::visitMethodDeclaration);
        addVisit("ObjectMethodParameters", this::visitMethodParameters);
    }

    public Boolean visitMethodParameters(JmmNode objectMethodParameters, Analysis analysis){
        String methodName = Utils.getParentMethodName(objectMethodParameters);

        for (JmmNode node: objectMethodParameters.getChildren()) {
            if (node.getNumChildren() > 0)
                validateExpression(methodName, node, analysis);
            else if (node.getKind().equals("Identifier"))
                varIsDefined(methodName, analysis, node);
        }

        return true;
    }


    public Boolean visitMethodDeclaration(JmmNode MethodDeclarationNode, Analysis analysis){
        JmmNode methodScope = MethodDeclarationNode.getChildren().get(0); // MethodMain or MethodGeneric
        String methodName = getMethodName(methodScope); // Get method name
        JmmNode methodBody = getMethodBody(methodScope);

        validateAssignments(methodName, methodBody, analysis);

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

    public void validateAssignments(String methodName, JmmNode methodBody, Analysis analysis){
        for (JmmNode node: methodBody.getChildren()){
            if(node.getKind().equals("Assignment")){
                validateExpression(methodName, node, analysis);
            }
        }
    }

    public Boolean validateExpression(String methodName, JmmNode node, Analysis analysis) {
        JmmNode left, right;

        while (true) {
            left = node.getChildren().get(0);
            right = node.getChildren().get(1);

            if (left.getKind().equals("Identifier")) {
                varIsDefined(methodName, analysis, left);
            }

            if (right.getKind().equals("Identifier")) {
                varIsDefined(methodName, analysis, right);
                break;
            } else if (Utils.isOperator(right.getKind())) {
                node = right;
            }  else break;
        }
        return true;
    }

    /**
     * Check if the variable is a local variable or a class field.
     * Register a Report if the variable is undefined.
     * @param methodName the name of the method in which the variable was found
     * @param analysis aggregates the symbol table and the list of reports
     * @param identifierNode the Identifier node that represents the variable
     */
    public void varIsDefined(String methodName, Analysis analysis, JmmNode identifierNode) {
        List<Symbol> localVariables = analysis.getSymbolTable().getLocalVariables(methodName);
        List<Symbol> classFields = analysis.getSymbolTable().getFields();
        List<Symbol> methodParams = analysis.getSymbolTable().getParameters(methodName);
        String varName = identifierNode.get("name");

        if(!containsSymbol(localVariables, varName) && !containsSymbol(classFields, varName) &&
                !containsSymbol(methodParams, varName)){
            analysis.addReport(identifierNode, "Variable \"" + identifierNode.get("name") + "\" is undefined");
        }
    }

    public Boolean containsSymbol(List<Symbol> vars, String varName){
        for(Symbol symbol: vars){
            if(symbol.getName().equals(varName)){
                return true;
            }
        }
        return false;
    }
}
