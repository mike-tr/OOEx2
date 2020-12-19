package gameClient.Graphics;

import gameClient.GameData.PokemonGameData;
import gameClient.utilities.PosTransformer;

import java.awt.*;

public class AgentDrawer {
    private PokemonGameData gameHandler;
    private PosTransformer transformer;

    Color upPok = Color.red;
    Color downPok = Color.orange;

    int size = 15;

    /**
     * This class is responsible for taking the gamedata agent's and drawing them.
     * @param gameHandler the game manager
     * @param transformer the transformer object
     */
    public AgentDrawer(PokemonGameData gameHandler, PosTransformer transformer){
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
