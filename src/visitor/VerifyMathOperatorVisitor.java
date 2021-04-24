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
        JmmNode leftNode = node.getChildren().get(0);
        JmmNode rightNode = node.getChildren().get(1);
        String parentMethodName = Utils.getParentMethodName(node);

        checkNodeExpression(leftNode, analysis, parentMethodName);
        checkNodeExpression(rightNode, analysis, parentMethodName);

        return true;
    }

    private void checkNodeExpression(JmmNode node, Analysis analysis, String parentMethodName){

        if (Utils.isMathExpression(node.getKind()))
            return;
        else if (Utils.isBooleanExpression(node.getKind())) {
            analysis.addReport(node, "\"" + node + "\" invalid operator.");
        }
        else if (node.getKind().equals("Dot")){
            String returnValueMethod = Utils.getReturnValueMethod(node, analysis);
            if (!isAcceptedTypes(returnValueMethod))
                analysis.addReport(node, "\"" + node + "\" invalid type.");
        }
        else if (!node.getKind().equals("ArrayAccess") && !isAcceptedTypes(Utils.getVariableType(node, analysis, parentMethodName))) {
            analysis.addReport(node, "\"" + node + "\" invalid type.");
        }
    }

    private boolean isAcceptedTypes(String kind){
        return kind.equals("undefined") || kind.equals("int");
    }


}
