package gameClient.GameData;

public class Agent extends AgentV1 {
    /**
     * This class is just responsible for choosing the Agent implementation, in case we have multiple of them.
     * @param gameHandler
     * @param id
     * @param src
     * @param pokemon
     */
    public Agent(PokemonGameHandler gameHandler, int id, int src, Pokemon pokemon){
        super(gameHandler, id, src, pokemon);
    }
}
