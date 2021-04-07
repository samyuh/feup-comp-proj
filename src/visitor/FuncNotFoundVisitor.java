package visitor;

import analysis.Analysis;
import pt.up.fe.comp.jmm.ast.PreorderJmmVisitor;
import pt.up.fe.comp.jmm.JmmNode;

public class FuncNotFoundVisitor extends PreorderJmmVisitor<Analysis, Boolean> {
    public FuncNotFoundVisitor() {
        addVisit("Dot", this::visitDot);
    }

    public Boolean visitDot(JmmNode node, Analysis analysis) {
        JmmNode nodeLeft = node.getChildren().get(0);
        JmmNode nodeRight = node.getChildren().get(1);

        // Check imported method
        // TODO: check if it is a variable and if it is defined
        if (nodeLeft.getKind().equals("Identifier")) {
            String nodeName = nodeLeft.get("name");
            if (!analysis.getSymbolTable().getImports().contains(nodeName)) {
                analysis.addReport(nodeLeft,"Import \"" + nodeName + "\" is undefined");
            }
        }

        // Check class methods
        if (nodeLeft.getKind().equals("This") && nodeRight.getKind().equals("DotMethod")){
            hasThisDotMethod(nodeRight, analysis);
        }
        // if (nodeLeft.getKind().equals())
        return true;
    }

    public void hasThisDotMethod(JmmNode node, Analysis analysis){
        String identifier = node.getChildren().get(0).get("name");
        if (!analysis.getSymbolTable().getMethods().contains(identifier)){
            analysis.addReport(node,"Function \"" + identifier + "\" is undefined");
        }
    }
}
