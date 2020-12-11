package gameClient.Graphics;

import gameClient.utilities.Pokemon;
import gameClient.utilities.PokemonGameHandler;
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
        for (Pokemon pokemon: gameHandler.getPokemons()) {
            if(pokemon.getType() == 1){
                g.setColor(upPok);
            }else{
                g.setColor(downPok);
            }
            ((Graphics2D)g).setStroke(new BasicStroke(3));
            var pos = transformer.transform(pokemon.getPos());
            g.drawOval(pos.x - size, pos.y - size, size * 2, size * 2);
        }
    }
}
