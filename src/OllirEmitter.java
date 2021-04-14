import pt.up.fe.comp.jmm.JmmNode;
import pt.up.fe.comp.jmm.analysis.table.Symbol;
import pt.up.fe.comp.jmm.analysis.table.SymbolTable;
import pt.up.fe.comp.jmm.analysis.table.Type;
import pt.up.fe.comp.jmm.report.Report;

import java.util.ArrayList;
import java.util.List;

public class OllirEmitter {
    SymbolTable symbolTable;
    List<Report> reports;
    StringBuilder sb;


    public OllirEmitter(SymbolTable symbolTable){
        this.symbolTable = symbolTable;
        reports = new ArrayList<>();
        sb = new StringBuilder();
    }

    public List<Report> getReports() {
        return reports;
    }

    /**
     * @param node the Program root node
     * */
    public String visit(JmmNode node){
        boolean hasImports = node.getNumChildren() == 2;

        sb.append(symbolTable.getClassName()).append("{\n");
        visitFields();
        classContructor();

        JmmNode classNode = hasImports ? node.getChildren().get(1) : node.getChildren().get(1);
        // Check if the class has methods
        if(classNode.getNumChildren() > 2){
            for(JmmNode methodNode: getMethodNodes(classNode)){
                // Visit each method of the class
                visitMethod(methodNode.getChildren().get(0));
            }
        }

        sb.append("}");
        return sb.toString();
    }

    private void visitFields(){
        for(Symbol field : symbolTable.getFields()){
            sb.append("\t.field private ");
            sb.append(field.getName());
            sb.append(getVarType(field.getType()));
            sb.append(";\n");
        }
    }

    private void classContructor(){
        sb.append("\t.construct ");
        sb.append(symbolTable.getClassName());
        sb.append("().V {\n");
        sb.append("\t\tinvokespecial(this, \"<init>\").V;\n\t}\n");
    }

    private void visitMethod(JmmNode methodNode){
        sb.append("\t.method public ");
        String methodName;
        if(methodNode.getKind().equals("MethodMain")){
            sb.append("static ");
            methodName = "main";
        }
        else methodName = methodNode.getChildren().get(1).get("name");
        sb.append(methodName);

        // Parameters
        sb.append("(");
        List<Symbol> parameters = symbolTable.getParameters(methodName);
        for(int i = 0; i < parameters.size(); i++){
            sb.append(parameters.get(i).getName());
            sb.append(getVarType(parameters.get(i).getType()));
            if(i < parameters.size()-1) sb.append(", ");
        }
        sb.append(")");

        // Return Type
        Type returnType = symbolTable.getReturnType(methodName);
        sb.append(getVarType(returnType));

        // Method Body
        sb.append("{\n");
        int bodyIdx = methodName.equals("main") ? methodNode.getNumChildren()-1 : methodNode.getNumChildren()-2;
        List<JmmNode> statements = methodNode.getChildren().get(bodyIdx).getChildren();
        visitStatements(methodName, statements);
        sb.append("\t}\n");
    }

    private void visitStatements(String methodName, List<JmmNode> statements){
        for(int i = 1; i < statements.size(); i++){

            switch (statements.get(i).getKind()){
                case("Assignment"):
                    assignmentStatement(methodName, statements.get(i));
                    break;
                case("IfElse"):
                    break;
                case("WhileStatment"):
                    break;
                //...
                default:
                    break;
            }
            sb.append("\n");
        }
    }
    // num_aux = 1;
    // num_aux.i32 :=.i32 1.i32; // num_aux is localVariable
    // putfield(this, num_aux.i32, ladoDireitoDoIgual).V; // num_aux is field
    // $1.num_aux := .i32 1.i32;
    private void assignmentStatement(String methodName, JmmNode statement){
        JmmNode left = statement.getChildren().get(0);
        JmmNode right = statement.getChildren().get(1);

        // Simple assignment
        if(right.getNumChildren() == 0){
            if(left.getKind().equals("ArrayAssignment")){
                String name = left.getChildren().get(0).get("name");
                buildArray(methodName, name);
            }
            else if(left.getKind().equals("Identifier")){
                String name = left.get("name");
                buildIdentifier(methodName, name);
            }
            sb.append(":=");
            sb.append("");
        }
    }

