package gameClient.GameData;

import api.directed_weighted_graph;
import api.edge_data;
import api.node_data;
import implementation.Pos3D;

import java.util.Iterator;

public class Pokemon {
    private Pos3D pos;
    private Pos3D look;
    private edge_data edge;
    private double value;
    private int type;
    private AgentBasic agent;
    private double distance;
    private int lastUpdate;
    public Pokemon(directed_weighted_graph graph, Pos3D loc, double value, int type){
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
                int t = e.getSrc() - e.getDest() > 0 ? -1 : 1;
                if(t != type){
                    continue;
                }

                var v2 = graph.getNode(e.getDest());
                var p1 = new Pos3D(v1.getLocation());
                var p2 = new Pos3D(v2.getLocation());

                var n = p2.sub(p1).normalize();
                var n2 = p2.sub(pos).normalize();

                if(n.checkExtraClose(n2)){
                    look = p2;
                    edge = e;
                    return;
                }
            }
        }
    }

    @Override
    public String toString() {
        if(agent == null){
            return "Pokemon :: edge : " + edge + " :: type - " + type + " :: AgentBasic : none";
        }
        return "Pokemon :: edge : " + edge + " :: type - " + type + " :: AgentBasic :" + agent.getId();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this){
            return true;
        }

        if(obj == null){
            return false;
        }

        if(obj instanceof Pokemon){
            return same((Pokemon) obj);
        }
        return false;
    }

    public boolean same(Pokemon pokemon){
        return this.value == pokemon.value && pos.checkExtraClose(pokemon.pos, 1);
    }

    public int getSrc(){
        return edge.getSrc();
    }

    public int getDest(){
        return edge.getDest();
    }

    public edge_data getEdge(){
        return edge;
    }

    public double getValue() {
        return value;
    }

    public int getType() {
        return type;
    }

    public Pos3D getPos() {
        return pos;
    }

    public Pos3D getLook(){
        return look;
    }

    public double getDistance() {
        return distance;
    }

    public void setAgent(AgentBasic agent, double distance){
        this.agent = agent;
        this.distance = distance;
    }

    public int getAgentId(){
        return agent == null ? -1 : agent.getId();
    }

    public boolean hasAgent(){
        return agent != null;
    }

    public AgentBasic agent(){
        return agent;
    }

    public void setLastUpdate(int lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public int getLastUpdate() {
        return lastUpdate;
    }
}
