package gameClient.Graphics;

import gameClient.GameData.Pokemon;
import gameClient.GameData.PokemonGameData;
import gameClient.utilities.*;

import java.awt.*;

public class PokemonDrawer {

    private PokemonGameData gameHandler;
    private PosTransformer transformer;

    private int size = 6;

    /**
     *  this Class would get the pokemon game data,
     *  and draw it on the right place.
     * @param gameHandler
     * @param transformer
     */
    public PokemonDrawer(PokemonGameData gameHandler, PosTransformer transformer){
        this.gameHandler = gameHandler;
        this.transformer = transformer;
    }

    /**
     * Draw the pokemons on the given Graphics
     * @param g
     */
    public void draw(Graphics g){
        for (Pokemon pokemon: gameHandler.getPokemons()) {
            if(pokemon.getType() == 1){
                //g.setColor(GraphDrawer.typeP1);
            }else{
                //g.setColor(GraphDrawer.typeN1);
            }
            //((Graphics2D)g).setStroke(new BasicStroke(3));
            var pos = transformer.transformD(pokemon.getPos());
            var look = transformer.transformD(pokemon.getLook());
            //dest_pos = dest_pos.scale(300).add(pos);

            //dest_pos = new Pos(pos).add(dest_pos.normalize());
            //dest_pos = dest_pos.normalize().scale(5).add(pos);
            //drawArrowLine(g, new PosInt(p2), new PosInt(p), 10, 10);
            g.setColor(Color.BLUE);
            GraphDrawer.drawArrow(g, pos, look, 20, 10);
            g.drawString(""+pokemon.getID(), (int)pos.x + 4 * size, (int)pos.y + size);
            g.setColor(Color.cyan);
            g.fillOval((int)pos.x - size, (int)pos.y - size, size * 2, size * 2);
        }
    }
}
