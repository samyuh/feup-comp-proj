package jasmin.methods;

import org.specs.comp.ollir.AccessModifiers;
import org.specs.comp.ollir.ClassUnit;
import org.specs.comp.ollir.Method;

public abstract class JasminMethod {
    ClassUnit ollir;
    StringBuilder methodString;

    public JasminMethod(ClassUnit ollir){
        this.ollir = ollir;
        this.methodString = new StringBuilder();
    }

    protected String getAccessModifiers(AccessModifiers accessModifier, Method method){
        return switch (accessModifier) {
            case PUBLIC -> "public";
            case PRIVATE -> "private";
            case DEFAULT -> method.isConstructMethod() ? "public" : "private";
            case PROTECTED -> "protected";
        };
    }

    public void addEndLine(){
        methodString.append("\n");
    }

    @Override
    public String toString() {
        return methodString.toString();
    }
}
