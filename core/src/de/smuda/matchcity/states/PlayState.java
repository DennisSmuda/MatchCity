package de.smuda.matchcity.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import de.smuda.matchcity.MatchCity;
import de.smuda.matchcity.gameobjects.GameObject;
import de.smuda.matchcity.gameworld.GameRenderer;
import de.smuda.matchcity.gameworld.GameWorld;
import de.smuda.matchcity.helpers.AssetLoader;
import de.smuda.matchcity.helpers.InputHandler;
import de.smuda.matchcity.ui.Score;

/**
 * Created by denni_000 on 02.01.2015.
 */
public class PlayState extends State {

    private GameWorld myWorld;
    private OrthographicCamera cam;
    private ShapeRenderer shapeRenderer;
    private SpriteBatch batcher;

    private GameRenderer renderer;
    private float runTime;

    private Score score;
    private int topBarHeight = 25;
    private String levelString = "";
    private Integer levelInt;
    private static int lvlIncrease;

    private boolean selected = false;

    private int infoXOff = 6;
    private int infoYoff = 4;

    private int scaleFactorX;
    private int scaleFactorY;
    private int screenX, screenY;
    private int barOffset = 25;
    private int nextTile;

    // Assets
    public static TextureRegion empty,  housing, farm, government,
            park, road, shops, power, industry,
            special, trash, city;


    // Constructor
    public PlayState(GSM gsm) {

        super(gsm);

        float screenWidth = Gdx.graphics.getWidth();  // 320
        float screenHeight = Gdx.graphics.getHeight(); // 480
        float gameWidth = 128;
        float gameHeight = screenHeight / (screenWidth / gameWidth);

        int midPointY = (int) (gameHeight / 2);

        scaleFactorX = (int)(screenWidth / gameWidth);
        scaleFactorY = (int)(screenHeight / gameHeight);


        cam = new OrthographicCamera();
        cam.setToOrtho(true, 128, gameHeight); //136

        batcher = new SpriteBatch();
        batcher.setProjectionMatrix(cam.combined);
        shapeRenderer = new ShapeRenderer();
        shapeRenderer.setProjectionMatrix(cam.combined);

        myWorld = new GameWorld();

        initGameObjects();
        initAssets();

        score = new Score(MatchCity.WIDTH / 2, MatchCity.HEIGHT / 2);

        //Gdx.input.setInputProcessor(new InputHandler(myWorld, screenWidth / gameWidth, screenHeight / gameHeight));

    }



