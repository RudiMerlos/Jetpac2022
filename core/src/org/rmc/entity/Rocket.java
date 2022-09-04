package org.rmc.entity;

import java.util.ArrayList;
import java.util.List;
import org.rmc.MainGame;
import org.rmc.framework.base.BaseActor;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;

public class Rocket extends BaseActor {

    private int state;
    private List<Animation<TextureRegion>> animations;

    private boolean newPlanet;

    public Rocket(float x, float y, Stage stage, boolean newPlanet) {
        super(x, y, stage);
        Texture texture = new Texture(Gdx.files.internal(MainGame.getFullRocketImage()), true);
        int rows = 1;
        int cols = 7;
        int frameWidth = texture.getWidth() / cols;
        int frameHeight = texture.getHeight() / rows;

        TextureRegion[][] temp = TextureRegion.split(texture, frameWidth, frameHeight);
        Array<TextureRegion> textureArray = new Array<>();
        this.animations = new ArrayList<>();

        for (int c = 0; c < cols; c++) {
            textureArray.add(temp[0][c]);
            this.animations.add(new Animation<>(0, textureArray));
            textureArray.clear();
        }

        this.state = 0;
        this.setAnimation(this.animations.get(this.state));

        this.setAcceleration(100);
        this.setMaxSpeed(60);
        this.setDeceleration(BaseActor.MAX_DECELERATION);

        this.setVisible(!newPlanet);
        this.newPlanet = newPlanet;
    }

    public int getState() {
        return this.state;
    }

    public void setState(int state) {
        this.state = state;
    }

    @Override
    public void act(float delta) {
        super.act(delta);

        this.setAnimation(this.animations.get(this.state));

        if (!this.newPlanet && this.state == 0)
            this.accelerateAtAngle(270);

        if (this.state >= 6)
            this.accelerateAtAngle(90);

        this.applyPhysics(delta);
    }

}
