package org.rmc.entity;

import org.rmc.MainGame;
import org.rmc.framework.base.BaseActor;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class RocketBottom extends BaseActor {

    private boolean active;

    public RocketBottom(float x, float y, Stage stage) {
        super(x, y, stage);
        this.loadTexture(MainGame.getRocketBottomImage());
        this.setBoundaryRectangle();
        this.active = false;
    }

    public boolean isActive() {
        return this.active;
    }

}
