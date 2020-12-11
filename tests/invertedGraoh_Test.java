import api.directed_weighted_graph;
import api.dw_graph_algorithms;
import api.node_data;
import implementation.DWGraph_Algo;
import implementation.DWGraph_DS;
import implementation.EdgeData;
import implementation.NodeData;
import implementation.utilities.InvertedGraph;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

public class invertedGraoh_Test {
    private static Random _rand;
    private static long _seed;


    @Test(timeout = 5000)
    public void runTime(){
        int v=100000, e=v*10;
        directed_weighted_graph g = graph_creator(v, e);
        new InvertedGraph(g);
    }

    @Test
    public void testInverted(){
        int v=100, e=0;
        directed_weighted_graph g = graph_creator(v, e);
        for (int i = 1; i < 100; i++) {
            g.connect(i-1, i, 1);
        }
        directed_weighted_graph g2 = new InvertedGraph(g);

        assertEquals(g2.getNode(0), g.getNode(0));
        assertNotNull(g.getEdge(0, 1));
        assertNotNull(g2.getEdge(1,0));
        assertNull(g.getEdge(1,0));
        assertTrue(EdgeData.areInverted(g.getEdge(0, 1),g2.getEdge(1,0)));

        dw_graph_algorithms algo = new DWGraph_Algo(g);
        dw_graph_algorithms algo2 = new DWGraph_Algo(g2);

        assertEquals(50, algo2.shortestPathDist(50,0));
        assertEquals(50, algo.shortestPathDist(0,50));

        assertEquals(algo2.shortestPathDist(50, 0), algo.shortestPathDist(0,50));

        List<node_data> path1 = algo2.shortestPath(50, 0);
        List<node_data> path2 = algo.shortestPath(0, 50);

        for (int i = 0; i < path1.size(); i++) {
            assertEquals(path1.get(i), path2.get(path2.size() - 1 - i));
        }
    }


    /////////////////////////////////////////////////
    private static int nextRnd(int min, int max) {
        double v = nextRnd(0.0+min, (double)max);
        int ans = (int)v;
        return ans;
    }
    private static double nextRnd(double min, double max) {
        double d = _rand.nextDouble();
        double dx = max-min;
        double ans = d*dx+min;
        return ans;
    }
    /**
     * Simple method for returning an array with all the node_data of the graph,
     * Note: this should be using an  Iterator<node_edge> to be fixed in Ex1
     * @param g
     * @return
     */
    private static int[] nodes(directed_weighted_graph g) {
        int size = g.nodeSize();
        Collection<node_data> V = g.getV();
        //System.out.println(V.size() + " ," + size);
        node_data[] nodes = new node_data[size];
        V.toArray(nodes); // O(n) operation
        int[] ans = new int[size];
        for(int i=0;i<size;i++) {ans[i] = nodes[i].getKey();}
        Arrays.sort(ans);
        return ans;
    }


    private static directed_weighted_graph graph_creator(int v_size, int e_size){
        return graph_creator(v_size, e_size, 31);
    }

    /**
     * Generate a random graph with v_size nodes and e_size edges
     * @param v_size
     * @param e_size
     * @param seed
     * @return
     */
    private static directed_weighted_graph graph_creator(int v_size, int e_size, int seed) {
        directed_weighted_graph g = new DWGraph_DS();
        initSeed(seed);
        for(int i=0;i<v_size;i++) {
            g.addNode(new NodeData(i));
        }
        // Iterator<node_data> itr = V.iterator(); // Iterator is a more elegant and generic way, but KIS is more important
        int[] nodes = nodes(g);
        while(g.edgeSize() < e_size) {
            int a = nextRnd(0,v_size);
            int b = nextRnd(0,v_size);
            double w = nextRnd(0, 10.0);
            int i = nodes[a];
            int j = nodes[b];
            g.connect(i,j,w);
        }
        return g;
    }

    public static void initSeed(long seed) {
        _seed = seed;
        _rand = new Random(_seed);
    }
    public static void initSeed() {
        initSeed(_seed);
    }
}