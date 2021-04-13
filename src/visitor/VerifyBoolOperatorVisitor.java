package visitor;

import analysis.Analysis;
import pt.up.fe.comp.jmm.JmmNode;
import pt.up.fe.comp.jmm.ast.PreorderJmmVisitor;

public class VerifyBoolOperatorVisitor extends PreorderJmmVisitor<Analysis, Boolean> {


    public VerifyBoolOperatorVisitor(){
        addVisit("Less", this::visitLess);
        //addVisit("And", this::visitAnd);
        //addVisit("Not", this::visitNot);

    }

    /**
     * Verifies if right and left node are math expression.
     */
    public Boolean visitLess(JmmNode lessNode, Analysis analysis){
        JmmNode leftNode = lessNode.getChildren().get(0);
        JmmNode rightNode = lessNode.getChildren().get(1);

        // Operators in one of the sides.
        if (leftNode.getChildren().size() > 0 && !isMathExpression(leftNode.getKind())){
            analysis.addReport(rightNode, "\"" + leftNode + "\" unexpected operator");
        }
        else if (rightNode.getChildren().size() > 0 && !isMathExpression(leftNode.getKind())) {
            analysis.addReport(rightNode, "\"" + rightNode + "\" unexpected operator");
        }

        // Identifiers or numbers.
        String parentMethodName = Utils.getParentMethodName(lessNode);
        String leftKind = Utils.getVariableType(leftNode, analysis, parentMethodName);
        String rightKind = Utils.getVariableType(rightNode, analysis, parentMethodName);

        // TODO: how is the []int
        if (!leftKind.equals("int") && !leftKind.equals("[]int")){
            analysis.addReport(rightNode, "\"" + leftNode + "\" invalid type");
        } else if (!rightKind.equals("int") && !rightKind.equals("[]int")) {
            analysis.addReport(leftNode, "\"" + leftNode + "\" invalid type");
        }

        // TODO: add for functions.

        return true;
    }

    public boolean isMathExpression(String kind){
        return kind.equals("Mul") || kind.equals("Add") || kind.equals("Sub") || kind.equals("Div");
    }




}
