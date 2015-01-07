package de.smuda.matchcity.gameobjects;

/**
 * Created by smuda on 17.12.2014.
 */
public class Shops extends GameObject {

    public Shops(int i, int j) {
        super(i,j);
        type = FieldType.SHOPS;
    }

    public Shops(int i, int j, int newLevel) {
        super(i, j, newLevel);
        type = FieldType.SHOPS;
    }
}
