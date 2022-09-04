package org.rmc.entity;

import org.rmc.MainGame;
import org.rmc.framework.base.BaseActor;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class Fuel extends BaseActor {

    private boolean displayed;
    private boolean collected;
    private boolean toRocket;

    private float timer;
    private static final float TIME_TO_SHOW = 2;

    public Fuel(float x, float y, Stage stage) {
        super(x, y, stage);
        this.loadTexture("images/item_fuel.png");
        this.setBoundaryRectangle();

        int posX = MathUtils.random(32, MainGame.WIDTH - (int) this.getWidth() - 32);
        while (posX > 576 && posX < 768)
            posX = MathUtils.random(MainGame.WIDTH - (int) this.getWidth());

        this.setPosition((float) posX, (float) MainGame.HEIGHT - this.getHeight());
        this.setVisible(false);

        this.displayed = false;
        this.collected = false;
        this.toRocket = false;

        this.timer = 0;

        this.setAcceleration(BaseActor.MAX_ACCELERATION);
        this.setMaxSpeed(80);
        this.setDeceleration(BaseActor.MAX_DECELERATION);
    }

    public boolean isDisplayed() {
        return this.displayed;
    }

    public boolean isCollected() {
        return this.collected;
    }

    public void setCollected(boolean collected) {
        this.collected = collected;
    }

    public boolean isToRocket() {
        return this.toRocket;
    }

    public boolean isOver() {
        return this.getX() >= 660 && this.getX() <= 680;
    }

    @Override
    public void act(float delta) {
        super.act(delta);

        if (!this.displayed)
            this.timer += delta;
        if (this.timer > TIME_TO_SHOW) {
            this.setVisible(true);
            this.accelerateAtAngle(270);
        }

        if (this.getY() + this.getHeight() < MainGame.HEIGHT - 8)
            this.displayed = true;

        if (this.isOver()) {
            this.setX(672);
            this.toRocket = true;
        }

        this.applyPhysics(delta);
        this.wrapAroundWorld();
    }

}
