package org.rmc.screen;

import org.rmc.MainGame;
import org.rmc.entity.Laser;
import org.rmc.entity.Player;
import org.rmc.entity.Rocket;
import org.rmc.entity.RocketBottom;
import org.rmc.entity.RocketMid;
import org.rmc.entity.RocketTop;
import org.rmc.entity.Solid;
import org.rmc.framework.base.BaseActor;
import org.rmc.framework.base.BaseScreen;
import org.rmc.framework.tilemap.TilemapActor;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapProperties;

public class LevelScreen extends BaseScreen {

    private Player player;

    private RocketBottom rocketBottom;
    private RocketMid rocketMid;
    private RocketTop rocketTop;
    private Rocket rocket;

    private float timer;
    private float timeToInit;

    @Override
    public void initialize() {
        TilemapActor tma =
                new TilemapActor("images/map.tmx", MainGame.WIDTH, MainGame.HEIGHT, this.mainStage);

        for (MapObject object : tma.getRectangleList("solid")) {
            MapProperties properties = object.getProperties();
            new Solid((float) properties.get("x"), (float) properties.get("y"),
                    (float) properties.get("width"), (float) properties.get("height"),
                    this.mainStage);
        }

        boolean newPlanet = MainGame.isNewPlanet();
        if (newPlanet) {
            MapProperties rocketBottomProperties =
                    tma.getRectangleList("start_rocket_bottom").get(0).getProperties();
            this.rocketBottom = new RocketBottom((float) rocketBottomProperties.get("x"),
                    (float) rocketBottomProperties.get("y"), this.mainStage);
            MapProperties rocketMidProperties =
                    tma.getRectangleList("start_rocket_mid").get(0).getProperties();
            this.rocketMid = new RocketMid((float) rocketMidProperties.get("x"),
                    (float) rocketMidProperties.get("y"), this.mainStage);
            MapProperties rocketTopProperties =
                    tma.getRectangleList("start_rocket_top").get(0).getProperties();
            this.rocketTop = new RocketTop((float) rocketTopProperties.get("x"),
                    (float) rocketTopProperties.get("y"), this.mainStage);
            this.rocket = new Rocket((float) rocketBottomProperties.get("x"),
                    (float) rocketBottomProperties.get("y"), this.mainStage, newPlanet);
        } else {
            this.rocketBottom = null;
            this.rocketMid = null;
            this.rocketTop = null;
            MapProperties rocketProperties =
                    tma.getRectangleList("start_rocket").get(0).getProperties();
            this.rocket = new Rocket((float) rocketProperties.get("x"),
                    (float) rocketProperties.get("y"), this.mainStage, newPlanet);
        }

        MapProperties playerProperties =
                tma.getRectangleList("start_player").get(0).getProperties();
        this.player = new Player((float) playerProperties.get("x"),
                (float) playerProperties.get("y"), this.mainStage);
        this.player.setVisible(false);

        this.timer = 0;
        if (newPlanet)
            this.timeToInit = 3;
        else
            this.timeToInit = 9.5f;
    }

    @Override
    public void update(float delta) {
        if (!this.player.isVisible()) {
            this.timer += delta;
            if (this.timer > this.timeToInit)
                this.player.setVisible(true);
        }

        this.checkForSolidCollision();

        this.checkForRocketMidCollision();
        this.checkForRocketTopCollision();
    }

    private void checkForSolidCollision() {
        for (BaseActor solid : BaseActor.getList(this.mainStage, Solid.class)) {
            this.player.preventOverlap(solid);

            for (BaseActor laser : BaseActor.getList(this.mainStage, Laser.class)) {
                if (laser.overlaps(solid))
                    laser.remove();
            }

            for (BaseActor rocketActor : BaseActor.getList(this.mainStage, Rocket.class)) {
                Rocket rocket = (Rocket) rocketActor;
                if (rocket.getState() != 6)
                    rocket.preventOverlap(solid);
            }
        }
    }

    private void checkForRocketMidCollision() {
        if (this.rocketMid == null)
            return;

        if (this.player.overlaps(this.rocketMid) && this.rocketMid.isActive()) {
            this.rocketMid.setActive(false);
            this.rocketMid.setCollected(true);
        }

        if (this.rocketMid.isCollected() && !this.rocketMid.isOver())
            this.rocketMid.centerAtActor(this.player);

        if (this.rocketMid.overlaps(this.rocketBottom, 1.01f) && this.rocketMid.isOver()) {
            this.rocketMid.preventOverlap(this.rocketBottom);
            this.rocketMid.setPlaced(true);
            this.rocketTop.setActive(true);
        }
    }

    private void checkForRocketTopCollision() {
        if (this.rocketTop == null)
            return;

        if (this.rocketMid.isPlaced()) {
            if (this.player.overlaps(this.rocketTop) && this.rocketTop.isActive()) {
                this.rocketTop.setActive(false);
                this.rocketTop.setCollected(true);
            }

            if (this.rocketTop.isCollected() && !this.rocketTop.isOver())
                this.rocketTop.centerAtActor(this.player);

            if (this.rocketTop.overlaps(this.rocketMid, 1.01f) && this.rocketTop.isOver()) {
                this.rocketBottom.remove();
                this.rocketMid.remove();
                this.rocketTop.remove();
                this.rocket.setVisible(true);
            }
        }
    }

    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Keys.SPACE)
            this.player.shoot();
        return false;
    }

}
