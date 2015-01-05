package de.smuda.matchcity.gameworld;

import de.smuda.matchcity.gameobjects.*;
import de.smuda.matchcity.states.State;

import java.util.Random;

/**
 * Created by denni_000 on 02.01.2015.
 */
public class GameWorld {

    public int nextThree[];
    public int nextRands[];

    public GameObject[][] gameField;

    private int score = 0;

    // Control variables
    private int firstHTile = -1;
    private int firstVTile = -1;

    private State currentState;

    private int maxStartingRoads = 6;
    private int roadsOnField = 0;
    private int freeTiles;

    public enum FieldState {
        EMPTY, ROAD, HOUSING, INDUSTRY,
        TRASH, FARM, PARK, SPECIAL, CITY,
        SHOPS, POWER, GOVERNMENT,
    }


    public GameWorld() {
        // Initialize next three elements
        nextThree = new int[3];
        for (int i = 0; i < 3; i++) {
            nextThree[i] = randInt(1, 4);
        }

        // Todo: number of random spawning tiles can be tied to difficulty
        nextRands = new int[5];
        for (int i = 0; i < 5; i++) {
            nextRands[i] = randInt(1, 4);
        }

        gameField = new GameObject[8][9];

        // Initialize
        for (int i = 0; i < 8; i++){
            for(int j = 0; j < 9; j++) {
                gameField[i][j] = new EmptyField(i, j);

                /*
                if(randInt(1,100) < 20 && roadsOnField <= maxStartingRoads) {
                    gameField[i][j] = new Road(i, j);
                    freeTiles--;
                    addRoad();
                }
                if(randInt(1,100) > 93) {
                    gameField[i][j] = new Housing(i, j);
                    freeTiles--;
                }
                if (randInt(1, 100) > 97) {
                    gameField[i][j] = new Farm(i, j);
                    freeTiles--;
                }
                */
            }
        }
    }

    public void update(float delta) {

    }

    public void addMoney(int increment) {
        score += increment;
    }

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
                gameField[i][j] = new Shops(i, j);
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

    public int getScore() {
        return score;
    }
    public int getNumRoads() {return roadsOnField;}
    public void addRoad() {roadsOnField++ ;}

    public static int randInt(int min, int max) {
        Random rand = new Random();
        int randomNum = rand.nextInt((max - min) + 1) + min;
        return randomNum;
    }

    public int checkFreeTiles() {
        freeTiles = 0;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 9; j++) {
                if (getGameObject(i, j).getType() == GameObject.FieldType.EMPTY) {
                    freeTiles++;
                } else {
                    // nothing
                }
            }
        }
        return freeTiles;
    }

    public void calculateNextThree() {
        nextThree[0] = nextThree[1];
        nextThree[1] = nextThree[2];
        nextThree[2] = randInt(1, 11);

        int next = randInt(1, 100);
        if (next < 30) {
            nextThree[2] = 1;
        } else if (next < 50) {
            nextThree[2] = 2;
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

    public void calculateNextRands() {
        nextRands[0] = randInt(1, 11);
        nextRands[1] = randInt(1, 11);
        nextRands[2] = randInt(1, 11);
        nextRands[3] = randInt(1, 11);
        nextRands[4] = randInt(1, 11);
    }

    public int getNext() {
        return nextThree[0];
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
                            firstHTile = (8 * j) + i;

                            return 1;
                        }
                    }
                    // check downwards for triple match. return 2
                    if (j < 7) {
                        if (currentT == gameField[i][j + 1].getType() &&
                                currentT == gameField[i][j + 2].getType()) {
                            // vertical match found!!
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

    public int getNextRand(int i) {
        return nextRands[i];
    }

    public int getFreeTiles() { return freeTiles; }
    public void addFreeTiles(int i) {freeTiles = freeTiles + i;}

    public int getFirstHTile() { return firstHTile; }
    public int getFirstVTile() { return firstVTile; }
    public void resetVHTile() { firstVTile = 0; firstHTile = 0; }

}
