package UI;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

/**
 * Created by smuda on 16.12.2014.
 */
public class GameButton {
    private float x, y, width, height;

    private TextureRegion buttonUp, buttonDown;
    private Rectangle bounds;
    private boolean isPressed = false;

    public GameButton(float x, float y, float width, float height,
                      TextureRegion buttonUp, TextureRegion buttondDown) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.buttonUp = buttonUp;
        this.buttonDown = buttondDown;

        bounds = new Rectangle(x, y, width, height);
    }

    public boolean isClicked(int screenX, int screenY) {
        return bounds.contains(screenX, screenY);
    }

    public void draw(SpriteBatch batcher) {
        if (isPressed){
            batcher.draw(buttonDown, x, y, width, height);
        } else {
            batcher.draw(buttonUp, x, y, width, height);

        }
    }

    public boolean isTouchDown(int screenX, int screenY) {
        if (bounds.contains(screenX, screenY)) {
            isPressed = true;
            return true;
        }
        return false;
    }

    public boolean isTouchUp(int screenX, int screenY) {
        if (bounds.contains(screenX, screenY) && isPressed) {
            isPressed = false;
            return true;
        }

        isPressed = false;
        return false;
    }

}
