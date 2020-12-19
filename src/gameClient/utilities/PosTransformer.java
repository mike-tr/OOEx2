package gameClient.utilities;

import api.geo_location;

/**
 * this class is responsible to fit the SCREEN and the graph!!
 */
public class PosTransformer {
    private double min_x, min_y;
    private double max_x, max_y;

    private double scaleX;
    private double scaleY;

    private double offset = 0.05;
    private double addX, addY;

    /**
     * get the height width of drawing space, and the min max x,y values, generate a transformer from it
     * we would simply apply the transformation on any given Vector3D we get, and return a new within screen position
     * @param width
     * @param height
     * @param min_x
     * @param min_y
     * @param max_x
     * @param max_y
     */
    public PosTransformer(int width, int height ,double min_x, double min_y, double max_x, double max_y){
        this.min_x = min_x;
        this.min_y = min_y;
        this.max_x = max_x;
        this.max_y = max_y;
        applyResize(width, height);
    }

    /**
     * on scale of the screen we need to recalculate the transformer
     * @param width
     * @param height
     */
    public void applyResize(int width, int height){
        scaleX = width / (max_x - min_x);
        scaleY = height / (max_y - min_y);
        scaleX *= (1 - 2 * offset);
        scaleY *= (1 - 2 * offset);
        addX = width * offset;
        addY = height * offset;
    }

    /**
     * use the transformation on geo_location
     * @param location
     * @return
     */
    public PosInt transform(geo_location location){
        return transform(location.x(), location.y());
    }

    /**
     * use transformation on given x,y
     * @param x
     * @param y
     * @return
     */
    public PosInt transform(double x, double y){
        double nx = (x - min_x) * scaleX + addX;
        double ny = (y - min_y) * scaleY + addY;

        return new PosInt((int)(nx), (int)(ny));
    }

    /**
     * use the rtansformation on geo location but spit out a double version ( more precise and with math )
     * @param location
     * @return
     */
    public Pos transformD(geo_location location){
        return transformD(location.x(), location.y());
    }

    /**
     * spit out transformation pos but as a double vector.
     * @param x
     * @param y
     * @return
     */
    public Pos transformD(double x, double y){
        double nx = (x - min_x) * scaleX + addX;
        double ny = (y - min_y) * scaleY + addY;

        return new Pos((int)nx, (int)ny);
    }
}
