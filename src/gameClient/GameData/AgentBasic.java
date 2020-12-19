package gameClient.GameData;

import api.directed_weighted_graph;
import api.dw_graph_algorithms;
import api.edge_data;
import api.node_data;
import implementation.DWGraph_Algo;
import implementation.Vector3D;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Base Agent, implements all the basic functions that we share with all agents
 */
public abstract class AgentBasic implements Runnable {
    enum State {
        idle,
        moving,
        onFinal,
    }
    protected int id, dest, src;
    protected double value;
    protected double speed;
    protected Vector3D pos;
    protected Vector3D dest_pos;
    protected Vector3D velocity;
    protected directed_weighted_graph graph;
    protected dw_graph_algorithms algo;
    private Pokemon currentTarget;
    protected edge_data edge;
    protected PokemonGameData gameHandler;
    private State state = State.idle;
    protected List<node_data> path = null;
    public static Object agentW8Stop = new Object();

    /**
     * The base for all agent some stuff just do not change, like handeling movement, calculating real position
     * getting server updated etc...
     * this class handles all the basic stuff that all agents have in common
     * @param gameHandler gama manager
     * @param id agent id
     * @param src initial src
     * @param pokemon initial pokemon
     */
    public AgentBasic(PokemonGameData gameHandler, int id, int src, Pokemon pokemon){
        this.id = id;
        this.gameHandler = gameHandler;
        this.src = src;
        this.graph = gameHandler.getGraph();
        this.pos = new Vector3D(graph.getNode(src).getLocation());
        this.algo = new DWGraph_Algo(gameHandler.getGraph_algorithms().copy());
        this.currentTarget = pokemon;
        if(pokemon != null){
            pokemon.setAgent(this, 0);
        }
        new Thread(this).start();
    }

    /**
     * the main loop of the agent
     */
    @Override
    public final void run() {
        waitForever(agentW8Stop);
        while (gameHandler.getGame().isRunning()) {
            //System.out.println(this + " :: " + gameHandler.getTick());
            //System.out.println(currentTarget);
            switch (state) {
                case idle:
                    if(velocity != null) {
                        changeState(State.moving);
                        continue;
                    }
                    onIdle();
                    break;
                case moving:
                case onFinal:
                    // optional
                    onMoving();
                    break;
            }
            updatePosition();
            waitFor(Agent.agentW8Stop, 25);
        }
    }

    /**
     * when the agent has stopped, a.k.a he is on the Node without destination
     */
    protected abstract void onIdle();
    /**
     * when we are moving towards a node
     */
    private final void onMoving(){
        if(velocity == null) {
            changeState(State.idle);
            return;
        }

        if(!hasTarget()){
            getNextTarget();
            return;
        }

        if(src == getCurrentTarget().getSrc()){
            if(onFinalEdge()){
                return;
            }
        }

        waitFor(this, 15);
        if(velocity == null) {
            sleepFor(10);
            if(velocity == null) {
                changeState(State.idle);
            }
        }
        //waitFor(this, 50);
    }

    public Pokemon getCurrentTarget(){
        return currentTarget;
    }

    /**
     * When we are on the same edge as the target pokemon
     * this method is straight forward, we call eat when we are close enough
     * then set the target to be null.
     */

    protected abstract void getNextTarget();

    private boolean onFinalEdge(){
        if (pos.checkExtraClose(getCurrentTarget().getPos(), 0)) {
            //System.out.println("Eating");
            //System.out.println(currentTarget);
            //sleepFor(5);
            gameHandler.forceMove();
            sleepFor(5);
            currentTarget = null;
            changeState(State.moving);
            getNextTarget();
        }
        return true;
    }

