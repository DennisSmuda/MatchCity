package GameWorld;


import GameObjects.*;
import Helpers.InputHandler;
import Helpers.ResourceHandler;
import UI.GameButton;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by smuda on 12.12.2014.
 */
public class GameWorld {

    private FieldState[][] feld;
    public GameObject[][] gameField;
    public int[] nextThree;
    public int[] nextRandoms;
    public boolean matchFoundH = false;
    public boolean matchFoundV = false;

    public GameObject[] neighbourField;

    private int netWorth = 0;

    private int firstHTile = -1;
    private int firstVTile = -1;

    private int freeTiles;

    private GameState currentState;
    public enum GameState {
        READY, RUNNING, GAMEOVER

    }

    private int maxStartingRoads = 6;
    private int roadsOnField = 0;
    private int maxRoads = 20;
    private boolean maxRoadB = false;


    // MoneyStuff



    public enum FieldState {
        EMPTY, ROAD, HOUSING, INDUSTRY,
        TRASH, FARM, PARK, SPECIAL, CITY,
        SHOPS, POWER, GOVERNMENT,
    }


    public GameWorld( int midPointY) {
        // Initialize next three elements
        nextThree = new int[3];
        nextRandoms = new int[5];

        freeTiles = 72;

        // First three
        for (int i = 0; i < 3; i++) {
            nextThree[i] = randInt(1, 11);
        }
        // First Randoms
        for (int i = 0; i < 5; i++) {
            nextRandoms[i] = randInt(1, 11);
        }

        // Initialize
        gameField = new GameObject[8][9];
        initializeField();

        checkFreeTiles();
        System.out.println("FreeTiles: " + getNumFree());

        currentState = GameState.READY;

    }

    public void update(float delta) {
        switch (currentState) {
            case READY:
                updateReady(delta);
                break;

            case GAMEOVER:
                updateGameOver(delta);
                break;

            case RUNNING:
            default:
                updateRunning(delta);
                break;
        }
    }

    private void updateReady(float delta) {

    }

    public void updateRunning(float delta) {
        if(delta > .15f) {
            delta = .15f;
            //ResourceHandler.update(delta);
            //System.out.println("handler update! " + delta);
        }
    }

    public void updateGameOver(float delta) {
        // show restart button and stuff.
    }

    public void addMoney(int increment) {
        netWorth += increment;
    }

    public boolean isReady() { return currentState == GameState.READY; }

    public void start() { currentState = GameState.RUNNING; }

    public void gameOver() { currentState = GameState.GAMEOVER;}


    public GameObject getGameObject(int i, int j) {
        return gameField[i][j];
    }

    public void setGameObject(int i, int j, int type) {
        switch (type) {
            case 0:
                gameField[i][j] = new EmptyField(i, j);
                break;
            case 1:
                gameField[i][j] = new Housing(i, j);
                break;
            case 2:
                gameField[i][j] = new Farm(i, j);
                break;
            case 3:
                gameField[i][j] = new Road(i, j);
                break;
            case 4:
                gameField[i][j] = new Government(i, j);
                break;
            case 5:
                gameField[i][j] = new Park(i, j);
                break;
            case 6:
                gameField[i][j] = new Shops(i,j);
                break;
            case 7:
                gameField[i][j] = new Power(i, j);
                break;
            case 8:
                gameField[i][j] = new Industry(i, j);
                break;
            case 9:
                gameField[i][j] = new Special(i, j);
                break;
            case 10:
                gameField[i][j] = new Trash(i, j);
                break;
            case 11:
                gameField[i][j] = new City(i, j);
                break;
            default:
                break;
        }

    }

    public int getWorth() {
        return netWorth;
    }
    public int getNumRoads() {return roadsOnField;}
    public void addRoad() {roadsOnField++ ;}

    public static int randInt(int min, int max) {
        Random rand = new Random();
        int randomNum = rand.nextInt((max - min) + 1) + min;
        return randomNum;
    }

    public void calculateNextThree() {
        nextThree[0] = nextThree[1];
        nextThree[1] = nextThree[2];
        nextThree[2] = randInt(1, 11);

        int next = randInt(1, 100);
        if (next < 20) {
            nextThree[2] = 1; // house
        } else if (next < 40) {
            nextThree[2] = 2; // farm
        } else if (next < 50 && getMaxRoadB()) {
        nextThree[2] = 3; // road
        } else if (next < 60) {
            nextThree[2] = 7; // power
        } else if (next < 70) {
            nextThree[2] = 10; // trash
        } else if (next < 75) {
            nextThree[2] = 5; // park
        } else if (next < 80) {
            nextThree[2] = 8; // industry
        } else if (next < 85) {
            nextThree[2] = 6; // shops
        } else if(next < 90) {
            nextThree[2] = 11; // CITY
        } else if (next < 95) {
            nextThree[2] = 9;
        } else {
            nextThree[2] = 2;
        }



        // Todo: Debug all housing spawn
        //nextThree[0] = 1;
        //nextThree[1] = 1;
        //nextThree[2] = 1;

    }

