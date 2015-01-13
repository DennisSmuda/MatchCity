package de.smuda.matchcity.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import de.smuda.matchcity.MatchCity;
import de.smuda.matchcity.helpers.AssetLoader;
import de.smuda.matchcity.ui.TextImage;

/**
 * Created by denni_000 on 02.01.2015.
 */
public class ScoreState extends State {

    private TextImage scoreImage, goAgain, scoreT;
    private int score;
    private int highscore;
    private boolean isNewHigh = false;
    private ShapeRenderer shapeRenderer;

    public ScoreState(GSM gsm, int newScore) {
        super(gsm);
        score = newScore;
        scoreImage = new TextImage(
                Integer.toString(score),
                MatchCity.WIDTH / 2,
                MatchCity.HEIGHT / 2 - 10);
        scoreT = new TextImage(
                "Score",
                MatchCity.WIDTH / 2,
                MatchCity.HEIGHT / 2 + 50);
        goAgain = new TextImage(
                "Go Again",
                MatchCity.WIDTH / 2,
                MatchCity.HEIGHT / 2 - 50);

        highscore = AssetLoader.getHighScore();

        // handle highscore
        if(score > AssetLoader.getHighScore()) {
            isNewHigh = true;
            AssetLoader.setHighScore(score);
        }

        shapeRenderer = new ShapeRenderer();

    }

    public void handleInput() {
        if(Gdx.input.justTouched()) {
            gsm.set(new TransitionState(
                    gsm, this,
                    new MenuState(gsm),
                    TransitionState.Type.BLACK_FADE));
        }
    }

    public void update(float dt) {
        handleInput();
    }

    public void render(SpriteBatch sb) {

        Gdx.gl.glClearColor(125 / 255.0f, 200 / 255.0f, 112 / 255.0f, 1);

        sb.setProjectionMatrix(cam.combined);

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        //Draw Background
        shapeRenderer.setColor(125 / 255.0f, 200 / 255.0f, 112 / 255.0f, 1);
        shapeRenderer.rect(0, 0, MatchCity.WIDTH * 4, MatchCity.HEIGHT * 2);
        shapeRenderer.end();

        sb.begin();

        //AssetLoader.scoreStateText.setColor(39 / 255.0f, 73 / 255.0f, 57 / 255.0f, 1f);
        AssetLoader.scoreStateText.setScale(.75f, .75f);

        if (isNewHigh) {

            AssetLoader.scoreStateText.draw(sb, "New Highscore!", (MatchCity.WIDTH / 2) - 140, MatchCity.HEIGHT / 2 + 70);
            scoreImage.render(sb);

            AssetLoader.scoreStateText.draw(sb, "go again?", MatchCity.WIDTH / 2 - 35, 125);
        } else {

            AssetLoader.scoreStateText.draw(sb, "Highscore: " + highscore, (MatchCity.WIDTH / 2) - 130, MatchCity.HEIGHT / 2 + 130);
            AssetLoader.scoreStateText.draw(sb, "your score: ", (MatchCity.WIDTH / 2) -120, MatchCity.HEIGHT / 2 + 70);
            scoreImage.render(sb);
            AssetLoader.scoreStateText.draw(sb, "go again?", MatchCity.WIDTH / 2 - 800, 120);
        }
        //scoreT.render(sb);
        //goAgain.render(sb);
        sb.end();
    }

}
