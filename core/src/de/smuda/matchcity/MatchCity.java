package de.smuda.matchcity;


import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import de.smuda.matchcity.helpers.AssetLoader;
import de.smuda.matchcity.helpers.Content;
import de.smuda.matchcity.states.GSM;
import de.smuda.matchcity.states.MenuState;


public class MatchCity extends Game {

	public static final String TITLE = "Match City";
	public static final int WIDTH = 320;
	public static final int HEIGHT = 400 ;

	public static Content res;

	private SpriteBatch batch;
	private GSM gsm;

	
	@Override
	public void create () {

		Gdx.gl.glClearColor(49 / 255.0f, 198 / 255.0f, 122 / 255.0f, 1);

		res = new Content();
		res.loadAtlas("pack.pack", "pack");

		AssetLoader.load();

		batch = new SpriteBatch();
		gsm = new GSM();
		gsm.push(new MenuState(gsm));
	}

	@Override
	public void render () {

		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		gsm.update(Gdx.graphics.getDeltaTime());
		gsm.render(batch);
	}

	@Override
	public void dispose() {
		super.dispose();
		AssetLoader.dispose();
	}
}
