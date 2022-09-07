package org.rmc.entity.enemies;

import org.rmc.MainGame;
import org.rmc.framework.base.BaseActor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Stage;

public abstract class Enemy extends BaseActor {

    protected boolean startLeft;
    protected float direction;

    protected static final Color[] COLORS = {Color.CYAN, Color.MAGENTA, Color.GREEN, Color.RED};

    protected Enemy(float x, float y, Stage stage, float speed) {
        super(x, y, stage);

        this.startLeft = MathUtils.randomBoolean();

        float startY = MathUtils.random(this.getHeight() * 3,
                MainGame.HEIGHT - this.getHeight() - this.getHeight() * 2);
        this.setPosition(this.startLeft ? -this.getWidth() : MainGame.WIDTH, startY);

        this.setAcceleration(BaseActor.MAX_ACCELERATION);
        this.setMaxSpeed(speed);
        this.setDeceleration(BaseActor.MAX_DECELERATION);
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

        this.accelerateAtAngle(this.direction);

        this.applyPhysics(delta);
        this.wrapAroundWorld();
    }

}
