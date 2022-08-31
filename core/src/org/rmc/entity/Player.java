package org.rmc.entity;

import org.rmc.MainGame;
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
    }

    @Override
    public void act(float delta) {
        super.act(delta);

        this.setAnimationPaused(true);

        // checks if player is flying
        this.flying = true;
        for (BaseActor solid : BaseActor.getList(this.getStage(), Solid.class))
            if (this.overlaps(solid, 1.01f))
                this.flying = false;

        if (this.flying) {
            this.setAcceleration(1400);
            this.setMaxSpeed(500);
            this.setDeceleration(1400);
        } else {
            this.setAcceleration(BaseActor.MAX_ACCELERATION);
            this.setMaxSpeed(300);
            this.setDeceleration(BaseActor.MAX_DECELERATION);
        }

        // move player
        if (Gdx.input.isKeyPressed(Keys.O)) {
            this.setAnimationPaused(false);
            this.setAnimation(this.flying ? this.flyLeft : this.walkLeft);
            this.accelerateAtAngle(180);
            this.facingRight = false;
        } else if (Gdx.input.isKeyPressed(Keys.P)) {
            this.setAnimationPaused(false);
            this.setAnimation(this.flying ? this.flyRight : this.walkRight);
            this.accelerateAtAngle(0);
            this.facingRight = true;
        }

        if (Gdx.input.isKeyPressed(Keys.Q)) {
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

        // check vertical limit
        if (this.getY() + this.getHeight() > MainGame.HEIGHT)
            this.setY(MainGame.HEIGHT - this.getHeight());

        this.applyPhysics(delta);
        this.wrapAroundWorld();
    }

}

