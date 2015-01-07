package de.smuda.matchcity.gameobjects;

/**
 * Created by smuda on 17.12.2014.
 */
public class Park extends GameObject {

    public Park(int i, int j) {
        super(i, j);
        type = FieldType.PARK;
    }

    public Park(int i, int j, int newLevel) {
        super(i, j, newLevel);
        type = FieldType.PARK;
    }
}
