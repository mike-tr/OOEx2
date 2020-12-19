package implementation;

import api.edge_data;

/**
 * edge inside the graph, its pretty self explanatory
 */
public class EdgeData implements edge_data {
    private int src;
    private int dest;
    private double weight;
    private String info;
    private int tag;

    public EdgeData(int src, int dest, double weight){
        this.src = src;
        this.dest = dest;
        this.weight = weight;
    }

    public static EdgeData invertedEdge(edge_data data){
        return new EdgeData(data.getDest(), data.getSrc(), data.getWeight());
    }

    public static boolean areInverted(edge_data a1, edge_data a2){
        return a1.getDest() == a2.getSrc() && a1.getSrc() == a2.getDest() && a1.getWeight() == a2.getWeight();
    }

    @Override
    public int hashCode() {
        return (src * 10 + 1) * dest + dest;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this){
            return true;
        }

        if(obj == null){
            return false;
        }

        if(obj instanceof EdgeData){
            return same((EdgeData)obj);
        }
        return false;
    }

    @Override
    public String toString() {
        return "src : " + src + " weight :" + weight + " dest : " + dest;
    }

    public boolean same(edge_data data){
        return src == data.getSrc() && dest == data.getDest();
    }

    @Override
    public int getSrc() {
        return src;
    }

    @Override
    public int getDest() {
        return dest;
    }

    @Override
    public double getWeight() {
        return weight;
    }

    @Override
    public String getInfo() {
        return info;
    }

    @Override
    public void setInfo(String s) {
        info = s;
    }

    @Override
    public int getTag() {
        return tag;
    }

    @Override
    public void setTag(int t) {
        tag = t;
    }
}
