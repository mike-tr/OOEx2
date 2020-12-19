package gameClient.Graphics;

import api.directed_weighted_graph;
import api.edge_data;
import api.node_data;
import gameClient.utilities.Pos;
import gameClient.utilities.PosInt;
import gameClient.utilities.PosTransformer;

import java.awt.*;

/**
 * Draw the graph, to the pane
 */
public class GraphDrawer {
    private directed_weighted_graph graph;
    private PosTransformer transformer;

    private static int size = 6;
    public static final Color typeP1 = Color.red; // start < end
    public static final Color typeN1 = Color.orange; // start > end

    Color colorNodes = Color.blue;
    Color colorEdges = Color.blue;

    /**
     * This class would draw the graph the edges and the overlay text.
     * @param graph the target graph we must draw
     * @param transformer the screen transformer object
     */
    public GraphDrawer(directed_weighted_graph graph, PosTransformer transformer){
        this.graph = graph;
        this.transformer = transformer;
        System.out.println(graph);
    }

    public void draw(Graphics g){
        //System.out.println("????");
        if(graph == null){
            return;
        }
        Graphics2D g2 = ((Graphics2D)(g));
        g2.setStroke(new BasicStroke(2));
        for (node_data node: graph.getV()) {
            int n1 = node.getKey();
            var pos = transformer.transform(node.getLocation());

            g.setColor(colorEdges);
            for (edge_data edge: graph.getE(node.getKey())) {
                int n2 = edge.getDest();
                var p2 = transformer.transform(graph.getNode(edge.getDest()).getLocation());

                g.setColor(colorEdges);
                g.drawLine((int) pos.x, (int) pos.y, (int) p2.x, (int) p2.y);

                // draw edge between n1 to n2.
                // type == 1 => n1 < n2, small to big
                // type == 2 => n1 > n2, big to small
                if(n1 > n2) {
                    g.setColor(typeN1); // type == -1
                }else{
                    g.setColor(typeP1); // type == 1
                }
                drawArrow(g, new Pos(pos), new Pos(p2), String.format("%.2f", edge.getWeight()));

            }
            g.setColor(colorNodes);
            g.drawString(""+n1, (int)pos.x, (int)pos.y-2*size);
            g.drawOval((int)pos.x - size, (int)pos.y - size, size * 2, size * 2);
        }
    }

    public void drawArrow(Graphics g, Pos p1, Pos p2, String string){
        Pos dir = p2.sub(p1);
        var p = p1.add(dir.scale(0.9));
        var p21 = p.sub(dir.normalize().scale(7));
        drawArrowLine(g, new PosInt(p21), new PosInt(p), 10, 10);
        p = p1.add(dir.scale(0.8));
        g.drawString(string, (int)p.x + size, (int)p.y + 6 * size);
    }

    public static void drawArrow(Graphics g, Pos p1, Pos p2, int d, int h) {
        // this code is from stack overflow as iam too lazy to write a code, that makes arrows.
        // i just don't remember how to find perpendicular vector, with i need in order to create arrows.
        p2 = p2.sub(p1).normalize().scale(d).add(p1);
        drawArrowLine(g, new PosInt(p1), new PosInt(p2), d, h);
    }

    public static void drawArrowLine(Graphics g, PosInt p1, PosInt p2, int d, int h) {
        // this code is from stack overflow as iam too lazy to write a code, that makes arrows.
        // i just don't remember how to find perpendicular vector, with i need in order to create arrows.
        int dx = p2.x - p1.x, dy = p2.y - p1.y;
        double D = Math.sqrt(dx*dx + dy*dy);
        double xm = D - d, xn = xm, ym = h, yn = -h, x;
        double sin = dy / D, cos = dx / D;

        x = xm*cos - ym*sin + p1.x;
        ym = xm*sin + ym*cos + p1.y;
        xm = x;

        x = xn*cos - yn*sin + p1.x;
        yn = xn*sin + yn*cos + p1.y;
        xn = x;

        int[] xpoints = {p2.x, (int) xm, (int) xn};
        int[] ypoints = {p2.y, (int) ym, (int) yn};

        //g.drawLine(p1.x, p1.y, p2.x, p2.y);
        g.fillPolygon(xpoints, ypoints, 3);
    }
}
