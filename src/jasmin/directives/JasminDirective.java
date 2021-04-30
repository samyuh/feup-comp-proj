package jasmin.directives;

import org.specs.comp.ollir.AccessModifiers;
import org.specs.comp.ollir.ClassUnit;

public abstract class JasminDirective {
    ClassUnit ollir;
    StringBuilder directiveString;

    public JasminDirective(ClassUnit ollir){
        this.ollir = ollir;
        this.directiveString = new StringBuilder();
    }

    public abstract String getDirective();

    protected String getAccessModifiers(AccessModifiers accessModifier){
        return switch (accessModifier) {
            case PUBLIC -> "public";
            case PRIVATE -> "private";
            case DEFAULT -> "private";
            case PROTECTED -> "protected";
        };
    }


    @Override
    public String toString() {
        return directiveString.toString();
    }

    public void addEndLine(){
        directiveString.append("\n");
    }
}
