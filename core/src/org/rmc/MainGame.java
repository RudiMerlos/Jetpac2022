package org.rmc;

import org.rmc.framework.base.BaseGame;
import org.rmc.screen.LevelScreen;

public class MainGame extends BaseGame {

    public static final int WIDTH = 1024;
    public static final int HEIGHT = 768;

    @Override
    public void create() {
        super.create();
        BaseGame.setActiveScreen(new LevelScreen());
    }
}
