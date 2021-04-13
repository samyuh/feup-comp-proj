package visitor;

import analysis.Analysis;
import pt.up.fe.comp.jmm.JmmNode;
import pt.up.fe.comp.jmm.ast.PreorderJmmVisitor;

public class VerifyBoolOperatorVisitor extends PreorderJmmVisitor<Analysis, Boolean> {


    public VerifyBoolOperatorVisitor(){
        addVisit("Less", this::visitLess);
        addVisit("And", this::visitAnd);
        addVisit("Not", this::visitNot);

    }

    /**
     * Verifies if right and left node are math expression.
     * If not, verifies if they are numbers.
     */
    public Boolean visitLess(JmmNode lessNode, Analysis analysis){
        JmmNode leftNode = lessNode.getChildren().get(0);
        JmmNode rightNode = lessNode.getChildren().get(1);

        // Operators in one of the sides.
        if (leftNode.getChildren().size() > 0 && !Utils.isMathExpression(leftNode.getKind())){
            analysis.addReport(rightNode, "\"" + leftNode + "\" unexpected operator");
        }
        else if (rightNode.getChildren().size() > 0 && !Utils.isMathExpression(leftNode.getKind())) {
            analysis.addReport(rightNode, "\"" + rightNode + "\" unexpected operator");
        }

        // Identifiers or numbers.
        String parentMethodName = Utils.getParentMethodName(lessNode);
        String leftKind = Utils.getVariableType(leftNode, analysis, parentMethodName);
        String rightKind = Utils.getVariableType(rightNode, analysis, parentMethodName);

        // TODO: how is the []int
        if (!leftKind.equals("int") && !leftKind.equals("int[]")){
            analysis.addReport(rightNode, "\"" + leftNode + "\" invalid type");
        } else if (!rightKind.equals("int") && !rightKind.equals("[]int")) {
            analysis.addReport(leftNode, "\"" + leftNode + "\" invalid type");
        }

        // TODO: add for functions.

        return true;
    }

    public Boolean visitAnd(JmmNode andNode, Analysis analysis){
        JmmNode leftNode = andNode.getChildren().get(0);
        JmmNode rightNode = andNode.getChildren().get(1);
        String parentMethodName = Utils.getParentMethodName(andNode);

        // Final elements and operations.
        if (leftNode.getNumChildren() > 0 && !Utils.isBooleanExpression(leftNode.getKind())) {
            analysis.addReport(leftNode, "\"" + leftNode + "\" invalid expression");
        }
        else if (!Utils.getVariableType(leftNode, analysis, parentMethodName).equals("boolean")){
            analysis.addReport(leftNode, "\"" + leftNode + "\" invalid expression");
        }
        else if (rightNode.getNumChildren() > 0 && !Utils.isBooleanExpression(rightNode.getKind())) {
            analysis.addReport(rightNode, "\"" + rightNode + "\" invalid expression");
        }
        else if(!Utils.getVariableType(rightNode, analysis, parentMethodName).equals("boolean")){
            analysis.addReport(rightNode, "\"" + rightNode + "\" invalid expression");
        }

        // TODO: functions.

        return true;
    }


    public Boolean visitNot(JmmNode notNode, Analysis analysis) {
        JmmNode child = notNode.getChildren().get(0);
        String parentMethodName = Utils.getParentMethodName(notNode);

        if (!Utils.isBooleanExpression(child.getKind()) && !Utils.getVariableType(child, analysis, parentMethodName).equals("boolean"))
            analysis.addReport(child, "\"" + child + "\" invalid expression");

        // TODO: functions
        return true;
    }

}
