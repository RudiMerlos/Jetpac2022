package org.rmc.screen;

import org.rmc.MainGame;
import org.rmc.entity.Laser;
import org.rmc.entity.Player;
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

        if (MainGame.isNewPlanet()) {
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
        } else {
            this.rocketBottom = null;
            this.rocketMid = null;
            this.rocketTop = null;
        }

        MapProperties playerProperties =
                tma.getRectangleList("start_player").get(0).getProperties();
        this.player = new Player((float) playerProperties.get("x"),
                (float) playerProperties.get("y"), this.mainStage);
    }

    @Override
    public void update(float delta) {
        // Solid collision
        for (BaseActor solid : BaseActor.getList(this.mainStage, Solid.class)) {
            this.player.preventOverlap(solid);

            for (BaseActor laser : BaseActor.getList(this.mainStage, Laser.class)) {
                if (laser.overlaps(solid))
                    laser.remove();
            }
        }

        // Rocket mid collision
        if (this.rocketMid != null && this.player.overlaps(this.rocketMid)
                && this.rocketMid.isActive()) {
            this.rocketMid.setActive(false);
            this.rocketMid.setCollected(true);
        }

        if (this.rocketMid.isCollected() && !this.rocketMid.isOver())
            this.rocketMid.centerAtActor(this.player);

        if (this.rocketMid != null && this.rocketMid.overlaps(this.rocketBottom, 1.01f)) {
            this.rocketMid.preventOverlap(this.rocketBottom);
            this.rocketMid.setPlaced(true);
            this.rocketTop.setActive(true);
        }

        // Rocket top collision
        if (this.rocketMid.isPlaced()) {
            if (this.rocketTop != null && this.player.overlaps(this.rocketTop)
                    && this.rocketTop.isActive()) {
                this.rocketTop.setActive(false);
                this.rocketTop.setCollected(true);
            }

            if (this.rocketTop.isCollected() && !this.rocketTop.isOver())
                this.rocketTop.centerAtActor(this.player);

            if (this.rocketTop != null && this.rocketTop.overlaps(this.rocketMid, 1.01f)) {
                this.rocketTop.preventOverlap(this.rocketMid);
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
