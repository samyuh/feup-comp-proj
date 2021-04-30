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
    static int ifElseLabelNum;
    static int whileNum;
    static int indent;
    SymbolTable symbolTable;
    List<Report> reports;
    StringBuilder sb;

    public OllirEmitter(SymbolTable symbolTable){
        this.symbolTable = symbolTable;
        reports = new ArrayList<>();
        sb = new StringBuilder();
        auxVarNumber = 0;
        ifElseLabelNum = 0;
        whileNum = 0;
        indent = 0;
    }

    /**
     * @param node the Program root node
     * */
    public String visit(JmmNode node){
        boolean hasImports = node.getNumChildren() == 2;

        // Imports
        List<String> stringImports = symbolTable.getImports();
        for(String importName : stringImports) {
            sb.append("import ").append(importName).append(";\n");
        }

        // Extend
        String superClass = symbolTable.getSuper();
        if (superClass != null) {
            sb.append(symbolTable.getClassName()).append(" extends ").append(superClass).append(" {\n");
        } else
            sb.append(symbolTable.getClassName()).append(" {\n");

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
        statements.remove(0);
        visitStatements(methodName, statements);

        // Return Statement
        if(!methodName.equals("main")){
            JmmNode returnValue = methodNode.getChildren().get(bodyIdx+1).getChildren().get(0);
            String returnStr;

            if(returnValue.getNumChildren() > 0 || isField(returnValue)){
                sb.append(newAuxiliarVar(returnTypeStr,methodName,returnValue));
                returnStr = "t" + auxVarNumber + returnTypeStr;
            } else returnStr = ollirExpression(methodName,returnValue);

            sb.append(prefix()).append("ret").append(returnTypeStr).append(" ").append(returnStr).append(";\n");
        } else sb.append(prefix()).append("ret.V;\n");

        indent = 1;
        sb.append(prefix()).append("}\n");
    }

    private void visitStatements(String methodName, List<JmmNode> statements){
        for (JmmNode statement : statements) {
            switch (statement.getKind()) {
                case ("Assignment"):
                    assignmentStatement(methodName, statement);
                    break;
                case ("IfElse"):
                    ifElseLabelNum++;
                    ifElseStatement(methodName, statement);
                    break;
                case ("WhileStatment"):
                    whileNum++;
                    whileStatement(methodName, statement);
                    break;
                default:
                    // Simple Expression
                    sb.append(ollirExpression(methodName, statement)).append(";");
                    break;
            }
            sb.append("\n");
        }
    }

    private void whileStatement(String methodName, JmmNode statement) {
        String valueNum = "" + whileNum;
        JmmNode conditionNode = statement.getChildren().get(0);
        JmmNode bodyBlock = statement.getChildren().get(1);

        int prevIndent = indent;
        sb.append(prefix()).append("Loop" + valueNum + ":\n");
        indent++;
        String condition = buildCondition(methodName, conditionNode);
        sb.append(prefix()).append("if (" + condition + ") goto Body" + valueNum + ";\n");
        sb.append(prefix()).append("goto EndLoop" + valueNum + ";\n");
        indent = prevIndent;
        sb.append(prefix()).append("Body" + valueNum + ":\n");
        indent++;
        visitStatements(methodName, bodyBlock.getChildren());
        sb.append(prefix()).append("goto Loop" + valueNum + ";\n");
        indent = prevIndent;
        sb.append(prefix()).append("EndLoop" + valueNum + ":");
        indent++;
    }

    private void ifElseStatement(String methodName, JmmNode statement) {
        String valueNum = "" + ifElseLabelNum;
        JmmNode conditionNode = statement.getChildren().get(0);
        JmmNode ifBlock = statement.getChildren().get(1);
        JmmNode elseBlock = statement.getChildren().get(2);

        String condition = buildCondition(methodName, conditionNode);
        String ifElseString = "if (" + condition + ") " +
                "goto then" + ifElseLabelNum + ";\n";
        int prevIndent = indent;
        sb.append(prefix()).append(ifElseString);

        sb.append(prefix()).append("else").append(valueNum).append(":\n");
        indent++;
        visitStatements(methodName, elseBlock.getChildren());
        sb.append(prefix()).append("goto endif").append(valueNum).append(";\n");
        indent = prevIndent;
        sb.append(prefix()).append("then").append(valueNum).append(":\n");
        indent++;
        visitStatements(methodName, ifBlock.getChildren());
        indent = prevIndent;
        sb.append(prefix()).append("endif").append(valueNum).append(":");
        indent++;
    }

    private String buildCondition(String methodName, JmmNode node){
        if(isField(node) || node.getKind().equals("Dot")){
            String type = ".bool";
            sb.append(newAuxiliarVar(type, methodName, node));
            return "t" + auxVarNumber + type + " &&.bool 1.bool";
        }

        String condition = ollirExpression(methodName, node);
        if(!Utils.isBooleanExpression(node.getKind())){
            condition += " &&.bool 1.bool";
        }
        return condition;
    }

    // Convert assignment expression to ollir
    private void assignmentStatement(String methodName, JmmNode statement){
        JmmNode left = statement.getChildren().get(0); // Left side of the assignment
        JmmNode right = statement.getChildren().get(1); // Right side of the assignment

        // Array Assignment
        if(left.getKind().equals("ArrayAssignment")){
            JmmNode arrayId = left.getChildren().get(0); // Array Identifier
            JmmNode indexNode = left.getChildren().get(1).getChildren().get(0); // Array Access Index
            ollirArrayAssignment(methodName, arrayId, indexNode, right);
            return;
        }

        // Identifier Assignment
        String name = left.get("name"); // Identifier Name
        ollirIdentifierAssignment(methodName, name, right);
    }

    private void ollirIdentifierAssignment(String methodName, String name, JmmNode valueNode){
        Type type;

        // LEFT NODE
        // Field
        if((type = getFieldType(name)) != null){
            sb.append(generatePutField(methodName, MyOllirUtils.ollirVar(name, type), type, valueNode));
            return;
        }

        // Parameter
        if((type = getLocalVariableType(methodName, name)) == null){
            int position = getParameterPosition(methodName,name);
            type = getParameterType(methodName, position);
            name = MyOllirUtils.ollirParameter(name,position);
        }

        // Local Variable or Parameter
        String leftSide = MyOllirUtils.ollirVar(name, type);
        String assignmentType = MyOllirUtils.ollirType(type);

        // RIGHT NODE: Assigned value
        String rightSide;
        boolean isNewObject = false;

        String kind = valueNode.getKind();
        switch(kind){
            case "Dot":
                rightSide = ollirDotMethod(methodName, valueNode, assignmentType);
                break;
            case "NewObject":
                rightSide = ollirNewObject(valueNode);
                isNewObject = true;
                break;
            case "NewIntArray":
                rightSide = ollirNewIntArray(methodName, valueNode);
                break;
            default:
                rightSide = ollirExpression(methodName, valueNode);
        }

        sb.append(prefix()).append(leftSide).append(" :=").append(assignmentType);
        sb.append(" ").append(rightSide).append(";");

        if(isNewObject) sb.append("\n").append(ollirInitObject(leftSide));
    }

    private void ollirArrayAssignment(String methodName, JmmNode arrayIdentifier, JmmNode indexNode, JmmNode rightNode){
        String name = arrayIdentifier.get("name"); // Name of the array
        Type type = getIdentifierType(methodName,name); // Type of the array
        String assignmentType = MyOllirUtils.ollirType(type).split("\\.")[2];

        String leftSide = ollirArrayAccess(methodName,arrayIdentifier,indexNode);
        String rightSide = ollirExpression(methodName, rightNode);

        sb.append(prefix()).append(leftSide).append(" :=.").append(assignmentType).append(" ").append(rightSide).append(";");
    }

    // Get ollir representation for an array access
    private String ollirArrayAccess(String methodName, JmmNode arrayNode, JmmNode indexNode){
        String arrayStr;
        String indexValue;
        boolean isStringArray = false;

        // Array accessed
        // Field or Not Identifier
        if(isField(arrayNode) || !arrayNode.getKind().equals("Identifier")){
            sb.append(newAuxiliarVar(".array.i32", methodName, arrayNode));
            arrayStr = "t" + auxVarNumber;
        }
        // Identifier
        else {
            arrayStr = arrayNode.get("name");
            int pos = getParameterPosition(methodName, arrayStr);
            if(pos != -1){
                if(methodName.equals("main") &&
                        MyOllirUtils.ollirType(getParameterType(methodName,pos)).equals(".array.String")){
                     isStringArray = true;
                }
                arrayStr = MyOllirUtils.ollirParameter(arrayStr, pos);
            }
        }

        // Index
        // Field or Not Identifier
        if(isField(indexNode) || !indexNode.getKind().equals("Identifier")){
            String aux = newAuxiliarVar(".i32", methodName, indexNode);
            sb.append(aux);
            indexValue = "t" + auxVarNumber + ".i32";
        }
        // Identifier
        else{
            indexValue = ollirFromIdentifierNode(methodName, indexNode);
        }

        return arrayStr + "[" + indexValue + "]" + (isStringArray ? ".String" : ".i32");
    }


    // Get ollir representation for an expression
    private String ollirExpression(String methodName, JmmNode node){
        String kind = node.getKind();

        // Math and Boolean Expressions
        if(Utils.isMathExpression(kind))
            return ollirMathBooleanExpression(methodName, node, ".i32");
        if(Utils.isBooleanExpression(kind))
            return ollirMathBooleanExpression(methodName, node, ".bool");

        switch(kind){
            case "Identifier":
                return ollirFromIdentifierNode(methodName, node);
            case "True":
                return "1.bool";
            case "False":
                return "0.bool";
            case "Number":
                return node.get("value") + ".i32";
            case "This":
                return "$0.this." + symbolTable.getClassName();
            case "ArrayAccess":
                JmmNode accessedNode = node.getChildren().get(0); // Array accessed
                JmmNode indexNode = node.getChildren().get(1); // Array Index
                return ollirArrayAccess(methodName,accessedNode,indexNode);
            case "Dot":
                return ollirDotMethod(methodName, node, null);
            case "NewObject":
                return ollirNewObject(node);
            case "NewIntArray":
                return ollirNewIntArray(methodName, node);
            default:
                MyOllirUtils.warning(node,"Unknown expression");
                return "INVALID EXPRESSION";
        }
    }

    // Get the ollir representation of a dot method
    public String ollirDotMethod(String methodName, JmmNode node, String expectedType){
        JmmNode left = node.getChildren().get(0);   // Left
        JmmNode right = node.getChildren().get(1); // DotMethod
        Type type;

        // This
        if(left.getKind().equals("This"))
            return ollirClassOrSuperMethod(methodName,left, right, expectedType);

        // Identifier
        if((type = getObjectType(methodName, left)) != null){
            // Object from the current class
            if(type.getName().equals(symbolTable.getClassName()))
                return ollirClassOrSuperMethod(methodName, left, right, expectedType);

            // Array Length
            String aux;
            if(type.isArray() && right.getKind().equals("Length")){
                if(isField(left)){
                    sb.append(newAuxiliarVar(MyOllirUtils.ollirType(type), methodName, left));
                    aux = "t" + auxVarNumber + MyOllirUtils.ollirType(type);
                } else aux = ollirExpression(methodName, left);

                return "arraylength(" + aux + ").i32";
            }

            // Object from other class
            if(type.getName().equals(symbolTable.getSuper()) ||
                    Utils.hasImport(type.getName(), symbolTable)){
                return ollirVirtual(methodName, left, right, expectedType);
            }
        }

        // New Object
        if(left.getKind().equals("NewObject")){
            return ollirVirtual(methodName, left, right, expectedType);
        }

        // Import
        if(left.getKind().equals("Identifier") && Utils.hasImport(left.get("name"), symbolTable))
            return ollirStaticMethod(methodName, left.get("name"), right, expectedType);

        MyOllirUtils.warning(node,"Couldn't parse method call.");
        return "INVALID DOT METHOD CALL";
    }

    public String ollirClassOrSuperMethod(String methodName, JmmNode left, JmmNode dotMethodNode, String expectedReturn){
        JmmNode methodNode = dotMethodNode.getChildren().get(0);
        JmmNode parametersNode = dotMethodNode.getChildren().get(1);
        String leftValue;

        // Left Node
        if(left.getKind().equals("This")){
            leftValue = "this";
        }
        else if(left.getNumChildren() > 0 || isField(left)){
            String type = "." + symbolTable.getClassName();
            sb.append(newAuxiliarVar(type, methodName, left));
            leftValue = "t" + auxVarNumber + type;
        } else leftValue = ollirExpression(methodName,left);

        // Invoked Method
        String invokedMethod, returnType;
        if(methodNode.getKind().equals("Identifier")){
            invokedMethod = methodNode.get("name");

            // Class Method
            if(symbolTable.getMethods().contains(invokedMethod)){
                returnType = MyOllirUtils.ollirType(symbolTable.getReturnType(invokedMethod));
                if(expectedReturn != null && !returnType.equals(expectedReturn)){
                    reports.add(MyOllirUtils.report(dotMethodNode,
                            "Method return type is not the expected."));
                }
            }

            // Super Method
            else{
                if(symbolTable.getSuper() == null)
                    reports.add(MyOllirUtils.report(dotMethodNode,"Calling unknow method."));
                if(expectedReturn == null) returnType = ".V";
                else returnType = expectedReturn;
            }
        }
        else {
            MyOllirUtils.warning(methodNode, "Couldn't parse method call");
            return "Invalid Method Call";
        }

        // Parameters
        String parameters = "";
        for(int i = 0; i < parametersNode.getNumChildren(); i++){
            JmmNode param = parametersNode.getChildren().get(i);
            parameters += ", ";
            if(param.getNumChildren() > 0 || param.getKind().equals("This")){
                String type;

                // Param is a NewObject
                if(param.getKind().equals("NewObject")){
                    type = "." + param.getChildren().get(0).get("name");
                    sb.append(newAuxiliarVar(type, methodName, param));
                    sb.append(ollirInitObject("t" + auxVarNumber + type));
                } else {
                    type = getNodeType(methodName, param);

                    if(type.equals("undefined")){
                        type = MyOllirUtils.ollirType(symbolTable.getParameters(invokedMethod).get(i).getType());
                    }
                    sb.append(newAuxiliarVar(type, methodName, param));
                }

                parameters += "t" + auxVarNumber + type;
            }
            else if(isField(param)){
                String type = MyOllirUtils.ollirType(getFieldType(param.get("name")));
                sb.append(newAuxiliarVar(type, methodName, param));
                parameters += "t" + auxVarNumber + type;
            }
            else parameters += ollirExpression(methodName, param);
        }

        if(expectedReturn == null) sb.append(prefix());
        return "invokevirtual(" + leftValue + ",\"" + invokedMethod + "\"" + parameters + ")" + returnType;
    }

    public String ollirStaticMethod(String methodName, String importName, JmmNode dotMethodNode, String expectedReturn){
        JmmNode methodNode = dotMethodNode.getChildren().get(0);
        JmmNode parametersNode = dotMethodNode.getChildren().get(1);

        String invokedMethod, returnType;
        if(methodNode.getKind().equals("Identifier")){
            invokedMethod = methodNode.get("name");
            if(expectedReturn == null) returnType = ".V";
            else returnType = expectedReturn;
        }
        else {
            MyOllirUtils.warning(methodNode, "Couldn't parse method call");
            return "Invalid Method Call";
        }

        // Parameters
        String parameters = "";
        for(int i = 0; i < parametersNode.getNumChildren(); i++){
            JmmNode param = parametersNode.getChildren().get(i);
            parameters += ", ";

            // Process parameter considering its node type
            // Param is a complex node
            if(param.getNumChildren() > 0 || param.getKind().equals("This")){
                String type;

                // Param is a NewObject
                if(param.getKind().equals("NewObject")){
                    type = "." + param.getChildren().get(0).get("name");
                    sb.append(newAuxiliarVar(type, methodName, param));
                    sb.append(ollirInitObject("t" + auxVarNumber + type));
                }
                else {
                    type = getNodeType(methodName, param);
                    sb.append(newAuxiliarVar(type, methodName, param));
                }
                parameters += "t" + auxVarNumber + type;
            }
            // Param is a field
            else if(isField(param)){
                String type = MyOllirUtils.ollirType(getFieldType(param.get("name")));
                sb.append(newAuxiliarVar(type, methodName, param));
                parameters += "t" + auxVarNumber + type;
            }
            else parameters += ollirExpression(methodName, param);
        }

        if(expectedReturn == null) sb.append(prefix());
        return "invokestatic(" + importName + ", \"" + invokedMethod +"\"" + parameters + ")" + returnType;
    }

    public String ollirVirtual(String methodName, JmmNode left, JmmNode dotMethodNode, String expectedReturn){
        JmmNode methodNode = dotMethodNode.getChildren().get(0);
        JmmNode parametersNode = dotMethodNode.getChildren().get(1);
        String leftType, leftValue;

        // Left Node
        if(left.getKind().equals("NewObject")){
            leftType = "." + left.getChildren().get(0).get("name");
            sb.append(newAuxiliarVar(leftType, methodName, left));
            sb.append(ollirInitObject("t" + auxVarNumber + leftType));
            leftValue = "t" + auxVarNumber + leftType;
        } else if(isField(left)){
            leftType = MyOllirUtils.ollirType(getFieldType(left.get("name")));
            sb.append(newAuxiliarVar(leftType, methodName, left));
            leftValue = "t" + auxVarNumber + leftType;
        } else if (left.getNumChildren() > 0){
            leftType = getNodeType(methodName, left);
            sb.append(newAuxiliarVar(leftType, methodName, left));
            leftValue = "t" + auxVarNumber + leftType;
        } else leftValue = ollirExpression(methodName,left);

        // Right node
        String invokedMethod, returnType;
        if(methodNode.getKind().equals("Identifier")){
            invokedMethod = methodNode.get("name");
            if(expectedReturn == null) returnType = ".V";
            else returnType = expectedReturn;
        }
        else {
            MyOllirUtils.warning(methodNode, "Couldn't parse method call");
            return "Invalid Method Call";
        }


        // Parameters
        String parameters = "";
        for(int i = 0; i < parametersNode.getNumChildren(); i++){
            JmmNode param = parametersNode.getChildren().get(i);
            parameters += ", ";

            // Param is a complex node
            if(param.getNumChildren() > 0 || param.getKind().equals("This")){
                String type;

                // Param is a NewObject
                if(param.getKind().equals("NewObject")){
                    type = "." + param.getChildren().get(0).get("name");
                    sb.append(newAuxiliarVar(type, methodName, param));
                    sb.append(ollirInitObject("t" + auxVarNumber + type));
                }
                else {
                    type = getNodeType(methodName, param);
                    sb.append(newAuxiliarVar(type, methodName, param));
                }
                parameters += "t" + auxVarNumber + type;
            }
            // Param is a field
            else if(isField(param)){
                String type = MyOllirUtils.ollirType(getFieldType(param.get("name")));
                sb.append(newAuxiliarVar(type, methodName, param));
                parameters += "t" + auxVarNumber + type;
            }
            else parameters += ollirExpression(methodName, param);
        }

        if(expectedReturn == null) sb.append(prefix());
        return "invokevirtual(" + leftValue + ", \"" + invokedMethod +"\"" + parameters + ")" + returnType;
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
        String operandType = node.getKind().equals("Less") ? ".i32" : type;

        String leftValue, rightValue;
        // Process left node
        if(left.getNumChildren() > 0 || isField(left)){
            sb.append(newAuxiliarVar(operandType, methodName, left));
            leftValue = "t" + auxVarNumber + operandType;
        } else leftValue = ollirExpression(methodName,left);

        if(node.getKind().equals("Not")){
            rightValue = leftValue;
        }
        else{
            // Process right node
            JmmNode right = node.getChildren().get(1);
            if(right.getNumChildren() > 0 || isField(right)){
                sb.append(newAuxiliarVar(operandType, methodName, right));
                rightValue = "t" + auxVarNumber + operandType;
            } else rightValue = ollirExpression(methodName, right);
        }

        return leftValue + MyOllirUtils.ollirOperator(node) + rightValue;
    }

    // Get the ollir representation for an object initialization
    public String ollirInitObject(String var){
        return prefix() + "invokespecial(" + var + ",\"<init>\").V;\n";
    }

    public String ollirNewObject(JmmNode node){
        String className = node.getChildren().get(0).get("name");
        return "new("+ className + ")"+ "." + className;
    }

    //Get ollir representation for a new int array
    public String ollirNewIntArray(String methodName, JmmNode node){
        JmmNode sizeNode = node.getChildren().get(0);
        String size;
        if(isField(sizeNode) || !sizeNode.getKind().equals("Identifier")){
            String aux = newAuxiliarVar(".i32", methodName, sizeNode);
            sb.append(aux);
            size = "t" + auxVarNumber + ".i32";
        } else size = ollirFromIdentifierNode(methodName, sizeNode);

        return "new(array," + size + ").array.i32";
    }


    // Get ollir putfield expression
    private String generatePutField(String methodName, String fieldNameAndType, Type type, JmmNode valueNode){
        String value;
        if(valueNode.getKind().equals("NewObject")){
            String className = valueNode.getChildren().get(0).get("name");
            sb.append(newAuxiliarVar("." + className, methodName, valueNode));
            value = "t" + auxVarNumber + "." + className;
            sb.append(ollirInitObject(value));
        }
        else if(valueNode.getNumChildren() > 0){
            String typeStr = MyOllirUtils.ollirType(type);
            sb.append(newAuxiliarVar(typeStr, methodName, valueNode));
            value = "t" + auxVarNumber + typeStr;
        } else value = ollirExpression(methodName, valueNode);

        return prefix() + "putfield(this," + fieldNameAndType + "," + value + ").V;";
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


    // Get the type of a local variable
    private Type getLocalVariableType(String methodName, String name){
        for (Symbol symb: symbolTable.getLocalVariables(methodName)){
            if (name.equals(symb.getName()))
                return symb.getType();
        }
        return null;
    }

    // Get the type of a field
    private Type getFieldType(String name){
        for (Symbol symb: symbolTable.getFields()){
            if (name.equals(symb.getName()))
                return symb.getType();
        }
        return null;
    }

    // Check if a node is a field
    private boolean isField(JmmNode node){
        return node.getKind().equals("Identifier") && (getFieldType(node.get("name")) != null);
    }

    // Get the type of a parameter
    private Type getParameterType(String methodName, int position){
        if(position == -1) return null;
        return symbolTable.getParameters(methodName).get(position-1).getType();
    }

    // Get the position of a parameter
    private int getParameterPosition(String method, String parameter){
        List<Symbol> parameters = symbolTable.getParameters(method);

        for (int i = 0; i < parameters.size();i++){
            if (parameter.equals(parameters.get(i).getName()))
                return i + 1;
        }
        return -1;
    }

    // Get the type of an object
    private Type getObjectType(String methodName, JmmNode node){
        if(!node.getKind().equals("Identifier")) return null;

        Type type = getIdentifierType(methodName, node.get("name"));
        return type;
    }

    // Get the type of an identifier
    private Type getIdentifierType(String methodName, String name){
        Type type;
        if((type = getFieldType(name)) == null){
            if((type = getLocalVariableType(methodName, name)) == null)
                return getParameterType(methodName, getParameterPosition(methodName, name));
        }
        return type;
    }

    // Get the type of a none identifier node
    private String getNodeType(String methodName, JmmNode node){
        String kind = node.getKind();

        if(Utils.isMathExpression(kind)) return ".i32";
        if(Utils.isBooleanExpression(kind)) return ".bool";

        switch (kind){
            case "This":
                return "." + symbolTable.getClassName();
            case "NewIntArray":
                return ".array.i32";
            case "NewObject":
                return node.getChildren().get(0).get("name");
            case "ArrayAccess":
                if(methodName.equals("main") && node.getChildren().get(0).getKind().equals("Identifier")){
                    String name = node.getChildren().get(0).get("name");
                    int pos;
                    if((pos = getParameterPosition(methodName, name)) != -1){
                        String type = MyOllirUtils.ollirType(getParameterType(methodName,pos));
                        return type.equals(".array.i32") ? ".i32" : ".String";
                    }
                }
                return ".i32";
            case "Dot":
                JmmNode methodNode = node.getChildren().get(1);
                if(methodNode.getKind().equals("Length")) return ".i32";
                JmmNode methodIdentifier = methodNode.getChildren().get(0);
                if(symbolTable.getMethods().contains(methodIdentifier.get("name")))
                    return MyOllirUtils.ollirType(symbolTable.getReturnType(methodIdentifier.get("name")));
                return "undefined";
            default:
                return "undefined";
        }
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
