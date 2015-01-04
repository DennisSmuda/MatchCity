package de.smuda.matchcity.gameobjects;

/**
 * Created by smuda on 15.12.2014.
 */
public class EmptyField extends GameObject {



    public EmptyField(int i, int j) {
        super(i, j);
        type = FieldType.EMPTY;

        resetLevel();
    }


}
