package gameClient.GameData;

import java.util.ArrayList;

/**
 * Manager for PokaGroups, its responsible for updating everything related to pokemons
 */
public class PokaGroupCreator {

    public ArrayList<PokaGroup> pokaGroups = new ArrayList<>();
    private ArrayList<Pokemon> old = null;
    protected PokemonGameData gameHandler;

    /**
     *  thus class is responsible for merging few pokemons that are "connected" together,
     *  that way our advanced Agent would pick a group of pokemons to eat, and always would target the "tail"
     *  of the group so he can eat the pokemons in one go.
     * @param gameHandler game manager
     */
    public PokaGroupCreator(PokemonGameData gameHandler){
        this.gameHandler = gameHandler;
    }

    public ArrayList<PokaGroup> getPokaGroups() {
        return pokaGroups;
    }

    /**
     * the first time the method was created we would initialize everything
     */
    public void initialize(){
        old = gameHandler.getPokemons();
        for (var p: old) {
            findGroup(p);
        }

        System.out.println("INITIALIZED");
        for (var group: pokaGroups) {
            group.updateHead(pokaGroups);
        }

//        for (var group: pokaGroups) {
//            System.out.println(group);
//        }
    }


    /**
     * Find a group for a given pokemon
     * @param pokemon the target pokemon we need to find group for
     */
    public void findGroup(Pokemon pokemon){
        if(pokemon.getGroup() == null){
            for (var group: pokaGroups) {
                if(group.tryAdd(pokemon)){
                    return;
                }
            }
            var group = new PokaGroup(pokemon);
            pokaGroups.add(group);
        }
    }

    /**
     * Update with groups are connected
     */
    public void updateLinks(){
        if(old == null){
            initialize();
            return;
        }

        var pokemons = gameHandler.getPokemons();
        int m = gameHandler.movesMade();

        for (var p: pokemons) {
            findGroup(p);
        }

        for (var p : old) {
            if(p.getLastUpdate() < m){
                p.getGroup().remove(p);
            }
        }
        old = pokemons;

        //System.out.println(pokaGroups);
        //System.out.println("---------------");
        var iterator = pokaGroups.iterator();
        while (iterator.hasNext()){
            var c = iterator.next();
            if(c.isEmpty()) {
                iterator.remove();
            }
            //System.out.println(c);
        }

        for (var group: pokaGroups) {
            group.updateHead(pokaGroups);
        }
//        for (var group: pokaGroups) {
//            System.out.println(group);
//        }
    }
}
