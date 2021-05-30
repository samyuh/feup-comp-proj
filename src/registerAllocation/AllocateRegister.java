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
    int k;
    public AllocateRegister(OllirResult ollirResult, int k){
        this.ollirResult = ollirResult;
        classUnit = ollirResult.getOllirClass();
        this.k = k;
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
    public Boolean allocateRegisterMethod(Method method){
        DataflowAnalysis dataflowAnalysis = new DataflowAnalysis(method);
        dataflowAnalysis.build();
        HashMap<String, ArrayList<String>> analysisInterference = dataflowAnalysis.getInterference();
        InterferenceGraph interferenceGraph = new InterferenceGraph(analysisInterference);
        GraphColoring graphColoring = new GraphColoring(k, interferenceGraph);
        graphColoring.buildStack();
        if (!graphColoring.coloring())
            return false;

        var varTable = method.getVarTable();
        for (var node: interferenceGraph.getNodeList())
            varTable.get(node.getValue()).setVirtualReg(node.getRegister());

        return true;
    }
}
