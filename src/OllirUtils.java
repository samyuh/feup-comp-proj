import pt.up.fe.comp.jmm.analysis.table.Type;

public class OllirUtils {

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

}
