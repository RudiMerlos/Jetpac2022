package org.rmc.entity;

import org.rmc.framework.base.BaseActor;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class Thrusters extends BaseActor {

    public Thrusters(float x, float y, Stage stage) {
        super(x, y, stage);
        this.loadAnimationFromSheet("images/thrusters.png", 1, 2, 0.1f, true);
    }

}
