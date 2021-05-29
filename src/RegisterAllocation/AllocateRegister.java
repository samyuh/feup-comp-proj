package RegisterAllocation;

import org.specs.comp.ollir.ClassUnit;
import org.specs.comp.ollir.Method;
import pt.up.fe.comp.jmm.ollir.OllirResult;

public class AllocateRegister {
    OllirResult ollirResult;
    ClassUnit classUnit;
    public AllocateRegister(OllirResult ollirResult){
        this.ollirResult = ollirResult;
        classUnit = ollirResult.getOllirClass();
    }

    /**
     * This method will build the var table for all the methods.
     */
    public void allocateRegistersClass(){
        for (var method: classUnit.getMethods()) {
            allocateRegisterMethod(method);
        }
    }

    /**
     * This method calls the data analysis and then allocate the registers
     * with graph coloring.
     * @param method Method for the allocation.
     */
    public void allocateRegisterMethod(Method method){
        new DataflowAnalysis(method).build();
    }
}
