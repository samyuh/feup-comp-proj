package visitor;

import pt.up.fe.comp.jmm.ast.AJmmVisitor;
import pt.up.fe.comp.jmm.JmmNode;
import pt.up.fe.comp.jmm.ast.JmmNodeImpl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class ConstantPropagationVisitor extends AJmmVisitor<HashMap<String, String> , Boolean> {
    private static int counter = 0;

    public ConstantPropagationVisitor(){
        addVisit("Identifier", this::visitIdentifier);
        addVisit("Dot", this::visitDot);
        addVisit("ArrayAccess", this::visitArray);
        addVisit("NewIntArray", this::visitNewIntArray);

        addVisit("IfElse", this::visitIfExpression);
        addVisit("WhileStatment", this::visitWhileExpression);

        addVisit("MethodBody", this::visitBlock);
        addVisit("IfBlock", this::visitBlock);
        addVisit("ElseBlock", this::visitBlock);
        addVisit("WhileBody", this::visitBlock);

        addVisit("ArrayAssignment", this::visitArrayAssignment);

        List<String> kinds = new ArrayList(Arrays.asList("Add", "Sub", "Mult", "Div", "Less", "And", "Not"));
        for(String kind: kinds){
            addVisit(kind, this::visitExpression);
        }

        setDefaultVisit(this::defaultVisit);
    }

    public Boolean visitIdentifier(JmmNode node, HashMap<String, String> constants) {
        System.out.println("Visit Identifier: " + node);
        if(constants.containsKey(node.get("name"))){
            String name = node.get("name");
            JmmNode parent = node.getParent();
            int index = node.getParent().getChildren().indexOf(node);

            JmmNode new_node;
            switch(constants.get(name)){
                case "true":
                    new_node = new JmmNodeImpl("True");
                    break;
                case "false":
                    new_node = new JmmNodeImpl("False");
                    break;
                default:
                    new_node = new JmmNodeImpl("Number");
                    new_node.put("value", constants.get(name));
                    break;
            }

            new_node.put("col", node.get("col"));
            new_node.put("line", node.get("line"));

            parent.add(new_node, index);
            parent.removeChild(node);

            counter++;
        }
        return true;
    }

    public Boolean visitExpression(JmmNode node, HashMap<String, String> constants){
        //System.out.println("Visit Expression: " + node);
        for (int i = 0; i < node.getNumChildren(); i++) {
            this.visit(node.getChildren().get(i), constants);
        }
        return true;
    }

    public Boolean visitDot(JmmNode node, HashMap<String, String> constants){
        //System.out.println("Visit dot method: " + node);
        if(node.getChildren().get(1).getKind().equals("Length")) return true;

        JmmNode parameters = node.getChildren().get(1).getChildren().get(1);

        for (int i = 0; i < parameters.getNumChildren(); i++) {
            this.visit(parameters.getChildren().get(i), constants);
        }
        return true;
    }

    public Boolean visitArray(JmmNode node, HashMap<String, String> constants)  {
        JmmNode index = node.getChildren().get(1);
        this.visit(index, constants);
        return true;
    }

    public Boolean visitArrayAssignment(JmmNode node, HashMap<String, String> constants)  {
        JmmNode index = node.getChildren().get(1).getChildren().get(0);
        this.visit(index, constants);
        return true;
    }

    public Boolean visitNewIntArray(JmmNode node, HashMap<String, String> constants)  {
        JmmNode index = node.getChildren().get(0);
        this.visit(index, constants);

        return true;
    }

    public Boolean visitIfExpression(JmmNode node, HashMap<String, String> constants) {
        JmmNode condition = node.getChildren().get(0);
        JmmNode ifBlock = node.getChildren().get(1);
        JmmNode elseBlock = node.getChildren().get(2);

        this.visit(condition, constants);
        this.visit(ifBlock, constants);
        this.visit(elseBlock, constants);

        return true;
    }

    public Boolean visitWhileExpression(JmmNode node, HashMap<String, String> constants) {
        JmmNode condition = node.getChildren().get(0);
        JmmNode whileBody = node.getChildren().get(1);

        this.visit(condition, constants);
        this.visit(whileBody, constants);

        return true;
    }

    private Boolean defaultVisit(JmmNode node, HashMap<String, String> constants) {
        //System.out.println("Visit default: " + node);
        return true;
    }

    private Boolean visitBlock(JmmNode node, HashMap<String, String> constants){
        int start = node.getKind().equals("MethodBody") ? 1 : 0;

        // Loop through method body
        for(int i = start; i < node.getNumChildren(); i++){
            JmmNode child = node.getChildren().get(i);
            // Assignment
            if(child.getKind().equals("Assignment")){
                JmmNode assignmentRight = child.getChildren().get(1);
                // Constant
                if(assignmentRight.getKind().equals("Number")) {
                    if(child.getChildren().get(0).getKind().equals("ArrayAssignment")) {
                        this.visit(child.getChildren().get(0), constants);
                    } else {
                        constants.put(child.getChildren().get(0).get("name"), assignmentRight.get("value"));
                        child.delete();
                        i--;
                    }
                } else if(assignmentRight.getKind().equals("True") || assignmentRight.getKind().equals("False")) {
                    if(child.getChildren().get(0).getKind().equals("ArrayAssignment")){
                        this.visit(child.getChildren().get(0), constants);
                    } else {
                        constants.put(child.getChildren().get(0).get("name"), assignmentRight.getKind().toLowerCase());
                        child.delete();
                        i--;
                    }
                }
                else { // Expression
                    this.visit(assignmentRight, constants);
                }
            }
            else { // Other Nodes
                this.visit(child, constants);
            }
        }

        return true;
    }

    public int getCounter(){
        return this.counter;
    }

    public void resetCounter(){
        this.counter = 0;
    }
}
