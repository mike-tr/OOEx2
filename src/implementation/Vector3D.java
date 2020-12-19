package implementation;

import api.geo_location;

public class Vector3D implements geo_location {
    public static final double EPS1 = 0.001, EPS2 = 0.0003, EPS3 = 0.003, EPS4 = 0.0001;

    double x,y,z;

    public Vector3D(){
        this(0,0,0);
    }

    public Vector3D(double x, double y, double z){
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public String toString() {
        return "x = " + x + ", y = " + y + ", z = " + z;
    }

    public static Vector3D fromString(String string){
        String[] group = string.split(",");
        double x = Double.parseDouble(group[0]);
        double y = Double.parseDouble(group[1]);
        double z = Double.parseDouble(group[2]);
        return new Vector3D(x,y,z);
    }

    public Vector3D(geo_location p){
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

    /**
     * add vector to another one
     * @param b
     * @return
     */
    public Vector3D add(Vector3D b){
        return new Vector3D(x + b.x, y + b.y, z + b.z);
    }

    /**
     * subtract vector from another one
     * @param b
     * @return
     */
    public Vector3D sub(Vector3D b){
        return new Vector3D(x - b.x, y - b.y, z - b.z);
    }

    /**
     * scale the vector by the value
     * @param scalar
     * @return
     */
    public Vector3D scale(double scalar){
        return new Vector3D(x * scalar, y * scalar, z * scalar);
    }

    /**
     * get the squared magnitude
     * @return
     */
    public double getSqrtMagnitude(){
        return Math.pow(x, 2) + Math.pow(y, 2) + Math.pow(z, 2);
    }

    /**
     * get the magnitude of the vector a.k.a the distance from zero
     * @return
     */
    public double getMagnitude(){
        return Math.sqrt(getSqrtMagnitude());
    }

    /**
     * normalize the "vector" a.k.a make the distance too 0,0,0 to be exactly 1
     * @return
     */
    public Vector3D normalize(){
        double magnitude = getMagnitude();
        return new Vector3D(x / magnitude, y / magnitude, z / magnitude);
    }

    /**
     * check if two points are "the same", by epsilone 4
     * @param other
     * @return
     */
    public boolean checkExtraClose(Vector3D other){
        return sub(other).getSqrtMagnitude() < EPS4 * EPS4; // if the two point extra close
    }

    /**
     * on varius levels check if two poins are really close together
     * @param other
     * @param precision
     * @return
     */
    public boolean checkExtraClose(Vector3D other, int precision){
        if(precision == 0) {
            return sub(other).getSqrtMagnitude() < EPS1 * EPS1; // if the two point extra close
        }else if(precision == 1){
            return sub(other).getSqrtMagnitude() < EPS2 * EPS2;
        }else{
            return sub(other).getSqrtMagnitude() < EPS3 * EPS3;
        }
    }

    /**
     * get the squared distance between two point, its much faster to compute
     * @param g
     * @return
     */
    public double getSqrtDistance(geo_location g){
        return Math.pow(this.x - g.x(), 2) + Math.pow(this.y - g.y(), 2) + Math.pow(this.z - g.z(), 2);
    }

    /**
     * get the distance between two points
     * @param g
     * @return
     */
    @Override
    public double distance(geo_location g) {
        return Math.sqrt(getSqrtDistance(g));
    }

    /**
     * Check if the target vector is in bounds inside the "Area" created by p1 and p2
     * @param rect_p1
     * @param rect_p2
     * @param target
     * @return
     */
    public static boolean inRect(Vector3D rect_p1, Vector3D rect_p2, Vector3D target){
        double min_x = Math.min(rect_p1.x, rect_p2.x) - EPS4;
        double min_y = Math.min(rect_p1.y, rect_p2.y) - EPS4;
        double max_x = Math.max(rect_p1.x, rect_p2.x) + EPS4;
        double max_y = Math.max(rect_p1.y, rect_p2.y) + EPS4;

        return target.x > min_x && target.x < max_x && target.y > min_y && target.y < max_y;
    }
}
