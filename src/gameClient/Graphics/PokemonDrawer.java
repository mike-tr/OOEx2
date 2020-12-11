package gameClient.Graphics;

import gameClient.utilities.Pokemon;
import gameClient.utilities.PokemonGameHandler;
import gameClient.utilities.PosTransformer;

import java.awt.*;

public class PokemonDrawer {

    private PokemonGameHandler gameHandler;
    private PosTransformer transformer;

    int size = 15;
    public PokemonDrawer(PokemonGameHandler gameHandler, PosTransformer transformer){
        this.gameHandler = gameHandler;
        this.transformer = transformer;
    }

    public void draw(Graphics g){
        for (Pokemon pokemon: gameHandler.getPokemons()) {
            if(pokemon.getType() == 1){
                g.setColor(GraphDrawer.typeP1);
            }else{
                g.setColor(GraphDrawer.typeN1);
            }
            ((Graphics2D)g).setStroke(new BasicStroke(3));
            var pos = transformer.transform(pokemon.getPos());
            g.drawOval(pos.x - size, pos.y - size, size * 2, size * 2);
        }
    }
}
