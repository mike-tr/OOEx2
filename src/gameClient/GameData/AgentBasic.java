package gameClient.GameData;

import api.directed_weighted_graph;
import api.dw_graph_algorithms;
import api.edge_data;
import implementation.Pos3D;
import org.json.JSONException;
import org.json.JSONObject;


public abstract class AgentBasic implements Runnable {
    enum State {
        idle,
        moving,
        onFinal,
    }
    protected int id, dest, src;
    protected double value;
    protected double speed;
    protected Pos3D pos;
    protected Pos3D dest_pos;
    protected Pos3D velocity;
    protected directed_weighted_graph graph;
    protected dw_graph_algorithms algo;
    private Pokemon currentTarget;
    protected edge_data edge;
    protected PokemonGameHandler gameHandler;
    private State state = State.idle;
    private double travelTime = 0;
    public static Object agentW8Stop = new Object();
    public AgentBasic(PokemonGameHandler gameHandler, int id, int src, Pokemon pokemon){
        this.id = id;
        this.gameHandler = gameHandler;
        this.src = src;
        this.graph = gameHandler.getGraph();
        this.pos = new Pos3D(graph.getNode(src).getLocation());
        this.algo = gameHandler.getGraph_algorithms();
        this.currentTarget = pokemon;
        pokemon.setAgent(this, 0);
        if(pokemon != null){
            pokemon.setAgent(this, 0);
        }
        new Thread(this).start();
    }

    @Override
    public final void run() {
        waitForever(agentW8Stop);
        while (gameHandler.getGame().isRunning()) {
//            System.out.println(this + " :: " + gameHandler.getTick());
//            System.out.println(currentTarget);
            switch (state) {
                case idle:
                    onIdle();
                    break;
                case moving:
                case onFinal:
                    // optional
                    onMoving();
                    break;
            }
            sleepFor(25);
        }
    }

    protected abstract void nextEdge();
    /**
     * when the agent has stopped, a.k.a he is on the Node without destination
     */
    protected abstract void onIdle();
    /**
     * when we are moving towards a node
     */
    private final void onMoving(){
        if(state == State.onFinal){
            if(onFinalEdge()){
                return;
            }
        }

        if(targetNotMine()){
            //System.out.println("???");
            removeTarget();
            getNextTarget();
            return;
        }


        waitFor(this, 50);
        // we landed on a cross road.
        if(velocity == null) {
            nextEdge();
            gameHandler.doMove();
        }
    }

    /**
     * When we are on the same edge as the target pokemon
     * this method is straight forward, we call eat when we are close enough
     * then set the target to be null.
     */

    protected abstract void getNextTarget();

    private boolean onFinalEdge(){
        if(velocity == null) {
            nextEdge();
            gameHandler.doMove();
            return true;
        }

        if(!hasTarget()){
            changeState(State.moving);
            return false;
        }if(currentTarget.getSrc() != src){
            changeState(State.moving);
            return false;
        }else if (pos.checkExtraClose(currentTarget.getPos())) {
            System.out.println("Eating");
            //System.out.println(currentTarget);
            sleepFor(10);
            gameHandler.forceMove();
            sleepFor(10);
            currentTarget = null;
            changeState(State.moving);
            //getNextTarget();
        }
        return true;
    }

    /**
     * We update all the relevant data of the agent, based on the query provided by the server.
     * @param agentObj
     * @throws JSONException
     */
    public void updateFromJson(JSONObject agentObj) throws JSONException {
        value = agentObj.getDouble("value");


        src = agentObj.getInt("src");
        var old = dest;
        var dest = agentObj.getInt("dest");
        var speed = agentObj.getDouble("speed");
        var p = Pos3D.fromString(agentObj.getString("pos"));

        if(this.speed != speed || this.dest != dest){
            this.speed = speed;
            this.dest = dest;
            if(dest == - 1){
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

        dest_pos = new Pos3D(target.getLocation());
        var source_pos = new Pos3D(graph.getNode(src).getLocation());


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

    public boolean targetNotMine(){
        return !(hasTarget() && currentTarget.agent() == this);
    }

    public boolean hasTarget(){
        return currentTarget != null;
    }

    protected void removeTarget(){
        currentTarget = null;
    }

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

    private synchronized void changeState(State newState){
        this.state = newState;
        wakeMeUp();
    }

    public void evaluateTarget(){
        if(hasTarget()){
            if(currentTarget.getLastUpdate() != gameHandler.movesMade()){
                System.out.println("INVALID POKEMON");
                currentTarget = null;
            }
        }
    }

    public void wakeMeUp(){
        synchronized (this){
            this.notifyAll();
        }
    }

    public Pos3D getVelocity() {
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

    public Pos3D getPos() {
        return pos;
    }

    public Pos3D getDest_pos() {
        return dest_pos;
    }

    public String description(){
        return "Agent : " + id;
    }

    @Override
    public String toString() {
        return description() + " state : " + state + " has target : " + (currentTarget != null);
    }
}
