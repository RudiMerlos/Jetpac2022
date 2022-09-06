package org.rmc.screen;

import org.rmc.MainGame;
import org.rmc.entity.Explosion;
import org.rmc.entity.Fuel;
import org.rmc.entity.Laser;
import org.rmc.entity.Player;
import org.rmc.entity.Rocket;
import org.rmc.entity.RocketBottom;
import org.rmc.entity.RocketMid;
import org.rmc.entity.RocketTop;
import org.rmc.entity.Solid;
import org.rmc.entity.enemies.Enemy;
import org.rmc.entity.enemies.Meteor;
import org.rmc.framework.base.BaseActor;
import org.rmc.framework.base.BaseGame;
import org.rmc.framework.base.BaseScreen;
import org.rmc.framework.tilemap.TilemapActor;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.math.Vector2;

public class LevelScreen extends BaseScreen {

    private Player player;
    private Vector2 playerStartPos;

    private RocketBottom rocketBottom;
    private RocketMid rocketMid;
    private RocketTop rocketTop;
    private Rocket rocket;
    private BaseActor rocketBase;
    private BaseActor rocketExit;

    private Fuel fuel;

    private float timer;
    private float timeToInit;
    private static final float TIME_TO_DIE = 1;

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

        MapProperties playerProperties =
                tma.getRectangleList("start_player").get(0).getProperties();
        this.playerStartPos =
                new Vector2((float) playerProperties.get("x"), (float) playerProperties.get("y"));
        this.player = new Player(this.playerStartPos.x, this.playerStartPos.y, this.mainStage);
        this.player.setVisible(false);

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

        MapProperties rocketBaseProperties =
                tma.getRectangleList("rocket_base").get(0).getProperties();
        this.rocketBase = new BaseActor((float) rocketBaseProperties.get("x"),
                (float) rocketBaseProperties.get("y"), this.mainStage);
        this.rocketBase.setSize((float) rocketBaseProperties.get("width"),
                (float) rocketBaseProperties.get("height"));
        this.rocketBase.setBoundaryRectangle();

        MapProperties rocketExitProperties =
                tma.getRectangleList("rocket_exit").get(0).getProperties();
        this.rocketExit = new BaseActor((float) rocketExitProperties.get("x"),
                (float) rocketExitProperties.get("y"), this.mainStage);
        this.rocketExit.setSize((float) rocketExitProperties.get("width"),
                (float) rocketExitProperties.get("height"));
        this.rocketExit.setBoundaryRectangle();

        this.fuel = null;