    private void buildIdentifier(String methodName, String name){
        Type type;

        if((type = getFieldType(name)) != null){
            // Variable is a Field: putfield(this, num_aux.i32, ladoDireitoDoIgual).V;
            String variable = getVar(name,type);
            sb.append(generatePutField(variable,"EXPRESSION"));
            return;
        } else if((type = getLocalVariableType(methodName, name)) == null){
            // Variable is a Parameter: $1.num_aux.i32 := .i32 1.i32;
            int position = getParameterPosition(methodName,name);
            type = getParameterType(methodName, position);
            name = "$" + position + "." + name;
        }
        sb.append(name);
        sb.append(getVarType(type));

    }

    //
    //
    private void buildArray(String methodName, String name){
        Type type;

        // putfield(this, A[i.i32].i32, ladoDoreito).V
        //
        if((type = getFieldType(name)) != null){
            // Variable is a Field: putfield(this, num_aux.i32, ladoDireitoDoIgual).V;
            String variable = name + "[" + "EXPRESSION" + "]" + getVarType(type);
            sb.append(generatePutField(variable,"EXPRESSION"));
            return;

        //$1.A[i.i32].i32
        } else if((type = getLocalVariableType(methodName, name)) == null){
            // Variable is a Parameter: $1.num_aux.i32 := .i32 1.i32;
            int position = getParameterPosition(methodName,name);
            type = getParameterType(methodName, position);
            name = "$" + position + "." + name;
        }
        sb.append(name);
        sb.append("[" + "EXPRESSION" + "]");
        sb.append(getVarType(type));
    }

    private String getVarType(Type type){
        String typeStr = "";
        if(type.isArray())
            typeStr = ".array";

        switch (type.getName()){
            case "int":
               typeStr += ".i32";
               break;
            case "boolean":
                typeStr += ".bool";
                break;
            case "void":
                typeStr += ".V";
                break;
            default:
                typeStr += "." + type.getName();
                break;
        }
        return typeStr;
    }

    private String getVar(String name, Type type){
        String varName = name;
        if(type.isArray())
            varName += ".array";

        switch (type.getName()){
            case "int":
                varName += ".i32";
                break;
            case "boolean":
                varName += ".bool";
                break;
            case "void":
                varName += ".V";
                break;
            default:
                varName += "." + type.getName();
                break;
        }
        return varName;
    }

    private Type getLocalVariableType(String methodName, String name){
        for (Symbol symb: symbolTable.getLocalVariables(methodName)){
            if (name.equals(symb.getName()))
                return symb.getType();
        }
        return null;
    }

    private Type getFieldType(String name){
        for (Symbol symb: symbolTable.getFields()){
            if (name.equals(symb.getName()))
                return symb.getType();
        }
        return null;
    }

    private Type getParameterType(String methodName, int position){
        return symbolTable.getParameters(methodName).get(position-1).getType();
    }

    private List<JmmNode> getMethodNodes(JmmNode classNode){
        List<JmmNode> methodDeclarations = new ArrayList<>();
        for(int i = 2; i < classNode.getNumChildren(); i++){
            methodDeclarations.add(classNode.getChildren().get(i));
        }
        return methodDeclarations;
    }


    private int getParameterPosition(String method, String parameter){
        List<Symbol> parameters = symbolTable.getParameters(method);

        for (int i = 0; i < parameters.size();i++){
            if (parameter.equals(parameters.get(i).getName()))
                return i + 1;

        }
        return -1;
    }

    private String generatePutField(String variable,String next){
        return "putfield(this," + variable + "," + next + ").V";
    }

}
