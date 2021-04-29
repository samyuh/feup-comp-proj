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
        addVisit("Return", this::visitReturn);
        addVisit("VarDeclaration", this::visitVarDeclaration); // EXTRA
        addVisit("Extends", this::visitExtends);
    }

    // EXTRA
    public Boolean visitVarDeclaration(JmmNode node, Analysis analysis){
        JmmNode typeNode = node.getChildren().get(0);

        String typeStr = typeNode.get("type");
        if (!isClassInstance(typeStr)) return true;

        if (Utils.hasImport(typeStr, analysis.getSymbolTable()) ||
                analysis.getSymbolTable().getClassName().equals(typeStr)){
            return true;
        };

        analysis.addReport(node, "Type \"" + typeStr + "\" is missing.");
        return false;
    }

    public Boolean visitExtends(JmmNode node, Analysis analysis) {
        String nodeName = node.get("name");

        if(Utils.hasImport(nodeName, analysis.getSymbolTable())) return true;

        analysis.addReport(node, "Extend class \"" + nodeName + "\" is not being imported");
        return false;
    }

    public Boolean isClassInstance(String typeStr){
        return !typeStr.equals("int") && !typeStr.equals("int[]") && !typeStr.equals("String") && !typeStr.equals("boolean");
    }

    public Boolean visitMethodParameters(JmmNode objectMethodParameters, Analysis analysis){
        String methodName = Utils.getParentMethodName(objectMethodParameters);

        // Check each parameter of a method call
        for (JmmNode node: objectMethodParameters.getChildren()) {
            if (node.getNumChildren() > 0 && !node.getKind().equals("Dot") && !node.getKind().equals("NewObject"))
                validateExpression(methodName, node, analysis);
            else if (node.getKind().equals("Identifier"))
                varIsDefined(methodName, analysis, node);
        }

        return true;
    }

    public Boolean visitMethodDeclaration(JmmNode methodDeclarationNode, Analysis analysis){
        JmmNode methodScope = methodDeclarationNode.getChildren().get(0); // MethodMain or MethodGeneric
        String methodName = getMethodName(methodScope); // Get method name
        JmmNode methodBody = getMethodBody(methodScope);

        validateAssignments(methodName, methodBody, analysis);

        return true;
    }

    public Boolean visitReturn(JmmNode returnNode, Analysis analysis){
        JmmNode node = returnNode.getChildren().get(0);
        String methodName = Utils.getParentMethodName(returnNode);

        if (node.getNumChildren() > 0 && !node.getKind().equals("Dot") && !node.getKind().equals("NewObject")) {
            validateExpression(methodName, node, analysis);
        }
        else if (node.getKind().equals("Identifier")){
            varIsDefined(methodName, analysis, node);
        }

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

    public void validateExpression(String methodName, JmmNode node, Analysis analysis) {
        JmmNode left, right;

        left = node.getChildren().get(0);
        validateNode(methodName,left,analysis);

        if(node.getNumChildren() == 1) return;

        right = node.getChildren().get(1);
        validateNode(methodName,right,analysis);

    }

    public void validateNode(String methodName, JmmNode node, Analysis analysis){
        if (node.getKind().equals("Identifier")) {
            varIsDefined(methodName, analysis, node);
        } else if (Utils.isOperator(node.getKind()) || node.getKind().equals("NewIntArray")) {
            validateExpression(methodName, node, analysis);
        }
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
