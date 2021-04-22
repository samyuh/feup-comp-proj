package visitor;

import analysis.Analysis;
import pt.up.fe.comp.jmm.analysis.table.Symbol;
import pt.up.fe.comp.jmm.ast.PreorderJmmVisitor;
import pt.up.fe.comp.jmm.JmmNode;

import java.util.List;

public class FuncNotFoundVisitor extends PreorderJmmVisitor<Analysis, Boolean> {
    public FuncNotFoundVisitor() {
        addVisit("Dot", this::visitDot);
        addVisit("NewObject",this::visitNewObject);
    }

    public Boolean visitDot(JmmNode node, Analysis analysis) {
        JmmNode nodeLeft = node.getChildren().get(0);
        JmmNode nodeRight = node.getChildren().get(1);

        // Check Length methods
        if(nodeRight.getKind().equals("Length")) {
            if(!validateLength(nodeLeft,analysis)){
                analysis.addReport(node, "Built-in \"length\" is only valid over arrays.");
                return false;
            }
            return true;
        }

        // Check imported methods
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
            if(hasInheritance(analysis)) return true;
            hasThisDotMethod(nodeRight, analysis);
        }

        return true;
    }

    public Boolean visitNewObject(JmmNode node, Analysis analysis){
        JmmNode objectNode = node.getChildren().get(0);
        String objectName = objectNode.get("name");

        // Check if the object is an instance of the actual class
        if(objectName.equals(analysis.getSymbolTable().getClassName())) return true;

        // Check if the object is is an instance of the extended class
        if(analysis.getSymbolTable().getSuper() != null &&
                objectName.equals(analysis.getSymbolTable().getSuper())) return true;

        // Check if it is an import
        if (analysis.getSymbolTable().getImports().contains(objectName)) return true;

        analysis.addReport(objectNode,  "\"" + objectName + "\" is not an import nor an object");
        return false;
    }

    private boolean hasInheritance(Analysis analysis) {
        return analysis.getSymbolTable().getSuper() != null;
    }

    public Boolean checkObject(JmmNode node, String nodeName, Analysis analysis) {
        String methodName = Utils.getParentMethodName(node);

        // Todo: fix error for length case
        JmmNode calledMethod = node.getChildren().get(1).getChildren().get(0);

        List<Symbol> localVariables = analysis.getSymbolTable().getLocalVariables(methodName);
        List<Symbol> classFields = analysis.getSymbolTable().getFields();
        List<Symbol> methodParams = analysis.getSymbolTable().getParameters(methodName);

        return containsObject(localVariables, nodeName, calledMethod, analysis) || containsObject(classFields, nodeName, calledMethod, analysis) ||
                containsObject(methodParams, nodeName, calledMethod, analysis);
    }

    public Boolean containsObject(List<Symbol> vars, String varName,  JmmNode calledMethod, Analysis analysis){
        for(Symbol symbol: vars){
            // Check if the variable exists and its type is valid
            if(symbol.getName().equals(varName) && isValidType(symbol.getType().getName())) {
                // Check if it is an object of the class
                if (symbol.getType().getName().equals(analysis.getSymbolTable().getClassName())) {
                    if (hasInheritance(analysis)) return true; // Extends
                    else if (!analysis.getSymbolTable().getMethods().contains(calledMethod.get("name"))) {
                        analysis.addReport(calledMethod,
                                "\"" + calledMethod.get("name") + "\" is not a class method");
                    }
                }
                return true;
            }
        }
        return false;
    }

    public Boolean isValidType(String typeName) {
        return !typeName.equals("int") && !typeName.equals("String") && !typeName.equals("boolean");
    }

    public void hasThisDotMethod(JmmNode node, Analysis analysis){
        String identifier = node.getChildren().get(0).get("name");
        if (!analysis.getSymbolTable().getMethods().contains(identifier)) {
            analysis.addReport(node,"Function \"" + identifier + "\" is undefined");
        }
    }

    public Boolean validateLength(JmmNode left, Analysis analysis){
        String type = Utils.getNodeType(left, analysis);
        System.out.println("Type = " + type);
        return type.equals("int[]") || type.equals("String[]");
    }
}
