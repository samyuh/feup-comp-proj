package visitor;

import analysis.MySymbolTable;
import pt.up.fe.comp.jmm.JmmNode;
import pt.up.fe.comp.jmm.ast.PreorderJmmVisitor;

public class SymbolTableVisitor extends PreorderJmmVisitor<MySymbolTable,Boolean> {

    public SymbolTableVisitor(){
        addVisit("ClassDeclaration", this::visitClassDeclaration);
        addVisit("ImportNames", this::visitImportNames);
    }

    Boolean visitImportNames(JmmNode jmmNode, MySymbolTable symbolTable){
        for(JmmNode node: jmmNode.getChildren()) {
            symbolTable.addImport(node.get("name"));
        }
        return true;
    }

    Boolean visitClassDeclaration(JmmNode jmmNode, MySymbolTable symbolTable){
        symbolTable.setClassName(jmmNode.getChildren().get(0).get("name"));
        return true;
    }







}
