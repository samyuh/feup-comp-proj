package registerAllocation.dataflow;

import org.specs.comp.ollir.*;

import java.util.*;

/**
 * This method is responsible for building the DataAnalysis and
 * return the live range.
 */
public class DataflowAnalysis {
    Method method;
    String[][] use;
    String[][] def;
    Integer[][] next;

    public DataflowAnalysis(Method method) {
        this.method = method;
        this.method.buildCFG();
        this.use = new String[method.getInstructions().size()][];
        this.def = new String[method.getInstructions().size()][];
        this.next = new Integer[method.getInstructions().size()][];
        method.show();
    }

    public void build() {
        //prepareDataflowAnalysis();
        prepareDataflowAnalysis(method.getBeginNode().getSucc1());
        printMatrix(use);
    }

    /**
     * This method is responsible for building the
     * next, in, out, def and use parameters for the
     * Dataflow Analysis
     */
    public void prepareDataflowAnalysis(Node node) {
        if (node == null || node.getNodeType() == NodeType.END || next[node.getId() -1] != null)
            return;

        storeDefined(node);
        storeNext(node);
        storeUsed(node);
        prepareDataflowAnalysis(node.getSucc1());
        prepareDataflowAnalysis(node.getSucc2());
    }


    /**
     * Stores the defined variable in the def instruction.
     */
    public void storeDefined(Node node) {
        int index = node.getId() - 1;
        if (method.getInstr(index).getInstType() != InstructionType.ASSIGN) {
            def[index] = new String[]{};
        } else {
            AssignInstruction instruction = (AssignInstruction) method.getInstr(index);
            Operand dest = (Operand) instruction.getDest();
            if (dest instanceof ArrayOperand)
                def[index] = new String[]{};
            else
                def[index] = new String[]{dest.getName()};
        }

    }

    /**
     * Stores the used variable in the array structure.
     */
    public void storeUsed(Node node){
        int index = node.getId() - 1;
        Instruction instruction = method.getInstr(index);
        String[] usedVariables = new UsedVariables(instruction).getUsed();
        Set<String> set = new HashSet<>(Arrays.asList(usedVariables));  // Remove repeated instances.
        use[index] = set.toArray(new String[0]);
    }

    /**
     * Stores the next instruction for the current node.
     */
    public void storeNext(Node node) {
        List<Integer> nextNodes = new ArrayList<>();

        Node nextNode1 = node.getSucc1();
        Node nextNode2 = node.getSucc2();

        if (nextNode1 != null) {
            nextNodes.add(nextNode1.getId() - 1);
        }
        if (nextNode2 != null)
            nextNodes.add(nextNode2.getId() - 1);

        next[node.getId() - 1] = nextNodes.toArray(new Integer[0]);
    }

    /**
     * Helper function to print a matrix.
     *
     * @param mat Matrix.
     */
    public void printMatrix(Object[][] mat) {
        System.out.print("[");
        for (int i = 0; i < mat.length; i++) {
            System.out.print("[ ");
            for (int j = 0; j < mat[i].length; j++)
                System.out.print(mat[i][j] + " ");
            System.out.print("]");
        }
        System.out.print("]");
    }
}
