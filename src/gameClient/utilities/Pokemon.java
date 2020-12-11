package gameClient.utilities;

import api.directed_weighted_graph;
import api.edge_data;
import api.geo_location;
import api.node_data;

import java.util.Iterator;

public class Pokemon {
    private geo_location pos;
    private double value;
    private int type;
    private Agent agent;
    public Pokemon(directed_weighted_graph graph, geo_location loc, double value, int type){
        pos = loc;
        this.value = value;
        this.type = type;
        calculateEdge(graph);
    }

    private void calculateEdge(directed_weighted_graph graph){
        Iterator<node_data> itr = graph.getV().iterator();
        while(itr.hasNext()) {
            node_data v1 = itr.next();
            Iterator<edge_data> iter = graph.getE(v1.getKey()).iterator();
            while(iter.hasNext()) {
                edge_data e = iter.next();
                var v2 = graph.getNode(e.getDest());


                //boolean f = isOnEdge(pos e,fr.getType(), g);
                //if(f) {fr.set_edge(e);}
            }
        }
    }


    public double getValue() {
        return value;
    }

    public int getType() {
        return type;
    }

    public geo_location getPos() {
        return pos;
    }

    public void setAgent(Agent agent){
        this.agent = agent;
    }

    public Agent agent(){
        return agent;
    }
}
