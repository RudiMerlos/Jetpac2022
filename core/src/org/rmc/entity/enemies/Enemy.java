package org.rmc.entity.enemies;

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

        this.setAcceleration(BaseActor.MAX_ACCELERATION);
        this.setMaxSpeed(speed);
        this.setDeceleration(BaseActor.MAX_DECELERATION);
    }

    @Override
    public void act(float delta) {
        super.act(delta);

        this.accelerateAtAngle(this.direction);

        this.applyPhysics(delta);
        this.wrapAroundWorld();
    }

}
