package jasmin;

import jasmin.methods.BuildMethod;
import jasmin.translation.TranslateType;
import org.specs.comp.ollir.*;


public class InstSingleton  {
    public static String extend;

    public static String istore(int reg){
        if (reg > 3 || reg < 0)
            return "istore " + reg + "\n";
        return "istore_" + reg + "\n";
    }

    public static String store2(int number){
       // if(1);
        return "istore";
    }
    public static String astore(int reg){
        if (reg > 3 || reg < 0)
            return "astore " + reg + "\n";
        return "astore_" + reg + "\n";
    }

    public static String iload(int reg){
        if (reg > 3 || reg < 0)
            return "iload " + reg + "\n";
        return "iload_" + reg + "\n";
    }

    public static String iconst(String number){
        int constant = Integer.parseInt(number);
        if (constant == -1)
            return "iconst_m1\n";
        if (constant <= 5 && constant >= -1)
            return "iconst_" + number + "\n";
        if (constant <= 127 && constant >= -128)
            return "bipush " + number + "\n";
        if (constant >= -32768 && constant <= 32767)
            return "sipush " + number + "\n";
        return "ldc " + number + "\n";
    }


    public static String aload(int reg){
        if (reg > 3 || reg < 0)
            return "aload " + reg + "\n";
        return "aload_"+ reg + "\n";
    }

    public static String label(String label){
        return label + ":" + "\n";
    }

    public static String anewarray(int regVar, String type){
        return iload(regVar) + "newarray " + "int" + "\n";

    }

    public static String gotoInst(String label){
        return "goto " + label + "\n";
    }

    public static String newCall(String className){
        return "new " + className + "\n";
    }

    public static String getfield(String className, String fieldName, String type){
        return "getfield " + className + "/" + fieldName + " " + type + "\n";
    }

    public static String putfield(String className, String fieldName, String type){
        return "putfield " + className + "/" + fieldName + " " + type + "\n";
    }



    public static String getOp(OperationType opType){
        switch (opType) {
            case MUL:
                return "imul \n";
            case ADD:
                return "iadd \n";
            case SUB:
                return "isub \n";
            case DIV:
                return "idiv \n";
            default:
                return opType.name();
        }
    }

    public static String iastore(){
        return "iastore" + "\n";
    }

    public static String getAccessArrayVar(int regArray, int regVar){
       return  aload(regArray) + iload(regVar) +  "iaload" + "\n";
    }

    public static String getStoreArrayVar(int regArray, int regVar, String rhs){
        return   aload(regArray) + iload(regVar) + rhs + iastore();
    }

    public static String getArrayLength(int arrayReg){
        return aload(arrayReg) + "arraylength \n";
    }

}
