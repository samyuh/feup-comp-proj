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
public class VarLitIncompVisitor extends PreorderJmmVisitor<Analysis, Boolean> {
    public VarLitIncompVisitor() {
        addVisit("Assignment", this::visitCheckAssigment);

    }

    // TODO: funcoes.
    public Boolean visitCheckAssigment(JmmNode assigmentNode, Analysis analysis) {
        JmmNode leftNode = assigmentNode.getChildren().get(0);
        JmmNode rightNode = assigmentNode.getChildren().get(1);
        String parentMethodName = Utils.getParentMethodName(assigmentNode);
        String right;
        String left = Utils.getVariableType(leftNode, analysis, parentMethodName);

        if (rightNode.getChildren().size() == 0) {
            right = Utils.getVariableType(rightNode, analysis, parentMethodName);
            if (!right.equals(left))
                analysis.addReport(rightNode, "\"" + rightNode + "\" and \"" + leftNode + "\" incompatible types");
        }else {
            if (isBooleanOp(rightNode.getKind()) != left.equals("boolean"))
                analysis.addReport(rightNode, "\"" + rightNode + "\" and \"" + leftNode + "\" incompatible types");
        }

        return true;
    }


    public Boolean isBooleanOp(String kind){
        return kind.equals("And") || kind.equals("Not") || kind.equals("Less");
    }


}
