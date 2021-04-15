package jasmin;


import jasmin.directives.ClassFields;
import jasmin.directives.ClassName;
import jasmin.methods.BuildMethod;
import org.specs.comp.ollir.ClassUnit;

public class BuildJasmin {
    protected StringBuilder jasminCode;
    protected ClassUnit ollir;

    public BuildJasmin(ClassUnit ollir){
        this.jasminCode = new StringBuilder();
        this.ollir = ollir;
    }

    public String build(){

        jasminCode.append(new ClassName(ollir).getDirective());
        // TODO: super directives
        // TODO: implements directives
        jasminCode.append(new ClassFields(ollir).getDirective());
        jasminCode.append(new BuildMethod(ollir).getMethod(1));
        return jasminCode.toString();
    }




}
