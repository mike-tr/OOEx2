package implementation;

import api.geo_location;
import api.node_data;

public class NodeData implements node_data {
    private int id;
    private GeoPos pos;
    private String info;
    private int tag;
    private double weight;

    public NodeData(int id){
        this.id = id;
        this.pos = new GeoPos();
    }

    public NodeData(node_data node){
        this.id = node.getKey();
        setLocation(node.getLocation());
    }

    @Override
    public String toString() {
        return "" + getKey();
    }

    @Override
    public int getKey() {
        return id;
    }

    @Override
    public geo_location getLocation() {
        return pos;
    }

    @Override
    public void setLocation(geo_location p) {
        this.pos = new GeoPos(p);
    }

    @Override
    public double getWeight() {
        return weight;
    }

    @Override
    public void setWeight(double w) {
        this.weight = w;
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
        this.tag = t;
    }
}
