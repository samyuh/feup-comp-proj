package visitor;

import analysis.MySymbolTable;
import pt.up.fe.comp.jmm.JmmNode;
import pt.up.fe.comp.jmm.analysis.table.Type;
import pt.up.fe.comp.jmm.ast.PreorderJmmVisitor;

public class SymbolTableVisitor extends PreorderJmmVisitor<MySymbolTable,Boolean> {

    public SymbolTableVisitor(){
        addVisit("ClassDeclaration", this::visitClassDeclaration);
        addVisit("ImportNames", this::visitImportNames);
        addVisit("MethodMain", this::visitMainMethod);
        addVisit("MethodGeneric", this::visitGenericMethod);
    }

    Boolean visitImportNames(JmmNode jmmNode, MySymbolTable symbolTable){
        for(JmmNode node: jmmNode.getChildren()) {
            symbolTable.addImport(node.get("name"));
        }
        return true;
    }

    Boolean visitClassDeclaration(JmmNode jmmNode, MySymbolTable symbolTable){
        symbolTable.setClassName(jmmNode.getChildren().get(0).get("name"));
        if (jmmNode.getChildren().size() == 2){
            symbolTable.setExtendSuper(jmmNode.getChildren().get(1).get("name"));
        }
        return true;
    }

    Boolean visitGenericMethod(JmmNode jmmNode, MySymbolTable symbolTable){
        String type = jmmNode.getChildren().get(0).get("type");
        String name = jmmNode.getChildren().get(1).get("name");
        Type returnType;

        // Verifies if it's an array.
        if (type.equals("int[]")) returnType = new Type(type, true);
        else returnType = new Type(type, false);

        symbolTable.addMethodTypes(name, returnType);
        symbolTable.addMethod(name);

        // Parse method body
        parseMethodBody(jmmNode, symbolTable);

        return true;
    }

    void parseMethodBody(JmmNode jmmNode, MySymbolTable symbolTable){

    }

    Boolean visitMainMethod(JmmNode jmmNode, MySymbolTable symbolTable){
        Type type = new Type("void", false);
        symbolTable.addMethod("main");
        symbolTable.addMethodTypes("main", type );

        return true;
    }




}
