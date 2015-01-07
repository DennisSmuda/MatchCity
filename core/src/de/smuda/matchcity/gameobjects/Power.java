package de.smuda.matchcity.gameobjects;

/**
 * Created by smuda on 17.12.2014.
 */
public class Power extends GameObject {
    public Power(int i, int j) {
        super(i, j);
        type = FieldType.POWER;
    }

    public Power(int i, int j, int newLevel) {
        super(i, j, newLevel);
        type = FieldType.POWER;
    }
}
