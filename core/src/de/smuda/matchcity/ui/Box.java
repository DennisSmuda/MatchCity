package de.smuda.matchcity.ui;

/**
 * Created by denni_000 on 02.01.2015.
 */
public class Box {

    protected float x;
    protected float y;
    protected float width;
    protected float height;

    public boolean contains(float x, float y) {
        return x > this.x - width/2 &&
                x < this.x + width / 2 &&
                y > this.y - height / 2 &&
                y < this.y + height / 2;
    }

    public float getx() { return x; }
    public float gety() { return y; }
    public float getWidth() { return width; }
    public float getHeight() { return height; }
}
