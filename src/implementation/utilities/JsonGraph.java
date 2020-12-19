package implementation.utilities;

import api.directed_weighted_graph;
import api.edge_data;
import api.node_data;
import com.google.gson.Gson;
import com.google.gson.JsonParser;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * transform a weighted graph into the json version
 */
public class JsonGraph {
    public JsonEdge[] Edges = new JsonEdge[0];
    public JsonNode[] Nodes = new JsonNode[0];

    public JsonGraph(directed_weighted_graph graph){
        if(graph == null){
            return;
        }
        Edges = new JsonEdge[graph.edgeSize()];
        Nodes = new JsonNode[graph.nodeSize()];

        int j,i = j = 0;
        for (node_data node: graph.getV()) {
            Nodes[i] = new JsonNode(node);
            for (edge_data edge: graph.getE(node.getKey())) {
                Edges[j] = new JsonEdge(edge);
                j++;
            }
            i++;
        }
    }

    public static JsonGraph fromFile(String file){
        String input = getReadFile(file);
        return input != null ? fromJson(input) : null;
    }

    public static String getReadFile(String file){
        try {
            FileReader fr = new FileReader(file);
            return JsonParser.parseReader(fr).toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static JsonGraph fromJson(String json){
        Gson gson = new Gson();
        return gson.fromJson(json, JsonGraph.class);
    }

    public static String toJson(directed_weighted_graph graph){
        return new JsonGraph(graph).toJson();
    }

    public String toJson(){
        Gson gson = new Gson();
        String json = gson.toJson(this);
        return json;
    }

    public boolean saveToFile(String fileName){
        try (FileWriter file = new FileWriter(fileName)) {
            file.write(toJson());
            file.flush();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}
