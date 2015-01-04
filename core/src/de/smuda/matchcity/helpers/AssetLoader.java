package de.smuda.matchcity.helpers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.Texture.TextureFilter;

/**
 * Created by denni_000 on 02.01.2015.
 */
public class AssetLoader {

    public static Texture texture, buttons;
    public static TextureRegion empty,  housing, farm, government,
            park, road, shops, power, industry,
            special, trash, city;

    public static TextureRegion houseB, farmB, governB, parkB;//usw


    public static BitmapFont font, shadow, pixel, text;

    private static Preferences prefs;

    public static void load() {

        // Load assets here..
        texture = new Texture(Gdx.files.internal("idleCity.png"));
        texture.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);

        empty = new TextureRegion(texture, 0, 0, 16, 16);
        empty.flip(false, true);
        housing = new TextureRegion(texture, 32, 0, 16, 16);
        housing.flip(false, true);
        farm = new TextureRegion(texture, 16, 0, 16, 16);
        farm.flip(false, true);
        government = new TextureRegion(texture, 48, 0, 16, 16);
        government.flip(false, true);

        park = new TextureRegion(texture, 0, 16, 16, 16);
        park.flip(false, true);
        road = new TextureRegion(texture, 16, 16, 16, 16);
        road.flip(false, true);
        shops = new TextureRegion(texture, 32, 16, 16, 16);
        shops.flip(false, true);
        power = new TextureRegion(texture, 48, 16, 16, 16);
        power.flip(false, true);
        industry = new TextureRegion(texture, 0, 32, 16, 16);
        industry.flip(false, true);
        special = new TextureRegion(texture, 16, 32, 16, 16);
        special.flip(false, true);
        trash = new TextureRegion(texture, 16, 48, 16, 16);
        trash.flip(false, true);
        city = new TextureRegion(texture, 0, 48, 16, 16);
        city.flip(false, true);



        // Load text
        font = new BitmapFont(Gdx.files.internal("data/text.fnt"));
        font.setScale(.2f, -.2f);
        shadow = new BitmapFont(Gdx.files.internal("data/shadow.fnt"));
        shadow.setScale(.2f, -.2f);
        pixel = new BitmapFont(Gdx.files.internal("data/04b_19.fnt"));
        pixel.setScale(.3f, -.3f);
        text = new BitmapFont(Gdx.files.internal("data/lcd_solid.fnt"));
        text.setScale(.2f, -.2f);


        // Create or retrieve existing Preferences file
        prefs = Gdx.app.getPreferences("MatchCity");
        if(!prefs.contains("highScore")) {
            prefs.putInteger("highScore", 0);
        }

    }


    public static void setHighScore(int val) {
        prefs.putInteger("highScore", val);
        prefs.flush();
    }

    public static int getHighScore() {
        return prefs.getInteger("highScore");
    }

    public static void dispose() {
        texture.dispose();

        font.dispose();
        shadow.dispose();
    }
}
