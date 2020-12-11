package gameClient.Graphics;

import api.directed_weighted_graph;
import api.node_data;
import gameClient.utilities.PokemonGameHandler;
import gameClient.utilities.PosTransformer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class GameDrawer extends JPanel {
    int width, height;
    int x = 0;
    int dir = 5;
    PokemonGameHandler gameData;
    PosTransformer transformer;

    GraphDrawer graphDrawer;
    PokemonDrawer pokemonDrawer;
    public GameDrawer(PokemonGameHandler gameData, int width, int height){
        this.width = width;
        this.height = height;
        this.gameData = gameData;

        this.setPreferredSize(new Dimension(width, height));
        this.addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent componentEvent) {
                GameDrawer.this.width = getWidth();
                GameDrawer.this.height = getHeight();

                if(transformer != null){
                    transformer.applyResize(getWidth(), getHeight());
                }
            }
        });
        generateTransformer(gameData.getGraph());
        graphDrawer = new GraphDrawer(gameData.getGraph(), transformer);
        pokemonDrawer = new PokemonDrawer(gameData, transformer);
    }

    public void generateTransformer(directed_weighted_graph graph){
        double minx, miny, maxx, maxy;
        minx = miny = Double.POSITIVE_INFINITY;
        maxx = maxy = Double.NEGATIVE_INFINITY;
        for (node_data node: graph.getV()) {
            double x = node.getLocation().x();
            double y = node.getLocation().y();
            if(x > maxx){
                maxx = x;
            }if(x < minx){
                minx = x;
            }if(y > maxy){
                maxy = y;
            }if(y < miny){
                miny = y;
            }
        }
        transformer = new PosTransformer(width, height, minx, miny, maxx, maxy);
    }

    @Override
    public void paintComponents(Graphics g) {
        if(transformer != null) {
            graphDrawer.draw(g);
            pokemonDrawer.draw(g);
        }
    }

    @Override
    public void paint(Graphics g) {
        Image buffer_image;
        Graphics buffer_graphics;
        // Create a new "canvas"
        buffer_image = createImage(width, height);
        buffer_graphics = buffer_image.getGraphics();
        // Draw on the new "canvas"
        paintComponents(buffer_graphics);

        // "Switch" the old "canvas" for the new one
        g.drawImage(buffer_image, 0, 0, this);
    }

    public PosTransformer getTransformer(){
        return transformer;
    }
}
