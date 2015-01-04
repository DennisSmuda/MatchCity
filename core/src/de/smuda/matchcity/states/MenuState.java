package de.smuda.matchcity.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import de.smuda.matchcity.helpers.AssetLoader;
import de.smuda.matchcity.states.TransitionState.Type;
import de.smuda.matchcity.MatchCity;
import de.smuda.matchcity.ui.Graphic;
import de.smuda.matchcity.ui.TextImage;

/**
 * Created by denni_000 on 02.01.2015.
 */
public class MenuState extends State {

    private Graphic title;
    private TextImage play, time;
    private ShapeRenderer shapeRenderer;

    public MenuState(GSM gsm) {
        super(gsm);

        shapeRenderer = new ShapeRenderer();

        title = new Graphic(
                MatchCity.res.getAtlas("pack").findRegion("matchcity"),
                MatchCity.WIDTH / 2,
                MatchCity.HEIGHT / 2 + 100);
        play = new TextImage(
                "play",
                MatchCity.WIDTH / 2,
                MatchCity.HEIGHT / 2 - 50);

        time = new TextImage(
                "time",
                MatchCity.WIDTH / 2,
                MatchCity.HEIGHT / 2 - 120);
    }

   // AssetLoader.pixel.draw(batcher, "Food: ", 150, 4);


    @Override
    public void handleInput() {
        if(Gdx.input.justTouched()) {
            mouse.x = Gdx.input.getX();
            mouse.y = Gdx.input.getY();
            cam.unproject(mouse);

            if(play.contains(mouse.x, mouse.y)) {
                gsm.set(new TransitionState(
                        gsm, this,
                        new PlayState(gsm),
                        Type.BLACK_FADE));
            }
            if(time.contains(mouse.x, mouse.y)) {
                gsm.set(new TransitionState(
                        gsm, this,
                        new TimeState(gsm),
                        Type.BLACK_FADE));
            }
        }
    }

    @Override
    public void update(float dt) {
        handleInput();
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.setProjectionMatrix(cam.combined);
        sb.begin();
        sb.end();

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        //Draw Background Topbar
        shapeRenderer.setColor(120 / 255.0f, 145 / 255.0f, 60 / 255.0f, 1);
        shapeRenderer.rect(0, +100, MatchCity.WIDTH, MatchCity.HEIGHT + 150);
        shapeRenderer.end();

        sb.begin();
        title.render(sb);
        play.render(sb);
       // time.render(sb);
        sb.end();
    }
}
