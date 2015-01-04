package de.smuda.matchcity.gameobjects;

/**
 * Created by denni_000 on 02.01.2015.
 */
public class GameObject {
    protected int x, y;
    protected int width = 16;
    protected int height = 16;
    protected int level = 1;
    protected int neighbours = 0;
    protected int multiplier = 1;

    protected FieldType type = FieldType.INIT;
    protected boolean isSelected = false;

    public enum FieldType {
        EMPTY, ROAD, HOUSING, INDUSTRY,
        TRASH, FARM, PARK, SPECIAL, CITY,
        SHOPS, POWER, GOVERNMENT, INIT
    }

    public GameObject(int i, int j) {
        int x = i;
        int y = j;

    }

    public void resetLevel() {
        level = 0;
    }

    public void update(float delta) {

    }

    public void checkNeighbours() {
        // todo: check neighbours
    }

    public void addLevel(int i) {
        level += i;
        //int t = (int) level % 3;
        //level = level - t;
        // todo: T will add some special shit
    }

    public int getLevel() {
        return level;
    }


    public boolean isSelected() { return isSelected; }
    public void setSelected(boolean b) { isSelected = b; }
    public FieldType getType() { return type; }
    public int getX() { return x; }
    public int getY() { return y; }
    public int getWidth() { return width; }
    public int getHeight() {return height;}

}
