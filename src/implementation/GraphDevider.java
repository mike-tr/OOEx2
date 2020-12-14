package implementation;

import api.directed_weighted_graph;
import api.edge_data;
import api.node_data;

import java.util.ArrayDeque;
import java.util.Collection;
import java.util.HashMap;

public class GraphDevider {
    public class SubGraph implements directed_weighted_graph{
        private HashMap<Integer, node_data> nodes = new HashMap<>();
        private HashMap<Integer, HashMap<Integer, edge_data>> edges = new HashMap<>();
        private int edgeSize = 0;

        @Override
        public node_data getNode(int key) {
            return nodes.get(key);
        }

        @Override
        public edge_data getEdge(int src, int dest) {
            if(nodes.containsKey(src) && nodes.containsKey(dest)){
                edges.get(src).get(dest);
            }
            return null;
        }

        private boolean validTarget(int target){
            return nodes.containsKey(target);
        }

        @Override
        public void addNode(node_data n) {
            if(graph.getNode(n.getKey()) != null){
                var key = n.getKey();
                if(validTarget(key)){
                    return;
                }

                nodes.put(key, n);
                System.out.println("added node " + n);
                edges.put(key, new HashMap<>());
                for (var edge: graph.getE(key)) {
                    if(validTarget(edge.getDest())){
                        edges.get(key).put(edge.getDest(), edge);
                    }
                }

                for (var node: nodes.keySet()) {
                    var edge = graph.getEdge(node, key);
                    if(edge != null){
                        edges.get(node).put(key,edge);
                        edgeSize++;
                    }
                }
            }
        }

        @Override
        public void connect(int src, int dest, double w) {
            // CAN'T
            return;
        }

        @Override
        public Collection<node_data> getV() {
            return nodes.values();
        }

        @Override
        public Collection<edge_data> getE(int node_id) {
            if(edges.containsKey(node_id)){
                return edges.get(node_id).values();
            }
            return null;
        }

        @Override
        public node_data removeNode(int key) {
            // can't
            return null;
        }

        @Override
        public edge_data removeEdge(int src, int dest) {
            // can't
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
    }

    private SubGraph[] subGraphs;
    private directed_weighted_graph graph;
    int pieces = 0;
    public GraphDevider(directed_weighted_graph graph, int pieces){
        this.graph = graph;
        this.pieces = pieces;
        if(pieces < 1){
            subGraphs = new SubGraph[0];
            return;
        }

        subGraphs = new SubGraph[pieces];
        var size = graph.nodeSize() / pieces;
        for (var node: graph.getV()) {
            node.setTag(-1);
        }

        int s = 0;
        for (int i = 0; i < pieces - 1; i++) {
            subGraphs[i] = new SubGraph();
            s += populateGraph(i, size);
        }
        subGraphs[pieces - 1] = new SubGraph();
        populateGraph(pieces - 1, graph.nodeSize() - s);
    }


    public int populateGraph(int id, int max_size){
        System.out.println("Adding to graph " + id + " max size " + max_size);
        node_data start = null;
        for (var node: graph.getV()) {
            if(node.getTag() == 0){
                node.setTag(-1);
            }
        }

        for (var node: graph.getV()) {
            if(node.getTag() == -1){
                start = node;
                break;
            }
        }

        subGraphs[id].addNode(start);
        ArrayDeque<node_data> open = new ArrayDeque<>();
        open.add(start);
        int added = 0;
        while (open.size() > 0){
            node_data current = open.pollFirst();
            current.setTag(-2);
            System.out.println(current);

            for (var edge: graph.getE(current.getKey())) {
                node_data ni = graph.getNode(edge.getDest());
                if(ni.getTag() == -1){
                    ni.setTag(0);
                    open.add(ni);
                }
                subGraphs[id].addNode(ni);
            }

            added++;
            if(added == max_size){
                return added;
            }
        }
        return added;
    }

    public directed_weighted_graph getSubGraph(int id){
        if(id >= 0 && id < pieces){
            return subGraphs[id];
        }
        return graph;
    }


}
