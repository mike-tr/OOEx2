import api.directed_weighted_graph;
import api.node_data;
import implementation.DWGraph_DS;
import implementation.GraphDevider;
import implementation.NodeData;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collection;
import java.util.Random;

public class DWGraph_Devider {
    private static Random _rand;
    private static long _seed;

    @Test
    public void testLoadDataGraph(){
        directed_weighted_graph graph = graph_creator(6,0);
        graph.connect(0, 2, 5);
        graph.connect(2, 1, 5);
        graph.connect(1, 3, 5);

        graph.connect(3,2,5);
        graph.connect(3,4,5);
        graph.connect(3,5,5);
        graph.connect(2,3,5);

        graph.connect(5,2,5);
        graph.connect(5,3,5);
        graph.connect(4,3,5);

        GraphDevider divader = new GraphDevider(graph, 2);
        for (int i = 0; i < 2; i++) {
            System.out.println(divader.getSubGraph(i).getV());
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

        int ff =0;
        while(g.edgeSize() < e_size && ff < e_size * 10) {
            int a = nextRnd(0,v_size);
            int b = nextRnd(0,v_size);
            double w = nextRnd(0, 10.0);
            int i = nodes[a];
            int j = nodes[b];
            ff++;
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