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

    private Thrusters thrusters;

    private boolean newPlanet;
    private boolean blastOff;
    private boolean fullDisplayed;

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

        this.thrusters = new Thrusters(0, 0, stage);
        this.addActor(this.thrusters);
        this.thrusters.setPosition(0, -this.thrusters.getHeight() - 2);

        this.setAcceleration(100);
        this.setMaxSpeed(60);
        this.setDeceleration(BaseActor.MAX_DECELERATION);

        this.setVisible(!newPlanet);
        this.newPlanet = newPlanet;
        this.blastOff = false;
        this.fullDisplayed = false;
    }

    public int getState() {
        return this.state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public void incrementState() {
        if (this.state < 6)
            this.state++;
    }

    public void setBlastOff(boolean blastOff) {
        this.blastOff = blastOff;
    }

    public boolean isFullDisplayed() {
        return this.fullDisplayed;
    }

    @Override
    public void act(float delta) {
        super.act(delta);

        this.setAnimation(this.animations.get(this.state));

        if (this.getY() + this.getHeight() + 8 < MainGame.HEIGHT)
            this.fullDisplayed = true;

        if (!this.newPlanet && this.state == 0)
            this.accelerateAtAngle(270);

        if (this.blastOff)
            this.accelerateAtAngle(90);

        if (this.getY() > 48)
            this.thrusters.setVisible(true);
        else
            this.thrusters.setVisible(false);

        this.applyPhysics(delta);
    }

}
