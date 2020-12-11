package gameClient.utilities;

import Server.Game_Server_Ex2;
import api.directed_weighted_graph;
import api.game_service;
import implementation.DWGraph_DS;
import implementation.GeoPos;
import implementation.utilities.JsonGraph;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Comparator;

public class PokemonGameHandler {

    private directed_weighted_graph graph;
    private game_service game;

    private ArrayList<Pokemon> pokemons = new ArrayList<>();
    private ArrayList<Agent> agents = new ArrayList<>();
    public PokemonGameHandler(int level){
        game = Game_Server_Ex2.getServer(level);
        String g = game.getGraph();
        graph = new DWGraph_DS(JsonGraph.fromJson(g));

        updatePokemons();
        System.out.println(game.getPokemons());
        System.out.println(game.toString());
        System.out.println(game.getAgents());
    }

    public directed_weighted_graph getGraph() {
        return graph;
    }

    public void update(){

    }

    public ArrayList<Pokemon> getPokemons(){
        return pokemons;
    }

    public void setAgents(){
        if(agents.size() > 0){
            return;
        }

        pokemons.sort(Comparator.comparing(Pokemon::getValue));
        for (var pokemon: pokemons) {
            if(pokemon.agent() == null){

            }
        }
    }

    public void updatePokemons() {
        String query = game.getPokemons();
        pokemons = new ArrayList<>();
        try {
            JSONObject Pokemons = new JSONObject(query);
            JSONArray pokemonsArr = Pokemons.getJSONArray("Pokemons");
            for(int i = 0; i< pokemonsArr.length(); i++) {
                JSONObject pokemonAtI = pokemonsArr.getJSONObject(i);
                JSONObject pokemonObj = pokemonAtI.getJSONObject("Pokemon");
                int type = pokemonObj.getInt("type");
                double value = pokemonObj.getDouble("value");

                String p = pokemonObj.getString("pos");
                var pokemon = new Pokemon(graph, GeoPos.fromString(p), value, type);
                pokemons.add(pokemon);
            }
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void updateAgents(){
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
                agent.update(agentObj);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
