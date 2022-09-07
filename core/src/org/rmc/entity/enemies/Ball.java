package org.rmc.entity.enemies;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class Ball extends Enemy {

    public Ball(float x, float y, Stage stage) {
        super(x, y, stage, 160);
        this.loadAnimationFromSheet("images/enemy_ball.png", 1, 2, 0.2f, true);
        this.setBoundaryPolygon(8);

        this.setColor(COLORS[MathUtils.random(3)]);

        boolean toUp = MathUtils.randomBoolean();
        this.direction = this.startLeft ? (toUp ? 45 : -45) : (toUp ? 135 : 225);
    }

}
