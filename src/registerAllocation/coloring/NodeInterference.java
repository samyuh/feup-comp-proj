package registerAllocation.coloring;

import java.util.ArrayList;

public class NodeInterference {

    String value;
    ArrayList<NodeInterference> edges;
    int register;

    public NodeInterference(String value){
        this.value = value;
        register = -1;
        this.edges = new ArrayList<>();
    }

    public void addEdge(NodeInterference nodeInterference){
        edges.add(nodeInterference);
    }

    public void removeEdge(NodeInterference nodeInterference){
        edges.remove(nodeInterference);
    }

    public String getValue(){return value; }

    public ArrayList<NodeInterference> getEdges(){
        return edges;
    }
}
