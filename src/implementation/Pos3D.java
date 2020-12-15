package implementation;

import api.geo_location;

public class Pos3D implements geo_location {
    public static final double EPS1 = 0.001, EPS2 = 0.0003, EPS3 = 0.005;

    double x,y,z;

    public Pos3D(){
        this(0,0,0);
    }

    public Pos3D(double x, double y, double z){
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public String toString() {
        return "x = " + x + ", y = " + y + ", z = " + z;
    }

    public static Pos3D fromString(String string){
        String[] group = string.split(",");
        double x = Double.parseDouble(group[0]);
        double y = Double.parseDouble(group[1]);
        double z = Double.parseDouble(group[2]);
        return new Pos3D(x,y,z);
    }

    public Pos3D(geo_location p){
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

    public Pos3D add(Pos3D b){
        return new Pos3D(x + b.x, y + b.y, z + b.z);
    }

    public Pos3D sub(Pos3D b){
        return new Pos3D(x - b.x, y - b.y, z - b.z);
    }

    public Pos3D scale(double scalar){
        return new Pos3D(x * scalar, y * scalar, z * scalar);
    }

    public double getSqrtMagnitude(){
        return Math.pow(x, 2) + Math.pow(y, 2) + Math.pow(z, 2);
    }

    public double getMagnitude(){
        return Math.sqrt(getSqrtMagnitude());
    }

    public Pos3D normalize(){
        double magnitude = getMagnitude();
        return new Pos3D(x / magnitude, y / magnitude, z / magnitude);
    }

    public boolean checkExtraClose(Pos3D other){
        return sub(other).getSqrtMagnitude() < EPS1 * EPS1; // if the two point extra close
    }

    public boolean checkExtraClose(Pos3D other, int precision){
        if(precision == 0) {
            return sub(other).getSqrtMagnitude() < EPS1 * EPS1; // if the two point extra close
        }else if(precision == 1){
            return sub(other).getSqrtMagnitude() < EPS2 * EPS2;
        }else{
            return sub(other).getSqrtMagnitude() < EPS3 * EPS3;
        }
    }

    public double getSqrtDistance(geo_location g){
        return Math.pow(this.x - g.x(), 2) + Math.pow(this.y - g.y(), 2) + Math.pow(this.z - g.z(), 2);
    }

    @Override
    public double distance(geo_location g) {
        return Math.sqrt(getSqrtDistance(g));
    }
}
