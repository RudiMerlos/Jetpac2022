package org.rmc.screen;

import org.rmc.MainGame;
import org.rmc.entity.Explosion;
import org.rmc.entity.Fuel;
import org.rmc.entity.Item;
import org.rmc.entity.Laser;
import org.rmc.entity.Player;
import org.rmc.entity.Rocket;
import org.rmc.entity.RocketBottom;
import org.rmc.entity.RocketMid;
import org.rmc.entity.RocketTop;
import org.rmc.entity.Solid;
import org.rmc.entity.enemies.Aircraft;
import org.rmc.entity.enemies.Alien;
import org.rmc.entity.enemies.Ball;
import org.rmc.entity.enemies.Cross;
import org.rmc.entity.enemies.Enemy;
import org.rmc.entity.enemies.Meteor;
import org.rmc.entity.enemies.Slick;
import org.rmc.entity.enemies.Spaceship;
import org.rmc.entity.enemies.UFO;
import org.rmc.framework.base.BaseActor;
import org.rmc.framework.base.BaseGame;
import org.rmc.framework.base.BaseScreen;
import org.rmc.framework.tilemap.TilemapActor;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

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
    private Item item;
    private float itemTimer;
    private static final float TIME_TO_ITEM = 5;

    private float timer;
    private float timeToInit;
    private static final float TIME_TO_DIE = 1;

    private int score;
    private int lives;

    private Label scoreTitleLabel;
    private Label scoreLabel;
    private Label livesLabel;
    private Table livesTable;
    private Label maxScoreTitleLabel;
    private Label maxScoreLabel;

    private boolean scoreRocketMidPart;
    private boolean scoreRocketTopPart;

    private boolean gameOver;

    private Sound upRocketSound;
    private Sound downRocketSound;
    private Sound startPlayerSound;
    private Sound explosionSound;
    private Sound laserSound;
    private Sound itemSound;

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
        this.item = null;

        this.itemTimer = 0;

        this.timer = 0;
        if (newPlanet)
            this.timeToInit = 3;
        else
            this.timeToInit = 12.5f;

        this.score = MainGame.getScore();
        this.lives = MainGame.getLives();

        this.scoreRocketMidPart = true;
        this.scoreRocketTopPart = true;

        this.gameOver = false;

        this.initializeTables();

        this.upRocketSound = Gdx.audio.newSound(Gdx.files.internal("sounds/up_rocket.ogg"));
        this.downRocketSound = Gdx.audio.newSound(Gdx.files.internal("sounds/down_rocket.ogg"));
        this.startPlayerSound = Gdx.audio.newSound(Gdx.files.internal("sounds/start_player.ogg"));
        this.explosionSound = Gdx.audio.newSound(Gdx.files.internal("sounds/explosion.ogg"));
        this.laserSound = Gdx.audio.newSound(Gdx.files.internal("sounds/laser.ogg"));
        this.itemSound = Gdx.audio.newSound(Gdx.files.internal("sounds/item.ogg"));

        if (!MainGame.isNewPlanet())
            this.downRocketSound.play();
    }

    private void initializeTables() {
        this.scoreTitleLabel = new Label("SCORE", BaseGame.labelStyle);
        this.scoreLabel = new Label(this.getScoreString(this.score), BaseGame.labelStyle);
        this.scoreLabel.setColor(Color.YELLOW);

        this.livesLabel = new Label(String.valueOf(this.lives), BaseGame.labelStyle);
        this.livesTable = new Table();
        BaseActor liveIcon = new BaseActor(0, 0, this.uiStage);
        liveIcon.loadTexture("images/player_icon.png");
        this.livesTable.add(liveIcon);

        this.maxScoreTitleLabel = new Label("HI SCORE", BaseGame.labelStyle);
        this.maxScoreTitleLabel.setColor(Color.CYAN);
        this.maxScoreLabel =
                new Label(this.getScoreString(MainGame.getMaxScore()), BaseGame.labelStyle);
        this.maxScoreLabel.setColor(Color.YELLOW);

        this.uiTable.left().top();
        this.uiTable.pad(10).padLeft(30);
        this.uiTable.add(this.scoreTitleLabel).width(140);
        this.uiTable.add(this.scoreLabel).width(240);
        this.uiTable.add(this.livesLabel);
        this.uiTable.add(this.livesTable).width(100);
        this.uiTable.add(this.maxScoreTitleLabel).width(220).padLeft(100);
        this.uiTable.add(this.maxScoreLabel);
    }

    private String getScoreString(int score) {
        String str = String.valueOf(score);
        StringBuilder scoreStr = new StringBuilder(str);
        for (int i = 0; i < 6 - str.length(); i++)
            scoreStr.insert(0, '0');
        return scoreStr.toString();
    }

    @Override
    public void update(float delta) {
        if (this.gameOver) {
            this.gameOverState(delta);
            return;
        }

        if (!this.player.isVisible() && (this.rocket.getState() == 0 || this.player.isDead()))
            this.initLevel(delta);
        else if (this.player.isDead())
            this.waitToInit(delta);

        this.checkForSolidCollision();
        this.checkForRocketMidCollision();
        this.checkForRocketTopCollision();
        this.checkForEnemyCollision();
        this.checkForEnemyOutOfBounce();
        this.checkForLaserCollision();
        this.checkForFuelCollision();

        this.createItem(delta);
        this.checkForItemCollision();

        // win level
        if (this.rocket.getState() == 6 && this.player.overlaps(this.rocket, 0.001f)) {
            this.rocket.setBlastOff(true);
            this.upRocketSound.play();
            this.player.setPosition(-10000, -10000);
            this.player.setVisible(false);
        }

        if (this.rocket.getState() == 6 && this.rocket.overlaps(this.rocketExit)) {
            MainGame.incrementLevel();
            MainGame.setLives(this.lives);
            BaseGame.setActiveScreen(new LevelScreen());
        }
    }

    // time for wait to initialize level
    private void initLevel(float delta) {
        this.timer += delta;
        if (this.timer > this.timeToInit) {
            this.player.setPosition(this.playerStartPos.x, this.playerStartPos.y);
            this.player.setDead(false);
            this.player.setVisible(true);
            this.createEnemies();
            if (this.rocket.isVisible())
                this.createFuel();
            this.timeToInit = 3;
            this.timer = 0;
            this.downRocketSound.stop();
            this.startPlayerSound.play();
        }
    }

    // time for wait when player is dead
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
                if (enemy.overlaps(solid, 1.01f)) {
                    if (enemy instanceof Meteor || enemy instanceof Aircraft
                            || enemy instanceof Spaceship) {
                        this.removeEnemy(enemyActor);
                    } else {
                        Vector2 v = enemy.preventOverlap(solid);
                        if (enemy instanceof UFO || enemy instanceof Alien) {
                            if (v != null) {
                                if (v.x > 0)
                                    enemy.setX(enemy.getX() + 10);
                                else if (v.x < 0)
                                    enemy.setX(enemy.getX() - 10);
                                if (v.y > 0)
                                    enemy.setY(enemy.getY() + 10);
                                else if (v.y < 0)
                                    enemy.setY(enemy.getY() - 10);
                            }
                        } else {
                            enemy.changeDirectionY();
                            // horizontal overlap
                            if (v != null && Math.abs(v.x) >= Math.abs(v.y))
                                enemy.changeDirectionX();
                        }
                    }
                }
            }

            for (BaseActor fuelActor : BaseActor.getList(this.mainStage, Fuel.class)) {
                Fuel fuel = (Fuel) fuelActor;
                if (fuel.isDisplayed())
                    fuel.preventOverlap(solid);
            }

            for (BaseActor itemActor : BaseActor.getList(this.mainStage, Item.class)) {
                Item item = (Item) itemActor;
                if (item.isDisplayed())
                    item.preventOverlap(solid);
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
            if (this.scoreRocketMidPart) {
                this.setScore(100);
                this.scoreRocketMidPart = false;
            }
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
                if (this.scoreRocketTopPart) {
                    this.setScore(100);
                    this.scoreRocketTopPart = false;
                }
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
                this.lives--;
                this.livesLabel.setText(String.valueOf(this.lives));
                if (this.lives == 0) {
                    this.player.setVisible(false);
                    this.gameOver = true;
                    BaseActor gameOverImage = new BaseActor(0, 0, this.mainStage);
                    gameOverImage.loadTexture("images/gameover.png");
                    gameOverImage.setPosition(MainGame.WIDTH / 2 - gameOverImage.getWidth() / 2,
                            MainGame.HEIGHT / 2 - gameOverImage.getHeight() / 2);
                }
            }
        }
    }

    private void checkForEnemyOutOfBounce() {
        for (BaseActor enemy : BaseActor.getList(this.mainStage, Enemy.class)) {
            if (enemy.getY() > MainGame.HEIGHT)
                this.removeEnemy(enemy);
        }
    }

    private void checkForLaserCollision() {
        for (BaseActor laser : BaseActor.getList(this.mainStage, Laser.class)) {
            for (BaseActor enemy : BaseActor.getList(this.mainStage, Enemy.class)) {
                if (laser.overlaps(enemy)) {
                    this.setScore(((Enemy) enemy).getScore());
                    this.removeEnemy(enemy);
                }
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
            this.setScore(100);
            this.fuel.remove();
            if (!this.player.isDead())
                this.createFuel();
            else
                this.fuel = null;
        }
    }

    private void createItem(float delta) {
        if (this.item != null || this.rocket.isBlastOff())
            return;

        this.itemTimer += delta;
        if (this.itemTimer > TIME_TO_ITEM) {
            this.itemTimer = 0;
            if (MathUtils.randomBoolean()) {
                this.item = new Item(0, 0, this.mainStage);
            }
        }
    }

    private void checkForItemCollision() {
        if (this.item == null)
            return;

        if (this.player.overlaps(this.item)) {
            this.itemSound.play();
            this.setScore(250);
            this.item.remove();
            this.item.setPosition(-10000, -10000);
            this.item = null;
        }
    }

    private void createEnemies() {
        for (int i = 0; i < MainGame.getMaxEnemies(); i++) {
            if (MainGame.getLevel() == 1 || MainGame.getLevel() == 9)
                new Meteor(0, 0, this.mainStage);
            else if (MainGame.getLevel() == 2 || MainGame.getLevel() == 10)
                new Slick(0, 0, this.mainStage);
            else if (MainGame.getLevel() == 3 || MainGame.getLevel() == 11)
                new Ball(0, 0, this.mainStage);
            else if (MainGame.getLevel() == 4 || MainGame.getLevel() == 12)
                new Aircraft(0, 0, this.mainStage, this.player);
            else if (MainGame.getLevel() == 5 || MainGame.getLevel() == 13)
                new UFO(0, 0, this.mainStage, this.player);
            else if (MainGame.getLevel() == 6 || MainGame.getLevel() == 14)
                new Cross(0, 0, this.mainStage);
            else if (MainGame.getLevel() == 7 || MainGame.getLevel() == 15)
                new Spaceship(0, 0, this.mainStage);
            else if (MainGame.getLevel() == 8 || MainGame.getLevel() == 16)
                new Alien(0, 0, this.mainStage, this.player);
        }
    }

    private void createNewEnemy(Enemy enemyDestroyed) {
        if (this.rocket.getState() != 6) {
            if (enemyDestroyed instanceof Meteor)
                new Meteor(0, 0, this.mainStage);
            else if (enemyDestroyed instanceof Slick)
                new Slick(0, 0, this.mainStage);
            else if (enemyDestroyed instanceof Ball)
                new Ball(0, 0, this.mainStage);
            else if (enemyDestroyed instanceof Aircraft)
                new Aircraft(0, 0, this.mainStage, this.player);
            else if (enemyDestroyed instanceof UFO)
                new UFO(0, 0, this.mainStage, this.player);
            else if (enemyDestroyed instanceof Cross)
                new Cross(0, 0, this.mainStage);
            else if (enemyDestroyed instanceof Spaceship)
                new Spaceship(0, 0, this.mainStage);
            else if (enemyDestroyed instanceof Alien)
                new Alien(0, 0, this.mainStage, this.player);
        }
    }

    private void removeEnemy(BaseActor enemy) {
        Explosion explosion = new Explosion(0, 0, this.mainStage);
        explosion.centerAtActor(enemy);
        this.explosionSound.play(0.5f);
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

    private void setScore(int score) {
        if (this.player.isVisible()) {
            this.score += score;
            MainGame.setScore(this.score);
            MainGame.setMaxScore(this.score);
            this.scoreLabel.setText(this.getScoreString(this.score));
            this.maxScoreLabel.setText(this.getScoreString(MainGame.getMaxScore()));
        }
    }

    private void gameOverState(float delta) {
        this.timer += delta;
        if (this.timer > this.timeToInit) {
            BaseGame.setActiveScreen(new MenuScreen());
        }
    }

    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Keys.SPACE) {
            this.player.shoot();
            this.laserSound.play(0.6f);
        }
        if (keycode == Keys.ESCAPE)
            this.paused = !this.paused;
        return false;
    }

}
