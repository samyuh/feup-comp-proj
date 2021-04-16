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


    // Convert assignment expression to ollir
    private void assignmentStatement(String methodName, JmmNode statement){
        JmmNode left = statement.getChildren().get(0); // Left side of the assignment
        JmmNode right = statement.getChildren().get(1); // Right side of the assignment

        // Array Assignment
        if(left.getKind().equals("ArrayAssignment")){
            JmmNode arrayId = left.getChildren().get(0); // Array Identifier
            JmmNode indexNode = left.getChildren().get(1).getChildren().get(0);
            ollirArrayAssignment(methodName, arrayId, indexNode, right);
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
        String assignmentType = MyOllirUtils.ollirType(type);
        String rightSide;
        if(valueNode.getKind().equals("Dot"))
            rightSide = ollirDotMethod(methodName, valueNode, assignmentType);
        else rightSide = ollirExpression(methodName, valueNode);

        sb.append(prefix()).append(leftSide).append(" :=").append(assignmentType);
        sb.append(" ").append(rightSide).append(";");
    }

    private void ollirArrayAssignment(String methodName, JmmNode arrayIdentifier, JmmNode indexNode, JmmNode rightNode){
        String name = arrayIdentifier.get("name");
        //  Left side of the assignment is a Field: e.g.putfield(this, A[i.i32].i32, ladoDoreito).V
        if(getFieldType(name) != null){
            String leftSide = ollirArrayAccess(methodName,arrayIdentifier,indexNode);
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
        String leftSide = ollirArrayAccess(methodName,arrayIdentifier,indexNode);
        String rightSide = ollirExpression(methodName, rightNode);

        sb.append(prefix()).append(leftSide).append(" :=.i32 ").append(rightSide).append(";");
    }

    // Get ollir representation for an array access
    private String ollirArrayAccess(String methodName, JmmNode arrayNode, JmmNode node){
        String arrayStr;
        String indexValue;

        // Array accessed
        if(isField(arrayNode) || !arrayNode.getKind().equals("Identifier")){
            sb.append(newAuxiliarVar(".array.i32", methodName, arrayNode));
            arrayStr = "t" + auxVarNumber;
        } else {
            arrayStr = arrayNode.get("name");
            int pos = getParameterPosition(methodName, arrayStr);
            if(pos != -1) arrayStr = MyOllirUtils.ollirParameter(arrayStr, pos);
        }

        // Index
        if(isField(node) || !node.getKind().equals("Identifier")){
            String aux = newAuxiliarVar(".i32", methodName, node);
            sb.append(aux);
            indexValue = "t" + auxVarNumber + ".i32";
        } else indexValue = ollirFromIdentifierNode(methodName, node);

        return arrayStr + "[" + indexValue + "].i32";
    }


    // Get ollir representation for an expression
    private String ollirExpression(String methodName, JmmNode node){
        String kind = node.getKind();

        // Terminals
        if(kind.equals("Identifier"))
            return ollirFromIdentifierNode(methodName, node);
        if(kind.equals("True") || kind.equals("False"))
            return kind.toLowerCase() + ".bool";
        if(kind.equals("Number"))
            return node.get("value") + ".i32";

        // Math and Boolean Expressions
        if(Utils.isMathExpression(kind))
            return ollirMathBooleanExpression(methodName, node, ".i32");
        if(Utils.isBooleanExpression(kind))
            return ollirMathBooleanExpression(methodName, node, ".bool");

        // Array Access
        if(kind.equals("ArrayAccess")){
            JmmNode accessedNode = node.getChildren().get(0); // Array accessed
            JmmNode indexNode = node.getChildren().get(1); // Array Index
            return ollirArrayAccess(methodName,accessedNode,indexNode);
        }

        // Dot Method
        /*
        if(kind.equals("Dot"))
            return ollirDotMethod(methodName, node);
        */

        // "new" , "int" , "[" , Expression , "]"
        // "new" , Identifier , "(" , ")"
        // "!" , Expression

        // TODO: implement recursive expression subdivision, with auxiliar variables
        return "EXPRESSION";
    }


    // Get the ollir representation of a dot method
    public String ollirDotMethod(String methodName, JmmNode node, String expectedType){
        JmmNode left = node.getChildren().get(0);
        JmmNode right = node.getChildren().get(1); // DotMethod

        if(left.getKind().equals("This"))
            return ollirClassOrSuperMethod(methodName,right,expectedType);

        // Class Object Method
        Type type;
        // Get the object type if it is a known variable
        if((type = getObjectType(methodName, node)) != null){
            // Object from the class
            if(type.getName().equals(symbolTable.getClassName()))
                return ollirClassOrSuperMethod(methodName, right, expectedType);

            // Int Array
            if(type.isArray() && type.getName().equals("int")){

            }
            MyOllirUtils.ollirType(type);
        }

        return "NOT IMPLEMENTED";
    }

    public String ollirClassOrSuperMethod(String methodName, JmmNode dotMethodNode, String expectedReturn){
        JmmNode methodNode = dotMethodNode.getChildren().get(0);
        JmmNode parametersNode = dotMethodNode.getChildren().get(1);

        // Invoked Method
        String invokedMethod,returnType;
        if(methodNode.getKind().equals("Identifier")){
            invokedMethod = methodNode.get("name");

            // Class Method
            if(symbolTable.getMethods().contains(invokedMethod)){
                returnType = MyOllirUtils.ollirType(symbolTable.getReturnType(invokedMethod));
                if(!returnType.equals(expectedReturn)){
                    reports.add(MyOllirUtils.report(dotMethodNode,
                            "Method return type is not the expected."));
                }
            }

            // Super Method
            else returnType = expectedReturn;
        }
        else  // TODO: acho que aqui só pode ser Dot
            return "DOT METHOD LEFT NOT ID";

        // Parameters
        String parameters = "";
        for(int i = 0; i < parametersNode.getNumChildren(); i++){
            JmmNode param = parametersNode.getChildren().get(i);
            parameters += ", ";
            if(param.getNumChildren() > 0){
                String type;
                if(param.getKind().equals("Dot"))
                    type = MyOllirUtils.ollirType(symbolTable.getParameters(invokedMethod).get(i).getType());
                else type = getNodeType(methodName, param);
                sb.append(newAuxiliarVar(type, methodName, param));
                parameters += "t" + auxVarNumber + type;
            }
            else if(isField(param)){
                String type = MyOllirUtils.ollirType(getFieldType(param.get("name")));
                sb.append(newAuxiliarVar(type, methodName, param));
                parameters += "t" + auxVarNumber + type;
            }
            else parameters += ollirExpression(methodName, param);
        }

        return "invokevirtual(this,\"" + invokedMethod + "\"" + parameters + ")" + returnType;
    }

    // Get the ollir expression of an identifier (parameter, field or local variable)
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

    // Get the ollir expression of a boolean or maths expression
    public String ollirMathBooleanExpression(String methodName, JmmNode node, String type){
        JmmNode left = node.getChildren().get(0);
        JmmNode right = node.getChildren().get(1);

        String leftValue, rightValue;
        // Process left node
        if(left.getNumChildren() > 0 || isField(left)){
            sb.append(newAuxiliarVar(type, methodName, left));
            leftValue = "t" + auxVarNumber + type;
        } else leftValue = ollirExpression(methodName,left);

        // Process right node
        if(right.getNumChildren() > 0 || isField(right)){
            sb.append(newAuxiliarVar(type, methodName, right));
            rightValue = "t" + auxVarNumber + type;
        } else rightValue = ollirExpression(methodName, right);

        return leftValue + MyOllirUtils.ollirOperator(node) + rightValue;
    }


    // Get ollir putfield expression
    private String generatePutField(String methodName, String field, JmmNode valueNode){
        String value = ollirExpression(methodName, valueNode);
        return prefix() + "putfield(this," + field + "," + value + ").V;";
    }

    // Get ollir getfield expression
    private String generateGetField(String field, Type type){
        return "getfield(this," + MyOllirUtils.ollirVar(field, type) + ")" + MyOllirUtils.ollirType(type);
    }

    // Create a new auxiliar variable
    private String newAuxiliarVar(String type, String methodName, JmmNode node){
        String value;
        if(node.getKind().equals("Dot"))
            value = ollirDotMethod(methodName, node, type);
        else value = ollirExpression(methodName, node);
        auxVarNumber++;
        return prefix() + "t" + auxVarNumber + type + " :=" + type +" " + value + ";\n";
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

    private boolean isField(JmmNode node){
        return node.getKind().equals("Identifier") && (getFieldType(node.get("name")) != null);
    }

    private Type getParameterType(String methodName, int position){
        if(position == -1) return null;
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
    private Type getObjectType(String methodName, JmmNode node){
        if(node.getKind().equals("Identifier")){
            Type type = getIdentifierType(methodName, node.get("name"));
            if(type == null) return null;

            return type;
        }
        return null; // TODO: deal with dot case
    }

    private Type getIdentifierType(String methodName, String name){
        Type type;
        if((type = getFieldType(name)) == null){
            if((type = getLocalVariableType(methodName, name)) == null){
                type = getParameterType(methodName, getParameterPosition(methodName, name));
            }
        }
        return type;
    }



    private List<JmmNode> getMethodNodes(JmmNode classNode){
        List<JmmNode> methodDeclarations = new ArrayList<>();
        for(int i = 2; i < classNode.getNumChildren(); i++){
            methodDeclarations.add(classNode.getChildren().get(i));
        }
        return methodDeclarations;
    }

    private String getNodeType(String methodName, JmmNode node){
        String kind = node.getKind();

        if(Utils.isMathExpression(kind) || kind.equals("ArrayAccess")) return ".i32";
        if(Utils.isBooleanExpression(kind)) return ".bool";
        if(kind.equals("NewIntArray")) return ".array.i32";
        if(kind.equals("NewObject")) return node.getChildren().get(0).get("name");

        return "ERROR";
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
