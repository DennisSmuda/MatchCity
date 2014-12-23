package Helpers;

import GameObjects.Bob;
import GameObjects.GameObject;
import GameWorld.GameWorld;
import Screens.GameScreen;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;

import java.util.Random;

/**
 * Created by smuda on 12.12.2014.
 */
public class InputHandler implements InputProcessor {
    private Bob bob;
    private GameWorld myWorld;

    private Vector2 currenSelect;
    private GameObject currentObj;

    private int barOffset = 25;
    private float scaleFactorX;
    private float scaleFactorY;

    private boolean repeatCheck = false;



    private static int lvlIncrease;

    private int nextTile;

    public InputHandler(GameWorld world, float scaleFactorX, float scaleFactorY) {

        this.myWorld = world;
        currenSelect = new Vector2();

        this.scaleFactorX = scaleFactorX;
        this.scaleFactorY = scaleFactorY;

        touchDown(100,100,0,0);

    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if(myWorld.isReady()) {
            myWorld.start();
        }

        screenX = scaleX(screenX);
        screenY = scaleY(screenY);

        int i = 0;
        int j = 0;

        if(screenY < barOffset ) {
            // TOPBAR PRESSED
            // Show stats or something
            i = -1;
            j = -1;

        } else if(screenY <  400) {

            if(0 <= screenX && screenX < 16) {
                i = 0;
            } else if(16 <= screenX && screenX < 32) {
                i = 1;
            } else if(32 <= screenX && screenX < 48) {
                i = 2;
            } else if(48 <= screenX && screenX < 64) {
                i = 3;
            } else if(64 <= screenX && screenX < 80) {
                i = 4;
            } else if(80 <= screenX && screenX < 96) {
                i = 5;
            } else if(96 <= screenX && screenX < 112) {
                i = 6;
            } else if(112 <= screenX && screenX < 128) {
                i = 7;
            }
            if(0 < screenY && screenY < 23) {
                j = -1;
            } else if(25 <= screenY && screenY < 41) {
                j = 0;
            } else if (41 <= screenY && screenY < 57) {
                j = 1;
            } else if (57 <= screenY && screenY < 73) {
                j = 2;
            } else if (73 <= screenY && screenY < 89) {
                j = 3;
            } else if (89 <= screenY && screenY < 105) {
                j = 4;
            } else if (105 <= screenY && screenY < 121) {
                j = 5;
            } else if (121 <= screenY && screenY < 135) {
                j = 6;
            } else if (135 <= screenY && screenY < 151) {
                j = 7;
            } else if (151 <= screenY && screenY < 167) {
                j = 8;
            }
        } else if (screenY >= 167){

            i = -1;
            j = -1;
            // bottom touch

        }

        // get the selected tile
        currentObj = myWorld.getGameObject(i, j);
        nextTile = myWorld.getNext();


        if (myWorld.getGameObject(i, j).getType() == GameObject.FieldType.EMPTY && i != -1 && j != -1) {
            // If the selection is empty, the next tile will be placed
            myWorld.setGameObject(i, j, nextTile);
            myWorld.calculateNextThree();
            lvlIncrease = 0;

            // 1=hmatch,2=vmatch, 3=
            int match = myWorld.checkMatches();
            handleMatch(match, i, j);

        }


        myWorld.getGameObject((int)currenSelect.x, (int)currenSelect.y).setSelected(false);
        myWorld.getGameObject(i, j).setSelected(true);
        currenSelect.x = i;
        currenSelect.y = j;

        int randomPlaced = 0;

        // place random fields on random spots.

        for (int c = 0; c < 2; c++) {
            int randI = randInt(0, 7);
            int randJ = randInt(0, 8);

            if (myWorld.getGameObject(randI, randJ).getType() == GameObject.FieldType.EMPTY) {
                myWorld.setGameObject(randI, randJ, myWorld.nextRandomType(c));
                int match = myWorld.checkMatches();
                handleMatch(match, i, j);
            } else {
                c = c-1;
            }

            if(myWorld.getNumFree() < 3) {
                myWorld.gameOver();
                c = 2;
            }
        }

        myWorld.calculateNextRandoms();

        if (myWorld.getNumFree() < 3) {
            myWorld.initializeField();
        }


        return true;
    }


    private void addResources(GameObject currentObj) {
        switch (currentObj.getType()) {
            case HOUSING:
                ResourceHandler.citizens += 5;
                break;
            case FARM:
                ResourceHandler.food += (10 * currentObj.getLevel());
                ResourceHandler.farms += 1;
                break;
            default:
                break;
        }
    }

