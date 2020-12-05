package implementation;

import api.directed_weighted_graph;
import api.dw_graph_algorithms;
import api.node_data;

import java.util.List;

public class WDGraph_Algo implements dw_graph_algorithms {
    directed_weighted_graph graph;

    public WDGraph_Algo(){

    }

    public WDGraph_Algo(directed_weighted_graph g){
        init(g);
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
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    @Override
    public directed_weighted_graph copy() {
        return null;
    }

    @Override
    public boolean isConnected() {
        return false;
    }





    @Override
    public double shortestPathDist(int src, int dest) {
        return 0;
    }

    @Override
    public List<node_data> shortestPath(int src, int dest) {
        return null;
    }

    @Override
    public boolean save(String file) {
        return false;
    }

    @Override
    public boolean load(String file) {
        return false;
    }
}
