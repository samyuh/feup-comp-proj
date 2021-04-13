package visitor;

import analysis.Analysis;
import pt.up.fe.comp.jmm.JmmNode;
import pt.up.fe.comp.jmm.ast.PreorderJmmVisitor;

public class ArrayVisitor extends PreorderJmmVisitor<Analysis, Boolean> {
    public ArrayVisitor(){
        addVisit("ArrayAccess",this::visitArrayAccess);
        addVisit("NewExpression", this::visitNewExpression);
    }

    public Boolean visitAccessIdentifier(JmmNode arrayNode, Analysis analysis){
        JmmNode identifierNode = arrayNode.getChildren().get(0);
        String kind = identifierNode.getKind();

        // Check if it is an identifier
        if(kind.equals("Identifier")){
            String parentMethodName = Utils.getParentMethodName(arrayNode);
            String type = Utils.getVariableType(identifierNode, analysis, parentMethodName);
            if(!type.equals("int[]")){
                analysis.addReport(identifierNode, "Invalid identifier, must be an int[]. Provided: " + type);
                return false;
            }
        }
        else{
            analysis.addReport(identifierNode, "Invalid array access operation, must be an int[] identifier. Provided: " + kind);
            return false;
        }
        return true;
    }

    public Boolean visitArrayAccess(JmmNode arrayNode, Analysis analysis){
        JmmNode accessNode = arrayNode.getChildren().get(1);

        return visitAccessIdentifier(arrayNode, analysis) &&
                checkInsideBrackets(arrayNode, analysis ,accessNode,"access");

    }

    public Boolean visitNewExpression(JmmNode arrayNode, Analysis analysis){
        JmmNode sizeNode = arrayNode.getChildren().get(0);

        return checkInsideBrackets(arrayNode, analysis ,sizeNode,"size");

    }

    private Boolean checkInsideBrackets(JmmNode arrayNode, Analysis analysis, JmmNode node, String context) {
        String kind = node.getKind();
        // Check if it is an identifier
        if(kind.equals("Identifier")){
            String parentMethodName = Utils.getParentMethodName(arrayNode);
            String type = Utils.getVariableType(node, analysis, parentMethodName);

            // If it is an identifier, it must be an integer
            if(!type.equals("int")){
                analysis.addReport(node, "Invalid array " + context + ", identifier must be an integer. Provided: " + type);
                return false;
            }
        } else if(!kind.equals("Number") && !Utils.isOperator(kind)){
            analysis.addReport(node,
                    "Invalid array " + context + ", must be an integer. Provided: " + kind);
            return false;
        }
        return true;
    }
}
