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
        switch (accessModifier) {
            case PUBLIC:
                return "public";
            case PRIVATE:
                return "private";
            case DEFAULT:
                return "private";
            case PROTECTED:
                return "protected";
            default:
                return "";
        }
    }


    @Override
    public String toString() {
        return directiveString.toString();
    }

}
