package jasmin;


import jasmin.directives.ClassFields;
import jasmin.directives.ClassName;
import jasmin.methods.BuildMethod;
import org.specs.comp.ollir.ClassUnit;
import org.specs.comp.ollir.Method;

public class BuildJasmin {
    protected StringBuilder jasminCode;
    public static ClassUnit ollir;

    public BuildJasmin(ClassUnit ollir){
        this.jasminCode = new StringBuilder();
        BuildJasmin.ollir = ollir;
    }

    public String build(){

        jasminCode.append(new ClassName(ollir).getDirective());
        // TODO: super directives
        // TODO: implements directives
        jasminCode.append(new ClassFields(ollir).getDirective());
        jasminCode.append("\n");
        for (int i = 0 ; i < ollir.getNumMethods(); i++)
            jasminCode.append(new BuildMethod(ollir).getMethod(i)).append("\n");
        return jasminCode.toString();
    }

}
