package gameClient.Graphics;

import gameClient.GameData.Pokemon;
import gameClient.GameData.PokemonGameHandler;
import gameClient.utilities.*;

import java.awt.*;

public class PokemonDrawer {

    private PokemonGameHandler gameHandler;
    private PosTransformer transformer;

    int size = 6;
    public PokemonDrawer(PokemonGameHandler gameHandler, PosTransformer transformer){
        this.gameHandler = gameHandler;
        this.transformer = transformer;
    }

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
            g.setColor(Color.cyan);
            g.fillOval((int)pos.x - size, (int)pos.y - size, size * 2, size * 2);
        }
    }
}
