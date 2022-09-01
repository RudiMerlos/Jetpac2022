package org.rmc.entity;

import org.rmc.framework.base.BaseActor;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.Array;

public class Laser extends BaseActor {

    private Animation<TextureRegion> leftAnimation;
    private Animation<TextureRegion> rightAnimation;

    private boolean facingRight;

    public Laser(float x, float y, Stage stage, boolean facingRight) {
        super(x, y, stage);

        Texture texture = new Texture(Gdx.files.internal("images/laser.png"), true);
        int rows = 2;
        int cols = 8;
        int frameWidth = texture.getWidth() / cols;
        int frameHeight = texture.getHeight() / rows;
        float frameDuration = 0.02f;

        TextureRegion[][] temp = TextureRegion.split(texture, frameWidth, frameHeight);
        Array<TextureRegion> textureArray = new Array<>();

        for (int c = 0; c < cols; c++)
            textureArray.add(temp[0][c]);
        this.rightAnimation =
                new Animation<>(frameDuration, textureArray, Animation.PlayMode.NORMAL);

        textureArray.clear();
        for (int c = 0; c < cols; c++)
            textureArray.add(temp[1][c]);
        this.leftAnimation =
                new Animation<>(frameDuration, textureArray, Animation.PlayMode.NORMAL);

        this.setAnimation(facingRight ? this.rightAnimation : this.leftAnimation);
        this.facingRight = facingRight;

        this.setAcceleration(BaseActor.MAX_ACCELERATION);
        this.setMaxSpeed(1000);
        this.setDeceleration(BaseActor.MAX_DECELERATION);

        this.addAction(Actions.delay(0.7f));
        this.addAction(Actions.after(Actions.removeActor()));
    }

    @Override
    public void act(float delta) {
        super.act(delta);

        if (this.facingRight) {
            this.accelerateAtAngle(0);
        } else {
            this.accelerateAtAngle(180);
        }

        this.applyPhysics(delta);
        this.wrapAroundWorld();
    }

}
