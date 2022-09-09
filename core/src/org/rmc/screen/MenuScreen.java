package org.rmc.screen;

import org.rmc.MainGame;
import org.rmc.framework.base.BaseActor;
import org.rmc.framework.base.BaseGame;
import org.rmc.framework.base.BaseScreen;
import com.badlogic.gdx.Input.Keys;

public class MenuScreen extends BaseScreen {

    @Override
    public void initialize() {
        BaseActor background = new BaseActor(0, 0, this.mainStage);
        background.loadTexture("images/menu.png");
    }

    @Override
    public void update(float delta) {
        // not used
    }

    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Keys.ENTER) {
            MainGame.reset();
            BaseGame.setActiveScreen(new LevelScreen());
        }
        return false;
    }

}
