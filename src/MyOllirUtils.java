import pt.up.fe.comp.jmm.JmmNode;
import pt.up.fe.comp.jmm.analysis.table.Type;
import pt.up.fe.comp.jmm.report.Report;
import pt.up.fe.comp.jmm.report.ReportType;
import pt.up.fe.comp.jmm.report.Stage;

public class MyOllirUtils {

    public static String sanitizerType(String typeName) {

        if(typeName.contains(".i32") || typeName.contains(".String") || typeName.contains(".bool")) {
            return typeName;
        }

        return sanitizer(typeName);
    }

    public static String sanitizer(String varName) {
        if (varName.contains("$")) {
            varName = varName.replaceAll("\\$","SPEC_0");
        }
        if (varName.contains("ret")) {
            varName = varName.replaceAll("ret","SPEC_1");
        }
        if (varName.contains("array")) {
            varName = varName.replaceAll("array","SPEC_2");
        }
        if (varName.contains("bool")) {
            varName = varName.replaceAll("bool","SPEC_3");
        }
        if (varName.contains("i32")) {
            varName = varName.replaceAll("i32","SPEC_4");
        }

        return varName;
    }

    public static String ollirType(Type type){
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
                typeStr += MyOllirUtils.sanitizerType("." + type.getName());
                break;
        }
        return typeStr;
    }

    public static String ollirVar(String varName, Type type){
        return varName + ollirType(type);
    }

    public static String ollirParameter(String name, int position){
        return "$" + position + "." + MyOllirUtils.sanitizer(name);
    }

    public static String ollirOperator(JmmNode node){
        switch (node.getKind()){
            case "Add":
                return " +.i32 ";
            case "Less":
                return " <.i32 ";
            case "Sub":
                return " -.i32 ";
            case "Mult":
                return " *.i32 ";
            case "Div":
                return " /.i32 ";
            case "And":
                return " &&.bool ";
            case "Not":
                return " !.bool ";
            default:
                return "ERROR";
        }
    }

    public static Report report(JmmNode node, String message){
        return new Report(ReportType.ERROR, Stage.LLIR,
                Integer.parseInt(node.get("line")), Integer.parseInt(node.get("col")),message);
    }

}
