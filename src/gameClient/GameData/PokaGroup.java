package gameClient.GameData;

import java.util.ArrayList;
import java.util.Comparator;

public class PokaGroup {
    private static int next_id = 0;

    protected PokaGroup head;
    protected PokaGroup tail;

    protected Pokemon main;
    protected ArrayList<Pokemon> overlapping = new ArrayList<>();
    private int id;

    private double value;
    private AgentVG agent;
    private double agentScore;
    public PokaGroup(Pokemon main){
        this.main = main;
        overlapping.add(main);
        main.setGroup(this);
        id = next_id++;
        value = main.getValue();
    }

    /**
     * if the pokemon is on the same edge, it then belongs to this group, so add it.
     * @param pokemon target pokemon
     * @return true if success
     */
    public boolean tryAdd(Pokemon pokemon){
        if(pokemon.edge == main.edge){
            overlapping.add(pokemon);
            overlapping.sort(Comparator.comparing(Pokemon::getPercentage));
            main = overlapping.get(0);
            pokemon.setGroup(this);
            value += pokemon.getValue();
            return true;
        }
        return false;
    }

    /**
     * remove a pokemon from the group
     * @param pokemon the remove target
     */
    public void remove(Pokemon pokemon){
        var ob = overlapping.remove(pokemon);
        if(!ob){
            return;
        }
        value -= pokemon.getValue();
        overlapping.sort(Comparator.comparing(Pokemon::getPercentage));
        if(overlapping.isEmpty()){
            main = null;
            return;
        }
        main = overlapping.get(0);
    }

    /**
     * if any of the head/tail was removed we delete it too.
     */
    private void reupdate(){
        if(tail != null && tail.isEmpty()){
            tail = null;
        }

        if(head != null && head.isEmpty()){
            head = null;
        }
    }

    /**
     * Update the head for this list, either to new one or leave the old one.
     * @param groups list of all groups
     */
    public void updateHead(ArrayList<PokaGroup> groups){
        agentScore *= 0.95;
        reupdate();
        if(head == null){
            for (var group: groups) {
                if(group == this){
                    continue;
                }
                if(this.getFirst() == group){
                    continue;
                }
                if(group.tail == null && group.isHeadOf(this)){
                    this.head = group;
                    group.tail = this;
                    return;
                }
            }
        }

        if(tail == null){
            if(agent != null){
                //System.out.println("?????2???????");
                if(agent.group != this){
                    agent = null;
                    agentScore = 1000;
                    //System.out.println("????????????");
                }
            }
        }
    }



    /**
     * iam the tail of Other if his End is my Start, yet my End is not his start.
     * @param other
     * @return
     */
    private boolean isHeadOf(PokaGroup other){
        return this.main.getSrc() == other.main.getDest()
                && other.main.getSrc() != this.main.getDest();
    }

    /**
     * return if the Group is empty
     * @return true if no pokemons in this group
     */
    public boolean isEmpty(){
        return main == null;
    }

    public void getSrc(){

    }

    @Override
    public String toString() {
        if(isEmpty()){
            return "Empty";
        }
        String add = "";
        if(head != null){
            add += " | head : " + head.id;
        }
        if(tail != null){
            add += " | tail : " + tail.id;
        }
        if(agent != null){
            add += " | Agent : " + agent.getId();
        }
        return "ID : " + id + " | Num members : " + overlapping.size() + " | Edge : " + main.edge + add;
    }

    public void setAgent(AgentVG agent, double score) {
        var first = getFirst();
        first.agent = agent;
        first.agentScore = score;
    }

    public AgentVG getAgent() {
        return getFirst().agent;
    }

    public Pokemon getFirstPokemon(){
        if(tail == null){
            return main;
        }
        return tail.getFirstPokemon();
    }

    public PokaGroup getFirst(){
        if(tail == null){
            return this;
        }
        return tail.getFirst();
    }

    public PokaGroup getHead() {
        return head;
    }

    public double getAgentScore() {
        return getFirst().agentScore;
    }

    public PokaGroup getTail() {
        return tail;
    }

    public double getTotalValue() {
        if(tail == null) {
            double v = value;
            var next = head;
            while (next != null) {
                v += next.value;
                next = next.head;
            }
            return v;
        }
        return getFirst().getTotalValue();
    }
}
