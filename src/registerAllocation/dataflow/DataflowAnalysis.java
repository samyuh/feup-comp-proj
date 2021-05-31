package registerAllocation.dataflow;

import org.specs.comp.ollir.*;

import java.awt.image.AreaAveragingScaleFilter;
import java.lang.reflect.Array;
import java.util.*;
import java.util.stream.IntStream;
import java.util.stream.Stream;

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

    HashMap<String, int[]> liveRange;
    HashSet<String> variables;

    HashMap<String, ArrayList<String>> interference;

    public DataflowAnalysis(Method method) {
        this.method = method;
        this.method.buildCFG();
        this.use = new String[method.getInstructions().size()][];
        this.def = new String[method.getInstructions().size()][];
        this.next = new Integer[method.getInstructions().size()][];
        this.variables = new HashSet<>();

        this.in = new String[method.getInstructions().size()][];
        this.out = new String[method.getInstructions().size()][];
        this.liveRange = new HashMap<>();
        this.interference = new HashMap<>();
    }

    public void build() {
        prepareDataflowAnalysis(method.getBeginNode().getSucc1());
        processDataflow();
        calculateLiveRange();
        calculateInterference();
        method.show();
        this.show();
        this.showLiveRange();
    }

    public HashMap<String, ArrayList<String>> getInterference(){
        return this.interference;
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

                out[i] = removeParameters(getOut(i));
                in[i] = removeParameters(getIn(i));
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

    /**
     * Remove the parameters from the array.
     */
    public String[] removeParameters(String[] array) {

        ArrayList<Element> parameters = method.getParams();
        ArrayList<String> parametersName = new ArrayList<>();

        for (int i = 0; i < parameters.size(); i++) {
            parametersName.add(((Operand) parameters.get(i)).getName());
        }
        ArrayList<String> temp = new ArrayList<>(Arrays.asList(array));
        temp.removeAll(parametersName);

        return temp.toArray(new String[0]);
    }

    // LIVE RANGE -----------------------------------------------------------
    public void calculateLiveRange() {
        for (var varName : variables) {
            Integer lastIn = getLastIn(varName);
            Integer firstDef = getFirstDef(varName);
            if (lastIn == null || firstDef == null)
                continue;
            int[] varLiveRange = IntStream.range(firstDef, lastIn).toArray();
            liveRange.put(varName, varLiveRange);
        }
    }

    public Integer getLastIn(String varName) {
        for (int i = in.length - 1; i >= 0; i--) {
            ArrayList<String> inTemp = new ArrayList<>(Arrays.asList(in[i]));
            if (inTemp.contains(varName)) return i;
        }
        return null;
    }

    public Integer getFirstDef(String varName) {
        for (int i = 0; i < def.length; i++) {
            ArrayList<String> defTemp = new ArrayList<>(Arrays.asList(def[i]));
            if (defTemp.contains(varName)) return i;
        }
        return null;
    }

    // INTERFERENCE --------------------------------------------------------------
    public void calculateInterference() {
        System.out.println("VARIABLES");
        Utils.printArray(variables.toArray());
        for (var variable : variables) {
            System.out.println("VARIABLE - " + variable);
            ArrayList<String> temp = new ArrayList<>();
            if (!liveRange.containsKey(variable)) {
                interference.put(variable, temp);
                continue;
            }
            for (var variableCompare : liveRange.keySet()) {
                if (variableCompare.equals(variable)) continue;
                if (haveConflict(liveRange.get(variable), liveRange.get(variableCompare))) {
                    temp.add(variableCompare);
                }
            }
               interference.put(variable, temp);
        }
    }

    public Boolean haveConflict(int[] arr1, int[] arr2) {
        return Arrays.stream(arr1)
                .distinct()
                .filter(x -> Arrays.stream(arr2).anyMatch(y -> y == x))
                .toArray().length != 0;
    }

    // SHOW ----------------------------------------------------------------------
    public void show() {
        System.out.println("DEF");
        Utils.printMatrix(def);
        System.out.println("\nUSE");
        Utils.printMatrix(use);
        System.out.println("\nIN");
        Utils.printMatrix(in);
        System.out.println("\nOUT");
        Utils.printMatrix(out);
        System.out.println("\nNEXT");
        Utils.printMatrix(next);
        System.out.println("\nVARIABLES");
        Utils.printArray(variables.toArray());
    }

    public void showLiveRange() {
        liveRange.forEach((key, value) -> {
            System.out.print("{" + key + " - ");
            Stream.of(value).forEach(e -> System.out.print(Arrays.toString(e)));
            System.out.print("}\n");

        });

    }

    public void showInterference() {
        System.out.println("INTERFERENCE");
        interference.forEach((key, value) -> {
            System.out.print("{" + key + " - ");
            System.out.print(value);
            System.out.print("}\n");

        });
    }


}
