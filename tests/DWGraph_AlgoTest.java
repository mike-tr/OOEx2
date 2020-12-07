import api.directed_weighted_graph;
import api.dw_graph_algorithms;
import api.node_data;
import implementation.DWGraph_Algo;
import implementation.DWGraph_DS;
import implementation.NodeData;
import implementation.utilities.JsonGraph;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collection;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class DWGraph_AlgoTest {
    private static Random _rand;
    private static long _seed;

    @Test
    public void testShortestPath(){
        int v = 100, e = 0;
        directed_weighted_graph g = graph_creator(v,e);
        for (int i = 1; i < 100; i++) {
            g.connect(i-1,i, 10);
        }
        dw_graph_algorithms algo = new DWGraph_Algo(g);
        assertEquals(100,algo.shortestPathDist(0, 10));
        for (int i = 1; i < 100; i++) {
            g.connect(i-1,i, 1);
        }
        assertEquals(10,algo.shortestPathDist(0, 10));
    }

    @Test
    public void graphPathTest(){
        directed_weighted_graph graph = new DWGraph_DS();
        graph.addNode(new NodeData(1));
        graph.addNode(new NodeData(2));
        graph.addNode(new NodeData(3));
        graph.addNode(new NodeData(4));
        graph.addNode(new NodeData(5));

        graph.connect(1, 2, 10);
        graph.connect(1, 3, 5);
        graph.connect(1,4,100);
        graph.connect(2,4,5);
        graph.connect(2,3,50);
        graph.connect(3,5,30);
        graph.removeEdge(4,5);

        dw_graph_algorithms algo = new DWGraph_Algo(graph);
        double v1 = algo.shortestPathDist(1,5);
        
        assertEquals(35, v1);
        int i = 0;
        int arr[] = {1, 3, 5};
        for (node_data node: algo.shortestPath(1, 5)) {
            assertEquals(node.getKey(), arr[i]);
            i++;
        }

        graph.connect(4,5,10);

        v1 = algo.shortestPathDist(1,5);
        double v2 = algo.shortestPathDist(5,1);

        assertEquals(25, v1);
        assertEquals(-1, v2);

        i = 0;
        arr = new int[]{1, 2, 4, 5};
        for (node_data node: algo.shortestPath(1, 5)) {
            assertEquals(node.getKey(), arr[i]);
            i++;
        }

        graph.connect(4,5,100);
        v1 = algo.shortestPathDist(1,5);
        assertEquals(35, v1);
    }

    @Test
    public void testLoadSave(){
        int v = 10000, e = v*10;
        directed_weighted_graph g = graph_creator(v,e);
        dw_graph_algorithms algo = new DWGraph_Algo(g);
        algo.save("Test");
        algo.load("Test");
        assertEquals(g,algo.getGraph());
        g.removeNode(0);
        assertNotEquals(g,algo.getGraph());
    }

    @Test
    public void testLoadDataGraph(){
        dw_graph_algorithms algo = new DWGraph_Algo();

        algo.load("data/A1");
        directed_weighted_graph g = algo.getGraph();
        String graphJsonS = JsonGraph.getReadFile("data/A1");
        assertEquals(graphJsonS, JsonGraph.toJson(g));
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