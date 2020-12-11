package gameClient.utilities;

import implementation.GeoPos;
import org.json.JSONException;
import org.json.JSONObject;

public class Agent {
    int id, dest, src;
    double value;
    double speed;
    GeoPos pos;
    public Agent(int id, int src){
        this.id = id;
        this.src = src;
    }

    public void update(JSONObject agentObj) throws JSONException {
        value = agentObj.getDouble("value");
        src = agentObj.getInt("src");
        dest = agentObj.getInt("dest");
        speed = agentObj.getDouble("speed");
        pos = GeoPos.fromString(agentObj.getString("pos"));
    }
}