        this.timer = 0;
        if (newPlanet)
            this.timeToInit = 3;
        else
            this.timeToInit = 12.5f;
    }

    @Override
    public void update(float delta) {
        if (!this.player.isVisible() && (this.rocket.getState() == 0 || this.player.isDead()))
            this.initLevel(delta);
        else if (this.player.isDead())
            this.waitToInit(delta);

        this.checkForSolidCollision();
        this.checkForRocketMidCollision();
        this.checkForRocketTopCollision();
        this.checkForEnemyCollision();
        this.checkForLaserCollision();
        this.checkForFuelCollision();

        // win level
        if (this.rocket.getState() == 6 && this.player.overlaps(this.rocket, 0.001f)) {
            this.rocket.setBlastOff(true);
            this.player.setPosition(-10000, -10000);
            this.player.setVisible(false);
        }

        if (this.rocket.getState() == 6 && this.rocket.overlaps(this.rocketExit)) {
            MainGame.incrementLevel();
            BaseGame.setActiveScreen(new LevelScreen());
        }
    }

    private void initLevel(float delta) {
        this.timer += delta;
        if (this.timer > this.timeToInit) {
            this.player.setDead(false);
            this.player.setVisible(true);
            this.createEnemies();
            if (this.rocket.isVisible())
                this.createFuel();
            this.timer = 0;
        }
    }

    private void waitToInit(float delta) {
        this.timer += delta;
        if (this.timer > TIME_TO_DIE) {
            for (BaseActor enemy : BaseActor.getList(this.mainStage, Enemy.class)) {
                enemy.remove();
                enemy.setVisible(false);
                enemy.setPosition(-10000, -10000);
            }
            for (BaseActor fuel : BaseActor.getList(this.mainStage, Fuel.class)) {
                fuel.remove();
                fuel.setVisible(false);
                fuel.setPosition(-10000, -10000);
            }
            this.player.setVisible(false);
            this.player.setPosition(this.playerStartPos.x, this.playerStartPos.y);
            this.timer = 0;
        }
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
                if (rocket.getState() != 6 && rocket.isFullDisplayed())
                    rocket.preventOverlap(solid);
            }

            for (BaseActor enemyActor : BaseActor.getList(this.mainStage, Enemy.class)) {
                Enemy enemy = (Enemy) enemyActor;
                if (enemy.overlaps(solid)) {
                    if (enemy instanceof Meteor)
                        this.removeEnemy(enemyActor);
                    else
                        enemy.changeDirectionY();
                }
            }

            for (BaseActor fuelActor : BaseActor.getList(this.mainStage, Fuel.class)) {
                Fuel fuel = (Fuel) fuelActor;
                if (fuel.isDisplayed())
                    fuel.preventOverlap(solid);
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
                this.rocketBottom = null;
                this.rocketMid = null;
                this.rocketTop = null;
                this.createFuel();
            }
        }
    }

    private void checkForEnemyCollision() {
        for (BaseActor enemy : BaseActor.getList(this.mainStage, Enemy.class)) {
            if (this.player.overlaps(enemy)) {
                this.player.setDead(true);
                this.player.setPosition(-10000, -10000);
                this.removeEnemy(enemy);
            }
        }
    }

    private void checkForLaserCollision() {
        for (BaseActor laser : BaseActor.getList(this.mainStage, Laser.class)) {
            for (BaseActor enemy : BaseActor.getList(this.mainStage, Enemy.class)) {
                if (laser.overlaps(enemy))
                    this.removeEnemy(enemy);
            }
        }
    }

    private void checkForFuelCollision() {
        if (this.fuel == null)
            return;

        if (this.player.overlaps(this.fuel) && !this.fuel.isOver() && this.fuel.isDisplayed())
            this.fuel.setCollected(true);

        if (this.fuel.isCollected() && !this.fuel.isOver())
            this.fuel.centerAtActor(this.player);

        if (this.fuel.overlaps(this.rocketBase) && this.fuel.isOver()) {
            this.rocket.incrementState();
            this.fuel.remove();
            if (!this.player.isDead())
                this.createFuel();
            else
                this.fuel = null;
        }
    }

    private void createEnemies() {
        if (MainGame.getLevel() == 1 || MainGame.getLevel() == 9) {
            for (int i = 0; i < MainGame.getMaxEnemies(); i++)
                new Meteor(0, 0, this.mainStage);
        }
    }

    private void createNewEnemy(Enemy enemyDestroyed) {
        if (enemyDestroyed instanceof Meteor) {
            if (this.rocket.getState() != 6)
                new Meteor(0, 0, this.mainStage);
        }
    }

    private void removeEnemy(BaseActor enemy) {
        Explosion explosion = new Explosion(0, 0, this.mainStage);
        explosion.centerAtActor(enemy);
        enemy.remove();
        enemy.setVisible(false);
        enemy.setPosition(-10000, -10000);
        if (this.player.isVisible())
            this.createNewEnemy((Enemy) enemy);
    }

    private void createFuel() {
        if (this.rocket.getState() < 6 && !this.player.isDead())
            this.fuel = new Fuel(0, 0, this.mainStage);
    }

    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Keys.SPACE)
            this.player.shoot();
        return false;
    }

}
