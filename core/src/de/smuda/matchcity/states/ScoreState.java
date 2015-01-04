package de.smuda.matchcity.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import de.smuda.matchcity.MatchCity;
import de.smuda.matchcity.ui.TextImage;

/**
 * Created by denni_000 on 02.01.2015.
 */
public class ScoreState extends State {

    private TextImage image;

    public ScoreState(GSM gsm, int score) {
        super(gsm);
        image = new TextImage(
                Integer.toString(score),
                MatchCity.WIDTH / 2,
                MatchCity.HEIGHT / 2);
    }

    public void handleInput() {
        if(Gdx.input.justTouched()) {
            gsm.set(new TransitionState(
                    gsm, this,
                    new MenuState(gsm),
                    TransitionState.Type.EXPAND));
        }
    }

    public void update(float dt) {
        handleInput();
    }

    public void render(SpriteBatch sb) {
        sb.setProjectionMatrix(cam.combined);
        sb.begin();
        image.render(sb);
        sb.end();
    }

}
