package org.rmc.entity.enemies;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class Slick extends Enemy {

    public Slick(float x, float y, Stage stage) {
        super(x, y, stage, 200);
        this.loadAnimationFromSheet("images/enemy_slick.png", 1, 2, 0.1f, true);
        this.setBoundaryPolygon(8);

        if (!this.startLeft)
            this.setScale(-1);

        this.setColor(COLORS[MathUtils.random(COLORS.length - 1)]);

        boolean toUp = MathUtils.randomBoolean();
        this.direction = this.startLeft ? (toUp ? 45 : -45) : (toUp ? 135 : 225);
    }

}
