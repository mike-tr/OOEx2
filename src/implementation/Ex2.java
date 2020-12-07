package implementation;

import implementation.utilities.JsonGraph;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Random;

public class Ex2 implements Serializable {
    static int seed = 31;
    static Random _rnd = new Random(seed);
    //HashSet<Test> test = new HashSet<>();


    public static void main(String[] args) {
        DWGraph_DS graph = new DWGraph_DS();

        for (int i = 0; i < 10; i++) {
            graph.addNode(new NodeData(i));
        }

        for (int i = 0; i < 20; i++) {
            int src = nextRnd(0,10);
            int dest = nextRnd(0,10);
            double w = nextRnd(1.0, 9.0);
            graph.connect(src,dest,w);
        }


        System.out.println();
        JsonGraph graphJsonOBJ = new JsonGraph(graph);

        System.out.println(graphJsonOBJ.toJson());

        System.out.println(Arrays.toString(JsonGraph.fromFile("data/A0").Nodes));


    }

    public static int nextRnd(int min, int max) {
        double v = nextRnd(0.0+min, (double)max);
        int ans = (int)v;
        return ans;
    }
    public static double nextRnd(double min, double max) {
        double d = _rnd.nextDouble();
        double dx = max-min;
        double ans = d*dx+min;
        return ans;
    }
}
