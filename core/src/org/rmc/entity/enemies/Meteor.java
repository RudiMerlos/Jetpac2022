package org.rmc.entity.enemies;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class Meteor extends Enemy {

    public Meteor(float x, float y, Stage stage) {
        super(x, y, stage, 200);
        this.loadAnimationFromSheet("images/enemy_meteor.png", 1, 2, 0.05f, true);
        this.setBoundaryPolygon(8);

        this.startLeft = MathUtils.random(100) > 90;
        if (!this.startLeft)
            this.setScale(-1);

        this.setColor(COLORS[MathUtils.random(COLORS.length - 1)]);

        this.direction = this.startLeft ? MathUtils.random(340, 360) : MathUtils.random(180, 200);
    }

    @Override
    public int getScore() {
        return 25;
    }

}
