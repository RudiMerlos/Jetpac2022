package org.rmc.screen;

import org.rmc.MainGame;
import org.rmc.entity.Solid;
import org.rmc.framework.base.BaseScreen;
import org.rmc.framework.tilemap.TilemapActor;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapProperties;

public class LevelScreen extends BaseScreen {

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
    }

    @Override
    public void update(float delta) {

    }

}
