package GameObjects;

/**
 * Created by smuda on 17.12.2014.
 */
public class City extends GameObject {
    public City(int i, int j) {
        super(i, j);
        type = FieldType.CITY;
    }
}
