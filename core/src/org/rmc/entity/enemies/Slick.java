package org.rmc.entity.enemies;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class Slick extends Enemy {

    public Slick(float x, float y, Stage stage) {
        super(x, y, stage, 200);
        this.loadAnimationFromSheet("images/enemy_slick.png", 1, 2, 0.05f, true);
        this.setBoundaryPolygon(8);

        this.setColor(COLORS[MathUtils.random(3)]);

        this.direction = this.startLeft ? MathUtils.random(-45, 45) : MathUtils.random(135, 225);
    }

}
