package de.smuda.matchcity.gameobjects;

/**
 * Created by smuda on 17.12.2014.
 */
public class Special extends GameObject {


    public Special(int i, int j) {
        super(i, j);
        type = FieldType.SPECIAL;
    }

    public Special(int i, int j, int newLevel) {
        super(i, j, newLevel);
        type = FieldType.SPECIAL;
    }


}
