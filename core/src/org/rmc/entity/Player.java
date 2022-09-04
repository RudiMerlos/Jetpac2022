package org.rmc.entity;

import org.rmc.framework.base.BaseActor;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;

public class Player extends BaseActor {

    private Animation<TextureRegion> walkLeft;
    private Animation<TextureRegion> walkRight;
    private Animation<TextureRegion> flyLeft;
    private Animation<TextureRegion> flyRight;

    private boolean flying;
    private boolean facingRight;

    private BaseActor belowSensor;

    public Player(float x, float y, Stage stage) {
        super(x, y, stage);

        Texture texture = new Texture(Gdx.files.internal("images/player.png"), true);
        int rows = 4;
        int cols = 4;
        int frameWidth = texture.getWidth() / cols;
        int frameHeight = texture.getHeight() / rows;
        float frameDuration = 0.05f;

        TextureRegion[][] temp = TextureRegion.split(texture, frameWidth, frameHeight);
        Array<TextureRegion> textureArray = new Array<>();

        for (int c = 0; c < cols; c++)
            textureArray.add(temp[0][c]);
        this.walkLeft = new Animation<>(frameDuration, textureArray, Animation.PlayMode.LOOP);

        textureArray.clear();
        for (int c = 0; c < cols; c++)
            textureArray.add(temp[1][c]);
        this.walkRight = new Animation<>(frameDuration, textureArray, Animation.PlayMode.LOOP);

        textureArray.clear();
        for (int c = 0; c < cols; c++)
            textureArray.add(temp[2][c]);
        this.flyLeft = new Animation<>(frameDuration, textureArray, Animation.PlayMode.LOOP);

        textureArray.clear();
        for (int c = 0; c < cols; c++)
            textureArray.add(temp[3][c]);
        this.flyRight = new Animation<>(frameDuration, textureArray, Animation.PlayMode.LOOP);

        this.setAnimation(this.walkRight);
        this.setBoundaryPolygon(8);
        this.setAcceleration(BaseActor.MAX_ACCELERATION);
        this.setMaxSpeed(200);
        this.setDeceleration(BaseActor.MAX_DECELERATION);

        this.flying = false;
        this.facingRight = true;

        this.belowSensor = new BaseActor(0, 0, stage);
        this.belowSensor.setSize(this.getWidth() - 8, 1);
        this.belowSensor.setBoundaryRectangle();
    }

    @Override
    public void act(float delta) {
        super.act(delta);

        this.setAnimationPaused(true);

        // checks if player is flying
        this.flying = true;
        for (BaseActor solid : BaseActor.getList(this.getStage(), Solid.class)) {
            if (this.overlaps(solid, 1.01f)) {
                if (this.belowOverlaps(solid)) {
                    this.flying = false;
                } else {
                    this.setPosition(this.getX(), this.getY() - 10);
                    this.accelerateAtAngle(270);
                }
            }
        }

        if (this.flying) {
            this.setAcceleration(1400);
            this.setMaxSpeed(600);
            this.setDeceleration(1400);
        } else {
            this.setAcceleration(BaseActor.MAX_ACCELERATION);
            this.setMaxSpeed(400);
            this.setDeceleration(BaseActor.MAX_DECELERATION);
        }

        // move player
        if (Gdx.input.isKeyPressed(Keys.O) && this.isVisible()) {
            this.setAnimationPaused(false);
            this.setAnimation(this.flying ? this.flyLeft : this.walkLeft);
            this.accelerateAtAngle(180);
            this.facingRight = false;
        } else if (Gdx.input.isKeyPressed(Keys.P) && this.isVisible()) {
            this.setAnimationPaused(false);
            this.setAnimation(this.flying ? this.flyRight : this.walkRight);
            this.accelerateAtAngle(0);
            this.facingRight = true;
        }

        if (Gdx.input.isKeyPressed(Keys.Q) && this.isVisible()) {
            this.accelerateAtAngle(90);
            if (!this.flying) {
                Explosion explosion = new Explosion(0, 0, this.getStage());
                explosion.centerAtPosition(this.getX() + this.getWidth() / 2,
                        this.getY() + this.getHeight() / 4);
            }
        }

        if (!Gdx.input.isKeyPressed(Keys.Q))
            this.accelerateAtAngle(270);

        // set animation
        if (this.flying) {
            this.setAnimationPaused(false);
            if (this.facingRight)
                this.setAnimation(this.flyRight);
            else
                this.setAnimation(this.flyLeft);
        } else {
            this.setAnimation(this.facingRight ? this.walkRight : this.walkLeft);
        }

        // set below sensor
        this.belowSensor.setPosition(this.getX() + 4, this.getY() - 0.2f);

        this.applyPhysics(delta);
        this.wrapAroundWorld();
    }

    private boolean belowOverlaps(BaseActor actor) {
        return this.belowSensor.overlaps(actor);
    }

    public void shoot() {
        if (!this.isVisible())
            return;
        float x = this.facingRight ? this.getX() + this.getWidth() / 3
                : this.getX() + this.getWidth() - this.getWidth() / 4;
        Laser laser = new Laser(0, 0, this.getStage(), this.facingRight);
        laser.centerAtPosition(x, this.getY() + this.getHeight() / 2);
    }

}

