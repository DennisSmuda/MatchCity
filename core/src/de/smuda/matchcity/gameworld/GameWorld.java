package de.smuda.matchcity.gameworld;

import de.smuda.matchcity.gameobjects.*;
import de.smuda.matchcity.states.State;

import java.util.Random;

/**
 * Created by denni_000 on 02.01.2015.
 * GameWorld class holds info and logic about the playing field.
 * Basically helper class for PlayState class, for array traversal etc..
 */
public class GameWorld {

    public int nextThree[];
    public int nextRands[];

    public GameObject[][] gameField;

    private int score = 0;

    // Control variables
    private int firstHTile = -1;
    private int firstVTile = -1;

    private int maxStartingRoads = 6;
    private int roadsOnField = 0;
    private int freeTiles;


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

                if(randInt(1,100) < 20 && roadsOnField <= maxStartingRoads) {
                    gameField[i][j] = new Road(i, j);
                    addRoad();
                }
                if(randInt(1,100) > 85) {
                    gameField[i][j] = new Housing(i, j);
                }
                if (randInt(1, 100) > 90) {
                    gameField[i][j] = new Farm(i, j);
                }
            }
        }
    }

    public void update(float delta) {

    }

    public void addMoney(int increment, int combo) {
        int newScore = (increment+1) * combo;
        //increment = (increment * combo);
        score = score + newScore;
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
                gameField[i][j] = new Government(i, j);
                break;
            case 4:
                gameField[i][j] = new Park(i, j);
                break;
            case 5:
                gameField[i][j] = new Shops(i, j);
                break;
            case 6:
                gameField[i][j] = new Power(i, j);
                break;
            case 7:
                gameField[i][j] = new Industry(i, j);
                break;
            case 8:
                gameField[i][j] = new Trash(i, j);
                break;
            case 9:
                gameField[i][j] = new City(i, j);
                break;
            case 10:
                gameField[i][j] = new Special(i, j);
                break;
            case 11:
                gameField[i][j] = new Road(i, j);
                break;
            case 12:
                gameField[i][j] = new TNT(i, j);
                break;
            case 13:
                gameField[i][j] = new Reset(i, j);
                break;
            case 14:
                gameField[i][j] = new Swap(i, j);
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
        roadsOnField = 0;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 9; j++) {
                if (getGameObject(i, j).getType() == GameObject.FieldType.EMPTY) {
                    freeTiles++;
                } else if(getGameObject(i, j).getType() == GameObject.FieldType.ROAD){
                    roadsOnField++;
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

        int next = randInt(1, 120);
        if (next < 20) {
            nextThree[2] = 1; // house
        } else if (next < 40) {
            nextThree[2] = 2; // farm
        } else if (next < 50) {
            nextThree[2] = 3; // gov
        } else if (next < 60) {
            nextThree[2] = 4; // park
        } else if (next < 70) {
            nextThree[2] = 5; // shops
        } else if (next < 80) {
            nextThree[2] = 6; // power
        } else if (next < 90) {
            nextThree[2] = 7; // industry
        } else if (next < 100) {
            nextThree[2] = 8; // trash
        } else if(next < 110) {
            nextThree[2] = 9; // city
        } else if (next < 120) {
            nextThree[2] = 10; // special
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

                GameObject.FieldType currentT = gameField[i][j].getType();
                // don't check empty fields, roads or powerups for matches
                if (currentT != GameObject.FieldType.EMPTY && currentT != GameObject.FieldType.ROAD &&
                        currentT != GameObject.FieldType.TNT && currentT != GameObject.FieldType.SWAP &&
                        currentT != GameObject.FieldType.RESET) {
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
        // no match found
        return 0;
    }

    public int getNextRand(int i) {
        return nextRands[i];
    }

    public int getFreeTiles() { return freeTiles; }

    public int getFirstHTile() { return firstHTile; }
    public int getFirstVTile() { return firstVTile; }
    public void resetVHTile() { firstVTile = 0; firstHTile = 0; }

    public void resetGameObject(int i, int j, int type, int level) {
        switch (type) {
            case 0:
                gameField[i][j] = new EmptyField(i, j);
                break;
            case 1:
                gameField[i][j] = new Housing(i, j, level);
                break;
            case 2:
                gameField[i][j] = new Farm(i, j, level);
                break;
            case 3:
                gameField[i][j] = new Government(i, j, level);
                break;
            case 4:
                gameField[i][j] = new Park(i, j, level);
                break;
            case 5:
                gameField[i][j] = new Shops(i, j, level);
                break;
            case 6:
                gameField[i][j] = new Power(i, j, level);
                break;
            case 7:
                gameField[i][j] = new Industry(i, j, level);
                break;
            case 8:
                gameField[i][j] = new Trash(i, j, level);
                break;
            case 9:
                gameField[i][j] = new City(i, j, level);
                break;
            case 10:
                gameField[i][j] = new Special(i, j, level);
                break;
            case 11:
                gameField[i][j] = new Road(i, j, level);
                break;
            default:
                break;
        }
    }

    // *********** //
    //   PowerUps  //
    // *********** //

    public void deleteRoad() {
        boolean found = false;
        int i;
        int j;

        while (!found && getNumRoads() > 0) {
            i = randInt(0, 7);
            j = randInt(0, 8);

            if (getGameObject(i, j).getType() == GameObject.FieldType.ROAD) {
                setGameObject(i, j, 0);
                found = true;
            }
        }
    }

    public void resetBoard() {

        int[] levels;
        levels = new int[10];
        for (int n = 0; n < 10; n++) {
            levels[n] = 1;
        }

        int randI, randJ;

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 9; j++) {
                    // add all levels together
                switch (getGameObject(i, j).getType()) {
                    case FARM:
                        levels[2] += getGameObject(i, j).getLevel();
                        setGameObject(i, j, 0);
                        break;
                    case HOUSING:
                        levels[1] += getGameObject(i, j).getLevel();
                        setGameObject(i, j, 0);
                        break;
                    case GOVERNMENT:
                        levels[3] += getGameObject(i, j).getLevel();
                        setGameObject(i, j, 0);
                        break;
                    case PARK:
                        levels[4] += getGameObject(i, j).getLevel();
                        setGameObject(i, j, 0);
                        break;
                    case SHOPS:
                        levels[5] += getGameObject(i, j).getLevel();
                        setGameObject(i, j, 0);
                        break;
                    case POWER:
                        levels[6] += getGameObject(i, j).getLevel();
                        setGameObject(i, j, 0);
                        break;
                    case INDUSTRY:
                        levels[7] += getGameObject(i, j).getLevel();
                        setGameObject(i, j, 0);
                        break;
                    case TRASH:
                        levels[8] += getGameObject(i, j).getLevel();
                        setGameObject(i, j, 0);
                        break;
                    case CITY:
                        levels[9] += getGameObject(i, j).getLevel();
                        setGameObject(i, j, 0);
                        break;
                    default: break;

                }
            }
        }
        for (int n = 0; n < 10; n++) {
            System.out.println(levels[n]);
        }

        // combine
        int typeCounter = 1;

        while(typeCounter <= 9) {
            randI = randInt(0, 7);
            randJ = randInt(0, 8);

            if (getGameObject(randI, randJ).getType() == GameObject.FieldType.EMPTY) {
                System.out.println("set" + typeCounter + "to " + levels[typeCounter]);
                resetGameObject(randI, randJ, typeCounter, levels[typeCounter]);
                typeCounter++;
            }
        }
    }

    public void swapNext() {
        int tmp;
        tmp = nextThree[0];
        nextThree[0] = nextThree[1];
        nextThree[1] = tmp;
    }


}
