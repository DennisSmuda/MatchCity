package de.smuda.matchcity.scenes;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import de.smuda.matchcity.MatchCity;

/**
 * Created by smuda on 10.02.2015.
 */
public class Scene2DUiDemo1 extends ApplicationAdapter {
    private Skin skin;
    private Stage stage;
    private Stage gameStage;

    boolean slide = false;

    @Override
    public void create() {
        skin = new Skin(Gdx.files.internal("data/uiskin.json"));
        stage = new Stage(new ScreenViewport());
        gameStage = new Stage(new ScreenViewport());

        final TextButton button = new TextButton("Click Me", skin, "default");
        button.setWidth(200);
        button.setHeight(50);

        final Dialog dialog = new Dialog("Click Message", skin);

        button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                dialog.show(stage);
                Timer.schedule(new Timer.Task() {
                    @Override
                    public void run() {
                        dialog.hide();
                    }
                }, 10);
            }
        });
        stage.addActor(button);

        Gdx.input.setInputProcessor(stage);
    }



    @Override
    public void render() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
    }


    public void updateMenu() {
    }

}
