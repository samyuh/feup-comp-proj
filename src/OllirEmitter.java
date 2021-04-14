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
            visitClassBody(sb);
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
            sb.append(".field private ");
            sb.append(field.getName());
            sb.append(getVarType(field.getType()));
            sb.append(";\n");
        }
    }

    private void visitClassBody(StringBuilder sb){
        // TODO
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
            default:
                typeStr += "." + type.getName();
                break;
        }
        return typeStr;
    }
}
