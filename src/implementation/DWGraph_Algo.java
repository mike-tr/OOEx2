package implementation;

import api.directed_weighted_graph;
import api.dw_graph_algorithms;
import api.edge_data;
import api.node_data;
import implementation.MyHeap.Heap;
import implementation.utilities.InvertedGraph;
import implementation.utilities.JsonGraph;

import java.util.*;

public class DWGraph_Algo implements dw_graph_algorithms{

    directed_weighted_graph graph;
    public DWGraph_Algo(){
        graph = new DWGraph_DS();
    }

    public DWGraph_Algo(directed_weighted_graph graph){
        init(graph);
    }

    @Override
    public void init(directed_weighted_graph g) {
        graph = g;
    }

    @Override
    public directed_weighted_graph getGraph() {
        return graph;
    }

    @Override
    public directed_weighted_graph copy() {
        return new DWGraph_DS(graph);
    }

    @Override
    public boolean isConnected() {
        int first = graph.getV().iterator().next().getKey();
        if(directionalConnection(first, graph)){
            directed_weighted_graph inverted = new InvertedGraph(graph);
            return directionalConnection(first, inverted);
        }
        return false;
    }

    public boolean directionalConnection(int start, directed_weighted_graph graph){
        node_data startn = graph.getNode(start);
        if(startn == null){
            return false;
        }

        int size = graph.nodeSize();
        if(size <= 1){
            return true;
        }
        if(graph.edgeSize() < size-1){
            return false;
        }

        for (node_data node: graph.getV()) {
            node.setTag(0);
        }
        ArrayDeque<node_data> open = new ArrayDeque<>();
        open.add(startn);
        size--;
        while (open.size() > 0){
            node_data current = open.pollFirst();
            if(size == 0){

                return true;
            }

            Collection<edge_data> neighbours = graph.getE(current.getKey());
            if(neighbours.size() == 0){
                return false;
            }
            for (edge_data edge: neighbours) {
                node_data ni = graph.getNode(edge.getDest());
                if(ni.getTag() == 0){
                    ni.setTag(1);
                    open.add(ni);
                    size--;
                }
            }
        }

        return false;
    }

    @Override
    public double shortestPathDist(int src, int dest) {
        WPathNode pathNode = getShortestPath(src,dest);
        return pathNode != null ? pathNode.getDistance() : -1;
    }

    @Override
    public List<node_data> shortestPath(int src, int dest) {
        WPathNode pathNode = getShortestPath(src,dest);
        if(pathNode != null){
            List<node_data> path = new LinkedList<>();
            while (pathNode != null){
                path.add(0, pathNode.getNode());
                pathNode = pathNode.getParent();
            }
            return path;
        }
        return null;
    }


    public WPathNode getShortestPath(int src, int dest){
        if(graph.getNode(dest) == null){
            return null;
        }

        node_data first = graph.getNode(src);
        if(first == null){
            return null;
        }

        for (node_data node: graph.getV()) {
            node.setTag(Heap.NOT_IN_HEAP);
        }

        Heap<WPathNode> open = new Heap<>();
        open.add(new WPathNode(first), 0);
        while (open.size() > 0){
            WPathNode current = open.poll();
            int key = current.getNode().getKey();
            if(key == dest){
                return current;
            }
            double distance = current.getDistance();
            for (edge_data edge: graph.getE(key)) {
                node_data node = graph.getNode(edge.getDest());
                if(node.getTag() == Heap.OLD_MEMBER){
                    continue;
                }

                if(node.getTag() == Heap.NOT_IN_HEAP){
                    WPathNode pn = new WPathNode(node, current);
                    open.add(pn, distance + edge.getWeight());
                    continue;
                }

                WPathNode pw = open.getAt(node.getTag());
                if(open.increasePriority(pw, distance + edge.getWeight()) == Heap.HEAPIFIED_UP){
                    pw.setParent(current);
                }
            }
        }
        return null;
    }

    @Override
    public boolean save(String file) {
        JsonGraph jg = new JsonGraph(graph);
        return jg.saveToFile(file);
    }

    @Override
    public boolean load(String file) {
        JsonGraph jg = JsonGraph.fromFile(file);
        if(jg != null){
            graph = new DWGraph_DS(jg);
            return true;
        }
        return false;
    }
}
