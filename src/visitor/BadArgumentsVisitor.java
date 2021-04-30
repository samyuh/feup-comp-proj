package visitor;

import analysis.Analysis;
import pt.up.fe.comp.jmm.JmmNode;
import pt.up.fe.comp.jmm.analysis.table.Symbol;
import pt.up.fe.comp.jmm.analysis.table.Type;
import pt.up.fe.comp.jmm.ast.PreorderJmmVisitor;

import java.util.List;

public class BadArgumentsVisitor extends PreorderJmmVisitor<Analysis, Boolean> {
    public BadArgumentsVisitor(){
       addVisit("DotMethod", this::visitDotMethod);
    }

    public Boolean visitDotMethod(JmmNode node, Analysis analysis){

        String methodName = node.getChildren().get(0).get("name");
        JmmNode parametersNode = node.getChildren().get(1);

        if (!analysis.getSymbolTable().getMethods().contains(methodName)) return true;

        // Check left side
        JmmNode parentNode = node.getParent();
        JmmNode left = parentNode.getChildren().get(0);

        if (!Utils.getNodeType(left, analysis).equals(analysis.getSymbolTable().getClassName())) return true;

        // Check arguments of method calls
        hasCorrectParameters(parametersNode, analysis , methodName);

        return true;
    }

    public void hasCorrectParameters(JmmNode node, Analysis analysis, String methodName){
        List<Symbol> parameters = analysis.getSymbolTable().getParameters(methodName);
        boolean hasSuper = analysis.getSymbolTable().getSuper() != null;
        int providedArgs = node.getNumChildren();
        int requiredArgs = parameters.size();
        if(providedArgs != requiredArgs){
            if(!hasSuper)
                analysis.addReport(node,"Wrong number of arguments. " + "Provided: " + providedArgs + " Required: " + requiredArgs);
            return;
        }

        for (int i = 0 ; i < requiredArgs; i++){
            Type type = parameters.get(i).getType();
            String requiredType = type.getName() + (type.isArray() ? "[]" : "");

            String providedType = Utils.getNodeType(node.getChildren().get(i), analysis);
            if (!providedType.equals(requiredType) && !providedType.equals("undefined")){
                analysis.addReport(node,"Parameter at position " + i + " has invalid type." +
                        " Provided: " + providedType + " Required: " + requiredType);
            }
        }
    }

}
