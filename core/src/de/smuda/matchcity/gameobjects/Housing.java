package de.smuda.matchcity.gameobjects;

/**
 * Created by smuda on 15.12.2014.
 */
public class Housing extends GameObject {

    public Housing(int i, int j) {
        super(i, j);
        type = FieldType.HOUSING;
    }

    public Housing(int i, int j, int newLevel) {
        super(i, j, newLevel);
        type = FieldType.HOUSING;
    }
}

