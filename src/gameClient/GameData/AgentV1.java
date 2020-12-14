package gameClient.GameData;

public class AgentV1 extends AgentBasic {
    public AgentV1(PokemonGameHandler gameHandler, int id, int src, Pokemon pokemon){
        super(gameHandler, id, src, pokemon);
    }

    @Override
    protected void nextEdge() {
        if(targetNotMine()){
            removeTarget();
            getNextTarget();
            sleepFor(5);
            nextEdge();
        } else if(getTarget().getSrc() == src){
            gameHandler.getGame().chooseNextEdge(id, getTarget().getDest());
        } else{
            var path = algo.shortestPath(src, getTarget().getSrc());
            gameHandler.getGame().chooseNextEdge(id, path.get(1).getKey());
        }
    }

    @Override
    protected void onIdle() {
        if(velocity == null) {
            nextEdge();
            gameHandler.forceMove();
        }
    }

    @Override
    protected void getNextTarget() {
        var distance = Double.POSITIVE_INFINITY;
        Pokemon next = null;

        for (var pokemon : gameHandler.getPokemons()) {
            if(pokemon.getSrc() == src){
                setTarget(pokemon, 0);
                return;
            }
            var dist = algo.shortestPathDist(src, pokemon.getSrc());
            if(dist < distance){
                if(pokemon.hasAgent() && pokemon.getDistance() + 1 < dist){
                    continue;
                }
                next = pokemon;
                distance = dist;
            }
        }

        if(next != null){
            setTarget(next, distance);
        }
    }
}
