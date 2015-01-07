package de.smuda.matchcity.gameobjects;

/**
 * Created by smuda on 16.12.2014.
 */
public class Government extends GameObject {

    public Government(int i, int j) {
        super(i, j);
        type = FieldType.GOVERNMENT;
    }

    public Government(int i, int j, int newLevel) {
        super(i, j, newLevel);
        type = FieldType.GOVERNMENT;
    }
}
