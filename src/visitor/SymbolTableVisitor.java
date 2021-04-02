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
        addVisit("ClassDeclaration", this::visitClassDeclaration);
        addVisit("ImportNames", this::visitImportNames);
        addVisit("MethodMain", this::visitMainMethod);
        addVisit("MethodGeneric", this::visitGenericMethod); // parameters and local variables
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

        // TODO: parse fields
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

        for (int i = 0 ; i < parametersNode.getNumChildren(); i += 2){
            JmmNode childType = parametersNode.getChildren().get(i);
                boolean isArray = childType.get("type").equals("int[]");
            Type type = new Type(childType.get("type"), isArray);

            String childName = parametersNode.getChildren().get(i+1).get("name");
            parametersSymbols.add(new Symbol(type, childName));
        }

        return parametersSymbols;
    }

    List<Symbol> parseMethodBody(JmmNode jmmNode, MySymbolTable symbolTable){
        JmmNode methodBody = jmmNode.getChildren().get(3);
        JmmNode varDeclarations = methodBody.getChildren().get(0);
        List<Symbol> declarations = new ArrayList<>();

        for (int i = 0 ; i < varDeclarations.getChildren().size(); i++){
            JmmNode varDeclaration = varDeclarations.getChildren().get(i);
            JmmNode typeNode = varDeclaration.getChildren().get(0);
            String name = varDeclaration.getChildren().get(1).get("name");

            boolean isArray = typeNode.get("type").equals("int[]");
            Type type = new Type(typeNode.get("type"), isArray);

            declarations.add(new Symbol(type, name));
        }

        return declarations;
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




}
