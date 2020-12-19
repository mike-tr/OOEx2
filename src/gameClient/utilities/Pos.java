package gameClient.utilities;

import implementation.Vector3D;

/**
 * This class is Pos2d, in the end iam using way more Vector3D, but it's almost the same
 * it provides addition multiplication normalization etc...
 */
public class Pos {
    public double x;
    public double y;
    public Pos(double x, double y){
        this.x = x;
        this.y = y;
    }

    public Pos(PosInt p){
        this.x = p.x;
        this.y = p.y;
    }

    public Pos(Vector3D p){
        this.x = p.x();
        this.y = p.y();
    }

    public Pos add(PosInt b){
        return new Pos(x + b.x, y + b.y);
    }

    public Pos add(Pos b){
        return new Pos(x + b.x, y + b.y);
    }

    public Pos sub(Pos b){
        return new Pos(x - b.x, y - b.y);
    }

    public Pos scale(double scalar){
        return new Pos(x * scalar, y * scalar);
    }

    public double getSqrtMagnitude(){
        return Math.pow(x, 2) + Math.pow(y, 2);
    }

    public double getMagnitude(){
        return Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
    }

    public Pos normalize(){
        double magnitude = getMagnitude();
        return new Pos(x / magnitude, y / magnitude);
    }

    @Override
    public String toString() {
        return "x = " + x + ", y = " + y;
    }
}
