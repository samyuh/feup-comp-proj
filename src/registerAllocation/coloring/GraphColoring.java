package registerAllocation.coloring;


import java.util.ArrayList;

public class GraphColoring {

    ArrayList<NodeInterference> stackVisited;
    InterferenceGraph interferenceGraph;
    int k;
    public GraphColoring(int k, InterferenceGraph interferenceGraph){
        stackVisited = new ArrayList<NodeInterference>();
        this.interferenceGraph = interferenceGraph;
        this.k = k;
    }

    public boolean buildStack(){
        ArrayList<NodeInterference> nodeList = interferenceGraph.nodeList;
        Boolean removedNode;

        while(stackVisited.size() != nodeList.size()) {
            removedNode = false;
            for (var node : nodeList) {
                if (stackVisited.contains(node)) continue;
                if (edgeNotInStack(node) <= k){
                    stackVisited.add(node);
                    removedNode = true;
                }
            }
            if (!removedNode) return false;
        }
        return true;
    }


    public int edgeNotInStack(NodeInterference node){
        int edgesNotInStack= 0;
        for (var child: node.getEdges()){
            if (!stackVisited.contains(child)) edgesNotInStack++;
        }
        return edgesNotInStack;
    }

    public void printStack(){
        for (var node: this.stackVisited){
            System.out.print(node.value);
        }
    }
}
