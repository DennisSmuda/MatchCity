package de.smuda.matchcity.gameobjects;

/**
 * Created by denni_000 on 02.01.2015.
 *
 * Base class for all gameobjects (tiles in this case)
 *
 */



public class GameObject {
    protected int x, y;
    protected int width = 16;
    protected int height = 16;
    protected int level = 1;


    protected FieldType type = FieldType.INIT;
    protected boolean isSelected = false;

    public enum FieldType {
        EMPTY, ROAD, HOUSING, INDUSTRY,
        TRASH, FARM, PARK, SPECIAL, CITY,
        SHOPS, POWER, GOVERNMENT, INIT,
        TNT,RESET, SWAP
    }

    public GameObject(int i, int j) {
        x = i;
        y = j;
        level = 1;
    }

    public GameObject(int i, int j, int newLevel) {
        x = i;
        y = j;
        level = newLevel;
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
