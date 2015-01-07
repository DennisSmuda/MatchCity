package de.smuda.matchcity.gameobjects;

/**
 * Created by smuda on 16.12.2014.
 */
public class Farm extends GameObject {

    public Farm(int i, int j) {
        super(i, j);
        type = FieldType.FARM;
    }

    public Farm(int i, int j, int newLevel) {
        super(i, j, newLevel);
        type = FieldType.FARM;
    }
}
