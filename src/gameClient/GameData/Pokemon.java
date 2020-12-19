package gameClient.GameData;

import api.directed_weighted_graph;
import api.edge_data;
import api.node_data;
import implementation.Vector3D;

import java.util.Iterator;

/**
 * Pokemon data implementation
 */
public class Pokemon{
    private Vector3D pos;
    private Vector3D look;
    protected edge_data edge;
    protected double value;
    protected int type;
    private AgentBasic agent;
    private double distance;
    private int lastUpdate;
    protected int hash = 0;
    protected double percentage;
    protected int initializationTime = -1;
    protected PokaGroup group;
    private int id;

    private static int next_id = 0;

    /**
     * This class would create a pokemon, at the given location, as well as calculating
     * the pokemon Edge.
     * @param graph the graph with we are on
     * @param loc the position
     * @param value the value of the pokemon
     * @param type the type
     * @param moves number of moves made so far
     */
    public Pokemon(directed_weighted_graph graph, Vector3D loc, double value, int type, int moves){
        pos = loc;
        //this.initializationTime = lastUpdate = moves;
        this.value = value;
        this.type = type;
        calculateEdge(graph);
        id = next_id++;
        if(next_id > 99){
            next_id = -99;
        }
    }

    /**
     * find the edge the pokemon is located on
     * @param graph
     */
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
                var p1 = new Vector3D(v1.getLocation());
                var p2 = new Vector3D(v2.getLocation());
                if(Vector3D.inRect(p1,p2,pos) == false){
                    continue;
                }


                var n = p2.sub(p1).normalize();
                var n2 = p2.sub(pos).normalize();

                if(n.checkExtraClose(n2)){
                    look = p2;
                    edge = e;
                    percentage = p1.sub(pos).getSqrtMagnitude();
                    hash = edge.getDest() * (edge.getSrc() + 1) + edge.getSrc();
                    return;
                }
            }
        }
    }

    /**
     * return the pokemon description
     * @return agent string data
     */
    @Override
    public String toString() {
        if(agent == null){
            return "Pokemon-ID : " + id + " :: edge : " + edge + " :: AgentBasic : none";
        }
        return "Pokemon-ID : " + id + " :: edge : " + edge + " :: AgentBasic :" + agent.getId();
    }

    /**
     * if the pokemon is the same one a.k.a same geo pos, we return true
     * @param obj the object we comparing too
     * @return true if equals
     */
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

    @Override
    public int hashCode() {
        return hash;
    }

    /**
     * the pokemons are the same if they are on the same geo-location
     * @param pokemon
     * @return
     */
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

    public Vector3D getPos() {
        return pos;
    }

    public Vector3D getLook(){
        return look;
    }

    public double getDistance() {
        return distance;
    }

    /**
     * mark this pokemon as the target of agent.
     * @param agent
     * @param distance
     */
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
        if(agent != null) {
            return agent;
        }
        return group.getFirst().getAgent();
    }

    public void setLastUpdate(int lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public boolean isFresh(){
        return initializationTime == lastUpdate;
    }

    public int getLastUpdate() {
        return lastUpdate;
    }

    public PokaGroup getGroup() {
        return group;
    }

    public void setGroup(PokaGroup group) {
        this.group = group;
    }

    public double getPercentage() {
        return percentage;
    }

    public int getID(){
        return id;
    }
}
