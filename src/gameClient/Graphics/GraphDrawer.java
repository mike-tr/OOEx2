package gameClient.Graphics;

import api.directed_weighted_graph;
import api.edge_data;
import api.node_data;
import gameClient.utilities.Pos;
import gameClient.utilities.PosTransformer;

import java.awt.*;

public class GraphDrawer {
    private directed_weighted_graph graph;
    private PosTransformer transformer;

    private static int size = 6;
    public static final Color typeP1 = Color.red; // start > end
    public static final Color typeN1 = Color.orange; // start > end

    Color colorNodes = Color.blue;
    Color colorEdges = Color.blue;
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
                    drawArrow(g, new Pos(pos), new Pos(p2), n1 - n2);
                }else{
                    g.setColor(typeP1); // type == 1
                    drawArrow(g, new Pos(pos), new Pos(p2), n2 - n1);
                }

            }
            g.setColor(colorNodes);
            g.drawString(""+n1, (int)pos.x, (int)pos.y-2*size);
            g.drawOval((int)pos.x - size, (int)pos.y - size, size * 2, size * 2);
        }
    }

    public void drawArrow(Graphics g, Pos p1, Pos p2, int d){
        Pos dir = d > 0 ? p2.sub(p1) : p1.sub(p2);

        Pos p;
        if(d > 0){
            p = p1.add(dir.scale(0.8));
            p1 = p.sub(dir.normalize().scale(7));
            drawArrowLine(g, (int)p1.x, (int)p1.y, (int)p.x, (int)p.y, 10, 10);
        }else{
            p = p2.add(dir);
            p2 = p.sub(dir.normalize().scale(7));
            drawArrowLine(g, (int)p2.x, (int)p2.y, (int)p.x, (int)p.y, 10, 10);
        }
    }

    private void drawArrowLine(Graphics g, int x1, int y1, int x2, int y2, int d, int h) {
        // this code is from stack overflow as iam too lazy to write a code, that makes arrows.
        // i just don't remember how to find perpendicular vector, with i need in order to create arrows.
        int dx = x2 - x1, dy = y2 - y1;
        double D = Math.sqrt(dx*dx + dy*dy);
        double xm = D - d, xn = xm, ym = h, yn = -h, x;
        double sin = dy / D, cos = dx / D;

        x = xm*cos - ym*sin + x1;
        ym = xm*sin + ym*cos + y1;
        xm = x;

        x = xn*cos - yn*sin + x1;
        yn = xn*sin + yn*cos + y1;
        xn = x;

        int[] xpoints = {x2, (int) xm, (int) xn};
        int[] ypoints = {y2, (int) ym, (int) yn};

        g.drawLine(x1, y1, x2, y2);
        g.fillPolygon(xpoints, ypoints, 3);
    }
}
