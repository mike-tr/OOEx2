package implementation;

import api.geo_location;

public class GeoPos implements geo_location {
    double x,y,z;

    public GeoPos(){
        this(0,0,0);
    }

    public GeoPos(double x, double y, double z){
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public String toString() {
        return "x = " + x + ", y = " + y + ", z = " + z;
    }

    public static GeoPos fromString(String string){
        String[] group = string.split(",");
        double x = Double.parseDouble(group[0]);
        double y = Double.parseDouble(group[1]);
        double z = Double.parseDouble(group[2]);
        return new GeoPos(x,y,z);
    }

    public GeoPos(geo_location p){
        this.x = p.x();
        this.y = p.y();
        this.z = p.z();
    }
    @Override
    public double x() {
        return x;
    }

    @Override
    public double y() {
        return y;
    }

    @Override
    public double z() {
        return z;
    }

    @Override
    public double distance(geo_location g) {
        double sqrtMag = Math.pow(this.x - g.x(), 2) + Math.pow(this.y - g.y(), 2) + Math.pow(this.z - g.z(), 2);
        return Math.sqrt(sqrtMag);
    }
}
