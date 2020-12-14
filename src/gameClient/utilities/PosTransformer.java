package gameClient.utilities;

import api.geo_location;

public class PosTransformer {
    private double min_x, min_y;
    private double max_x, max_y;

    private double scaleX;
    private double scaleY;

    private double offset = 0.05;
    private double addX, addY;

    public PosTransformer(int width, int height ,double min_x, double min_y, double max_x, double max_y){
        this.min_x = min_x;
        this.min_y = min_y;
        this.max_x = max_x;
        this.max_y = max_y;
        applyResize(width, height);
    }

    public void applyResize(int width, int height){
        scaleX = width / (max_x - min_x);
        scaleY = height / (max_y - min_y);
        scaleX *= (1 - 2 * offset);
        scaleY *= (1 - 2 * offset);
        addX = width * offset;
        addY = height * offset;
    }

    public PosInt transform(geo_location location){
        return transform(location.x(), location.y());
    }

    public PosInt transform(double x, double y){
        double nx = (x - min_x) * scaleX + addX;
        double ny = (y - min_y) * scaleY + addY;

        return new PosInt((int)(nx), (int)(ny));
    }

    public Pos transformD(geo_location location){
        return transformD(location.x(), location.y());
    }

    public Pos transformD(double x, double y){
        double nx = (x - min_x) * scaleX + addX;
        double ny = (y - min_y) * scaleY + addY;

        return new Pos((int)nx, (int)ny);
    }
}
