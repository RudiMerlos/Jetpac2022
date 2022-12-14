package org.rmc.framework.base;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;

/**
 * Created when program is launched. Manages the screens that appear during the game.
 */
public abstract class BaseGame extends Game {

    // Stores reference to game; used when calling setActiveScreen method.
    private static BaseGame game;

    // BitmapFont + Color
    public static final LabelStyle labelStyle = new LabelStyle();

    // NPD + BitmapFont + Color
    public static final TextButtonStyle textButtonStyle = new TextButtonStyle();

    /**
     * Called when game is initialized. Stores global reference to game object.
     */
    protected BaseGame() {
        game = this;
    }

    /**
     * Called when game is initialized, after Gdx.input and other objects have been initialized.
     */
    public void create() {
        this.create(null, null);
    }

    /**
     * Called when game is initialized, after Gdx.input and other objects have been initialized.
     *
     * @param fontFile String path to font file
     */
    public void create(String fontFile) {
        this.create(fontFile, null);
    }

    /**
     * Called when game is initialized, after Gdx.input and other objects have been initialized.
     *
     * @param fontFile String path to font file
     * @param buttonImage String path to button image file
     */
    public void create(String fontFile, String buttonImage) {
        // prepare for multiple classes/stages to receive discrete input
        Gdx.input.setInputProcessor(new InputMultiplexer());

        if (fontFile != null) {
            // parameters for generating a custom bitmap font
            FreeTypeFontGenerator fontGenerator =
                    new FreeTypeFontGenerator(Gdx.files.internal(fontFile));
            FreeTypeFontParameter fontParameters = new FreeTypeFontParameter();
            fontParameters.size = 20;
            fontParameters.color = Color.WHITE;
            fontParameters.borderWidth = 0;
            fontParameters.borderColor = fontParameters.color;
            fontParameters.borderStraight = true;
            fontParameters.minFilter = TextureFilter.Linear;
            fontParameters.magFilter = TextureFilter.Linear;

            BitmapFont customFont = fontGenerator.generateFont(fontParameters);

            labelStyle.font = customFont;

            if (buttonImage != null) {
                NinePatch buttonPatch = new NinePatch(new Texture(buttonImage), 24, 24, 24, 24);
                textButtonStyle.up = new NinePatchDrawable(buttonPatch);
                textButtonStyle.font = customFont;
                textButtonStyle.fontColor = Color.GRAY;
            }
        }
    }

    /**
     * Used to switch screens while game is running. Method is static to simplify usage.
     *
     * @param screen BaseScreen to switch
     */
    public static void setActiveScreen(BaseScreen screen) {
        game.setScreen(screen);
    }

}
