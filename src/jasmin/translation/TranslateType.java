package jasmin.translation;


import org.specs.comp.ollir.ArrayType;
import org.specs.comp.ollir.ClassType;
import org.specs.comp.ollir.ElementType;
import org.specs.comp.ollir.Type;

public class TranslateType {


    public static String getJasminType(Type type){

        if (type.getTypeOfElement() == ElementType.ARRAYREF)
            return getJasminTypeArray((ArrayType)type);
        else if (type.getTypeOfElement() == ElementType.CLASS) {
            System.out.println("TO IMPLEMENT");
            return "";
        }

        return getJasminTypeVar(type, type.getTypeOfElement());

    }
    public static String getJasminTypeArray(ArrayType type){
        StringBuilder translation = new StringBuilder();

        translation.append("[".repeat(Math.max(0, type.getNumDimensions())));

        //TODO: fix when the class is from an import
        translation.append(getJasminTypeVar(type, type.getTypeOfElements()));
        System.out.println(type.getTypeOfElements());
        return translation.toString();

    }

    public static String getJasminTypeVar(Type type, ElementType elementType){
        switch(elementType){
            case INT32:
                return "I";
            case BOOLEAN:
                return "Z";
            case STRING:
                return "Ljava/lang/String;";
            case CLASS:
                return ((ClassType)type).getName();
            case VOID:
                return "V";

        }
        return "";
    }



}
