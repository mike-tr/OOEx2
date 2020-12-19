package gameClient.GameData;

import Server.Game_Server_Ex2;
import api.directed_weighted_graph;
import api.dw_graph_algorithms;
import api.game_service;
import gameClient.utilities.IMain;
import implementation.DWGraph_Algo;
import implementation.DWGraph_DS;
import implementation.Vector3D;
import implementation.utilities.JsonGraph;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Comparator;

/**
 * This is the GameManager, holding all the data, and handling server updates
 */
public class PokemonGameData implements Runnable{
    private int tick;
    private int movesMade;
    private directed_weighted_graph graph;
    private dw_graph_algorithms graph_algorithms;
    private game_service game;
    private boolean finished = false;

    private ArrayList<Pokemon> pokemons = new ArrayList<>();
    private ArrayList<AgentBasic> agents = new ArrayList<>();

    private int forcedMoves = 0;

    private IMain mainProgra;

    private PokaGroupCreator pokaGroupCreator;

    private GameData gameData;
    /**
     * This class is basically the game, it would communicate with the server.
     * start the game, initialize everything and call updates.
     * @param level the level of the game
     * @param mainProgra the exit object it will notify on end
     */
    public PokemonGameData(int level, int id, IMain mainProgra){
        System.out.println("--------------------------- Initializing --------------------------------");
        this.mainProgra = mainProgra;
        game = Game_Server_Ex2.getServer(level);
        if(id != -1) {
            game.login(id);
        }
        String g = game.getGraph();
        graph = new DWGraph_DS(JsonGraph.fromJson(g));
        graph_algorithms = new DWGraph_Algo(graph);
        pokaGroupCreator = new PokaGroupCreator(this);

        gameData = new GameData(game.toString());

        //System.out.println(game.getPokemons());
        System.out.println(game.toString());
        System.out.println(game.getAgents());
        getPokemonsFromServer();
        initialize();
        System.out.println(pokemons);
        System.out.println(game.getAgents());
        getAgentsFromServer();
        System.out.println("------------ Start Game --------------");
        tick = 0;
        movesMade = 0;
        game.startGame();
        new Thread(this).start();
    }

    public directed_weighted_graph getGraph() {
        return graph;
    }

    public dw_graph_algorithms getGraph_algorithms() {
        return graph_algorithms;
    }

    public GameData getGameData() {
        return gameData;
    }

