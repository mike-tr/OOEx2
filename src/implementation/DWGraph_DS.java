package implementation;

import api.directed_weighted_graph;
import api.edge_data;
import api.node_data;
import implementation.utilities.JsonEdge;
import implementation.utilities.JsonGraph;
import implementation.utilities.JsonNode;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;

public class DWGraph_DS implements directed_weighted_graph{
    private HashMap<Integer, node_data> nodes = new HashMap<>();
    private HashMap<Integer, HashMap<Integer,edge_data>> edges = new HashMap<>();
    private HashMap<Integer, HashSet<Integer>> flipped = new HashMap<>();
    private int edgeSize = 0;
    private int mc = 0;

    public DWGraph_DS(){ }

    public DWGraph_DS(directed_weighted_graph graph){
        JsonGraph gj = new JsonGraph(graph);
        reloadFromJson(gj);
    }

    public DWGraph_DS(JsonGraph graphJson){
        reloadFromJson(graphJson);
    }

    private void reloadFromJson(JsonGraph graph){
        nodes = new HashMap<>();
        edges = new HashMap<>();
        flipped = new HashMap<>();
        for (JsonNode node: graph.Nodes) {
            addNode(node.toNodeData());
        }

        for (JsonEdge edge: graph.Edges) {
            connect(edge.src, edge.dest, edge.w);
        }
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this){
            return true;
        }
        if(obj == null){
            return false;
        }

        if(obj instanceof directed_weighted_graph){
            directed_weighted_graph g = (directed_weighted_graph)obj;
            return g.edgeSize() == edgeSize() && g.nodeSize() == nodeSize();
        }
        return false;
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
    public void addNode(node_data n) {
        if(getNode(n.getKey()) == null) {
            nodes.put(n.getKey(), new NodeData(n));
            edges.put(n.getKey(), new HashMap<>());
            flipped.put(n.getKey(), new HashSet<>());
            mc++;
        }
    }

    @Override
    public void connect(int src, int dest, double w) {
        if(src == dest){
            return;
        }
        if(getNode(src) != null){
            if(getNode(dest) == null){
                return;
            }
            if(!edges.get(src).containsKey(dest)){
                flipped.get(dest).add(src);
                edgeSize++;
            }
            edges.get(src).put(dest, new EdgeData(src, dest, w));
            mc++;
        }
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
        node_data n = getNode(key);
        if(n != null){
            int size = edges.get(key).size() + flipped.get(key).size();
            mc += size + 1;
            edgeSize -= size;
            for (Integer other: flipped.get(key)){
                edges.get(other).remove(key);
            }
            for (Integer edge: edges.get(key).keySet()) {
                flipped.get(edge).remove(key);
            }
            flipped.remove(key);
            edges.remove(key);
            nodes.remove(key);
        }
        return n;
    }

    @Override
    public edge_data removeEdge(int src, int dest) {
        if(getNode(src) != null){
            edge_data e = edges.get(src).get(dest);
            if(e != null){
                edges.get(src).remove(dest);
                flipped.get(dest).remove(src);
                mc++;
                edgeSize--;
            }
            return e;
        }
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
        return mc;
    }
}
