package org.rmc.entity.enemies;

import org.rmc.MainGame;
import org.rmc.framework.base.BaseActor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Stage;

public abstract class Enemy extends BaseActor {

    protected boolean startLeft;
    protected float direction;
    protected boolean wait;

    private float timer;
    private static final float TIME_TO_WAIT = 1;

    private float startY;
    private float speed;

    protected static final Color[] COLORS = {Color.CYAN, Color.MAGENTA, Color.GREEN, Color.RED};

    protected Enemy(float x, float y, Stage stage, float speed) {
        super(x, y, stage);

        this.wait = false;
        this.timer = 0;

        this.startLeft = MathUtils.randomBoolean();

        this.startY = MathUtils.random(this.getHeight() * 3,
                MainGame.HEIGHT - this.getHeight() - this.getHeight() * 3);
        this.setPosition(this.startLeft ? -this.getWidth() : MainGame.WIDTH, this.startY);

        this.setAcceleration(BaseActor.MAX_ACCELERATION);
        this.setMaxSpeed(speed);
        this.setDeceleration(BaseActor.MAX_DECELERATION);

        this.speed = speed;
    }

    public void changeDirectionY() {
        this.direction = -this.direction;
    }

    public void changeDirectionX() {
        this.direction -= 180;
    }

    @Override
    public void act(float delta) {
        super.act(delta);

        if (!this.wait) {
            this.accelerateAtAngle(this.direction);
        } else {
            this.timer += delta;
            this.setMaxSpeed(this.speed / 2);
            if (this.getY() > this.startY + 20)
                this.accelerateAtAngle(270);
            else
                this.accelerateAtAngle(90);
            if (this.timer > TIME_TO_WAIT) {
                this.wait = false;
                this.setMaxSpeed(this.speed);
            }
        }

        this.applyPhysics(delta);
        this.wrapAroundWorld();
    }

}
