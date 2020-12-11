package implementation;

import java.io.Serializable;
import java.util.ArrayList;

public class Ex2 implements Serializable {
    public static class QueryVar{
        String name;
        String value;

        QueryVar(String name, String value){
            this.name = name;
            this.value = value;
        }

        @Override
        public String toString() {
            return name + "=" + value;
        }
    }


    public static void main(String[] args) {
        ArrayList<QueryVar> given = new ArrayList<>();
        given.add(new QueryVar("A","true"));
        given.add(new QueryVar("B","false"));

        ArrayList<String> hidden = new ArrayList<>();
        hidden.add("C");
        hidden.add("F");
        var queries = getQueries(given, hidden);

        System.out.println("Given : " + given);
        System.out.println("hidden :" + hidden);
        System.out.println("query : " + queries);
    }

    static ArrayList<ArrayList<QueryVar>> getQueries(ArrayList<QueryVar> given, ArrayList<String> hidden){
        ArrayList<ArrayList<QueryVar>> queries = new ArrayList<>();
        queries.add(given);
        for (var hid: hidden) {
            queries = getAdded(queries, hid, new String[]{"true", "false"});
        }
        return queries;
    }

    static ArrayList<ArrayList<QueryVar>> getAdded(ArrayList<ArrayList<QueryVar>> queries, String var, String[] vals){
        ArrayList<ArrayList<QueryVar>> added = new ArrayList<>();
        for (var query: queries) {
            for (var val: vals) {
                QueryVar add = new QueryVar(var, val);
                var list = new ArrayList<>(query);
                list.add(add);
                added.add(list);
            }
        }
        return added;
    }
}
