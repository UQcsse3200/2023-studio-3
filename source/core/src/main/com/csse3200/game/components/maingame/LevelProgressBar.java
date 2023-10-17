package com.csse3200.game.components.maingame;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.csse3200.game.screens.GameLevelData;
import com.csse3200.game.services.ServiceLocator;

public class LevelProgressBar extends ProgressBar {

    static int selectedLevel = GameLevelData.getSelectedLevel();

    /**
     * @param width of the health bar
     * @param height of the health bar
     */
    public LevelProgressBar(int width, int height) {
        super(0f, getMobCount(), 0.01f, false, new ProgressBarStyle());
        getStyle().background = getColoredDrawable(width, height, Color.RED);
        getStyle().knob = new TextureRegionDrawable(new TextureRegion(new Texture("images/skeleton.png")));
        getStyle().knobBefore = getColoredDrawable(width, height, Color.GREEN);

        setWidth(width);
        setHeight(height);

        setAnimateDuration(0.0f);
        setValue(1f);

        setAnimateDuration(0.25f);
    }

    /**
     * Color filling for the Progress Bar
     * @param width the width in pixels
     * @param height the height in pixels
     * @param color the color of the filling
     * @return Drawable
     */
    public static Drawable getColoredDrawable(int width, int height, Color color) {
        Pixmap pixmap = new Pixmap(width, height, Pixmap.Format.RGBA8888);
        pixmap.setColor(color);
        pixmap.fill();

        TextureRegionDrawable drawable = new TextureRegionDrawable(new TextureRegion(new Texture(pixmap)));

        pixmap.dispose();

        return drawable;
    }

    /**
     * Get the number of mobs based on the level
     * @return number of total mobs
     */
    public static int getMobCount() {

        switch (selectedLevel) {
            // Desert
            case 1 -> { // Ice
                ServiceLocator.getWaveService().setTotalMobs(91);
                return 91;
            }
            case 2 -> { // Lava
                ServiceLocator.getWaveService().setTotalMobs(204);
                return 204;
            }
            default -> {
                ServiceLocator.getWaveService().setTotalMobs(27);
                return 27;
            }
        }
    }
}
