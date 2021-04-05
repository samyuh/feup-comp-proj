package visitor;
import analysis.Analysis;
import pt.up.fe.comp.jmm.JmmNode;
import pt.up.fe.comp.jmm.analysis.table.Symbol;
import pt.up.fe.comp.jmm.ast.PreorderJmmVisitor;
import pt.up.fe.comp.jmm.report.Report;
import pt.up.fe.comp.jmm.report.ReportType;
import pt.up.fe.comp.jmm.report.Stage;

import java.util.List;

public class UndefinedVarVisitor extends PreorderJmmVisitor<Analysis, Boolean>{

    public UndefinedVarVisitor(){
        addVisit("MethodDeclaration", this::visitMethodDeclaration);
    }

    public Boolean visitMethodDeclaration(JmmNode MethodDeclarationNode, Analysis analysis){
        JmmNode methodScope = MethodDeclarationNode.getChildren().get(0); // MethodMain or MethodGeneric
        String methodName = getMethodName(methodScope);
        JmmNode methodBody = getMethodBody(methodScope);

        validateAssignments(methodName, methodBody, analysis);

        return true;
    }

    public String getMethodName(JmmNode methodScope){
        if(methodScope.getKind().equals("MethodGeneric")){
            return methodScope.getChildren().get(1).get("name");
        }
        return "main";
    }

    public JmmNode getMethodBody(JmmNode methodScope){
        for (JmmNode child: methodScope.getChildren()){
            if (child.getKind().equals("MethodBody")) return child;
        }
        return null;
    }

    public void validateAssignments(String methodName, JmmNode methodBody, Analysis analysis){
        for (JmmNode node: methodBody.getChildren()){
            if(node.getKind().equals("Assignment")){
                validateAssignment(methodName, node, analysis);
            }
        }
    }

    public Boolean validateAssignment(String methodName, JmmNode assignment, Analysis analysis){
        JmmNode left = assignment.getChildren().get(0);
        JmmNode right = assignment.getChildren().get(1);

        if (left.getKind().equals("Identifier")){
            varIsDefined(methodName, analysis, left);
        }

        if (right.getKind().equals("Identifier")){
            varIsDefined(methodName, analysis, right);
        }
        return true;
    }

    /**
     * Check if the variable is a local variable or a class field.
     * Register a Report if the variable is undefined.
     * @param methodName the name of the method in which the variable was found
     * @param analysis aggregates the symbol table and the list of reports
     * @param identifierNode the Identifier node that represents the variable
     */
    public void varIsDefined(String methodName, Analysis analysis, JmmNode identifierNode){
        List<Symbol> localVariables = analysis.getSymbolTable().getLocalVariables(methodName);
        List<Symbol> classFields = analysis.getSymbolTable().getFields();
        String varName = identifierNode.get("name");

        if(!containsSymbol(localVariables, varName) && !containsSymbol(classFields, varName)){
            // DONE: report error
            analysis.addReport(new Report(ReportType.ERROR, Stage.SEMANTIC,
                    Integer.parseInt(identifierNode.get("line")) , Integer.parseInt(identifierNode.get("col")),
                    "Variable \"" + identifierNode.get("name") + "\" is undefined"));
        }
    }

    public Boolean containsSymbol(List<Symbol> vars, String varName){
        for(Symbol symbol: vars){
            if(symbol.getName().equals(varName)){
                return true;
            }
        }
        return false;
    }
}
