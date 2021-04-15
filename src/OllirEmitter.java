import pt.up.fe.comp.jmm.JmmNode;
import pt.up.fe.comp.jmm.analysis.table.Symbol;
import pt.up.fe.comp.jmm.analysis.table.SymbolTable;
import pt.up.fe.comp.jmm.analysis.table.Type;
import pt.up.fe.comp.jmm.report.Report;

import java.util.ArrayList;
import java.util.List;
import visitor.Utils;

public class OllirEmitter {
    static int auxVarNumber;
    static int indent;
    SymbolTable symbolTable;
    List<Report> reports;
    StringBuilder sb;

    public OllirEmitter(SymbolTable symbolTable){
        this.symbolTable = symbolTable;
        reports = new ArrayList<>();
        sb = new StringBuilder();
        auxVarNumber = 0;
        indent = 0;
    }

    /**
     * @param node the Program root node
     * */
    public String visit(JmmNode node){
        boolean hasImports = node.getNumChildren() == 2;

        sb.append(symbolTable.getClassName()).append("{\n");
        indent++;
        visitFields();
        classConstructor();

        JmmNode classNode = hasImports ? node.getChildren().get(1) : node.getChildren().get(0);
        // Check if the class has methods
        if(classNode.getNumChildren() > 2){
            for(JmmNode methodNode: getMethodNodes(classNode)){
                // Visit each method of the class
                visitMethod(methodNode.getChildren().get(0));
            }
        }

        indent--;
        sb.append("}");
        return sb.toString();
    }

    private void visitFields(){
        for(Symbol field : symbolTable.getFields()){
            sb.append(prefix()).append(".field private ");
            sb.append(field.getName());
            sb.append(MyOllirUtils.ollirType(field.getType()));
            sb.append(";\n");
        }
    }

    private void classConstructor(){
        sb.append(prefix()).append(".construct ");
        sb.append(symbolTable.getClassName());
        sb.append("().V {\n");
        indent++;
        sb.append(prefix()).append("invokespecial(this, \"<init>\").V;\n\t}\n");
        indent--;
    }

    private void visitMethod(JmmNode methodNode) {
        sb.append(prefix()).append(".method public ");
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
            sb.append(MyOllirUtils.ollirType(parameters.get(i).getType()));
            if(i < parameters.size()-1) sb.append(", ");
        }
        sb.append(")");

        // Return Type
        Type returnType = symbolTable.getReturnType(methodName);
        String returnTypeStr = MyOllirUtils.ollirType(returnType);
        sb.append(returnTypeStr);

        // Method Body
        sb.append("{\n");
        indent++;
        int bodyIdx = methodName.equals("main") ? methodNode.getNumChildren()-1 : methodNode.getNumChildren()-2;
        List<JmmNode> statements = methodNode.getChildren().get(bodyIdx).getChildren();
        visitStatements(methodName, statements);

        // Return Statement
        if(!methodName.equals("main")){
            JmmNode returnValue = methodNode.getChildren().get(bodyIdx+1).getChildren().get(0);
            sb.append(prefix()).append("ret");
            sb.append(returnTypeStr).append(" ");
            sb.append(ollirExpression(methodName,returnValue)).append(";\n");
        }

        indent--;
        sb.append(prefix()).append("}\n");
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


    private void assignmentStatement(String methodName, JmmNode statement){
        JmmNode left = statement.getChildren().get(0); // Left side of the assignment
        JmmNode right = statement.getChildren().get(1); // Right side of the assignment

        // Array Assignment
        if(left.getKind().equals("ArrayAssignment")){
            String name = left.getChildren().get(0).get("name"); // Array name
            JmmNode indexNode = left.getChildren().get(1).getChildren().get(0);
            ollirArrayAssignment(methodName, name, indexNode, right);
            return;
        }

        // Identifier Assignment
        String name = left.get("name"); // Identifier Name
        ollirIdentifierAssignment(methodName, name, right);
    }

    private void ollirIdentifierAssignment(String methodName, String name, JmmNode valueNode){
        Type type;

        // Left side of the assignment is a Field: e.g. putfield(this, num_aux.i32, value).V;
        if((type = getFieldType(name)) != null){
            sb.append(generatePutField(methodName, MyOllirUtils.ollirVar(name,type),valueNode));
            return;
        }

        // Left side of the assignment is a Parameter: e.g. $1.num_aux.i32 := .i32 1.i32;
        if((type = getLocalVariableType(methodName, name)) == null){
            int position = getParameterPosition(methodName,name);
            type = getParameterType(methodName, position);
            name = MyOllirUtils.ollirParameter(name,position);
        }

        // Left side of the assignment is a Local Variable or Parameter
        String leftSide = MyOllirUtils.ollirVar(name, type);
        String rightSide = ollirExpression(methodName, valueNode);

        sb.append(prefix()).append(leftSide).append(":=").append(MyOllirUtils.ollirType(type)).append(" ").append(rightSide).append(";\n");
    }

