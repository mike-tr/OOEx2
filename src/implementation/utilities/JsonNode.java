package implementation.utilities;

import api.geo_location;
import api.node_data;
import implementation.NodeData;
import implementation.Pos;

import java.io.Serializable;

public class JsonNode implements Serializable {
    public String pos;
    public int id;

    public JsonNode(node_data node){
        id = node.getKey();
        geo_location pos = node.getLocation();
        this.pos = pos.x() + "," + pos.y() + "," + pos.z();
    }

    public NodeData toNodeData(){
        NodeData convert = new NodeData(id);
        convert.setLocation(Pos.fromString(pos));
        return convert;
    }

    @Override
    public String toString() {
        return "\"pos\":\"" + pos + "\",\"id\":" + id;
    }
}
