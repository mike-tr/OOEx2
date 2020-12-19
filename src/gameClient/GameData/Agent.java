package gameClient.GameData;

/**
 * the main Agent just Extends some version
 */
public class Agent extends AgentVG {
    /**
     * @param gameHandler the game manager
     * @param id the id of the agent
     * @param src the starting position ( default if no pokemon )
     * @param pokemon the target pokemon
     */
    public Agent(PokemonGameData gameHandler, int id, int src, Pokemon pokemon){
        super(gameHandler, id, src, pokemon);
    }
}
