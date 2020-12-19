import api.directed_weighted_graph;
import api.node_data;
import implementation.DWGraph_DS;
import implementation.NodeData;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collection;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

public class DWGraph_DSTest {
    private static Random _rand;
    private static long _seed;


    @Test
    public void testCopy(){
        int v=100000, e=v*10;
        directed_weighted_graph g = graph_creator(v, e);
        directed_weighted_graph g2 = new DWGraph_DS(g);
        assertEquals(g,g2);
        g.removeNode(0);
        assertNotEquals(g,g2);
    }

    @Test(timeout = 5000)
    public void graphTest_runtime() {
        int v=10000*20, e=v*10;
        directed_weighted_graph g = graph_creator(v, e);
        assertEquals(v, g.nodeSize());
        assertEquals(e, g.edgeSize());
        for (int i = 1; i < v; i++) {
            g.removeNode(i);
        }
        assertEquals(1, g.nodeSize());
        assertEquals(0, g.edgeSize());
    }
    /**
     * Test graph's getV methods.
     */
    @Test
    public void testGetV(){
        directed_weighted_graph g = new DWGraph_DS();
        for (int i = 0; i < 5; i++) {
            g.addNode(new NodeData(i));
        }
        assertEquals(5,g.getV().size());
    }

    /**
     * Test graph's nodeSize() method.
     */
    @Test
    public void testNode(){
        directed_weighted_graph g = graph_creator(10,0);
        assertEquals(10,g.nodeSize());
        g.connect(0,1,5);
        assertEquals(10,g.nodeSize());
        g.removeNode(1);
        g.removeNode(1);
        g.removeNode(5);
        assertEquals(8,g.nodeSize());
        g.removeNode(5);
        g.removeNode(3);
        assertEquals(7,g.nodeSize());
    }

    /**
     * Test graph's edgeSize() method.
     */
    @Test
    public void testEdge(){
        directed_weighted_graph g = graph_creator(10,0);
        for (int i = 1; i < 6; i++) {
            g.connect(i,0, 2);
        }
        g.connect(0, 5, 2);
        g.connect(1,2,10);
        g.connect(2,1,10);
        g.connect(3,1,10);
        g.connect(5,1,10);
        assertEquals(10, g.edgeSize());

        g.removeEdge(1,2);
        g.removeEdge(1,2);
        assertEquals(9, g.edgeSize());

        assertEquals(2, g.getEdge(0,5).getWeight());
        g.connect(0,5,5);
        assertEquals(9, g.edgeSize());
        assertEquals(5, g.getEdge(0,5).getWeight());

        g.removeNode(0);
        assertEquals(3, g.edgeSize());

        g.connect(0,5,10);
        assertEquals(3, g.edgeSize());

        assertNull(g.getEdge(1,2));
        assertNull(g.getEdge(0,2));
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
     * Simple method for returning an array with overlapping the node_data of the graph,
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