import pt.up.fe.comp.jmm.JmmNode;
import pt.up.fe.comp.jmm.analysis.table.Type;
import pt.up.fe.comp.jmm.report.Report;
import pt.up.fe.comp.jmm.report.ReportType;
import pt.up.fe.comp.jmm.report.Stage;

public class MyOllirUtils {

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
                typeStr += "." + type.getName();
                break;
        }
        return typeStr;
    }

    public static String ollirVar(String varName, Type type){
        return varName + ollirType(type);
    }

    public static String ollirParameter(String name, int position){
        return "$" + position + "." + name;
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

    public static Report reportOptimization(JmmNode node, String message){
        return new Report(ReportType.ERROR, Stage.OPTIMIZATION,
                Integer.parseInt(node.get("line")), Integer.parseInt(node.get("col")),message);
    }

    public static Report warning(JmmNode node, String message){
        return new Report(ReportType.WARNING, Stage.LLIR,
                Integer.parseInt(node.get("line")), Integer.parseInt(node.get("col")),message);
    }
}