    @Override
    public void handleInput() {
        if(Gdx.input.justTouched()) {
            mouse.x = Gdx.input.getX();
            mouse.y = Gdx.input.getY();
            cam.unproject(mouse);

            screenX = (int)mouse.x;
            screenY = (int)mouse.y;
            int i = 0;
            int j = 0;


            if(screenY < barOffset ) {
                // TOPBAR PRESSED
                // Show stats or something

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

                // bottom touch

            }

            // Checked the position of the touchDown event - need to handle.
            nextTile = myWorld.getNext();
            if (myWorld.getGameObject(i, j).getType() == GameObject.FieldType.EMPTY) {
                // If the selection is empty, the next tile will be placed
                myWorld.setGameObject(i, j, nextTile);
                myWorld.calculateNextThree();
                lvlIncrease = 0;

                // 1=hmatch,2=vmatch, 3=
                int match = myWorld.checkMatches();
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

                        if (i < 7) {
                            // check reverse L
                            if (myWorld.getGameObject(i, j).getType() == myWorld.getGameObject(i-1, j).getType()) {
                                lvlIncrease += myWorld.getGameObject(i-1, j).getLevel();
                                myWorld.setGameObject(i-1, j, 0);

                                if (i < 6) { // check half cross
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

                        if (i < 7) {
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

                        if (j > 0) {
                            // check upwards
                            if (myWorld.getGameObject(i, j).getType() == myWorld.getGameObject(i, j -1).getType()) {
                                lvlIncrease += myWorld.getGameObject(i, j -1).getLevel();
                                myWorld.setGameObject(i, j -1, 0);
                                // one more up
                                if (j > 1) {
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

                        myWorld.setGameObject(i,j+1, 0);
                        myWorld.setGameObject(i+1,j+1, 0);

                        myWorld.getGameObject(i, j).addLevel(lvlIncrease);
                        myWorld.addMoney(lvlIncrease * 10);
                        myWorld.resetVHTile();

                    }
                    if (currentTile == (myWorld.getFirstVTile() + 8)) {

                        lvlIncrease += myWorld.getGameObject(i, j + 1).getLevel();
                        lvlIncrease += myWorld.getGameObject(i + 1, j + 1).getLevel();

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

            } else {
                //myWorld.checkMatches();
            }
        }
    }

    @Override
    public void update(float dt) {
        runTime += dt;
        myWorld.update(dt);
        handleInput();

    }

    @Override
    public void render(SpriteBatch sb) {
        Gdx.gl.glClearColor(0, 1, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);



        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        // Draw Background Topbar
        shapeRenderer.setColor(209 / 255.0f, 85 / 255.0f, 85 / 255.0f, 1);
        shapeRenderer.rect(0, 0, 128, topBarHeight);
        // Draw middle background grass
        shapeRenderer.setColor(78 / 255.0f, 168 / 255.0f, 34 / 255.0f, 1);
        shapeRenderer.rect(0, 24, 320, 465);


        shapeRenderer.end();


        batcher.begin();

        // Run through entire array and draw the right stuff.
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 9; j++) {
                switch (myWorld.getGameObject(i,j).getType()) {
                    case EMPTY:
                        drawEmpty(i, j);
                        break;
                    case HOUSING:
                        drawHousing(i, j);
                        break;
                    case GOVERNMENT:
                        drawGovernment(i, j);
                        break;
                    case FARM:
                        drawFarm(i, j);
                        break;
                    case ROAD:
                        drawRoad(i, j);
                        break;
                    case PARK:
                        drawPark(i, j);
                        break;
                    case SHOPS:
                        drawShops(i, j);
                        break;
                    case POWER:
                        drawPower(i, j);
                        break;
                    case INDUSTRY:
                        drawIndustry(i, j);
                        break;
                    case SPECIAL:
                        drawSpecial(i, j);
                        break;
                    case TRASH:
                        drawTrash(i, j);
                        break;
                    case CITY:
                        drawCity(i, j);
                        break;
                    default:
                        break;
                }
                // todo: maybe do something with selected tile
                // if(currentObj.isSelected()) { drawSelectionBox(i,j); }
            }
        }

        // Draw current Networth
        String netWorth = myWorld.getScore() + "";
        // System out print ln (make something amazin for this game
        AssetLoader.shadow.draw(batcher,"$ " + netWorth, 5, 6);
        AssetLoader.font.draw(batcher,"$ " + netWorth, 4, 5);

        drawBottom();

        batcher.end();
    }


    private void initGameObjects() {}

    private void initAssets() {
        empty = AssetLoader.empty;
        farm = AssetLoader.farm;
        housing = AssetLoader.housing;
        government = AssetLoader.government;

        park = AssetLoader.park;
        road = AssetLoader.road;
        shops = AssetLoader.shops;
        power = AssetLoader.power;

        industry = AssetLoader.industry;
        special = AssetLoader.special;
        trash = AssetLoader.trash;
        city = AssetLoader.city;
    }

    // Draw Methods
    private void drawInfo() {


        levelString = levelInt.toString();
        AssetLoader.pixel.setColor(115 / 255.0f, 55 / 255.0f, 55 / 255.0f, 1f);
        AssetLoader.pixel.draw(batcher, "Food: ", 150, 4);
    }

    private void drawBottom() {
        AssetLoader.font.setScale(.1f, -.1f);
        AssetLoader.shadow.setScale(.1f, -.1f);
        AssetLoader.shadow.draw(batcher, "Next tile: ", 5, 181);
        AssetLoader.font.draw(batcher, "Next tile: ", 4, 180);

        AssetLoader.font.setScale(.2f, -.2f);
        AssetLoader.shadow.setScale(.2f, -.2f);

        int x = 45;
        int y = 178;
        // draw next three elements
        for (int i = 0; i < 3; i++) {
            switch (myWorld.nextThree[i]) {
                case 1:
                    batcher.draw(housing, x, y, 8, 8);
                    break;
                case 2:
                    batcher.draw(farm, x, y, 8, 8);
                    break;
                case 3:
                    batcher.draw(road, x, y, 8, 8);
                    break;
                case 4:
                    batcher.draw(government, x, y, 8, 8);
                    break;
                case 5:
                    batcher.draw(park, x, y, 8, 8);
                    break;
                case 6:
                    batcher.draw(shops, x, y, 8, 8);
                    break;
                case 7:
                    batcher.draw(power, x, y, 8, 8);
                    break;
                case 8:
                    batcher.draw(industry, x, y, 8, 8);
                    break;
                case 9:
                    batcher.draw(special, x, y, 8, 8);
                    break;
                case 10:
                    batcher.draw(trash, x, y, 8, 8);
                    break;
                case 11:
                    batcher.draw(city, x, y, 8, 8);
                    break;

                default:
                    break;
            }
            x += 10;
        }
    }

    private void drawEmpty(int i,int j) {
        int x = (i * 16);
        int y = (j * 16) + 25;
        batcher.draw(empty, x, y, 16, 16);
    }

    private void drawHousing(int i, int j){
        int x = (i * 16);
        int y = (j * 16) + 25;
        batcher.draw(housing, x, y, 16, 16);

        levelInt = myWorld.getGameObject(i, j).getLevel();
        levelString = levelInt.toString();
        AssetLoader.pixel.setColor(115 / 255.0f, 55 / 255.0f, 55 / 255.0f, 1f);
        AssetLoader.pixel.draw(batcher, levelString, (x + infoXOff), (y + infoYoff));
    }

    private void drawGovernment(int i, int j) {
        int x = (i * 16);
        int y = (j * 16) + 25;
        batcher.draw(government, x, y, 16, 16);

        levelInt = myWorld.getGameObject(i, j).getLevel();
        levelString = levelInt.toString();
        AssetLoader.pixel.setColor(104 / 255.0f, 71 / 255.0f, 12 / 255.0f, 1f);
        AssetLoader.pixel.draw(batcher, levelString, (x + infoXOff), (y + infoYoff));
    }

    private void drawRoad(int i, int j) {
        int x = (i * 16);
        int y = (j * 16) + 25;
        batcher.draw(road, x, y, 16, 16);
    }

    private void drawFarm(int i, int j) {
        int x = (i * 16);
        int y = (j * 16) + 25;
        batcher.draw(farm, x, y, 16, 16);

        levelInt = myWorld.getGameObject(i, j).getLevel();
        levelString = levelInt.toString();
        AssetLoader.pixel.setColor(138 / 255.0f, 106 / 255.0f, 46 / 255.0f, 1f);
        AssetLoader.pixel.draw(batcher, levelString, (x + infoXOff), (y + infoYoff));
    }

    private void drawPark(int i, int j) {
        int x = (i * 16);
        int y = (j * 16) + 25;
        batcher.draw(park, x, y, 16, 16);

        levelInt = myWorld.getGameObject(i, j).getLevel();
        levelString = levelInt.toString();
        AssetLoader.pixel.setColor(61 / 255.0f, 138 / 255.0f, 25 / 255.0f, 1f);
        AssetLoader.pixel.draw(batcher, levelString, (x + infoXOff), (y + infoYoff));
    }

    private void drawShops(int i, int j) {
        int x = (i * 16);
        int y = (j * 16) + 25;
        batcher.draw(shops, x, y, 16, 16);

        levelInt = myWorld.getGameObject(i, j).getLevel();
        levelString = levelInt.toString();
        AssetLoader.pixel.setColor(46 / 255.0f, 116 / 255.0f, 150 / 255.0f, 1f);
        AssetLoader.pixel.draw(batcher, levelString, (x + infoXOff), (y + infoYoff));
    }

    private void drawPower(int i, int j) {
        int x = (i * 16);
        int y = (j * 16) + 25;
        batcher.draw(power, x, y, 16, 16);

        levelInt = myWorld.getGameObject(i, j).getLevel();
        levelString = levelInt.toString();
        AssetLoader.pixel.setColor(144 / 255.0f, 147 / 255.0f, 18 / 255.0f, 1f);
        AssetLoader.pixel.draw(batcher, levelString, (x + infoXOff), (y + infoYoff));
    }

    private void drawIndustry(int i, int j) {
        int x = (i * 16);
        int y = (j * 16) + 25;
        batcher.draw(industry, x, y, 16, 16);

        levelInt = myWorld.getGameObject(i, j).getLevel();
        levelString = levelInt.toString();
        AssetLoader.pixel.setColor(80 / 255.0f, 58 / 255.0f, 21 / 255.0f, 1f);
        AssetLoader.pixel.draw(batcher, levelString, (x + infoXOff), (y + infoYoff));
    }

    private void drawSpecial(int i, int j) {
        int x = (i * 16);
        int y = (j * 16) + 25;
        batcher.draw(special, x, y, 16, 16);
    }

    private void drawTrash(int i, int j) {
        int x = (i * 16);
        int y = (j * 16) + 25;
        batcher.draw(trash, x, y, 16, 16);

        levelInt = myWorld.getGameObject(i, j).getLevel();
        levelString = levelInt.toString();
        AssetLoader.pixel.setColor(50 / 255.0f, 50 / 255.0f, 50 / 255.0f, 1f);
        AssetLoader.pixel.draw(batcher, levelString, (x + infoXOff), (y + infoYoff));
    }

    private void drawCity(int i, int j) {
        int x = (i * 16);
        int y = (j * 16) + 25;
        batcher.draw(city, x, y, 16, 16);

        levelInt = myWorld.getGameObject(i, j).getLevel();
        levelString = levelInt.toString();
        AssetLoader.pixel.setColor(82 / 255.0f, 124 / 255.0f, 136 / 255.0f, 1f);
        AssetLoader.pixel.draw(batcher, levelString, (x + infoXOff), (y + infoYoff));
    }

    private void drawSelectionBox(int i, int j) {
        batcher.end();
        //System.out.println("draw at" + i + " " + j);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(255 / 255.0f, 255 / 255.0f, 255 / 255.0f, 0.1f);
        shapeRenderer.rect((i*16), (j*16) + topBarHeight, 16, 15);
        shapeRenderer.end();
        batcher.begin();

    }



    private int scaleX(int screenX) {
        return (int) (screenX / scaleFactorX);
    }

    private int scaleY(int screenY) {
        return (int) (screenY / scaleFactorY);
    }

}