    /**
     * We updateAll overlapping the relevant data of the agent, based on the query provided by the server.
     * @param agentObj JSON Object with the agent data
     * @throws JSONException
     */
    public void updateFromJson(JSONObject agentObj) throws JSONException {
        value = agentObj.getDouble("value");


        src = agentObj.getInt("src");
        var dest = agentObj.getInt("dest");
        var speed = agentObj.getDouble("speed");
        var p = Vector3D.fromString(agentObj.getString("pos"));
        if(this.speed != speed || this.dest != dest){
            this.speed = speed;
            this.dest = dest;
            if(dest == - 1){
                if(currentTarget != null && edge != null){
                    currentTarget.setAgent(this, currentTarget.getDistance() - edge.getWeight());
                }
                pos = p;
                velocity = null;
                return;
            }
            pos = p;
            calculateVelocity();
        } else if(!pos.checkExtraClose(p)){
            pos = p;
            return;
        }
        wakeMeUp();
    }

    public void waitFor(Object lock, int timeMillis){
        try{
            synchronized (lock){
                lock.wait(timeMillis);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void waitForever(Object lock){
        try{
            synchronized (lock){
                lock.wait();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    protected void sleepFor(int millis){
        try{
            Thread.sleep(millis);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * We calculate the velocity of the agent based on the edge its currently on.
     */
    double time_left = 0;
    private void calculateVelocity(){
        var target = graph.getNode(dest);
        edge = graph.getEdge(src, dest);

        dest_pos = new Vector3D(target.getLocation());
        var source_pos = new Vector3D(graph.getNode(src).getLocation());


        var max_length = source_pos.distance(dest_pos);
        var length_left = dest_pos.distance(pos);
        time_left = (length_left / max_length) * edge.getWeight() / speed;

        velocity = dest_pos.sub(source_pos).scale(speed / edge.getWeight());
        time = System.currentTimeMillis();
    }

    /**
     * this method is for graphics we would calculate the position of the agent at time x, based on velocity,
     * those printing his new position.
     */
    long time;
    public void updatePosition(){
        if(velocity != null) {
            long t = time;
            time = System.currentTimeMillis();
            double passed = (time - t) * 0.001;
            pos = pos.add(velocity.scale(passed));
            time_left -= passed;

            if(time_left < 0){
                velocity = null;
                wakeMeUp();
            }
        }
    }

    protected Pokemon getTarget(){
        return currentTarget;
    }

    /**
     * check if target valid
     * @return true if target valid
     */
    public boolean updateTarget(){
        if(!(hasTarget() && currentTarget.agent() == this)){
            removeTarget();
            getNextTarget();
            return true;
        }
        return false;
    }


    public boolean hasTarget(){
        return currentTarget != null;
    }

    protected void removeTarget(){
        currentTarget = null;
    }

    /**
     * set the next target pokemon
     * @param target chosen pokemon
     * @param distance distance to pokemon
     */
    protected void setTarget(Pokemon target, double distance){
        if(target == null){
            currentTarget = null;
            return;
        }
        currentTarget = target;
        currentTarget.setAgent(this, distance);
        if(state == State.moving && currentTarget.getSrc() == src){
            changeState(State.onFinal);
        }
    }

    /**
     * change the state
     * @param newState
     */
    private synchronized void changeState(State newState){
        this.state = newState;
        wakeMeUp();
    }

    /**
     * check if the target still valid, on server update
     */
    public void evaluateTarget(){
        if(!hasTarget()){
            getNextTarget();
            return;
        }

        var target = currentTarget;
        currentTarget.setAgent(null, 0);
        getNextTarget();
        if(currentTarget == target){
            return;
        }

        //System.out.println("Found Better one!");
    }

    public void wakeMeUp(){
        synchronized (this){
            this.notifyAll();
        }
    }

    public Vector3D getVelocity() {
        return velocity;
    }

    public int getDest() {
        return dest;
    }

    public int getId(){
        return id;
    }

    public int getSrc() {
        return src;
    }

    public Vector3D getPos() {
        return pos;
    }

    public Vector3D getDest_pos() {
        return dest_pos;
    }

    public String description(){
        return "Agent : " + id;
    }

    public double getValue() {
        return value;
    }

    @Override
    public String toString() {
        return description() + " state : " + state + " has target : " + (currentTarget != null);
    }
}
