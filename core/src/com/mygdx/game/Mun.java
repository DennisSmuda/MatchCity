package com.mygdx.game;

import Helpers.AssetLoader;
import Screens.GameScreen;
import com.badlogic.gdx.Game;


public class Mun extends Game {
	
	@Override
	public void create () {
		AssetLoader.load();
		setScreen(new GameScreen());
	}

	@Override
	public void dispose() {
		super.dispose();
		AssetLoader.dispose();
	}
}
