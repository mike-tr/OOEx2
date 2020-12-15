package gameClient.GameData;

import java.util.ArrayList;

public class AgentV1 extends AgentBasic {
    public AgentV1(PokemonGameHandler gameHandler, int id, int src, Pokemon pokemon){
        super(gameHandler, id, src, pokemon);
    }

    @Override
    protected void recalculatePath(int times) {
        path = algo.shortestPath(src, getTarget().getSrc());
        gameHandler.getGame().chooseNextEdge(id, path.get(0).getKey());
    }

    @Override
    protected void onIdle() {
        if(velocity == null) {
            if(targetNotMine()){
                removeTarget();
                getNextTarget();
            } else if(getTarget().getSrc() == src){
                gameHandler.getGame().chooseNextEdge(id, getTarget().getDest());
                gameHandler.doMove();
            } else{
                path = algo.shortestPath(src, getTarget().getSrc());
                if(gameHandler.getGame().chooseNextEdge(id, path.get(1).getKey()) > 0){
                    return;
                }
                gameHandler.doMove();
            }

            //recalculatePath();
        }else{

        }
    }

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

//            boolean close = false;
//            for (var agent: gameHandler.getAgents()) {
//                if(agent != this){
//                    //System.out.println(pokemon.getPos().getSqrtDistance(agent.pos));
//                    if(Math.abs(pokemon.getPos().x()-agent.pos.x()) < 0.003){
//                        //System.out.println(Math.abs(pokemon.getPos().x()-agent.pos.x()) + " " + agent +  " " + pokemon);
//                        close = true;
//                        continue;
//                    }
//                }
//            }
//            if(close){
//                continue;
//            }

            var dist = algo.shortestPathDist(src, pokemon.getSrc());
            if(dist < distance){
                if(pokemon.hasAgent() && pokemon.getDistance() - 3 < dist){
                    continue;
                }
                next = pokemon;
                distance = dist;
            }
        }

        if(next != null){
            setTarget(next, distance);
            recalculatePath(0);
        }
    }
}
