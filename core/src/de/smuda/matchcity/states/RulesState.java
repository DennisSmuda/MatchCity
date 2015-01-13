package de.smuda.matchcity.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import de.smuda.matchcity.MatchCity;
import de.smuda.matchcity.helpers.AssetLoader;

/**
 * Created by smuda on 06.01.2015.
 */
public class RulesState extends State {

    private TextureRegion tnt, swap, reset;
    private ShapeRenderer shapeRenderer;

    public RulesState(GSM gsm) {
        super(gsm);

        tnt = AssetLoader.tnt;
        tnt.flip(false, true);
        swap = AssetLoader.swap;
        swap.flip(false, true);
        reset = AssetLoader.reset;
        reset.flip(false, true);

        shapeRenderer = new ShapeRenderer();
    }


    @Override
    public void handleInput() {
        if(Gdx.input.justTouched()) {
            reset.flip(false, true);
            tnt.flip(false, true);
            swap.flip(false, true);

            gsm.set(new TransitionState(
                    gsm, this,
                    new PlayState(gsm),
                    TransitionState.Type.BLACK_FADE));
        }
    }

    @Override
    public void update(float dt) {
        handleInput();
    }

    @Override
    public void render(SpriteBatch sb) {

        Gdx.gl.glClearColor(125 / 255.0f, 200 / 255.0f, 112 / 255.0f, 1);

        sb.setProjectionMatrix(cam.combined);

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        //Draw Background
        shapeRenderer.setColor(125 / 255.0f, 200 / 255.0f, 112 / 255.0f, 1);
        shapeRenderer.rect(0, 0, MatchCity.WIDTH, MatchCity.HEIGHT + 150);
        shapeRenderer.end();

        sb.begin();
        AssetLoader.scoreStateText.setScale(.45f, .45f);
        AssetLoader.scoreStateText.draw(sb, "This is a tile matching game!", 30, MatchCity.HEIGHT / 2 + 140);

        // Top text
        AssetLoader.scoreStateText.setScale(.35f, .35f);
        AssetLoader.scoreStateText.draw(sb, "Place your next tile and try to match ", 15, MatchCity.HEIGHT / 2 + 110);
        AssetLoader.scoreStateText.draw(sb, "at least three tiles of the same color", 15, MatchCity.HEIGHT / 2 + 95);
        AssetLoader.scoreStateText.draw(sb, "Every 5 turns, three random tiles will  ", 15, MatchCity.HEIGHT / 2 + 80);
        AssetLoader.scoreStateText.draw(sb, "spawn. Which can be delayed by matching ", 15, MatchCity.HEIGHT / 2 + 65);
        AssetLoader.scoreStateText.draw(sb, "tiles in the meantime.", 15, MatchCity.HEIGHT / 2 + 50);

        // Show powerups
        drawTNT(sb, MatchCity.WIDTH / 2 - 60, MatchCity.HEIGHT / 2 - 15 );
        drawSwap(sb, MatchCity.WIDTH / 2 - 10, MatchCity.HEIGHT / 2 - 15);
        drawReset(sb, MatchCity.WIDTH / 2 + 40, MatchCity.HEIGHT / 2 - 15);

        // Explain powerups
        AssetLoader.scoreStateText.draw(sb, "Above, you can see the three powerups  ", 15, MatchCity.HEIGHT / 2 - 40);
        AssetLoader.scoreStateText.draw(sb, "you can get, once you match three crowns ", 15, MatchCity.HEIGHT / 2 - 55);
        AssetLoader.scoreStateText.draw(sb, "T(NT) gets rid of one grey tile ", 15, MatchCity.HEIGHT / 2 - 80);
        AssetLoader.scoreStateText.draw(sb, "S(WAP), swaps your next two tiles ", 15, MatchCity.HEIGHT / 2 - 95);
        AssetLoader.scoreStateText.draw(sb, "R(ESET), combines all tiles at once ", 15, MatchCity.HEIGHT / 2 - 110);


        sb.end();
    }


    private void drawTNT(SpriteBatch sb, int x, int y) {
        sb.draw(tnt, x, y, 32, 32);
    }
    private void drawReset(SpriteBatch sb, int x, int y) {
        sb.draw(reset, x, y, 32, 32);
    }
    private void drawSwap(SpriteBatch sb, int x, int y) {
        sb.draw(swap, x, y, 32, 32);
    }
}