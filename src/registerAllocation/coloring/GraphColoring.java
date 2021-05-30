package registerAllocation.coloring;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;

public class GraphColoring {

    ArrayList<NodeInterference> stackVisited;
    InterferenceGraph interferenceGraph;
    int k;

    public GraphColoring(int k, InterferenceGraph interferenceGraph) {
        stackVisited = new ArrayList<NodeInterference>();
        this.interferenceGraph = interferenceGraph;
        this.k = k;
    }

    public boolean buildStack() {
        ArrayList<NodeInterference> nodeList = interferenceGraph.nodeList;
        Boolean removedNode;

        while (stackVisited.size() != nodeList.size()) {
            removedNode = false;
            for (var node : nodeList) {
                if (stackVisited.contains(node)) continue;
                if (edgeNotInStack(node) <= k) {
                    stackVisited.add(node);
                    removedNode = true;
                }
            }
            if (!removedNode) return false;
        }
        return true;
    }


    public int edgeNotInStack(NodeInterference node) {
        int edgesNotInStack = 0;
        for (var child : node.getEdges()) {
            if (!stackVisited.contains(child)) edgesNotInStack++;
        }
        return edgesNotInStack;
    }

    public int getAvailableColor(NodeInterference node) {
        ArrayList<Integer> usedColors = new ArrayList<>();

        for (var child : node.getEdges()) {
            if (child.getRegister() != -1)
                usedColors.add(child.getRegister());
        }
        for (int i = 1; i < k; i++) {
            if (!usedColors.contains(i)) {
                return i;
            }
        }
        return -1;
    }

    public Boolean coloring() {
        for (int i = stackVisited.size() - 1; i >= 0; i--){
            NodeInterference node = stackVisited.get(i);
            int register = getAvailableColor(node);
            if (register == -1) return false;
            node.setRegister(register);
            stackVisited.remove(i);
        }
        return true;
    }

    public void printStack() {
        for (var node : this.stackVisited) {
            System.out.print(node.value);
        }
    }
}
