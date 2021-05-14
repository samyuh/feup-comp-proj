package jasmin.directives;

import jasmin.translation.TranslateType;
import org.specs.comp.ollir.ClassUnit;
import org.specs.comp.ollir.Field;

public class ClassFields extends JasminDirective {
    Field field;
    public ClassFields(ClassUnit ollir){
        super(ollir);
    }

    @Override
    public String getDirective() {
        int numFields = ollir.getNumFields();

        for (int i = 0 ; i < numFields; i++){
           buildField(i);
        }
        return this.toString();
    }

    public void buildField(int index){
        this.field = ollir.getField(index);

        directiveString.append(".field ");
        addAccessSpec();
        // <field-name>
        directiveString.append(field.getFieldName()).append(" ");
        // <description>
        directiveString.append(TranslateType.getJasminType(field.getFieldType())).append(" ");
        addValue();
        directiveString.append("\n");
    }

    public void addAccessSpec(){
        String accessModifiers = this.getAccessModifiers(field.getFieldAccessModifier());
        directiveString.append(accessModifiers);
        directiveString.append(" ");
        if (ollir.isStaticClass())
            directiveString.append("static");
        if (ollir.isFinalClass())
            directiveString.append("final");
    }

    public void addValue(){
        if (field.isInitialized()){
            directiveString.append(" = ");
            directiveString.append(field.getInitialValue());
        }
    }
}
