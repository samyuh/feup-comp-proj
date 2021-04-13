package visitor;

import analysis.Analysis;
import pt.up.fe.comp.jmm.JmmNode;
import pt.up.fe.comp.jmm.ast.PreorderJmmVisitor;

public class VerifyMathOperatorVisitor extends PreorderJmmVisitor<Analysis, Boolean> {
    public VerifyMathOperatorVisitor() {
        addVisit("Add", this::visitMathExpression);
        addVisit("Sub", this::visitMathExpression);
        addVisit("Mult", this::visitMathExpression);
        addVisit("Div", this::visitMathExpression);
    }

    public Boolean visitMathExpression(JmmNode node, Analysis analysis) {
        JmmNode left = node.getChildren().get(0);
        JmmNode right = node.getChildren().get(1);
        String parentMethodName = Utils.getParentMethodName(node);

        if(!Utils.getVariableType(left, analysis, parentMethodName).equals("int") && !Utils.getVariableType(left, analysis, parentMethodName).equals("[]int")) {
            analysis.addReport(left, "Math expression is not valid with bool" + left);
        }
        else if(!Utils.getVariableType(right, analysis, parentMethodName).equals("int") && !Utils.getVariableType(right, analysis, parentMethodName).equals("[]int")) {
            analysis.addReport(right, "Math expression is not valid with bool" + right);
        }

        // TODO: functions.
        return true;
    }
}
