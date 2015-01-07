package de.smuda.matchcity.gameobjects;

/**
 * Created by smuda on 16.12.2014.
 */
public class Road extends GameObject {

    public Road(int i, int j) {
        super(i, j);
        type = FieldType.ROAD;
    }

    public Road(int i, int j, int newLevel) {
        super(i, j, newLevel);
        type = FieldType.ROAD;
    }
}
