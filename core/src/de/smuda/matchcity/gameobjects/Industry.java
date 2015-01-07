package de.smuda.matchcity.gameobjects;

/**
 * Created by smuda on 17.12.2014.
 */
public class Industry extends GameObject {
    public Industry(int i, int j) {
        super(i, j);
        type = FieldType.INDUSTRY;
    }

    public Industry(int i, int j, int newLevel) {
        super(i, j, newLevel);
        type = FieldType.INDUSTRY;
    }
}
