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

        sb.append(symbolTable.getClassName() + "{\n");
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

    /*
        Note: The elements of the OLLIR that start with a ‘$’ are parameters of the method and
        the number following the ‘$’ identifies the position of the parameter in the method signature
         from 0 to N-1(N is the number of parameters of the method) for static methods,
         and from 1 to N for the other methods (in this later case the parameter 0 is the this).
     */
    private void visitStatements(String methodName, List<JmmNode> statements){
        for(int i = 1; i < statements.size(); i++){
            switch (statements.get(i).getKind()){
                case("Dot"):
                    dotStatement(methodName, statements.get(i));
                    break;
                case("IfElse"):
                    ifElseStatement(methodName, statements.get(i));
                    break;
                case("WhileStatment"):
                    break;
                //...
                default:
                    break;
            }
        }
    }

    private void dotStatement(String methodName, JmmNode statement){
        JmmNode beforeDot = statement.getChildren().get(0);
        JmmNode afterDot = statement.getChildren().get(1);

        if(symbolTable.getImports().contains(beforeDot.get("name").equals("this"))){
            sb.append("invokevirtual(this, \"" + afterDot.get("name") + ",\"");
            // Get parameters
        }
    }

    private void ifElseStatement(String methodName, JmmNode statement){
        JmmNode condition = statement.getChildren().get(0);
        JmmNode ifBody = statement.getChildren().get(1);
        JmmNode elseBody = statement.getChildren().get(2);
        /*
        sb.append("if(");
        switch (condition.getKind()){
            case "Less":
                sb.append(">=");
            case "And":
        }
        sb.append(") goto else;\n");
        // ifbody here
        sb.append("goto endif;");
        sb.append("else:\n");
        // elsebody here
        sb.append("endif:\n");
        */

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

    private List<JmmNode> getMethodNodes(JmmNode classNode){
        List<JmmNode> methodDeclarations = new ArrayList<>();
        for(int i = 2; i < classNode.getNumChildren(); i++){
            methodDeclarations.add(classNode.getChildren().get(i));
        }
        return methodDeclarations;
    }
}
