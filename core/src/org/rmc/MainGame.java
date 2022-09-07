package org.rmc;

import org.rmc.framework.base.BaseGame;
import org.rmc.screen.LevelScreen;

public class MainGame extends BaseGame {

    public static final int WIDTH = 1024;
    public static final int HEIGHT = 768;

    private static int level = 1;
    private static int maxEnemies = 4;

    @Override
    public void create() {
        super.create();
        BaseGame.setActiveScreen(new LevelScreen());
    }

    public static int getLevel() {
        return level;
    }

    public static void incrementLevel() {
        level++;
    }

    public static boolean isNewPlanet() {
        return level == 1 || level == 5 || level == 9 || level == 13;
    }

    public static String getFullRocketImage() {
        if (level <= 4)
            return "images/rocket_1.png";
        else if (level <= 8)
            return "images/rocket_2.png";
        else if (level <= 12)
            return "images/rocket_3.png";
        else
            return "images/rocket_4.png";
    }

    public static String getRocketBottomImage() {
        if (level == 1)
            return "images/rocket_1_bottom.png";
        else if (level == 5)
            return "images/rocket_2_bottom.png";
        else if (level == 9)
            return "images/rocket_3_bottom.png";
        else
            return "images/rocket_4_bottom.png";
    }

    public static String getRocketMidImage() {
        if (level == 1)
            return "images/rocket_1_mid.png";
        else if (level == 5)
            return "images/rocket_2_mid.png";
        else if (level == 9)
            return "images/rocket_3_mid.png";
        else
            return "images/rocket_4_mid.png";
    }

    public static String getRocketTopImage() {
        if (level == 1)
            return "images/rocket_1_top.png";
        else if (level == 5)
            return "images/rocket_2_top.png";
        else if (level == 9)
            return "images/rocket_3_top.png";
        else
            return "images/rocket_4_top.png";
    }

    public static int getMaxEnemies() {
        return maxEnemies;
    }

    public static void incrementMaxEnemies() {
        maxEnemies++;
    }

    public static void reset() {
        level = 1;
        maxEnemies = 4;
    }

}
