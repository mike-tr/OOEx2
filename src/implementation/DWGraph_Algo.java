package implementation;

import api.directed_weighted_graph;
import api.dw_graph_algorithms;
import api.edge_data;
import api.node_data;
import implementation.MyHeap.Heap;
import implementation.utilities.InvertedGraph;
import implementation.utilities.JsonGraph;

import java.util.*;

/**
 * My graph algo implementation
 */
public class DWGraph_Algo implements dw_graph_algorithms{
    directed_weighted_graph graph;
    public DWGraph_Algo(){
        graph = new DWGraph_DS();
    }

    public DWGraph_Algo(directed_weighted_graph graph){
        init(graph);
    }


    /**
     * init graph
     * @param g
     */
    @Override
    public void init(directed_weighted_graph g) {
        graph = g;
    }

    @Override
    public directed_weighted_graph getGraph() {
        return graph;
    }

    /**
     * @return copy of the graph
     */
    @Override
    public directed_weighted_graph copy() {
        return new DWGraph_DS(graph);
    }

    /**
     * @return true if fully connected
     */
    @Override
    public boolean isConnected() {
        int first = graph.getV().iterator().next().getKey();
        if(checkIfStartConnectedToAll(first, graph)){
            directed_weighted_graph inverted = new InvertedGraph(graph);
            return checkIfStartConnectedToAll(first, inverted);
        }
        return false;
    }

    /**
     * Return if there is a path between start and every other node.
     * @param start starting node
     * @param graph target graph
     * @return
     */
    public synchronized boolean checkIfStartConnectedToAll(int start, directed_weighted_graph graph){
        synchronized (this) {

            node_data startn = graph.getNode(start);
            if (startn == null) {
                return false;
            }

            int size = graph.nodeSize();
            if (size <= 1) {
                return true;
            }
            if (graph.edgeSize() < size - 1) {
                return false;
            }

            for (node_data node : graph.getV()) {
                node.setTag(0);
            }
            ArrayDeque<node_data> open = new ArrayDeque<>();
            open.add(startn);
            size--;
            while (open.size() > 0) {
                node_data current = open.pollFirst();
                if (size == 0) {

                    return true;
                }

                Collection<edge_data> neighbours = graph.getE(current.getKey());
                if (neighbours.size() == 0) {
                    return false;
                }
                for (edge_data edge : neighbours) {
                    node_data ni = graph.getNode(edge.getDest());
                    if (ni.getTag() == 0) {
                        ni.setTag(1);
                        open.add(ni);
                        size--;
                    }
                }
            }

            return false;
        }
    }

    /**
     * return the shortest path distance, between src and dest, returns -1 if no path.
     * @param src - start node
     * @param dest - end (target) node
     * @return distance
     */
    @Override
    public double shortestPathDist(int src, int dest) {
        WPathNode pathNode = getShortestPath(src,dest);
        return pathNode != null ? pathNode.getDistance() : -1;
    }

    /**
     * return the path between src and dest, return null if there is no path
     * @param src - start node
     * @param dest - end (target) node
     * @return path
     */
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

    /**
     * calculate the shortest path return WPATHNODE if path was found.
     * @param src
     * @param dest
     * @return end node Path object
     */
    public synchronized WPathNode getShortestPath(int src, int dest){
        synchronized (this) {
            if (graph.getNode(dest) == null) {
                return null;
            }

            node_data first = graph.getNode(src);
            if (first == null) {
                return null;
            }

            if (src == dest) {
                var p = new WPathNode(first);
                var p2 = new WPathNode(first, p);
                return p2;
            }

            for (node_data node : graph.getV()) {
                node.setTag(Heap.NOT_IN_HEAP);
            }

            Heap<WPathNode> open = new Heap<>();
            open.add(new WPathNode(first), 0);
            while (open.size() > 0) {
                WPathNode current = open.poll();
                int key = current.getNode().getKey();
                if (key == dest) {
                    return current;
                }
                double distance = current.getDistance();
                for (edge_data edge : graph.getE(key)) {
                    node_data node = graph.getNode(edge.getDest());
                    if (node.getTag() == Heap.OLD_MEMBER) {
                        continue;
                    }

                    if (node.getTag() == Heap.NOT_IN_HEAP) {
                        WPathNode pn = new WPathNode(node, current);
                        open.add(pn, distance + edge.getWeight());
                        continue;
                    }

                    WPathNode pw = open.getAt(node.getTag());
                    if (open.increasePriority(pw, distance + edge.getWeight()) == Heap.HEAPIFIED_UP) {
                        pw.setParent(current);
                    }
                }
            }
            return null;
        }
    }

    /**
     * save graph to file
     * @param file - the file name (may include a relative path).
     * @return true if success
     */
    @Override
    public boolean save(String file) {
        JsonGraph jg = new JsonGraph(graph);
        return jg.saveToFile(file);
    }

    /**
     * load graph from file
     * @param file - file name of JSON file
     * @return true if success
     */
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
