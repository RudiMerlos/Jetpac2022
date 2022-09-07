package org.rmc.entity.enemies;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class Spaceship extends Enemy {

    public Spaceship(float x, float y, Stage stage) {
        super(x, y, stage, 200);
        this.loadTexture("images/enemy_spaceship.png");
        this.setBoundaryPolygon(8);

        if (!this.startLeft)
            this.setScale(-1);

        this.setColor(COLORS[MathUtils.random(COLORS.length - 1)]);

        this.direction = this.startLeft ? MathUtils.random(340, 360) : MathUtils.random(180, 200);
    }

}
