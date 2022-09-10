package org.rmc.screen;

import org.rmc.MainGame;
import org.rmc.framework.base.BaseActor;
import org.rmc.framework.base.BaseGame;
import org.rmc.framework.base.BaseScreen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.audio.Sound;

public class MenuScreen extends BaseScreen {

    private Sound startSound;

    @Override
    public void initialize() {
        BaseActor background = new BaseActor(0, 0, this.mainStage);
        background.loadTexture("images/menu.png");

        this.startSound = Gdx.audio.newSound(Gdx.files.internal("sounds/start.ogg"));
    }

    @Override
    public void update(float delta) {
        // not used
    }

    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Keys.ENTER) {
            this.startSound.play();
            MainGame.reset();
            BaseGame.setActiveScreen(new LevelScreen());
        }
        return false;
    }

}
