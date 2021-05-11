package visitor;

import pt.up.fe.comp.jmm.ast.AJmmVisitor;
import pt.up.fe.comp.jmm.JmmNode;
import pt.up.fe.comp.jmm.ast.JmmNodeImpl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class ConstantPropagationVisitor extends AJmmVisitor<HashMap<String, Integer> , Boolean> {

    public ConstantPropagationVisitor(){
        addVisit("Identifier", this::visitIdentifier);
        addVisit("Dot", this::visitDot);

        List<String> kinds = new ArrayList(Arrays.asList("Add", "Sub", "Mult", "Div", "Less", "And"));
        for(String kind: kinds){
            addVisit(kind, this::visitExpression);
        }

        setDefaultVisit(this::defaultVisit);
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

        for (int i = 0; i < node.getNumChildren(); i++) {
            this.visit(node.getChildren().get(i), constants);
        }
        return true;
    }

    public Boolean visitDot(JmmNode node, HashMap<String, Integer> constants){
        System.out.println("Visit dot method: " + node);
        JmmNode parameters = node.getChildren().get(1).getChildren().get(1);

        for (int i = 0; i < parameters.getNumChildren(); i++) {
            this.visit(parameters.getChildren().get(i), constants);
        }
        return true;
    }

    private Boolean defaultVisit(JmmNode node, HashMap<String, Integer> constants) {
        System.out.println("Visit default: " + node);
        return true;
    }
}
