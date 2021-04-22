package visitor;

import analysis.Analysis;
import analysis.MySymbolTable;
import pt.up.fe.comp.jmm.JmmNode;
import pt.up.fe.comp.jmm.ast.PreorderJmmVisitor;

/**
 * This visitor checks invalidates the following assigment:
 * int j;
 * j = false;
 */
public class AssignmentVisitor extends PreorderJmmVisitor<Analysis, Boolean> {
    public AssignmentVisitor() {
        addVisit("Assignment", this::visitCheckAssigment);

    }

    public Boolean visitCheckAssigment(JmmNode assigmentNode, Analysis analysis) {
        JmmNode leftNode = assigmentNode.getChildren().get(0);
        JmmNode rightNode = assigmentNode.getChildren().get(1);
        String parentMethodName = Utils.getParentMethodName(assigmentNode);
        String right;
        String left = Utils.getVariableType(leftNode, analysis, parentMethodName);

        // Check simple assignments j = false or j = i, where i is a boolean and j = this.bla();
        if (rightNode.getChildren().size() == 0) {
            right = Utils.getVariableType(rightNode, analysis, parentMethodName);
            if (!right.equals(left))
                analysis.addReport(rightNode, "\"" + rightNode + "\" and \"" + leftNode + "\" incompatible types");
        }
        // Check j = (true && false);
        else if (Utils.isBooleanExpression(rightNode.getKind()) && !left.equals("boolean")){
            analysis.addReport(rightNode, "\"" + rightNode + "\" and \"" + leftNode + "\" incompatible types");
        }
        // check j = 1+2+3;
        else if (Utils.isMathExpression(rightNode.getKind()) && !left.equals("int") && !left.equals("int[]"))
            analysis.addReport(rightNode, "\"" + rightNode + "\" and \"" + leftNode + "\" incompatible types");

        else if (rightNode.getKind().equals("Dot")){
            String returnValueMethod = Utils.getReturnValueMethod(rightNode, analysis);
            if (!returnValueMethod.equals("none") && !returnValueMethod.equals(left))
                analysis.addReport(rightNode, "\"" + rightNode + "\" and \"" + leftNode + "\" incompatible types");
        }

        return true;
    }



}
