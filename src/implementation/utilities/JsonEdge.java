package implementation.utilities;

import api.edge_data;

public class JsonEdge {
    public int src;
    public double w;
    public int dest;

    public JsonEdge(edge_data edge){
        this.src = edge.getSrc();
        this.w = edge.getWeight();
        this.dest = edge.getDest();
    }

    @Override
    public String toString() {
        return "Src : " + src + ", w : " + w + ", dest : " + dest;
    }
}
