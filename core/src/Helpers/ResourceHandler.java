package Helpers;

import GameWorld.GameWorld;

/**
 * Created by smuda on 17.12.2014.
 */
public class ResourceHandler {

    private GameWorld myWorld;

    // Mandatory needs
    public static int citizens = 0;
    public static int food = 0;
    public static int trash = 0;
    public static int power = 0;

    // Field counters
    public static int farms = 0;
    public static int trashFields = 0;
    public static int houses = 0;



    // secondary
    public static int jobs;


    public ResourceHandler(GameWorld world) {
        myWorld = world;
        resetCounters();
    }

    public static void update(float delta) {
        food = food + (farms * 10);
        System.out.println("handler update! " + delta);



    }

    public void updateFields() {
        for (int i = 0; i < 7; i++) {
            for( int j = 0; j < 8; j++) {
                switch (myWorld.getGameObject(i, j).getType()) {
                    case FARM:
                        farms++;
                        break;
                    case HOUSING:
                        houses++;
                        break;

                    default:
                        break;
                }
            }
        }
    }


    public void resetCounters() {
        farms = 0;
        trashFields = 0;
    }

}
