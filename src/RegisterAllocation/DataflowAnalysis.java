package RegisterAllocation;

import org.specs.comp.ollir.*;

import java.util.ArrayList;

/**
 * This method is responsible for building the DataAnalysis and
 * return the live range.
 */
public class DataflowAnalysis {
    Method method;
    String[][] use;
    String[][] def;

    public DataflowAnalysis(Method method) {
        this.method = method;
        this.method.buildCFG();
        this.use = new String[method.getInstructions().size()][];
        this.def = new String[method.getInstructions().size()][];
    }

    public void build() {
        buildUseDef();
    }

    public void buildUseDef() {
        Node currentInst = method.getBeginNode();
        do {
            currentInst = currentInst.getSucc1();
            getUsedDefVariables(currentInst);
        } while (currentInst.getSucc1().getNodeType() != NodeType.END);
        printMatrix(def);
    }

    /**
     * Stores in the array the used and defined variables.
     */
    public void getUsedDefVariables(Node node) {
        int index = node.getId() - 1;
        Instruction instruction = method.getInstr(index);

        def[index] = new String[]{};
        use[index] = new String[]{};

        // Defined variable.
        if (instruction.getInstType() == InstructionType.ASSIGN)
            storeDefined((AssignInstruction) instruction, index);

        // Used variable.

    }

    /**
     * Stored the defined variable in the array structure.
     */
    public void storeDefined(AssignInstruction instruction, int index) {
        Operand dest = (Operand) instruction.getDest();
        def[index] = new String[]{dest.getName()};
    }

    public void printMatrix(String[][] mat) {
        System.out.print("[");
        for (int i = 0; i < mat.length; i++) {
            for (int j = 0; j < mat[i].length; j++)
                System.out.print("[" + mat[i][j] + "]");
        }
        System.out.print("]");
    }
}
