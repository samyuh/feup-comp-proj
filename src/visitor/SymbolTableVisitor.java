package visitor;

import analysis.MySymbolTable;
import pt.up.fe.comp.jmm.JmmNode;
import pt.up.fe.comp.jmm.analysis.table.Symbol;
import pt.up.fe.comp.jmm.analysis.table.Type;
import pt.up.fe.comp.jmm.ast.PreorderJmmVisitor;

import java.util.ArrayList;
import java.util.List;

public class SymbolTableVisitor extends PreorderJmmVisitor<MySymbolTable,Boolean> {

    public SymbolTableVisitor(){
        addVisit("ImportNames", this::visitImportNames);
        addVisit("ClassDeclaration", this::visitClassDeclaration);
        addVisit("ClassFields", this::visitClassFields);
        addVisit("MethodMain", this::visitMainMethod);
        addVisit("MethodGeneric", this::visitGenericMethod); // parameters and local

    }

    Boolean visitImportNames(JmmNode jmmNode, MySymbolTable symbolTable) {
        StringBuilder importNames = new StringBuilder();
        for(int i = 0; i < jmmNode.getChildren().size(); i++) {
            importNames.append(jmmNode.getChildren().get(i).get("name"));
            if (i != jmmNode.getChildren().size() - 1)
                importNames.append(".");
        }

        symbolTable.addImport(importNames.toString());
        return true;
    }

    Boolean visitClassDeclaration(JmmNode jmmNode, MySymbolTable symbolTable){
        symbolTable.setClassName(jmmNode.getChildren().get(0).get("name"));
        if (jmmNode.getChildren().size() == 2){
            symbolTable.setExtendSuper(jmmNode.getChildren().get(1).get("name"));
        }
        return true;
    }

    Boolean visitClassFields(JmmNode jmmNode, MySymbolTable symbolTable){

        for (int i = 0 ; i < jmmNode.getChildren().size(); i++){
            JmmNode varDeclaration = jmmNode.getChildren().get(i);
            Symbol symbol = parseVariable(varDeclaration);
            symbolTable.addField(symbol);
        }
        return true;
    }

    Boolean visitMainMethod(JmmNode jmmNode, MySymbolTable symbolTable){
        Type type = new Type("void", false);

        // Parse parameter
        JmmNode stringArray = jmmNode.getChildren().get(0);
        String identifier = stringArray.getChildren().get(0).get("name");

        // Create symbol
        Symbol symbol = new Symbol(new Type("String", true), identifier);
        List<Symbol> parameters = new ArrayList<>();
        parameters.add(symbol);

        symbolTable.addMethod("main", parameters);
        symbolTable.addMethodType("main", type );

        return true;
    }

    Boolean visitGenericMethod(JmmNode jmmNode, MySymbolTable symbolTable){
        String type = jmmNode.getChildren().get(0).get("type");
        String name = jmmNode.getChildren().get(1).get("name");
        Type returnType;

        // Verifies if it's an array.
        if (type.equals("int[]")) returnType = new Type(type, true);
        else returnType = new Type(type, false);

        // Parse parameters
        List<Symbol> parameters = parseMethodParameters(jmmNode);
        symbolTable.addMethod(name, parameters);
        symbolTable.addMethodType(name, returnType);

        // Parse method body
        List<Symbol> localVariables = parseMethodBody(jmmNode, symbolTable);
        symbolTable.addLocalVariables(name, localVariables);

        return true;
    }

    List<Symbol> parseMethodParameters(JmmNode jmmNode){
        List<Symbol> parametersSymbols = new ArrayList<>();
        JmmNode parametersNode = jmmNode.getChildren().get(2);

        for (int i = 0 ; i < parametersNode.getNumChildren(); i ++){
            JmmNode methodParameter = parametersNode.getChildren().get(i);
            Symbol symbol = parseVariable(methodParameter);
            parametersSymbols.add(symbol);
        }

        return parametersSymbols;
    }

    List<Symbol> parseMethodBody(JmmNode jmmNode, MySymbolTable symbolTable){
        JmmNode methodBody = jmmNode.getChildren().get(3);
        JmmNode varDeclarations = methodBody.getChildren().get(0);
        List<Symbol> declarations = new ArrayList<>();

        for (int i = 0 ; i < varDeclarations.getChildren().size(); i++){
            JmmNode varDeclaration = varDeclarations.getChildren().get(i);
            Symbol symbol = parseVariable(varDeclaration);
            declarations.add(symbol);
        }

        return declarations;
    }

    Symbol parseVariable(JmmNode variableNode){

        JmmNode typeNode = variableNode.getChildren().get(0);
        String name = variableNode.getChildren().get(1).get("name");

        boolean isArray = typeNode.get("type").equals("int[]");
        Type type = new Type(typeNode.get("type"), isArray);

        return new Symbol(type, name);
    }

}
