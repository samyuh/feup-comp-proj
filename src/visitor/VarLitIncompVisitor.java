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
    public VarLitIncompVisitor(){
        addVisit("Assignment", this::visitCheckAssigment);
    }

    public Boolean visitCheckAssigment(JmmNode assigmentNode, Analysis analysis){
        JmmNode leftNode = assigmentNode.getChildren().get(0);
        JmmNode rightNode = assigmentNode.getChildren().get(1);
        String parentMethodName = Utils.getParentMethodName(assigmentNode);

        String left =  Utils.getVariableType(leftNode, analysis, parentMethodName);
        String right = Utils.getVariableType(rightNode, analysis, parentMethodName);

        if (leftNode.getKind().equals("Identifier"))
            System.out.println(leftNode.get("name") + " " + left);
        if (rightNode.getKind().equals("Identifier"))
            System.out.println(rightNode.get("name") + " " + right);
        if (left.equals("boolean") && !right.equals("boolean")) {
            analysis.addReport(rightNode, "\"" + rightNode + "\" is not a boolean");
        }

        if (!left.equals("boolean") && right.equals("boolean")) {
            analysis.addReport(leftNode, "\"" + leftNode + "\" is not a boolean");
        }
        return true; }

}
