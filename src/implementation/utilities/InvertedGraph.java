package implementation.utilities;

import api.directed_weighted_graph;
import api.edge_data;
import api.node_data;
import implementation.DWGraph_DS;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;

/**
 * this graph creates an inverted graph with inverted edges out of any directed weighted graph
 */
public class InvertedGraph implements directed_weighted_graph {
    public static final class Inverter {
        private Inverter() {}
    }
    private static final Inverter permission = new Inverter();
    protected HashMap<Integer, node_data> nodes;
    protected HashMap<Integer, HashMap<Integer,edge_data>> edges;

    int edgeSize = 0;

    public InvertedGraph(directed_weighted_graph graph){
        DWGraph_DS parent;
        if(graph instanceof DWGraph_DS){
            parent = (DWGraph_DS)graph;
        }else{
            parent = new DWGraph_DS(graph);
        }
        nodes = parent.getNodes(permission);
        edges = parent.getInvertedEdges(permission);
        edgeSize = graph.edgeSize();
    }

    @Override
    public node_data getNode(int key) {
        return nodes.get(key);
    }

    @Override
    public edge_data getEdge(int src, int dest) {
        HashMap<Integer, edge_data> eg = edges.get(src);
        if(eg != null){
            return eg.get(dest);
        }
        return null;
    }

    @Override
    public Collection<node_data> getV() {
        return nodes.values();
    }

    @Override
    public Collection<edge_data> getE(int node_id) {
        HashMap<Integer, edge_data> e = edges.get(node_id);
        if(e != null){
            return e.values();
        }
        return new HashSet<>();
    }

    @Override
    public node_data removeNode(int key) {
        return null;
    }

    @Override
    public edge_data removeEdge(int src, int dest) {
        return null;
    }

    @Override
    public int nodeSize() {
        return nodes.size();
    }

    @Override
    public int edgeSize() {
        return edgeSize;
    }

    @Override
    public int getMC() {
        return 0;
    }

    @Override
    public void addNode(node_data n) {
        return;
    }

    @Override
    public void connect(int src, int dest, double w) {
        return;
    }
}
