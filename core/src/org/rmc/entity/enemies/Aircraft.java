package org.rmc.entity.enemies;

import org.rmc.MainGame;
import org.rmc.entity.Player;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class Aircraft extends Enemy {

    protected static final Color[] COLORS_AIRCRAFT =
            {Color.CYAN, Color.MAGENTA, Color.GREEN, Color.RED, Color.WHITE};

    public Aircraft(float x, float y, Stage stage, Player player) {
        super(x, y, stage, 200);
        this.loadTexture("images/enemy_aircraft.png");
        this.setBoundaryPolygon(8);

        if (!this.startLeft)
            this.setScaleX(-1);

        int randomWait = MathUtils.random(100);
        this.wait = randomWait > 30;

        float startY = MathUtils.random(this.getHeight() * 3,
                MainGame.HEIGHT - this.getHeight() - this.getHeight() * 2);
        this.setPosition(this.startLeft ? 0 : MainGame.WIDTH - this.getWidth(), startY);

        this.setColor(COLORS_AIRCRAFT[MathUtils.random(COLORS_AIRCRAFT.length - 1)]);

        this.direction = (float) Math
                .toDegrees(Math.atan2(player.getY() - this.getY(), player.getX() - this.getX()));
    }

    @Override
    public int getScore() {
        return 55;
    }

}
