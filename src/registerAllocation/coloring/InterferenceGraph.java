package registerAllocation.coloring;

import java.util.ArrayList;
import java.util.HashMap;

public class InterferenceGraph {

    HashMap<String, ArrayList<String>> interferenceRelations;
    ArrayList<NodeInterference> nodeList;

    public InterferenceGraph(HashMap<String, ArrayList<String>> interference){
        this.interferenceRelations = interference;
        this.nodeList = new ArrayList<>();
        buildNodes();
        buildEdges();
    }

    public void buildNodes(){
        for (var value: interferenceRelations.keySet()){
            nodeList.add(new NodeInterference(value));
        }
    }

    public void buildEdges(){
        for (var node: nodeList) {
            for (var value: interferenceRelations.get(node.getValue())) {
                node.addEdge(getNode(value));
            }
        }
    }

    public NodeInterference getNode(String value){
        for (var node: nodeList){
            if (node.getValue().equals(value))  return node;
        }
        return null;
    }

    public ArrayList<NodeInterference> getNodeList(){
        return nodeList;
    }


}
