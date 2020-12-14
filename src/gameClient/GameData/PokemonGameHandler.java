package gameClient.GameData;

import Server.Game_Server_Ex2;
import api.directed_weighted_graph;
import api.dw_graph_algorithms;
import api.game_service;
import implementation.DWGraph_Algo;
import implementation.DWGraph_DS;
import implementation.Pos3D;
import implementation.utilities.JsonGraph;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Comparator;

public class PokemonGameHandler implements Runnable{
    private int tick;
    private int movesMade;
    private directed_weighted_graph graph;
    private dw_graph_algorithms graph_algorithms;
    private game_service game;

    private ArrayList<Pokemon> pokemons = new ArrayList<>();
    private ArrayList<Agent> agents = new ArrayList<>();

    private int forcedMoves = 0;

    private boolean running = true;
    public PokemonGameHandler(int level){
        game = Game_Server_Ex2.getServer(level);
        String g = game.getGraph();
        graph = new DWGraph_DS(JsonGraph.fromJson(g));
        graph_algorithms = new DWGraph_Algo(graph);

        System.out.println(game.getPokemons());
        System.out.println(game.toString());
        System.out.println(game.getAgents());
        getPokemonsFromServer();
        initialize();
        System.out.println(game.getAgents());
        getAgentsFromServer();
        System.out.println("---- Game started ----");
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

    @Override
    public void run() {
        try{
            long t = System.currentTimeMillis();
            while (game.isRunning()) {
                updateTick();
                Thread.sleep(25);
                if(move){
                    move();
                }
                Thread.sleep(75);
            }
            wakeupAll();
            t = System.currentTimeMillis() - t;
            t /= 1000;
            GameData data = new GameData(game.toString());
            System.out.println("Level finished - time : " + t + " movesMade per sec :" + data.moves / (double)t
                    + " Forced movesMade : " + forcedMoves);
            System.out.println(game.getPokemons());
            System.out.println(game.getAgents());
            System.out.println(game.toString());
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    int last = -10;
    int last2 = -10;
    public synchronized void forceMove(){
        if(tick == last){
            return;
        }
        System.out.println("FORCED MOVE - " + tick);

        game.move();
        getAgentsFromServer();
        getPokemonsFromServer();

        forcedMoves++;
        movesMade++;
        last = tick;
        last2 = last;
        synchronized (Agent.agentW8Stop) {
            Agent.agentW8Stop.notifyAll();
        }
    }

    boolean move = false;
    public synchronized void doMove(){
        if(last2 + 3 > tick){
            last2 -= 2;
            return;
        }
        move = true;
    }

    private void move(){
        System.out.println("Request granted doing move " + tick);
        move = false;
        game.move();
        getAgentsFromServer();
        getPokemonsFromServer();
        last2 = tick;
        movesMade++;
        synchronized (Agent.agentW8Stop) {
            Agent.agentW8Stop.notifyAll();
        }
    }

    public void stop(){
        game.stopGame();
    }

    public synchronized ArrayList<Pokemon> getPokemons(){
        return pokemons;
    }

    public ArrayList<Agent> getAgents(){
        return agents;
    }

    /**
     * this method is for updating agents path's etc...
     */

    int k = 0;
    private void updateTick(){
        tick++;
        synchronized (Agent.agentW8Stop){
            Agent.agentW8Stop.notifyAll();
        }
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
                    var agent = new Agent(this, i, pokemon.getSrc(), pokemon);
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
    private void getPokemonsFromServer() {
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
                var pokemon = new Pokemon(graph, Pos3D.fromString(p), value, type);
                new_pokemons.add(pokemon);
            }

            if(agents.size() == 0){
                pokemons = new_pokemons;
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
