package jasmin;


import jasmin.directives.ClassFields;
import jasmin.methods.BuildMethod;
import org.specs.comp.ollir.ClassUnit;

public class BuildJasmin {
    protected StringBuilder jasminCode;
    public static ClassUnit ollir;

    public BuildJasmin(ClassUnit ollir){
        this.jasminCode = new StringBuilder();
        BuildJasmin.ollir = ollir;
    }

    public String build(){

        addClassName();
        addClassSuper();
        // TODO: implements directives
        jasminCode.append(new ClassFields(ollir).getDirective());
        jasminCode.append("\n");
        for (int i = 0 ; i < ollir.getNumMethods(); i++)
            jasminCode.append(new BuildMethod(ollir).getMethod(i)).append("\n");
        return jasminCode.toString();
    }


    private void addClassName(){
        jasminCode.append(".class ");
        jasminCode.append(ollir.getClassName());
        jasminCode.append("\n");
    }

    private void addClassSuper(){
        if (ollir.getSuperClass() != null)
            InstSingleton.extend = ollir.getSuperClass();
        else
            InstSingleton.extend = "java/lang/Object";

        jasminCode.append(".super ").append(InstSingleton.extend).append("\n");
    }

}
