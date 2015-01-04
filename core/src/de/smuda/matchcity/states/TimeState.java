package de.smuda.matchcity.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import de.smuda.matchcity.MatchCity;
import de.smuda.matchcity.ui.Score;
import de.smuda.matchcity.ui.TextImage;
import de.smuda.matchcity.ui.Tile;

import java.util.Random;

/**
 * Created by denni_000 on 02.01.2015.
 */
public class TimeState extends State {

    private final int MAX_FINGERS = 4;

    Random random;
    private Score score;
    private float timer;

    private Tile[][] tiles;
    private int tileSize;
    private float boardOffset;
    private int boardHeight;

    private TextImage back;


    public TimeState(GSM gsm) {
        super(gsm);

        random = new Random();
        createBoard(4, 4);
        score = new Score(MatchCity.WIDTH/2, MatchCity.HEIGHT - 50);
        back = new TextImage("back", MatchCity.WIDTH / 2, 100);
    }

    private void createBoard(int numRows, int numCols) {
        tiles = new Tile[numRows][numCols];
        tileSize = MatchCity.WIDTH / tiles[0].length;
        boardHeight = tileSize * tiles.length;
        boardOffset = (MatchCity.HEIGHT - boardHeight) / 2;
        for(int row = 0; row < tiles.length; row++) {
            for(int col = 0; col < tiles[0].length; col++) {
                tiles[row][col] =
                        new Tile(
                                col * tileSize + tileSize / 2,
                                row * tileSize + boardOffset + tileSize / 2,
                                tileSize,
                                tileSize);
                tiles[row][col].setTimer(
                        (-(tiles.length - row) - col) * 0.1f);
                tiles[row][col].setSelected(true);
            }
        }
    }

    public void updateSelected(float dt) {
        timer += dt;

        if(random.nextInt(1000) < 2 * timer) {
            score.incrementScore(5);
            tiles[random.nextInt(4)][random.nextInt(4)].setSelected(false);
        }
    }

    public void handleInput() {

        int touched = 0;

        for(int i = 0; i < MAX_FINGERS; i++) {
            if(Gdx.input.isTouched(i)) {

                mouse.x = Gdx.input.getX(i);
                mouse.y = Gdx.input.getY(i);
                cam.unproject(mouse);

                if(mouse.x > 0 &&
                        mouse.x < MatchCity.WIDTH &&
                        mouse.y > boardOffset &&
                        mouse.y < boardOffset + boardHeight) {
                    int row = (int) ((mouse.y - boardOffset) / tileSize);
                    int col = (int) (mouse.x / tileSize);
                    if(!tiles[row][col].isSelected()) {
                        tiles[row][col].setSelected(true);
                    }
                    else
                    {
                        //tiles[row][col].setSelected(false);
                    }
                }

                touched++;
            }
        }

        if(touched == 2)
        {
            score.incrementScore(20);
        }

        if(touched == 4)
        {
            score.incrementScore(40);
        }

        if(Gdx.input.justTouched()) {
            mouse.x = Gdx.input.getX();
            mouse.y = Gdx.input.getY();
            cam.unproject(mouse);
            if(back.contains(mouse.x, mouse.y)) {
                gsm.set(new TransitionState(
                        gsm,
                        this,
                        new MenuState(gsm),
                        TransitionState.Type.EXPAND));
            }
        }

    }

    public void update(float dt) {

        handleInput();

        updateSelected(dt);
        score.update(dt);

        for(int row = 0; row < tiles.length; row++) {
            for(int col = 0; col < tiles[0].length; col++) {
                tiles[row][col].update(dt);
            }
        }

        boolean remaining = false;
        for(int row = 0; row < tiles.length; row++) {
            for(int col = 0; col < tiles[0].length; col++) {
                remaining = tiles[row][col].isSelected();
                if(remaining == true) { break; }
            }
        }

        if(!remaining)
        {
            gsm.set(new TransitionState(
                    gsm,
                    this,
                    new ScoreState(gsm, score.getScore()),
                    TransitionState.Type.EXPAND));
        }
    }

    public void render(SpriteBatch sb) {

        sb.setProjectionMatrix(cam.combined);
        sb.begin();

        score.render(sb);
        back.render(sb);

        for(int row = 0; row < tiles.length; row++) {
            for(int col = 0; col < tiles[0].length; col++) {
                tiles[row][col].render(sb);
            }
        }

        sb.end();

    }
}