    public int getNext() {
        return nextThree[0];
    }

    public boolean getMaxRoadB() {
        return (roadsOnField == maxRoads);
    }

    public void checkFreeTiles() {
        freeTiles = 0;
        int emptyFields = 0;

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 9; j++) {
                if (getGameObject(i, j).getType() == GameObject.FieldType.EMPTY) {
                    freeTiles++;
                }
            }
        }

        System.out.println("empty fields: " + freeTiles);
    }


    // returns 1 for hmatch, 2 for vmatch
    public int checkMatches() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 9; j++) {

                int currentLvl = gameField[i][j].getLevel();

                GameObject.FieldType currentT = gameField[i][j].getType();
                // dont check empty fields and roads for matches
                if (currentT != GameObject.FieldType.EMPTY && currentT != GameObject.FieldType.ROAD) {
                    // check to the right for triple match. returns 1
                    if (i < 6) {
                        if (currentT == gameField[i + 1][j].getType() &&
                                currentT == gameField[i + 2][j].getType()) {
                            // horizontal match found!
                            matchFoundH = true;
                            firstHTile = (8 * j) + i;

                            return 1;
                        }
                    }
                    // check downwards for triple match. return 2
                    if (j < 7) {
                        if (currentT == gameField[i][j + 1].getType() &&
                                currentT == gameField[i][j + 2].getType()) {
                            // vertical match found!!
                            matchFoundV = true;
                            firstVTile = (8 * j) + i;
                            return 2;
                        }
                    }
                    // check for reversed L based shape.
                    if (i < 7 && j < 8) {
                        // double H match
                        if(currentT == gameField[i+1][j].getType()) {
                            if(currentT == gameField[i][j+1].getType()) {
                                firstHTile = (8 * j) + i;
                                return 3;
                            }
                            if(currentT == gameField[i+1][j+1].getType()) {
                                firstHTile = (8*j) +i;
                                return 4;
                            }
                        }
                    }

                    if (i > 0 && j < 8) {
                        // double v match
                        if(currentT == gameField[i][j+1].getType()) {
                            if(currentT == gameField[i-1][j+1].getType()) {
                                firstVTile = (8 * j) + i;
                                return 5;
                            }
                        }
                    }
                    if (i < 7 && j < 8) {
                        if (currentT == gameField[i][j + 1].getType()) {
                            if (currentT == gameField[i + 1][j + 1].getType()) {
                                firstVTile = (8 * j) + i;
                                return 6;
                            }
                        }
                    }
                }
            }
        }
        // todo: maybe check for tetromino shapes?!
        // no match found
        return 0;
    }

    public int getFirstHTile() { return firstHTile; }
    public int getFirstVTile() { return firstVTile; }
    public void resetVHTile() { firstVTile = 0; firstHTile = 0; }

    public void calculateNextRandoms() {
        nextRandoms[0] = randInt(1, 11);
        nextRandoms[1] = randInt(1, 11);
        nextRandoms[2] = randInt(1, 11);
        nextRandoms[3] = randInt(1, 11);
        nextRandoms[4] = randInt(1, 11);

        // Todo: Debug all housing spawn
        //nextRandoms[0] = 1;
        //nextRandoms[1] = 1;
        //nextRandoms[2] = 1;

    }

    public int nextRandomType(int i) {
        return nextRandoms[i];
    }

    public int getNumFree() {
        return freeTiles;
    }

    public void initializeField() {

        for (int i = 0; i < 8; i++){
            for(int j = 0; j < 9; j++) {
                gameField[i][j] = new EmptyField(i, j);
                if(randInt(1,100) < 10 && roadsOnField <= maxStartingRoads) {
                    gameField[i][j] = new Road(i, j);
                    addRoad();
                }
                if(randInt(1,100) > 93) {
                    gameField[i][j] = new Housing(i, j);
                }
                if (randInt(1, 100) > 97) {
                    gameField[i][j] = new Farm(i, j);
                }
            }
        }
    }
}
