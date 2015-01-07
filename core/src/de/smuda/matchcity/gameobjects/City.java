package de.smuda.matchcity.gameobjects;

/**
 * Created by smuda on 17.12.2014.
 */
public class City extends GameObject {
    public City(int i, int j) {
        super(i, j);
        type = FieldType.CITY;
    }

    public City(int i, int j, int newLevel) {
        super(i, j, newLevel);
        type = FieldType.CITY;
    }
}
