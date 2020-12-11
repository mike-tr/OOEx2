package gameClient.utilities;

public class PosInt {
    /*
    simply pos x,y
    bare bones nothing more, use Pos, for calculations!
     */
    public int x;
    public int y;
    public PosInt(int x, int y){
        this.x = x;
        this.y = y;
    }

    public PosInt(double x, double y){
        this.x = (int)x;
        this.y = (int)y;
    }

    public PosInt(Pos original){
        this(original.x, original.y);
    }

    @Override
    public String toString() {
        return "x = " + x + ", y = " + y;
    }
}
