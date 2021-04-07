package visitor;

import analysis.Analysis;
import pt.up.fe.comp.jmm.JmmNode;
import pt.up.fe.comp.jmm.analysis.table.Symbol;
import pt.up.fe.comp.jmm.analysis.table.Type;
import pt.up.fe.comp.jmm.ast.PreorderJmmVisitor;

import java.util.List;

// TODO: Ver também se as variáveis passadas a um dot method estão definidas
public class BadArgumentsVisitor extends PreorderJmmVisitor<Analysis, Boolean> {
    public BadArgumentsVisitor(){
       addVisit("DotMethod", this::visitDotMethod);
    }

    public Boolean visitDotMethod(JmmNode node, Analysis analysis){
        String methodName = node.getChildren().get(0).get("name");
        JmmNode parametersNode = node.getChildren().get(1);

        if (!analysis.getSymbolTable().getMethods().contains(methodName)) return true;
        // Check arguments of method calls
        hasCorrectParameters(parametersNode, analysis , methodName);

        return true;
    }

    public void hasCorrectParameters(JmmNode node, Analysis analysis, String methodName){
        List<Symbol> parameters = analysis.getSymbolTable().getParameters(methodName);
        int providedArgs = node.getNumChildren();
        int requiredArgs = parameters.size();
        if(providedArgs != requiredArgs){
            analysis.addReport(node,"Wrong number of arguments. " +
                    "Provided: " + providedArgs + " Required: " + requiredArgs);
            return;
        }

        String parentMethodName = getParentMethod(node);

        for (int i = 0 ; i < requiredArgs; i++){
            Type type = parameters.get(i).getType();
            String requiredType = type.getName();
            String providedType = getVariableType(node.getChildren().get(i), analysis, parentMethodName);
            if (!providedType.equals(requiredType)){
                analysis.addReport(node,"Parameter at position " + i + " has invalid type." +
                        " Provided: " + providedType + " Required: " + requiredType);
            }
        }
    }

    public String getVariableType(JmmNode node, Analysis analysis, String parentMethodName){

        if (node.getKind().equals("Number")) return "int";
        else if (node.getKind().equals("True") || node.getKind().equals("False")) return "boolean";
        else if (node.getKind().equals("This")) return analysis.getSymbolTable().getClassName();

        List<Symbol> localVariables = analysis.getSymbolTable().getLocalVariables(parentMethodName);
        List<Symbol> fields = analysis.getSymbolTable().getFields();

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

        return "int";
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


}
