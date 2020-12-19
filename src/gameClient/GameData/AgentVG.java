package gameClient.GameData;

import java.util.Comparator;

public class AgentVG extends AgentBasic {

    PokaGroup group;

    /**
     * this is the advanced agent that would use GROUPS in order to find pathing more efficeintly,
     * and reduce the amount of "collisions" between other agents, it can be further improved.
     * @param gameHandler game manager
     * @param id agent id
     * @param src initial src
     * @param pokemon initial pokemon
     */
    public AgentVG(PokemonGameData gameHandler, int id, int src, Pokemon pokemon){
        super(gameHandler, id, src, pokemon);

        System.out.println("------------- ID " + id + " ----------------");
        var groups = gameHandler.getGroups();
        groups.sort(Comparator.comparing(PokaGroup::getTotalValue).reversed());
        for (var group: gameHandler.getGroups()) {
            System.out.println(group + " " + group.getTotalValue());
            if(group.getAgent() == null){
                setGroup(group, 0);
                break;
            }
        }
        if(group != null) {
            this.src = group.getFirstPokemon().getSrc();
        }
        //getNextTarget();
    }


    /**
     * when the agent is idle and does nothing, a.k.a the agent is at a Node, but not on edge.
     * we are putting here what should he do next.
     */
    @Override
    protected void onIdle() {
        if(velocity == null) {
            //System.out.println(group + "  -  " + gameHandler.getGroups());
//            if(updateTarget()){
//                return;
//            }
            if(!hasTarget()){
                //System.out.println("STUCK ::  " + group);
                path = algo.shortestPath(src, 0);
                var next = path.get(1).getKey();
                gameHandler.getGame().chooseNextEdge(id, next);
                gameHandler.doMove();
                sleepFor(50);
                return;
            }
            //System.out.println(getTarget());
            if(getTarget().getSrc() == src){
                //System.out.println(group);
                //System.out.println("this");
                gameHandler.getGame().chooseNextEdge(id, getTarget().getDest());
                gameHandler.doMove();
                //sleepFor(5);
            } else{
                //System.out.println(id + " ------- ");
                if(path == null || path.size() == 2 || path.get(0).getKey() != src){
                    //System.out.println(id + " RECALCULATING");
                    path = algo.shortestPath(src, getTarget().getSrc());
                }
                var next = path.get(1).getKey();
                //System.out.println("NEXT - " + next + " :: " + src);
                if(gameHandler.getGame().chooseNextEdge(id, next) > 0){
                    return;
                }
                if(gameHandler.doMove()) {
                    sleepFor(25);
                }
                sleepFor(5);
            }

            //recalculatePath();
        }else{

        }
    }

    /**
     * this is a method that we use when we got an update from the server,
     * we want to make sure our target still exist, and if maybe it can be overitten.
     */
    @Override
    public void evaluateTarget() {
        if(hasTarget()){
            var closest = getClosest();
            if(closest.group != null && closest.group != group){
                group.setAgent(null, 0);
                group = closest.group.getFirst();
                group.setAgent(this, closest.distance);
                setTarget(group.getFirstPokemon(), closest.distance);
            }
            //PokaGroup g = getClosest();

        }
        getNextTarget();
    }

    /**
     * Check if we have target, and make sure the group not empty
     * @return true if has target
     */
    @Override
    public boolean hasTarget() {
        if(group == null){
            return false;
        }else if(group.isEmpty()){
            if(group.getHead() != null) {
                //System.out.println(group.getHead());
                setGroup(group.getHead(), 0);
                group.setAgent(this, 0);
                return !group.isEmpty();
            }
            group = null;
            return false;
        }
        if(getCurrentTarget() == null){
            path = null;
            var p = group.getFirstPokemon();
            setTarget(p, 0);
            return hasTarget();
        }
        return true;
    }

    /**
     * make sure the target is still valid
     * @return returns true if the target is valid
     */
    @Override
    public boolean updateTarget() {
        if(!hasTarget()){
            getNextTarget();
            return true;
        }
        if(group.getAgent() != this){
            group = null;
            getNextTarget();
            return true;
        }
        return false;
    }

    /**
     * get the next Target group or (first) pokemon in the group
     */
    @Override
    protected void getNextTarget() {
        if(hasTarget()){
            if(group.getAgent() == this) {
                var o = getCurrentTarget();
                var p = group.getFirstPokemon();
                if(o != p){
                    path = null;
                }
                setTarget(p, 0);
                return;
            }
        }

        var next = getClosest();
        if(next.group != null){
            setGroup(next.group, next.distance);
        }
    }

    static class closestGroup{
        public double distance;
        public PokaGroup group;
        public closestGroup(double distance, PokaGroup group){
            this.distance = distance;
            this.group = group;
        }
    }

    /**
     * Set the group target
     * @param group the chosen target group
     * @param score the score of the group
     */
    public void setGroup(PokaGroup group, double score){
        if(group != null) {
            this.group = group.getFirst();
            group.setAgent(this, score);
            setTarget(group.getFirstPokemon(), score);
        }else{
            this.group = null;
        }
    }

    /**
     * get the closest group that is not Taken by a closer agent.
     * @return returns the closest group
     */
    private closestGroup getClosest(){
        //System.out.println(this);
        PokaGroup next = null;
        double score = Double.POSITIVE_INFINITY;
        for (var group: gameHandler.getGroups()) {
            if(group.getFirst() != group){
                continue;
            }

            var pokemon = group.getFirstPokemon();
            if(pokemon.getSrc() == src || dest != -1 && pokemon.getSrc() == dest){
                if(group.getAgent() == null || group.getAgent() == this || group.getAgentScore() > 1) {
                    return new closestGroup(0, group);
                }
            }

            var src = this.dest;
            if(dest == -1){
                src = this.src;
            }
            var dist = algo.shortestPathDist(src, pokemon.getSrc());
            if(dist < 0){
                continue;
            }
            var sc = dist * 10 + 100 / (group.getTotalValue() + 15 - speed);
            //var sc = dist * dist * 10 - group.getTotalValue() + 100 - speed;
            //var sc = (dist * Math.min(10 - speed, 1)) * 20 / (group.getTotalValue() + 1 + this.speed) + 1;
            //var sc = dist * dist * 10 + 100 - group.getTotalValue();
            //System.out.println(group + " \n " + " TOTOAL : " + group.getTotalValue() + " DIST : " + dist + " SCORE : " + sc);
            if(sc < score){
                if(group.getAgent() == null || group.getAgent() == this ||
                        (group.getAgentScore() * 0.8 - 1 > sc && group.getAgentScore() > 1)) {
                    next = group;
                    score = sc;
                }
            }
        }
        return new closestGroup(score, next);
    }
}
