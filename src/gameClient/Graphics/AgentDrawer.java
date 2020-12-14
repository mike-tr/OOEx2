package gameClient.Graphics;

import gameClient.GameData.PokemonGameHandler;
import gameClient.utilities.PosTransformer;

import java.awt.*;

public class AgentDrawer {
    private PokemonGameHandler gameHandler;
    private PosTransformer transformer;

    Color upPok = Color.red;
    Color downPok = Color.orange;

    int size = 15;
    public AgentDrawer(PokemonGameHandler gameHandler, PosTransformer transformer){
        this.gameHandler = gameHandler;
        this.transformer = transformer;
    }

    public void draw(Graphics g){
        for (var agent: gameHandler.getAgents()) {
            ((Graphics2D)g).setStroke(new BasicStroke(3));
            var pos = transformer.transformD(agent.getPos());
            g.setColor(Color.magenta);
            g.drawString("AgentBasic : "+agent.getId(), (int)pos.x + 25, (int)pos.y-2);
            g.drawOval((int)pos.x - size, (int)pos.y - size, size * 2, size * 2);

            if(agent.getVelocity() != null) {
                var look = transformer.transformD(agent.getDest_pos());
                GraphDrawer.drawArrow(g, pos, look, 20, 10);
                agent.updatePosition();
            }
        }
    }
}
