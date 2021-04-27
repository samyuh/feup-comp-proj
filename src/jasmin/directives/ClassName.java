package jasmin.directives;

import org.specs.comp.ollir.AccessModifiers;
import org.specs.comp.ollir.ClassUnit;

public class ClassName extends JasminDirective {


    public ClassName(ClassUnit ollir){
        super(ollir);
    }

    @Override
    public String getDirective() {
        addClassDirective();

        return this.toString();
    }

    private void addClassDirective(){
        AccessModifiers accessModifiers = ollir.getClassAccessModifier();
        directiveString.append(".class ");
        directiveString.append(this.getAccessModifiers(accessModifiers));
        directiveString.append(" ");
        if (ollir.isStaticClass())
            directiveString.append(" static ");
        if (ollir.isFinalClass())
            directiveString.append(" final ");
        directiveString.append(ollir.getClassName());
        addEndLine();

    }



}
