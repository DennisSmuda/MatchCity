package de.smuda.matchcity.gameobjects;

/**
 * Created by smuda on 15.12.2014.
 */
public class Housing extends GameObject {

    private int level;


    public Housing(int i, int j) {
        super(i, j);
        type = FieldType.HOUSING;
        level = 1;
    }

    public Housing(int i, int j, int newLevel) {
        super(i, j, newLevel);
        type = FieldType.HOUSING;
    }

    public void addLevel(int i) {
        level += i;
    }


}