    private void handleMatch(int match, int i, int j) {
        nextTile = myWorld.getNext();
        int currentTile = (j*8) + i;

        if(match == 1) {
            System.out.println("match = 1");
            // hmatch:
            // i,j is current element getFirstVTile/HTile gets first element from three queue
            if (currentTile == myWorld.getFirstHTile()) {

                lvlIncrease += myWorld.getGameObject(i+1, j).getLevel();
                lvlIncrease += myWorld.getGameObject(i+2, j).getLevel();

                if (j > 0) { // check for big right rotatet L
                    if(myWorld.getGameObject(i, j).getType() == myWorld.getGameObject(i, j-1).getType() ){
                        lvlIncrease += myWorld.gameField[i][j-1].getLevel();
                        myWorld.setGameObject(i, j - 1, 0);

                    }
                }

                if(j  < 8) {
                    // check downwards lying L
                    if (myWorld.getGameObject(i, j).getType() == myWorld.getGameObject(i, j + 1).getType()) {
                        lvlIncrease += myWorld.gameField[i][j + 1].getLevel();
                        myWorld.setGameObject(i, j + 1, 0);

                        if (j < 7) {  // check for half cross
                            if (myWorld.getGameObject(i, j).getType() == myWorld.getGameObject(i, j + 2).getType()) {
                                lvlIncrease += myWorld.getGameObject(i, j + 2).getLevel();
                                myWorld.setGameObject(i, j + 2, 0);
                            }
                        }
                    }
                }

                // current tile is first htile
                myWorld.setGameObject(i+1, j, 0);
                myWorld.setGameObject(i+2, j, 0);
                // check if upwards T

                myWorld.resetVHTile();

                // Levels are added together
                myWorld.getGameObject(i, j).addLevel(lvlIncrease);
                myWorld.addMoney(lvlIncrease * 10);

            } else if (currentTile == (myWorld.getFirstHTile()+1)) {

                lvlIncrease = 0;
                lvlIncrease += myWorld.getGameObject(i - 1, j).getLevel();
                lvlIncrease += myWorld.getGameObject(i + 1, j).getLevel();


                if (j > 0) {
                    // check for upwardw pointing "T"
                    if (myWorld.getGameObject(i, j).getType() == myWorld.getGameObject(i, j - 1).getType()) {
                        lvlIncrease += myWorld.getGameObject(i, j - 1).getLevel();
                        myWorld.setGameObject(i, j - 1, 0);

                        if (j > 1) {
                            if (myWorld.getGameObject(i, j).getType() == myWorld.getGameObject(i, j - 2).getType()) {
                                lvlIncrease += myWorld.getGameObject(i, j - 2).getLevel();
                                myWorld.setGameObject(i, j - 2, 0);
                            }
                        }

                    }
                    // check lying L
                    if (myWorld.getGameObject(i, j).getType() == myWorld.getGameObject(i + 1, j - 1).getType()) {
                        lvlIncrease += myWorld.getGameObject(i + 1, j - 1).getLevel();
                        myWorld.setGameObject(i + 1, j - 1, 0);
                    }
                    // check reverse lying L
                    if (myWorld.getGameObject(i, j).getType() == myWorld.getGameObject(i - 1, j - 1).getType()) {
                        lvlIncrease += myWorld.getGameObject(i - 1, j - 1).getLevel();
                        myWorld.setGameObject(i - 1, j - 1, 0);
                    }
                }

                if (j  < 8) {
                    // Check for normal T
                    if (myWorld.getGameObject(i, j).getType() == myWorld.getGameObject(i, j + 1).getType()) {
                        lvlIncrease += myWorld.getGameObject(i, j + 1).getLevel();
                        myWorld.setGameObject(i, j + 1, 0);
                        System.out.println("small T");

                        if (j < 7) { // chekc for big T
                            if (myWorld.getGameObject(i, j).getType() == myWorld.getGameObject(i, j + 2).getType()) {
                                lvlIncrease += myWorld.getGameObject(i, j + 2).getLevel();
                                myWorld.setGameObject(i, j + 2, 0);
                                System.out.println("big T");
                            }
                        }
                    }

                    // check for downwards lying L
                    if (myWorld.getGameObject(i, j).getType() == myWorld.getGameObject(i + 1, j + 1).getType()) {
                        lvlIncrease += myWorld.getGameObject(i + 1, j + 1).getLevel();
                        myWorld.setGameObject(i + 1, j + 1, 0);
                        if(j < 7) {
                            // cross
                            if (myWorld.getGameObject(i,j).getType() == myWorld.getGameObject(i + 1, j + 2).getType()) {
                                lvlIncrease += myWorld.getGameObject(i + 1, j + 2).getLevel();
                                myWorld.setGameObject(i + 1, j + 2, 0);
                            }
                        }
                    }
                    // check downwards reverse lying L
                    if (myWorld.getGameObject(i, j).getType() == myWorld.getGameObject(i - 1, j + 1).getType()) {
                        lvlIncrease += myWorld.getGameObject(i - 1, j + 1).getLevel();
                        myWorld.setGameObject(i - 1, j + 1, 0);
                    }
                }

                if (i < 6) {
                    // check four in a row
                    if(myWorld.getGameObject(i, j).getType() == myWorld.getGameObject(i + 2, j).getType()) {
                        lvlIncrease += myWorld.getGameObject(i + 2, j).getLevel();
                        myWorld.setGameObject(i + 2, j, 0);

                    }
                }

                // current tile is second htile
                myWorld.setGameObject(i - 1, j, 0);
                myWorld.setGameObject(i + 1, j, 0);

                myWorld.getGameObject(i, j).addLevel(lvlIncrease);
                myWorld.addMoney(lvlIncrease * 10);
                myWorld.resetVHTile();

            } else if (currentTile == (myWorld.getFirstHTile()+2)) {
                // current tile is last htile
                lvlIncrease = 0;
                lvlIncrease += myWorld.getGameObject(i-2, j).getLevel();
                lvlIncrease += myWorld.getGameObject(i-1, j).getLevel();


                if(j > 0) {
                    // check for upwards lying L
                    if (myWorld.getGameObject(i, j).getType() == myWorld.getGameObject(i, j - 1).getType() ) {
                        lvlIncrease += myWorld.getGameObject(i , j - 1).getLevel();
                        myWorld.setGameObject(i, j - 1, 0);

                        if (j > 1) { // check double upwards
                            if (myWorld.getGameObject(i, j).getType() == myWorld.getGameObject(i, j - 2).getType() ) {
                                lvlIncrease += myWorld.getGameObject(i, j - 2).getLevel();
                                myWorld.setGameObject(i, j - 2, 0);
                            }
                        }
                    }
                }

                if(j < 8) {
                    // check for downwards lying L
                    if (myWorld.getGameObject(i, j).getType() == myWorld.getGameObject(i, j + 1).getType()) {
                        lvlIncrease += myWorld.getGameObject(i, j + 1).getLevel();
                        myWorld.setGameObject(i, j + 1, 0);
                        System.out.println("downwardsLcheck");

                        if (j < 7) {// _
                            // check    | shape
                            if (myWorld.getGameObject(i, j).getType() == myWorld.getGameObject(i, j + 2).getType()) {
                                lvlIncrease += myWorld.getGameObject(i, j + 2).getLevel();
                                myWorld.setGameObject(i, j + 2, 0);
                                System.out.println("Crosscheck");
                            }
                        }
                    }
                }

                if (i < 7) {
                    // check 4 in a row
                    if (myWorld.getGameObject(i, j).getType() == myWorld.getGameObject(i + 1 , j).getType() ) {
                        lvlIncrease += myWorld.getGameObject(i + 1, j).getLevel();
                        myWorld.setGameObject(i + 1, j, 0);
                        System.out.println("4 in row");

                        if (i < 6) {
                            // check 5 in a row
                            if (myWorld.getGameObject(i, j).getType() == myWorld.getGameObject(i + 2, j).getType()) {
                                lvlIncrease += myWorld.getGameObject(i + 2, j).getLevel();
                                myWorld.setGameObject(i + 2, j, 0);
                                System.out.println("5 in row");
                            }
                        }
                    }
                }
                myWorld.setGameObject(i-2, j, 0);
                myWorld.setGameObject(i-1, j, 0);

                myWorld.getGameObject(i,j).addLevel(lvlIncrease);
                myWorld.addMoney(lvlIncrease * 10);

                myWorld.resetVHTile();

            }

        } else if (match == 2) {
            // vmatch
            if (currentTile == myWorld.getFirstVTile()) {
                // current tile is first htile
                lvlIncrease = 0;
                lvlIncrease += myWorld.getGameObject(i, j+1).getLevel();
                lvlIncrease += myWorld.getGameObject(i, j+2).getLevel();

                if (i > 0) {
                    // check upward standing L pointing left
                    if (myWorld.getGameObject(i,j).getType() == myWorld.getGameObject(i-1,j).getType()){
                        lvlIncrease += myWorld.getGameObject(i-1, j).getLevel();
                        myWorld.setGameObject(i - 1, j, 0);
                    }
                }

                if (i < 7) {
                    // check  upwardstanding L pointing right
                    if (myWorld.getGameObject(i,j).getType() == myWorld.getGameObject(i+1,j).getType()){
                        lvlIncrease += myWorld.getGameObject(i+1, j).getLevel();
                        myWorld.setGameObject(i + 1, j, 0);
                        if (i < 6) {
                            if (myWorld.getGameObject(i,j).getType() == myWorld.getGameObject(i+2,j).getType()) {
                                lvlIncrease += myWorld.getGameObject(i + 2, j).getLevel();
                                myWorld.setGameObject(i + 2, j, 0);
                            }
                        }
                    }
                }

                myWorld.setGameObject(i, j+1, 0);
                myWorld.setGameObject(i, j+2, 0);

                myWorld.getGameObject(i,j).addLevel(lvlIncrease);
                myWorld.addMoney(lvlIncrease * 10);

                myWorld.resetVHTile();

            } else if (currentTile == (myWorld.getFirstVTile()+8)) {
                // current tile is second htile
                lvlIncrease = 0;
                lvlIncrease += myWorld.getGameObject(i, j-1).getLevel();
                lvlIncrease += myWorld.getGameObject(i, j+1).getLevel();

                if (i > 0) {
                    // check tripletetro to the left
                    if (myWorld.getGameObject(i, j).getType() == myWorld.getGameObject(i-1, j).getType()) {
                        lvlIncrease += myWorld.getGameObject(i-1, j).getLevel();
                        myWorld.setGameObject(i-1, j, 0);

                        if (i > 1) { // check big liyng T to the right
                            if (myWorld.getGameObject(i, j).getType() == myWorld.getGameObject(i-2, j).getType()) {
                                lvlIncrease += myWorld.getGameObject(i - 2, j).getLevel();
                                myWorld.setGameObject(i - 2, j, 0);
                            }
                        }
                    }
                    // upward L left
                    if (myWorld.getGameObject(i, j).getType() == myWorld.getGameObject(i-1, j-1).getType()) {
                        lvlIncrease += myWorld.getGameObject(i-1, j-1).getLevel();
                        myWorld.setGameObject(i-1, j-1, 0);

                        if (i > 1) { // check big liyng T to the right
                            if (myWorld.getGameObject(i, j).getType() == myWorld.getGameObject(i-2, j-1).getType()) {
                                lvlIncrease += myWorld.getGameObject(i - 2, j - 1).getLevel();
                                myWorld.setGameObject(i - 2, j -1, 0);
                            }
                        }
                    }

                }
                if (i < 7) {
                    // check tripletetro to the right
                    if (myWorld.getGameObject(i, j).getType() == myWorld.getGameObject(i+1, j).getType()) {
                        lvlIncrease += myWorld.getGameObject(i+1, j).getLevel();
                        myWorld.setGameObject(i+1, j, 0);

                        if (i < 6) { // check big liyng T to the right
                            if (myWorld.getGameObject(i, j).getType() == myWorld.getGameObject(i+2, j).getType()) {
                                lvlIncrease += myWorld.getGameObject(i + 2, j).getLevel();
                                myWorld.setGameObject(i + 2, j, 0);
                            }
                        }
                    }
                }

                if (i > 0) {
                    // check reverse L
                    if (myWorld.getGameObject(i, j).getType() == myWorld.getGameObject(i-1, j+1).getType()) {
                        lvlIncrease += myWorld.getGameObject(i-1, j+1).getLevel();
                        myWorld.setGameObject(i-1, j+1, 0);

                        if (i > 1) { // check big liyng T to the right
                            if (myWorld.getGameObject(i, j).getType() == myWorld.getGameObject(i-2, j+1).getType()) {
                                lvlIncrease += myWorld.getGameObject(i - 2, j+1).getLevel();
                                myWorld.setGameObject(i - 2, j+1, 0);
                            }
                        }
                    }
                }

                if (j < 7) {
                    // 4 downwards
                    if (myWorld.getGameObject(i, j).getType() == myWorld.getGameObject(i, j+1).getType()) {
                        lvlIncrease += myWorld.getGameObject(i, j+1).getLevel();
                        myWorld.setGameObject(i, j+1, 0);

                        if (i < 6) { // 5 downwards
                            if (myWorld.getGameObject(i, j).getType() == myWorld.getGameObject(i, j+2).getType()) {
                                lvlIncrease += myWorld.getGameObject(i, j+2).getLevel();
                                myWorld.setGameObject(i, j+2, 0);
                            }
                        }
                    }

                }

                myWorld.setGameObject(i, j-1, 0);
                myWorld.setGameObject(i, j+1, 0);

                myWorld.getGameObject(i, j).addLevel(lvlIncrease);
                myWorld.addMoney(lvlIncrease * 10);
                myWorld.resetVHTile();

            } else if (currentTile == (myWorld.getFirstVTile()+16)) {
                // current tile is last htile
                lvlIncrease = 0;
                lvlIncrease += myWorld.getGameObject(i, j-2).getLevel();
                lvlIncrease += myWorld.getGameObject(i, j-1).getLevel();

                if (i < 7) {
                    // check L
                    if (myWorld.getGameObject(i, j).getType() == myWorld.getGameObject(i+1, j).getType()) {
                        lvlIncrease += myWorld.getGameObject(i+1, j).getLevel();
                        myWorld.setGameObject(i+1, j, 0);

                        if (i < 6) { // check big liyng T to the right
                            if (myWorld.getGameObject(i, j).getType() == myWorld.getGameObject(i+2, j).getType()) {
                                lvlIncrease += myWorld.getGameObject(i + 2, j).getLevel();
                                myWorld.setGameObject(i + 2, j, 0);
                            }
                        }
                    }
                }

                if (i > 0) {
                    // check reverse L
                    if (myWorld.getGameObject(i, j).getType() == myWorld.getGameObject(i-1, j).getType()) {
                        lvlIncrease += myWorld.getGameObject(i-1, j).getLevel();
                        myWorld.setGameObject(i-1, j, 0);

                        if (i > 1) { // check half cross
                            if (myWorld.getGameObject(i, j).getType() == myWorld.getGameObject(i-2, j).getType()) {
                                lvlIncrease += myWorld.getGameObject(i - 2, j).getLevel();
                                myWorld.setGameObject(i - 2, j, 0);
                            }
                        }
                    }
                }

                if (j < 8) {
                    // check 4 in a row
                    if (myWorld.getGameObject(i, j).getType() == myWorld.getGameObject(i, j + 1).getType()) {
                        lvlIncrease += myWorld.getGameObject(i, j +1 ).getLevel();
                        myWorld.setGameObject(i, j + 1, 0);

                        if (i < 7) { // check 5 in a row
                            if (myWorld.getGameObject(i, j).getType() == myWorld.getGameObject(i, j + 2).getType()) {
                                lvlIncrease += myWorld.getGameObject(i , j +2).getLevel();
                                myWorld.setGameObject(i, j +2, 0);
                            }
                        }
                    }
                }

                myWorld.setGameObject(i, j-2, 0);
                myWorld.setGameObject(i, j-1, 0);


                myWorld.getGameObject(i, j).addLevel(lvlIncrease);
                myWorld.addMoney(lvlIncrease * 10);
                myWorld.resetVHTile();
            }

        } else if (match == 3) {
            System.out.println("match 3");
            // upwards L
            if (currentTile == (myWorld.getFirstHTile())) {
                lvlIncrease = 0;
                lvlIncrease += myWorld.getGameObject(i + 1, j).getLevel();
                lvlIncrease += myWorld.getGameObject(i, j+1).getLevel();

                if (i > 0) {
                    // check tetromino
                    if (myWorld.getGameObject(i, j).getType() == myWorld.getGameObject(i - 1, j + 1).getType()) {
                        lvlIncrease += myWorld.getGameObject(i - 1, j + 1).getLevel();
                        myWorld.setGameObject(i - 1, j + 1, 0);
                        System.out.println("tetromino checku");
                    }
                }

                myWorld.setGameObject(i+1,j, 0);
                myWorld.setGameObject(i,j+1, 0);

                myWorld.getGameObject(i, j).addLevel(lvlIncrease);
                myWorld.addMoney(lvlIncrease * 10);
                myWorld.resetVHTile();
            }
            if (currentTile == (myWorld.getFirstHTile() + 1)) {
                lvlIncrease = 0;
                lvlIncrease += myWorld.getGameObject(i - 1, j).getLevel();
                lvlIncrease += myWorld.getGameObject(i - 1, j + 1).getLevel();

                if (j > 0) {
                    // tetro
                    if (myWorld.getGameObject(i, j).getType() == myWorld.getGameObject(i, j -1).getType()) {
                        lvlIncrease += myWorld.getGameObject(i, j - 1).getLevel();
                        myWorld.setGameObject(i, j-1, 0);
                        // tetro + 1
                        if (j > 2) {
                            if (myWorld.getGameObject(i, j).getType() == myWorld.getGameObject(i, j - 2).getType()) {
                                lvlIncrease += myWorld.getGameObject(i, j - 2).getLevel();
                                myWorld.setGameObject(i, j - 2, 0);
                            }
                        }
                    }
                }

                myWorld.setGameObject(i-1,j, 0);
                myWorld.setGameObject(i-1,j+1, 0);

                myWorld.getGameObject(i, j).addLevel(lvlIncrease);
                myWorld.addMoney(lvlIncrease * 10);
                myWorld.resetVHTile();
            }
            if (currentTile == (myWorld.getFirstHTile() + 8)) {
                lvlIncrease = 0;
                lvlIncrease += myWorld.getGameObject(i, j - 1).getLevel();
                lvlIncrease += myWorld.getGameObject(i + 1, j - 1).getLevel();

                if (i > 0) {
                    // tetromino check
                    if(myWorld.getGameObject(i, j).getType() == myWorld.getGameObject(i - 1, j).getType()) {
                        lvlIncrease += myWorld.getGameObject(i - 1, j).getLevel();
                        myWorld.setGameObject(i - 1, j, 0);
                    }
                }


                myWorld.setGameObject(i,j-1, 0);
                myWorld.setGameObject(i+1,j-1, 0);

                myWorld.getGameObject(i, j).addLevel(lvlIncrease);
                myWorld.addMoney(lvlIncrease * 10);
                myWorld.resetVHTile();
            }
        } else if (match == 4) {
            System.out.println("match 4");
            // reverse upwards L
            if (currentTile == (myWorld.getFirstHTile())) {
                lvlIncrease = 0;
                lvlIncrease += myWorld.getGameObject(i + 1, j).getLevel();
                lvlIncrease += myWorld.getGameObject(i + 1, j+1).getLevel();

                myWorld.setGameObject(i+1,j, 0);
                myWorld.setGameObject(i+1,j+1, 0);


                myWorld.getGameObject(i, j).addLevel(lvlIncrease);
                myWorld.addMoney(lvlIncrease * 10);
                myWorld.resetVHTile();
            }
            if (currentTile == (myWorld.getFirstHTile() + 1)) {
                lvlIncrease = 0;
                lvlIncrease += myWorld.getGameObject(i - 1, j).getLevel();
                lvlIncrease += myWorld.getGameObject(i, j+1).getLevel();

                if (i < 6) {
                    // check tetromino
                    if (myWorld.getGameObject(i, j).getType() == myWorld.getGameObject(i + 1, j + 1).getType()) {
                        lvlIncrease += myWorld.getGameObject(i + 1, j + 1).getLevel();
                        myWorld.setGameObject(i + 1, j + 1, 0);
                    }
                }

                if (j < 7) {
                    // check upwards L right
                    if (myWorld.getGameObject(i, j).getType() == myWorld.getGameObject(i, j + 2).getType()) {
                        lvlIncrease += myWorld.getGameObject(i, j + 2).getLevel();
                        myWorld.setGameObject(i, j + 2, 0);
                    }
                }

                if (i > 0) {
                    // check upwards
                    if (myWorld.getGameObject(i, j).getType() == myWorld.getGameObject(i, j -1).getType()) {
                        lvlIncrease += myWorld.getGameObject(i, j -1).getLevel();
                        myWorld.setGameObject(i, j -1, 0);
                        // one more up
                        if (i > 1) {
                            if (myWorld.getGameObject(i, j).getType() == myWorld.getGameObject(i, j -2).getType() ) {
                                lvlIncrease += myWorld.getGameObject(i, j - 2).getLevel();
                                myWorld.setGameObject(i, j - 2, 0);
                            }
                        }
                    }

                }

                myWorld.setGameObject(i-1,j, 0);
                myWorld.setGameObject(i,j+1, 0);

                myWorld.getGameObject(i, j).addLevel(lvlIncrease);
                myWorld.addMoney(lvlIncrease * 10);
                myWorld.resetVHTile();
            }
            if (currentTile == (myWorld.getFirstHTile() + 8 + 1)) {

                lvlIncrease = 0;
                lvlIncrease += myWorld.getGameObject(i - 1, j - 1).getLevel();
                lvlIncrease += myWorld.getGameObject(i, j - 1).getLevel();

                if(i < 7) {
                    // check tetro
                    if (myWorld.getGameObject(i, j).getType() == myWorld.getGameObject(i + 1, j).getType()) {
                        lvlIncrease += myWorld.getGameObject(i + 1, j).getLevel();
                        myWorld.setGameObject(i + 1, j, 0);
                    }
                }

                if (j < 8) {
                    if (myWorld.getGameObject(i, j).getType() == myWorld.getGameObject(i, j + 1).getType()) {
                        lvlIncrease += myWorld.getGameObject(i, j + 1).getLevel();
                        myWorld.setGameObject(i, j + 1, 0);

                        if (j < 7) {
                            if (myWorld.getGameObject(i, j).getType() == myWorld.getGameObject(i, j + 2).getType()) {
                                lvlIncrease += myWorld.getGameObject(i, j + 2).getLevel();
                                myWorld.setGameObject(i, j + 2, 0);
                            }
                        }
                    }
                }

                myWorld.setGameObject(i-1,j-1, 0);
                myWorld.setGameObject(i,j-1, 0);

                myWorld.getGameObject(i, j).addLevel(lvlIncrease);
                myWorld.addMoney(lvlIncrease * 10);
                myWorld.resetVHTile();

            }
        } else if (match == 5) {
            System.out.println("match 5");
            // reverse L
            if (currentTile == (myWorld.getFirstVTile())) {

                lvlIncrease += myWorld.getGameObject(i, j + 1).getLevel();
                lvlIncrease += myWorld.getGameObject(i - 1, j + 1).getLevel();

                if (i < 7) {
                    // tetro check
                    if (myWorld.getGameObject(i, j).getType() == myWorld.getGameObject(i + 1, j).getType()) {
                        lvlIncrease += myWorld.getGameObject(i + 1, j).getLevel();
                        myWorld.setGameObject(i + 1, j, 0);
                    }
                }

                myWorld.setGameObject(i,j+1, 0);
                myWorld.setGameObject(i-1,j+1, 0);

                myWorld.getGameObject(i, j).addLevel(lvlIncrease);
                myWorld.addMoney(lvlIncrease * 10);
                myWorld.resetVHTile();
            }
            if (currentTile == (myWorld.getFirstVTile() + 8)) {

                lvlIncrease += myWorld.getGameObject(i, j - 1).getLevel();
                lvlIncrease += myWorld.getGameObject(i - 1, j).getLevel();

                if (i < 7) {
                    // tetro check
                    if (myWorld.getGameObject(i, j).getType() == myWorld.getGameObject(i + 1, j - 1).getType()) {
                        lvlIncrease += myWorld.getGameObject(i + 1, j -1 ).getLevel();
                        myWorld.setGameObject(i + 1, j - 1, 0);
                    }
                }


                myWorld.setGameObject(i,j-1, 0);
                myWorld.setGameObject(i-1,j, 0);

                myWorld.getGameObject(i, j).addLevel(lvlIncrease);
                myWorld.addMoney(lvlIncrease * 10);
                myWorld.resetVHTile();
            }
            if (currentTile == (myWorld.getFirstVTile() + 8 - 1)) {

                lvlIncrease += myWorld.getGameObject(i + 1, j).getLevel();
                lvlIncrease += myWorld.getGameObject(i + 1, j - 1).getLevel();

                myWorld.setGameObject(i+1,j, 0);
                myWorld.setGameObject(i+1,j-1, 0);

                myWorld.getGameObject(i, j).addLevel(lvlIncrease);
                myWorld.addMoney(lvlIncrease * 10);
                myWorld.resetVHTile();

            }
        } else if (match == 6) {
            System.out.println("match 6");
            // normal small L
            if (currentTile == (myWorld.getFirstVTile())) {

                lvlIncrease += myWorld.getGameObject(i, j + 1).getLevel();
                lvlIncrease += myWorld.getGameObject(i + 1, j + 1).getLevel();

                if (i > 0) {
                    // tetro check
                    if (myWorld.getGameObject(i, j).getType() == myWorld.getGameObject(i - 1, j).getType()) {
                        lvlIncrease += myWorld.getGameObject(i - 1, j).getLevel();
                        myWorld.setGameObject(i - 1, j, 0);
                    }
                }

                if (i > 7) {
                    // down tetro check
                    if (myWorld.getGameObject(i, j).getType() == myWorld.getGameObject(i + 1, j + 2).getType()) {
                        lvlIncrease += myWorld.getGameObject(i + 1, j+2).getLevel();
                        myWorld.setGameObject(i + 1, j+2, 0);
                    }
                }

                myWorld.setGameObject(i,j+1, 0);
                myWorld.setGameObject(i+1,j+1, 0);

                myWorld.getGameObject(i, j).addLevel(lvlIncrease);
                myWorld.addMoney(lvlIncrease * 10);
                myWorld.resetVHTile();

            }
            if (currentTile == (myWorld.getFirstVTile() + 8)) {

                lvlIncrease += myWorld.getGameObject(i, j - 1).getLevel();
                lvlIncrease += myWorld.getGameObject(i + 1, j).getLevel();

                if (j < 8) {
                    // downward tetro
                    if (myWorld.getGameObject(i, j).getType() == myWorld.getGameObject(i + 1, j+1).getType() ) {
                        lvlIncrease += myWorld.getGameObject(i+ 1, j+1).getLevel();
                        myWorld.setGameObject(i + 1, j + 1, 0);
                    }
                }

                if (i < 6) {
                    // Lying L
                    if (myWorld.getGameObject(i, j).getType() == myWorld.getGameObject(i + 2, j).getType()) {
                        lvlIncrease += myWorld.getGameObject(i+ 2, j).getLevel();
                        myWorld.setGameObject(i + 2, j, 0);
                    }
                }

                myWorld.setGameObject(i,j-1, 0);
                myWorld.setGameObject(i+1,j, 0);

                myWorld.getGameObject(i, j).addLevel(lvlIncrease);
                myWorld.addMoney(lvlIncrease * 10);
                myWorld.resetVHTile();
            }
            if (currentTile == (myWorld.getFirstVTile() + 8 + 1)) {

                lvlIncrease += myWorld.getGameObject(i - 1, j ).getLevel();
                lvlIncrease += myWorld.getGameObject(i - 1, j - 1).getLevel();

                if (j < 8) {
                    // downward tetro
                    if (myWorld.getGameObject(i, j).getType() == myWorld.getGameObject(i, j+1).getType()) {
                        lvlIncrease += myWorld.getGameObject(i, j+1).getLevel();
                        myWorld.setGameObject(i, j + 1, 0);
                    }
                }

                if (i < 7) {
                    // check three in row upward L shape
                    if (myWorld.getGameObject(i, j).getType() == myWorld.getGameObject(i+1, j).getType()) {
                        lvlIncrease += myWorld.getGameObject(i + 1, j).getLevel();
                        myWorld.setGameObject(i + 1, j, 0);
                        if (i < 6) {
                            // four in row upward L lying
                            if (myWorld.getGameObject(i, j).getType() == myWorld.getGameObject(i+2, j).getType()) {
                                lvlIncrease += myWorld.getGameObject(i+2, j).getLevel();
                                myWorld.setGameObject(i+2, j, 0);
                            }
                        }
                    }
                }


                myWorld.setGameObject(i-1,j, 0);
                myWorld.setGameObject(i-1,j-1, 0);

                myWorld.getGameObject(i, j).addLevel(lvlIncrease);
                myWorld.addMoney(lvlIncrease * 10);
                myWorld.resetVHTile();
            }
        }

        addResources(myWorld.getGameObject(i, j));
        addFields(myWorld.getGameObject(i, j).getType());

    }



    private void addFields(GameObject.FieldType currentType) {

    }


    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }

    private int scaleX(int screenX) {
        return (int) (screenX / scaleFactorX);
    }

    private int scaleY(int screenY) {
        return (int) (screenY / scaleFactorY);
    }

    public static int randInt(int min, int max) {
        Random rand = new Random();
        int randomNum = rand.nextInt((max - min) + 1) + min;
        return randomNum;
    }
}
