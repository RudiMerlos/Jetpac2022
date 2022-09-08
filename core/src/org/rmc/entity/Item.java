package org.rmc.entity;

import org.rmc.MainGame;
import org.rmc.framework.base.BaseActor;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class Item extends BaseActor {

    private static final String[] IMAGES = {"images/item_bean.png", "images/item_gold.png",
            "images/item_element1.png", "images/item_element2.png", "images/item_diamond.png"};

    private boolean displayed;

    public Item(float x, float y, Stage stage) {
        super(x, y, stage);

        int indexImage = MathUtils.random(4);
        if (indexImage < 2)
            this.loadTexture(IMAGES[indexImage]);
        else if (indexImage < 4)
            this.loadAnimationFromSheet(IMAGES[indexImage], 1, 3, 0.1f, true);
        else
            this.loadAnimationFromSheet(IMAGES[indexImage], 1, 7, 0.05f, true);

        this.setBoundaryPolygon(8);

        int posX = MathUtils.random(32, MainGame.WIDTH - (int) this.getWidth() - 32);
        while (posX > 576 && posX < 768)
            posX = MathUtils.random(MainGame.WIDTH - (int) this.getWidth());

        this.setPosition((float) posX, (float) MainGame.HEIGHT - this.getHeight());

        this.setAcceleration(BaseActor.MAX_ACCELERATION);
        this.setMaxSpeed(120);
        this.setDeceleration(BaseActor.MAX_DECELERATION);

        this.displayed = false;
    }

    public boolean isDisplayed() {
        return this.displayed;
    }

    @Override
    public void act(float delta) {
        super.act(delta);

        this.accelerateAtAngle(270);

        if (this.getY() + this.getHeight() < MainGame.HEIGHT - 8)
            this.displayed = true;

        this.applyPhysics(delta);
        this.wrapAroundWorld();
    }

}
