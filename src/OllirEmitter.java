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

    public OllirEmitter(SymbolTable symbolTable){
        this.symbolTable = symbolTable;
        reports = new ArrayList<>();
    }

    public List<Report> getReports() {
        return reports;
    }

    /**
     * @param node the Program root node
     * */
    public String visit(JmmNode node){
        StringBuilder sb = new StringBuilder();
        boolean hasImports = node.getNumChildren() == 2;

        if(hasImports)
            //visitImports(sb);
        sb.append(symbolTable.getClassName() + "{\n");
        visitFields(sb);
        classContructor(sb);

        JmmNode classNode = hasImports ? node.getChildren().get(1) : node.getChildren().get(1);
        // Check if the class has methods
        if(classNode.getNumChildren() > 2){
            for(JmmNode methodNode: getMethodNodes(classNode)){
                // Visit each method of the class
                visitMethod(methodNode.getChildren().get(0), sb);
            }
        }

        sb.append("}");
        return sb.toString();
    }

    private void visitImports(StringBuilder sb){
        // Loop through import names
        for(String importName: symbolTable.getImports()){
            // TODO
        }
    }

    private void visitFields(StringBuilder sb){
        for(Symbol field : symbolTable.getFields()){
            sb.append("\t.field private ");
            sb.append(field.getName());
            sb.append(getVarType(field.getType()));
            sb.append(";\n");
        }
    }

    private void classContructor(StringBuilder sb){
        sb.append("\t.construct ");
        sb.append(symbolTable.getClassName());
        sb.append("().V {\n");
        sb.append("\t\tinvokespecial(this, \"<init>\").V;\n\t}\n");
    }

    private void visitMethod(JmmNode methodNode, StringBuilder sb){
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
        // visitVarDeclarations();
        // visitStatements();
        sb.append("\t}\n");
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
