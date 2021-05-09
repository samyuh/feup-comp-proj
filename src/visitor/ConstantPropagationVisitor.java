package visitor;

import pt.up.fe.comp.jmm.analysis.table.SymbolTable;
import pt.up.fe.comp.jmm.ast.AJmmVisitor;
import pt.up.fe.comp.jmm.JmmNode;
import pt.up.fe.comp.jmm.ast.JmmNodeImpl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class ConstantPropagationVisitor extends AJmmVisitor<HashMap<String, Integer> , Boolean> {

    public ConstantPropagationVisitor(){
        addVisit("Identifier", this::visitIdentifier);

        List<String> kinds = new ArrayList(Arrays.asList("Add", "Sub", "Mult", "Div", "Less", "And"));
        for(String kind: kinds){
            addVisit(kind, this::visitExpression);
        }
    }

    public Boolean visitIdentifier(JmmNode node, HashMap<String, Integer> constants) {
        System.out.println("Visit Identifier: " + node);
        if(constants.containsKey(node.get("name"))){
            String name = node.get("name");
            JmmNode parent = node.getParent();
            int index = node.getParent().getChildren().indexOf(node);

            JmmNode new_node = new JmmNodeImpl("Number");
            new_node.put("value", Integer.toString(constants.get(name)));
            new_node.put("col", node.get("col"));
            new_node.put("line", node.get("line"));

            parent.add(new_node, index);
            parent.removeChild(node);
        }
        return true;
    }

    public Boolean visitExpression(JmmNode node, HashMap<String, Integer> constants){
        System.out.println("Visit Expression: " + node);
        int nodeIdx = 0;
        int numChildren = node.getNumChildren();

        while (nodeIdx < numChildren) {
            if(node.getChildren().get(nodeIdx).getKind().equals("Number")){
                nodeIdx++;
                continue;
            }
            this.visit(node.getChildren().get(nodeIdx), constants);
            nodeIdx++;
        }
        return true;
    }
}