    private void ollirArrayAssignment(String methodName, String name, JmmNode indexNode, JmmNode rightNode){

        //  Left side of the assignment is a Field: e.g.putfield(this, A[i.i32].i32, ladoDoreito).V
        if(getFieldType(name) != null){
            String indexValue = ollirArrayIndex(methodName, indexNode);
            String leftSide = name + "[" + indexValue + "].i32";
            sb.append(generatePutField(methodName, leftSide, rightNode));
            return;
        }

        // // Left side of the assignment is a Parameter: e.g. $1.A[i.i32].i32
        if(getLocalVariableType(methodName, name) == null){
            // Variable is a Parameter: $1.num_aux.i32 := .i32 1.i32;
            int position = getParameterPosition(methodName,name);
            name = MyOllirUtils.ollirParameter(name,position);
        }

        // Left side of the assignment is a Local Variable or Parameter
        String indexValue = ollirArrayIndex(methodName, indexNode);
        String leftSide = name + "[" + indexValue + "].i32";
        String rightSide = ollirExpression(methodName, rightNode);

        sb.append(indent).append(leftSide).append(":=.i32 ").append(rightSide).append(";\n");
    }

    private String ollirArrayIndex(String methodName, JmmNode node){
        if(node.getKind().equals("Number")){
            String aux = newAuxiliarVar(".i32", methodName, node);
            sb.append(aux);
            return "t" + auxVarNumber + ".i32";
        }
        return ollirExpression(methodName,node);
    }

    // TODO: process a node, generating auxiliar variables
    private String ollirExpression(String methodName, JmmNode node){
        String kind = node.getKind();

        if(kind.equals("Identifier"))
            return ollirFromIdentifierNode(methodName, node);
        if(kind.equals("True") || kind.equals("False"))
            return kind.toLowerCase() + ".bool";
        if(kind.equals("Number"))
            return node.get("value") + ".i32";
        if(Utils.isMathExpression(kind))
            return ollirMathBooleanExpression(methodName, node, ".i32");
        if(Utils.isBooleanExpression(kind))
            return ollirMathBooleanExpression(methodName, node, ".bool");

        // TODO: implement recursive expression subdivision, with auxiliar variables
        return "VALUE";// ollirExpression(valueNode);
    }

    public String ollirFromIdentifierNode(String methodName, JmmNode identifierNode){
        Type type;
        String name = identifierNode.get("name");

        // GetField
        if((type = getFieldType(name)) != null)
            return generateGetField(name, type);

        if((type = getLocalVariableType(methodName, name)) == null){
           // Parameter
           int position = getParameterPosition(methodName,name);
           type = getParameterType(methodName, position);
           name = MyOllirUtils.ollirParameter(name, position);
        }

        // Local Variable
        return MyOllirUtils.ollirVar(name,type);
    }

    public String ollirMathBooleanExpression(String methodName, JmmNode node, String type){
        JmmNode left = node.getChildren().get(0);
        JmmNode right = node.getChildren().get(1);

        String leftValue, rightValue;
        if(left.getNumChildren() > 0){
            sb.append(newAuxiliarVar(type, methodName, left));
            leftValue = "t" + auxVarNumber + type;
        } else leftValue = ollirExpression(methodName,left);

        if(right.getNumChildren() > 0){
            sb.append(newAuxiliarVar(type, methodName, right));
            rightValue = "t" + auxVarNumber + type;
        } else rightValue = ollirExpression(methodName, right);

        return leftValue + MyOllirUtils.ollirOperator(node) + rightValue;
    }

    private String generatePutField(String methodName, String field, JmmNode valueNode){
        String value = ollirExpression(methodName, valueNode);
        return prefix() + "putfield(this," + field + "," + value + ").V;";
    }

    private String generateGetField(String field, Type type){
        return prefix() + "getfield(this," + MyOllirUtils.ollirVar(field, type) + ")" + MyOllirUtils.ollirType(type);
    }

    private String newAuxiliarVar(String type, String methodName, JmmNode node){
        auxVarNumber++;
        return prefix() + "t" + auxVarNumber + type + " :=" + type +" " + ollirExpression(methodName, node) + ";\n";
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

    private int getParameterPosition(String method, String parameter){
        List<Symbol> parameters = symbolTable.getParameters(method);

        for (int i = 0; i < parameters.size();i++){
            if (parameter.equals(parameters.get(i).getName()))
                return i + 1;

        }
        return -1;
    }


    private List<JmmNode> getMethodNodes(JmmNode classNode){
        List<JmmNode> methodDeclarations = new ArrayList<>();
        for(int i = 2; i < classNode.getNumChildren(); i++){
            methodDeclarations.add(classNode.getChildren().get(i));
        }
        return methodDeclarations;
    }

    public List<Report> getReports() {
        return reports;
    }

    public static String prefix(){
        String tabs = "";
        for(int i = 0; i < indent; i++){
            tabs += "\t";
        }
        return tabs;
    }

}
