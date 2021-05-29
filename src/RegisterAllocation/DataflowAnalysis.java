package RegisterAllocation;

import org.specs.comp.ollir.Method;
import org.specs.comp.ollir.Node;
import org.specs.comp.ollir.NodeType;

import java.util.ArrayList;

/**
 * This method is responsible for building the DataAnalysis and
 * return the live range.
 */
public class DataflowAnalysis {
    Method method;
    ArrayList<ArrayList<String>> use;

    public DataflowAnalysis(Method method){
        this.method = method;
        this.method.buildCFG();
        this.use = new ArrayList<>();
    }

    public void build(){
        buildUse();
    }

    public void buildUse(){
        Node currentInst = method.getBeginNode();
        while(currentInst.getNodeType() != NodeType.END){
            currentInst.showNode();
            currentInst = currentInst.getSucc1();
        }
    }
}
