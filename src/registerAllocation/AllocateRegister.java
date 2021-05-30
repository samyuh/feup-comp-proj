package registerAllocation;

import org.specs.comp.ollir.ClassUnit;
import org.specs.comp.ollir.Method;
import pt.up.fe.comp.jmm.ollir.OllirResult;
import registerAllocation.coloring.GraphColoring;
import registerAllocation.coloring.InterferenceGraph;
import registerAllocation.dataflow.DataflowAnalysis;

import java.util.ArrayList;
import java.util.HashMap;

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
        DataflowAnalysis dataflowAnalysis = new DataflowAnalysis(method);
        dataflowAnalysis.build();
        HashMap<String, ArrayList<String>> analysisInterference = dataflowAnalysis.getInterference();
        InterferenceGraph interferenceGraph = new InterferenceGraph(analysisInterference);
        GraphColoring graphColoring = new GraphColoring(3, interferenceGraph);
        graphColoring.buildStack();
        System.out.println("REGISTER");
        graphColoring.printStack();

    }
}
