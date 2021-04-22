package visitor;

import analysis.Analysis;
import pt.up.fe.comp.jmm.JmmNode;
import pt.up.fe.comp.jmm.ast.PreorderJmmVisitor;

public class WhileIfVisitorCondition extends PreorderJmmVisitor<Analysis, Boolean>  {

    public WhileIfVisitorCondition(){
        addVisit("WhileStatment", this::visitWhileIf);
        addVisit("IfElse", this::visitWhileIf);
    }

    public Boolean visitWhileIf(JmmNode node, Analysis analysis){
        JmmNode conditionNode = node.getChildren().get(0);
        String parentMethodName = Utils.getParentMethodName(node);

        if (Utils.isBooleanExpression(conditionNode.getKind())) return true;
        if (Utils.isMathExpression(conditionNode.getKind()))
            analysis.addReport(conditionNode, "\"" + conditionNode+ "\" expecting a boolean expression.");
        else if (conditionNode.getKind().equals("Dot")){
            String returnValueMethod = Utils.getReturnValueMethod(conditionNode, analysis);
            if (!returnValueMethod.equals("undefined") && !returnValueMethod.equals("boolean"))
                analysis.addReport(conditionNode, "\"" + conditionNode + "\" invalid type: expecting an boolean.");
        }
        else if (!Utils.getVariableType(conditionNode, analysis, parentMethodName).equals("boolean"))
            analysis.addReport(conditionNode, "\"" + conditionNode+ "\" expecting a boolean expression.");

        return true;
    }
}
