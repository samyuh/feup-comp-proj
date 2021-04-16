package jasmin.translation;


import org.specs.comp.ollir.*;

public class TranslateType {

    public static String getJasminType(Type type, Operand operand) {

        switch (type.getTypeOfElement()) {
            case OBJECTREF:
                return operand.getName();
            default:
               return getJasminType(type);
        }
    }

    public static String getJasminType(Type type) {
        switch (type.getTypeOfElement()) {
            case ARRAYREF:
                return getJasminTypeArray((ArrayType) type);
            case CLASS:
                System.out.println("To implement");
                return "";
            default:
                return getJasminTypeVar(type, type.getTypeOfElement());
        }


    }

    public static String getJasminTypeArray(ArrayType type) {
        StringBuilder translation = new StringBuilder();

        translation.append("[".repeat(Math.max(0, type.getNumDimensions())));

        //TODO: fix when the class is from an import
        translation.append(getJasminTypeVar(type, type.getTypeOfElements()));
        return translation.toString();

    }

    public static String getJasminTypeVar(Type type, ElementType elementType) {
        switch (elementType) {
            case INT32:
                return "I";
            case BOOLEAN:
                return "Z";
            case STRING:
                return "Ljava/lang/String;";
            case CLASS:
                return ((ClassType) type).getName();
            case VOID:
                return "V";

        }
        return "";
    }


}
