package org.rmc.entity;

import org.rmc.MainGame;
import org.rmc.framework.base.BaseActor;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class RocketTop extends BaseActor {

    private boolean active;
    private boolean collected;

    public RocketTop(float x, float y, Stage stage) {
        super(x, y, stage);
        this.loadTexture(MainGame.getRocketTopImage());
        this.setBoundaryRectangle();
        this.active = false;
        this.collected = false;

        this.setAcceleration(BaseActor.MAX_ACCELERATION);
        this.setMaxSpeed(100);
        this.setDeceleration(BaseActor.MAX_DECELERATION);
    }

    public boolean isActive() {
        return this.active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isCollected() {
        return this.collected;
    }

    public void setCollected(boolean collected) {
        this.collected = collected;
    }

    public boolean isOver() {
        return this.getX() >= 667 && this.getX() <= 677 && this.getY() > 69;
    }

    @Override
    public void act(float delta) {
        super.act(delta);

        if (this.isOver()) {
            this.active = false;
            this.collected = false;
            this.setX(672);
            this.accelerateAtAngle(270);
        }

        this.applyPhysics(delta);
        this.wrapAroundWorld();
    }

}
