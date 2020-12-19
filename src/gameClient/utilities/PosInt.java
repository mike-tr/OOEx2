package gameClient.utilities;

import implementation.Vector3D;

/**
 * pos 2d but as integers
 */
public class PosInt {

    public int x;
    public int y;

    /**
     * simply pos x,y
     * bare bones nothing more, use Pos, for calculations!
     * @param x
     * @param y
     */
    public PosInt(int x, int y){
        this.x = x;
        this.y = y;
    }

    /**
     * convert from double
     * @param x
     * @param y
     */
    public PosInt(double x, double y){
        this.x = (int)x;
        this.y = (int)y;
    }

    /**
     * convert from Pos double
     * @param original
     */
    public PosInt(Pos original){
        this(original.x, original.y);
    }

    /**
     * convert from vector3D
     * @param original
     */
    public PosInt(Vector3D original){
        this(original.x(), original.y());
    }

    @Override
    public String toString() {
        return "x = " + x + ", y = " + y;
    }
}
