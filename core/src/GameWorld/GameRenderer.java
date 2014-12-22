package GameWorld;

import GameObjects.GameObject;
import GameObjects.Industry;
import Helpers.AssetLoader;
import Helpers.ResourceHandler;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

/**
 * Created by smuda on 15.12.2014.
 */
public class GameRenderer {

    private GameWorld myWorld;
    private OrthographicCamera cam;
    private ShapeRenderer shapeRenderer;

    private SpriteBatch batcher;

    private int midPointY;
    private int gameHeight;

    private int topBarHeight = 25;
    private String levelString = "";
    private Integer levelInt;

    private boolean selected = false;

    // GameObjects


    // GameInfo
    private int currentFood;
    private int currentPower;
    private int currentJobs;
    private int currentLiving;
    private int currentTrash;
    private int currentIncome;

    private int infoXOff = 5;
    private int infoYoff = 4;


    // GameAssets
    public static TextureRegion empty,  housing, farm, government,
            park, road, shops, power, industry,
            special, trash, city;


    public GameRenderer(GameWorld world, int gameHeight, int midPointY) {
        myWorld = world;

        this.gameHeight = gameHeight;
        this.midPointY = midPointY;

        cam = new OrthographicCamera();
        cam.setToOrtho(true, 128, gameHeight); //136

        batcher = new SpriteBatch();
        batcher.setProjectionMatrix(cam.combined);
        shapeRenderer = new ShapeRenderer();
        shapeRenderer.setProjectionMatrix(cam.combined);

        levelInt = new Integer(0);

        initGameObjects();
        initAssets();
    }

    public void render(float runTime) {

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
        GameObject currentObj;

        // Run through entire array and draw the right stuff.
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 9; j++) {
                currentObj = myWorld.getGameObject(i, j);
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
        String netWorth = myWorld.getWorth() + "";
        // System out print ln (make something amazin for this game
        AssetLoader.shadow.draw(batcher,"$ " + netWorth, 5, 6);
        AssetLoader.font.draw(batcher,"$ " + netWorth, 4, 5);

        drawNextTile();
        drawInfo();

        batcher.end();


    }

    private void drawInfo() {


        levelString = levelInt.toString();
        AssetLoader.pixelInfo.setColor(255 / 255.0f, 255 / 255.0f, 255 / 255.0f, 1f);
        AssetLoader.pixelInfo.setScale(.15f, -.15f);
        AssetLoader.pixelInfo.draw(batcher, "Citizens: " + ResourceHandler.citizens, 5, 174);
        AssetLoader.pixelInfo.draw(batcher, "Food: " + ResourceHandler.food, 5, 184);
    }

    private void drawNextTile() {

        AssetLoader.pixelInfo.draw(batcher, "Next tile: ", 60, 5);
        AssetLoader.pixelInfo.draw(batcher, "Random tiles: ", 60, 15);


        AssetLoader.font.setScale(.2f, -.2f);
        AssetLoader.shadow.setScale(.2f, -.2f);

        int x = 100;
        int y = 3;
        // draw next three elements
        for (int i = 0; i < 1; i++) {
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

        x = 100;
        y = 13;
        // draw next random elements
        for (int i = 0; i < 5; i++) {
            switch (myWorld.nextRandoms[i]) {
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
            x += 5;
            if (x >= 125) y += 10;
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

    private void initGameObjects() {

    }


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
}
