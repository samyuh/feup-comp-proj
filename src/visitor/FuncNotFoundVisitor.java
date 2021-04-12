package visitor;

import analysis.Analysis;
import pt.up.fe.comp.jmm.analysis.table.Symbol;
import pt.up.fe.comp.jmm.ast.PreorderJmmVisitor;
import pt.up.fe.comp.jmm.JmmNode;

import java.util.List;

public class FuncNotFoundVisitor extends PreorderJmmVisitor<Analysis, Boolean> {
    public FuncNotFoundVisitor() {
        addVisit("Dot", this::visitDot);
    }

    public Boolean visitDot(JmmNode node, Analysis analysis) {
        JmmNode nodeLeft = node.getChildren().get(0);
        JmmNode nodeRight = node.getChildren().get(1);
        if (nodeLeft.getKind().equals("Identifier")) {
            String nodeName = nodeLeft.get("name");
            // Check imported method
            if (!analysis.getSymbolTable().getImports().contains(nodeName)) {
                // Check if object
                if(!checkObject(node, nodeName, analysis)){
                    analysis.addReport(nodeLeft,  "\"" + nodeName + "\" is not an import nor an object");
                }
            }
        }

        // Check class methods
        if (nodeLeft.getKind().equals("This") && nodeRight.getKind().equals("DotMethod")){
            hasThisDotMethod(nodeRight, analysis);
        }

        return true;
    }

    public Boolean checkObject(JmmNode node, String nodeName, Analysis analysis) {
        String methodName = getParentMethod(node);
        JmmNode calledMethod = node.getChildren().get(1).getChildren().get(0);

        List<Symbol> localVariables = analysis.getSymbolTable().getLocalVariables(methodName);
        List<Symbol> classFields = analysis.getSymbolTable().getFields();
        List<Symbol> methodParams = analysis.getSymbolTable().getParameters(methodName);

        if(!containsObject(localVariables, nodeName, calledMethod, analysis) && !containsObject(classFields, nodeName, calledMethod, analysis) &&
                !containsObject(methodParams, nodeName, calledMethod, analysis)){
            return false;
        }
        return true;
    }

    public Boolean containsObject(List<Symbol> vars, String varName,  JmmNode calledMethod, Analysis analysis){
        for(Symbol symbol: vars){
            if(symbol.getName().equals(varName) && isValidType(symbol.getType().getName())){
                if(symbol.getType().getName().equals(analysis.getSymbolTable().getClassName())
                        && !analysis.getSymbolTable().getMethods().contains(calledMethod.get("name"))){
                    analysis.addReport(calledMethod,
                            "\"" + calledMethod.get("name") + "\" is not a class method");
                }
                return true;
            }
        }
        return false;
    }

    public Boolean isValidType(String typeName) {
        // TODO: Verificar se as maiusculas estao certas
        return !typeName.equals("int") && !typeName.equals("String") && !typeName.equals("boolean");
    }

    public String getParentMethod(JmmNode node){
        JmmNode currentNode = node;
        while(!currentNode.getKind().equals("MethodGeneric") && ! currentNode.getKind().equals("MethodMain")) {
            currentNode = currentNode.getParent();
        }

        if(currentNode.getKind().equals("MethodGeneric"))
            return currentNode.getChildren().get(1).get("name");
        return "main";
    }

    public void hasThisDotMethod(JmmNode node, Analysis analysis){
        String identifier = node.getChildren().get(0).get("name");
        if (!analysis.getSymbolTable().getMethods().contains(identifier)) {
            analysis.addReport(node,"Function \"" + identifier + "\" is undefined");
        }
    }
}
