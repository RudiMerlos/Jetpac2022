package org.rmc.entity.enemies;

import org.rmc.entity.Player;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class UFO extends Enemy {

    private Player player;

    public UFO(float x, float y, Stage stage, Player player) {
        super(x, y, stage, 160);
        this.loadTexture("images/enemy_ufo.png");
        this.setBoundaryPolygon(8);

        if (!this.startLeft)
            this.setScaleX(-1);

        int randomWait = MathUtils.random(100);
        this.wait = randomWait > 30;

        this.setColor(COLORS[MathUtils.random(COLORS.length - 1)]);

        this.player = player;
    }

    @Override
    public void act(float delta) {
        super.act(delta);

        if (MathUtils.random(100) > 60) {
            boolean moveAxis = MathUtils.randomBoolean();
            float movement = MathUtils.random(-10, 10);
            if (moveAxis)
                this.setX(this.getX() + movement);
            else
                this.setY(this.getY() + movement);
        }

        this.direction = (float) Math.toDegrees(
                Math.atan2(this.player.getY() - this.getY(), this.player.getX() - this.getX()));

        this.applyPhysics(delta);
        this.wrapAroundWorld();
    }

}
