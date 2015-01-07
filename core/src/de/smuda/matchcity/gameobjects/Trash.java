package de.smuda.matchcity.gameobjects;

/**
 * Created by smuda on 17.12.2014.
 */
public class Trash extends GameObject {
    public Trash(int i, int j) {
        super(i, j);
        type = FieldType.TRASH;
    }

    public Trash(int i, int j, int newLevel) {
        super(i, j, newLevel);
        type = FieldType.TRASH;
    }
}
