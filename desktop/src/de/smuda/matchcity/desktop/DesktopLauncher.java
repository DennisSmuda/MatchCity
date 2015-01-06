package de.smuda.matchcity.desktop;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import de.smuda.matchcity.MatchCity;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "Match City";
		config.width = 320;
		config.height = 480;
		config.addIcon("icon_64.png", Files.FileType.Internal);
		new LwjglApplication(new MatchCity(), config);
	}
}
