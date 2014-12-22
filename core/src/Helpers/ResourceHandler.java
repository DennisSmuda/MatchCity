package Helpers;

/**
 * Created by smuda on 17.12.2014.
 */
public class ResourceHandler {

    // Mandatory needs
    public static int citizens = 0;
    public static int food = 0;
    public static int trash = 0;
    public static int power = 0;

    // Field counters
    public static int farms = 0;
    public static int trashFields = 0;



    // secondary
    public static int jobs;


    public static void update(float delta) {
        food = food + (farms * 10);

    }


}
