package registerAllocation.dataflow;

import org.specs.comp.ollir.*;

import java.lang.reflect.Array;
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

    String[][] in;
    String[][] out;

    HashSet<String> variables;

    public DataflowAnalysis(Method method) {
        this.method = method;
        this.method.buildCFG();
        this.use = new String[method.getInstructions().size()][];
        this.def = new String[method.getInstructions().size()][];
        this.next = new Integer[method.getInstructions().size()][];
        this.variables = new HashSet<>();

        this.in = new String[method.getInstructions().size()][];
        this.out = new String[method.getInstructions().size()][];
    }

    public void build() {
        prepareDataflowAnalysis(method.getBeginNode().getSucc1());
        processDataflow();
        Utils.printMatrix(out);
        // TODO: calculate live range.
        // TODO: build interception graph.
        // TODO: coloring graph.
    }
    // BUILD -------------------------------------------------------------

    /**
     * This method is responsible for building the
     * next, in, out, def and use parameters for the
     * Dataflow Analysis
     */
    public void prepareDataflowAnalysis(Node node) {
        if (node == null || node.getNodeType() == NodeType.END || next[node.getId() - 1] != null)
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
            else {
                def[index] = new String[]{dest.getName()};
                variables.add(dest.getName());
            }
        }

    }

    /**
     * Stores the used variable in the array structure.
     */
    public void storeUsed(Node node) {
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

    // PROCESS ------------------------------------------------------------
    public void processDataflow() {
        String[][] previousOut;
        String[][] previousIn;

        do {
            previousOut = Utils.deepCopyMatrix(out);
            previousIn = Utils.deepCopyMatrix(in);
            for (int i = use.length - 1; i >= 0; i--) {
                // Remove null elements.
                if (out[i] == null) out[i] = new String[]{};
                if (in[i] == null) in[i] = new String[]{};

                out[i] = getOut(i);
                in[i] = getIn(i);
            }
        } while (!Utils.compareMatrix(out, previousOut) || !Utils.compareMatrix(in, previousIn));
    }

    public String[] getOut(int index) {
        HashSet<String> out = new HashSet<>();
        for (int i = 0; i < next[index].length; i++) {
            int instId = next[index][i];
            if (instId < 0) continue;
            if (in[instId] == null) in[instId] = new String[]{};
            out.addAll(Arrays.asList(in[instId]));
        }
        return out.toArray(new String[0]);
    }

    public String[] getIn(int index) {
        HashSet<String> in = new HashSet<>(Arrays.asList(this.out[index]));
        in.removeAll(Arrays.asList(this.def[index]));
        in.addAll(Arrays.asList(this.use[index]));
        return in.toArray(new String[0]);
    }

}
