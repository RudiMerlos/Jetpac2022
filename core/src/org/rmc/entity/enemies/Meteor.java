package org.rmc.entity.enemies;

import org.rmc.MainGame;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class Meteor extends Enemy {

    public Meteor(float x, float y, Stage stage) {
        super(x, y, stage, 200);
        this.loadAnimationFromSheet("images/enemy_meteor.png", 1, 2, 0.05f, true);
        this.setBoundaryPolygon(8);

        float startY = MathUtils.random(this.getHeight() * 2,
                MainGame.HEIGHT - this.getHeight() - this.getHeight() / 2);
        this.setPosition(this.startLeft ? -this.getWidth() : MainGame.WIDTH, startY);

        this.setColor(COLORS[MathUtils.random(3)]);

        this.direction = this.startLeft ? MathUtils.random(340, 360) : MathUtils.random(180, 200);
    }

}
