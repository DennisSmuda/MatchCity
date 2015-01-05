package de.smuda.matchcity.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import de.smuda.matchcity.MatchCity;
import de.smuda.matchcity.helpers.AssetLoader;
import de.smuda.matchcity.ui.TextImage;

/**
 * Created by denni_000 on 02.01.2015.
 */
public class ScoreState extends State {

    private TextImage image, goAgain, scoreT;
    private int score;

    public ScoreState(GSM gsm, int newScore) {
        super(gsm);
        score = newScore;
        image = new TextImage(
                Integer.toString(score),
                MatchCity.WIDTH / 2,
                MatchCity.HEIGHT / 2);
        scoreT = new TextImage(
                "Score",
                MatchCity.WIDTH / 2,
                MatchCity.HEIGHT / 2 + 50);
        goAgain = new TextImage(
                "Go Again",
                MatchCity.WIDTH / 2,
                MatchCity.HEIGHT / 2 - 50);
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
        sb.setProjectionMatrix(cam.combined);
        sb.begin();
        //AssetLoader.shadow.setScale(1f, 1f);
        //AssetLoader.font.setScale(1f, 1f);
        //AssetLoader.shadow.draw(sb,"$ " + score, 5 ,Gdx.graphics.getHeight()/2);
        //AssetLoader.font.draw(sb,"$ " + score, 4, (Gdx.graphics.getHeight()/2) + 1);
        //AssetLoader.shadow.setScale(.2f, .2f);
        //AssetLoader.font.setScale(.2f, .2f);

        image.render(sb);
        AssetLoader.scoreText.setColor(61 / 255.0f, 138 / 255.0f, 25 / 255.0f, 1f);
        AssetLoader.scoreText.setScale(1f, 1f);
        AssetLoader.scoreText.draw(sb, "go again", MatchCity.WIDTH / 2 - 30, 120);
        //scoreT.render(sb);
        //goAgain.render(sb);
        sb.end();
    }

}
