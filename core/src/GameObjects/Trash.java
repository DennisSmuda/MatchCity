package GameObjects;

/**
 * Created by smuda on 17.12.2014.
 */
public class Trash extends GameObject {
    public Trash(int i, int j) {
        super(i, j);
        type = FieldType.TRASH;
    }
}