    /**
     * the main loop it basically calling move but at "intervals" each time some agent requested,
     * a "move" but not an urgent one we would call move, we have some basic couldown on the "move" ability
     * we would call "FORCED MOVE" only once a "tick" when tick is one interval of this thread.
     * or we can call one move every x ticks, if a lot of agents call move we might actually do it sooner.
     */
    @Override
    public void run() {
        try{
            long t = System.currentTimeMillis();
            int sl = agents.size()*2 + 1;
            while (game.isRunning()) {
                updateTick();
                Thread.sleep(sl);
                if(move){
                    move();
                }
                //Thread.sleep(5);
            }
            wakeupAll();
            t = System.currentTimeMillis() - t;
            t /= 1000;
            gameData = new GameData(game.toString());
            System.out.println("--------------------------- Results --------------------------------");
            System.out.println("Level finished - time : " + t + " movesMade per sec :" + gameData.moves / (double)t
                    + " Forced movesMade : " + forcedMoves);
            //System.out.println(game.getPokemons());
            //System.out.println(game.getAgents());
            System.out.println(game.toString());
            System.out.println("--------------------------------------------------------------------");
            game.stopGame();
            finished = true;
            mainProgra.hasStopped(this);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    int last = -10;
    int last2 = -10;

    boolean updating = false;
    public boolean w8ingUpdate(){
        return updating;
    }
    /**
     * Forced move, this would be called from an agent we would do the move iff no other move was made this tick.
     */
    public synchronized void forceMove(){
        if(tick == last){
            return;
        }
        System.out.println("FORCED MOVE - " + tick);
        updating = true;
        game.move();
        forcedMoves++;
        movesMade++;
        getAgentsFromServer();
        getPokemonsFromServer();
        try{
            Thread.sleep(5);
            getAgentsFromServer();
            getPokemonsFromServer();
        }catch (Exception e) {

        }

        last = tick;
        last2 = last;
        updating = false;
        synchronized (Agent.agentW8Stop) {
            Agent.agentW8Stop.notifyAll();
        }
    }

    boolean move = false;

    /**
     * we pull a request from an agent to do a move at the end of the tick.
     */
    public synchronized boolean doMove(){
        if(move){
            return true;
        }
        if(last2 + 20 * agents.size() > tick){
            last2 -= 5;
            return false;
        }
        move = true;
        return true;
    }

    /**
     * if a move was requested we would call move.
     * @throws Exception
     */
    private void move() throws Exception{
        System.out.println("Request granted doing move " + tick);
        move = false;
        updating = true;
        game.move();
        last2 = tick;
        movesMade++;
        getAgentsFromServer();
        getPokemonsFromServer();
        Thread.sleep(5);
        getAgentsFromServer();
        getPokemonsFromServer();
        updating = false;
        synchronized (Agent.agentW8Stop) {
            Agent.agentW8Stop.notifyAll();
        }
    }

    public void stop(){
        game.stopGame();
    }

    public synchronized ArrayList<PokaGroup> getGroups(){
        return pokaGroupCreator.getPokaGroups();
    }

    public synchronized ArrayList<Pokemon> getPokemons(){
        return pokemons;
    }

    public ArrayList<AgentBasic> getAgents(){
        return agents;
    }

    /**
     * this method would let every sleeping agent know that a tick has passed.
     */
    private void updateTick(){
        tick++;
        synchronized (Agent.agentW8Stop){
            Agent.agentW8Stop.notifyAll();
        }
    }

    public boolean isFinished() {
        return finished;
    }

    public game_service getGame(){
        return game;
    }

    /**
     * this method is for initializing the Agents.
     */
    private void initialize(){
        if(agents.size() > 0){
            System.out.println("Already initialized!");
            return;
        }

        String query = game.toString();
        int agents = new GameData(query).agents;
        for (int i = 0; i < agents; i++) {
            boolean initialized = false;
            pokemons.sort(Comparator.comparing(Pokemon::getValue));
            for (var pokemon : pokemons) {
                if (pokemon.agent() == null) {
                    AgentBasic agent = new Agent(this, i, pokemon.getSrc(), pokemon);
                    game.addAgent(agent.getSrc());
                    //agent.setPokemon(currentTarget);
                    this.agents.add(agent);
                    game.chooseNextEdge(i, pokemon.getDest());
                    initialized = true;
                    break;
                }
            }

            if (!initialized) {
                var agent = new Agent(this, i, 0, null);
                game.addAgent(agent.getSrc());
                this.agents.add(agent);
            }
        }
    }

    /**
     * this method is for getting the updates of pokemons on the server side, and sync up with the game calculations
     */
    private synchronized void getPokemonsFromServer() {
        String query = game.getPokemons();
        //System.out.println(pokemons);

        ArrayList<Pokemon> new_pokemons = new ArrayList<>();
        ArrayList<Pokemon> override = new ArrayList<>();
        try {
            JSONObject Pokemons = new JSONObject(query);
            JSONArray pokemonsArr = Pokemons.getJSONArray("Pokemons");
            for(int i = 0; i< pokemonsArr.length(); i++) {
                JSONObject pokemonAtI = pokemonsArr.getJSONObject(i);
                JSONObject pokemonObj = pokemonAtI.getJSONObject("Pokemon");
                int type = pokemonObj.getInt("type");
                double value = pokemonObj.getDouble("value");

                String p = pokemonObj.getString("pos");
                var pokemon = new Pokemon(graph, Vector3D.fromString(p), value, type, movesMade);
                new_pokemons.add(pokemon);
            }

            if(agents.size() == 0){
                pokemons = new_pokemons;
                pokaGroupCreator.updateLinks();
                return;
            }

            for (var p2: new_pokemons) {
                boolean inside = false;
                for (var pokemon: pokemons) {
                    if(pokemon.same(p2)){
                        override.add(pokemon);
                        pokemon.setLastUpdate(movesMade);
                        inside = true;
                        break;
                    }
                }
                if(!inside){
                    override.add(p2);
                }
            }
            pokemons = override;
            pokaGroupCreator.updateLinks();
            for (var agent: agents) {
                agent.evaluateTarget();
            }
            //System.out.println(pokemons);
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public int getTick() {
        return tick;
    }

    public int movesMade(){
        return movesMade;
    }

    /**
     * this method would forcibly wake up all agents!
     */
    public void wakeupAll(){
        synchronized (Agent.agentW8Stop) {
            Agent.agentW8Stop.notifyAll();
        }
        for (var agent: agents) {
            synchronized (agent){
                agent.notifyAll();
            }
        }
    }

    /**
     * this method is for getting the updates of agents on the server side, and sync up with the game calculations
     */
    public void getAgentsFromServer(){
        //{"Agent":{"id":0,"value":0.0,"src":10,"dest":-1,"speed":1.0,"pos":"35.18910131880549,32.103618700840336,0.0"}}
        String query = game.getAgents();
        try {
            JSONObject Agents = new JSONObject(query);
            JSONArray AgentsArr = Agents.getJSONArray("Agents");
            for(int i = 0; i< AgentsArr.length(); i++) {
                JSONObject agentAtI = AgentsArr.getJSONObject(i);
                JSONObject agentObj = agentAtI.getJSONObject("Agent");
                int id = agentObj.getInt("id");
                var agent = agents.get(id);
                agent.updateFromJson(agentObj);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
