package jasmin.methods;

import org.specs.comp.ollir.*;


public class BuildMethod extends JasminMethod {


    public BuildMethod(ClassUnit ollir) {
        super(ollir);
    }

    public String getMethod(int index) {
        Method method = ollir.getMethod(index);
        methodString.append(new BuildMethodScope(ollir, method).getScope());
        methodString.append(new BuildMethodAssigment(ollir,method).getAssigments());
        addEnd();

        return this.toString();
    }


    public void addEnd() {
        methodString.append(".end method");
    }


}
