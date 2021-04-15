package jasmin.methods;

import org.specs.comp.ollir.*;


public class BuildMethod extends JasminMethod {


    public BuildMethod(ClassUnit ollir) {
        super(ollir);
    }

    public String getMethod(int index) {
        Method method = ollir.getMethod(index);
        methodString.append(new BuildMethodScope(ollir, method).getScope());
        for (Instruction inst: method.getInstructions())
            methodString.append(getInstruction(inst, method));
        addEnd();

        return this.toString();
    }

    public String getInstruction(Instruction inst, Method method){
        System.out.println(inst.toString());
        switch (inst.getInstType()){
            case ASSIGN:
                return new BuildMethodAssigment(ollir, method).getInstructionAssign((AssignInstruction) inst);

        }
        return "";
    }

    public void addEnd() {
        methodString.append(".end method");
    }


}
