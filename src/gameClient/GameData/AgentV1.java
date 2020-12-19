package gameClient.GameData;

import java.util.ArrayList;

/**
 * Version 1 of the agent, uses straight forward path finding
 */
public class AgentV1 extends AgentBasic {
    /**
     * OLD implementation of agent this one uses straight Path finding, its worse in most cases
     * @param gameHandler game manager
     * @param id agent id
     * @param src initial src
     * @param pokemon initial pokemon
     */
    public AgentV1(PokemonGameData gameHandler, int id, int src, Pokemon pokemon){
        super(gameHandler, id, src, pokemon);
    }

    /**
     * implement next move when agent on "node" and not moving
     */
    @Override
    protected void onIdle() {
        if(velocity == null) {
            //System.out.println(gameHandler.getPokemons());
            if(updateTarget()){
                return;
            }
            if(getTarget().getSrc() == src){
                //System.out.println("this");
                gameHandler.getGame().chooseNextEdge(id, getTarget().getDest());
                gameHandler.doMove();
                sleepFor(5);
            } else{
                path = algo.shortestPath(src, getTarget().getSrc());
                //System.out.println("RR "  + path + " -- " + getTarget().getSrc() + " ||| " + getTarget());
                //System.out.println(gameHandler.getPokemons());
                //System.out.println(gameHandler.getGame().getPokemons());
                if(gameHandler.getGame().chooseNextEdge(id, path.get(1).getKey()) > 0){
                    return;
                }
                gameHandler.doMove();
                sleepFor(5);
            }

            //recalculatePath();
        }else{

        }
    }

    /**
     * get the next target
     */
    @Override
    protected void getNextTarget() {
        var distance = Double.POSITIVE_INFINITY;
        Pokemon next = null;

        for (var pokemon : gameHandler.getPokemons()) {
            if(pokemon.getSrc() == src){
                setTarget(pokemon, 0);
                path = new ArrayList<>();
                return;
            }else if(dest != -1 && pokemon.getSrc() == dest){
                distance = -1;
                next = pokemon;
            }

            var dist = algo.shortestPathDist(src, pokemon.getSrc());
            if(dist < distance){
                if(pokemon.hasAgent() && pokemon.getAgentId() != id && pokemon.getDistance() - 3 < dist){
                    continue;
                }
                next = pokemon;
                distance = dist;
            }
        }

        if(next != null){
            setTarget(next, distance);
            path = algo.shortestPath(src, getTarget().getSrc());
            gameHandler.getGame().chooseNextEdge(id, path.get(1).getKey());
        }
    }
}
