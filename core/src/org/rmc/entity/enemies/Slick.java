package org.rmc.entity.enemies;

import org.rmc.MainGame;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class Slick extends Enemy {

    public Slick(float x, float y, Stage stage) {
        super(x, y, stage, 200);
        this.loadAnimationFromSheet("images/enemy_slick.png", 1, 2, 0.05f, true);
        this.setBoundaryPolygon(8);

        float startY = MathUtils.random(this.getHeight() * 2,
                MainGame.HEIGHT - this.getHeight() - this.getHeight());
        this.setPosition(this.startLeft ? -this.getWidth() : MainGame.WIDTH, startY);

        this.setColor(COLORS[MathUtils.random(3)]);

        boolean toUp = MathUtils.randomBoolean();
        this.direction = this.startLeft ? (toUp ? 45 : -45) : (toUp ? 135 : 225);
    }

}
