package org.rmc;

import org.rmc.framework.base.BaseGame;
import org.rmc.screen.MenuScreen;

public class MainGame extends BaseGame {

    public static final int WIDTH = 1024;
    public static final int HEIGHT = 768;

    private static int realLevel = 0;
    private static int level = 1;
    private static int maxEnemies = 4;
    private static int score = 0;
    private static int maxScore = 0;
    private static int lives = 5;

    @Override
    public void create() {
        super.create("fonts/zx-spectrum.ttf");
        BaseGame.setActiveScreen(new MenuScreen());
    }

    public static int getLevel() {
        return level;
    }

    public static int getRealLevel() {
        return realLevel + 1;
    }

    public static void incrementLevel() {
        realLevel++;
        if (realLevel % 16 == 0)
            level = 0;
        level++;
        if (level == 2 || level == 5 || level == 9 || level == 13) {
            incrementMaxEnemies();
            if (level != 2)
                lives++;
        }
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

    public static int getScore() {
        return score;
    }

    public static void setScore(int s) {
        score = s;
    }

    public static int getLives() {
        return lives;
    }

    public static void setLives(int l) {
        lives = l;
    }

    public static int getMaxScore() {
        return maxScore;
    }

    public static void setMaxScore(int s) {
        if (s > maxScore)
            maxScore = s;
    }

    public static void reset() {
        realLevel = 0;
        level = 1;
        maxEnemies = 4;
        score = 0;
        lives = 5;
        maxScore = getMaxScore();
    }

}
