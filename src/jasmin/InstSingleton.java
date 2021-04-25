package jasmin;

import jasmin.methods.BuildMethod;
import jasmin.translation.TranslateType;
import org.specs.comp.ollir.*;


public class InstSingleton {

    public static String istore(int reg){
        if (reg > 3)
            return "istore " + reg + "\n";
        return "istore_" + reg + "\n";
    }

    public static String astore(int reg){
        if (reg > 3)
            return "astore " + reg + "\n";
        return "astore_" + reg + "\n";
    }

    public static String iload(int reg){
        if (reg > 3)
            return "iload " + reg + "\n";
        return "iload_" + reg + "\n";
    }

    public static String iconst(String number){
        int constant = Integer.parseInt(number);
        if (constant > 5)
            return "ldc " + number + "\n";
        return "const_" + number + "\n";
    }

    public static String aload(int reg){
        if (reg > 3)
            return "aload " + reg + "\n";
        return "aload_"+reg + "\n";
    }

    public static String label(String label){
        return label + ":" + "\n";
    }

    public static String anewarray(int regVar, String type){
        return iload(regVar) + "anewarray " + type + "\n";

    }

    public static String newCall(String className){
        return "new " + className + "\n";
    }

    public static String getfield(int classReg, Type type, String fieldName){
        return aload(classReg) + "getfield " + TranslateType.getJasminType(type) + " " + fieldName + "\n";
    }

    public static String putfield(String className, String varName, String type){
        return "putfield " + className + "/" + varName + " " + type + "\n";
    }

    public static String getOp(OperationType opType){
        return switch (opType) {
            case MUL -> "imul \n";
            case ADD -> "iadd \n";
            case SUB -> "isub \n";
            case DIV -> "idiv \n";
            default -> opType.name();
        };
    }



    public static String getAccessArrayVar(int regArray, int regVar){
       return aload(regArray) + iload(regVar);
    }

    public static String getStoreArrayVar(int regArray, int regVar){
        return aload(regArray) + iload(regVar) + "iastore" + "\n";
    }

    public static String getArrayLength(int arrayReg){
        return aload(arrayReg) + "arraylength \n";
    }


}
