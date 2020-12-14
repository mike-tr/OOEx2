package gameClient.GameData;

import org.json.JSONException;
import org.json.JSONObject;

public class GameData {
    public int agents = 0;
    public int pokemons = 0;
    public int moves = 0;
    public int level = 0;
    public int grade = 0;

    public GameData(String json){
        try {
            JSONObject data = new JSONObject(json);
            JSONObject gameServer = data.getJSONObject("GameServer");
            agents = gameServer.getInt("agents");
            pokemons = gameServer.getInt("pokemons");
            moves = gameServer.getInt("moves");
            level = gameServer.getInt("game_level");
            grade = gameServer.getInt("grade");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
