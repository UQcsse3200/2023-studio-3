package com.csse3200.game.screens;

import com.badlogic.gdx.Gdx;

/**
 * A utility class that contains the properties and configuration data for different planets.
 * Each planet has its x and y position, width, height, and an identifier. The constants in this
 * class help with positioning and rendering the planets on the screen.
 *
 */
public class Planets {

    /**
     * DESERT Planet:
     * - Located at approximately 1/6th of the screen width and 1/2.3th of the screen height.
     * - Has a fixed width and height of 150x150.
     * - Assigned an ID of 0.
     */
    public final static int[] DESERT = {
            (int) (Gdx.graphics.getWidth() / 6.0f),  // pos x
            (int) (Gdx.graphics.getHeight() / 2.3f), // pos y
            150,                                     // width
            150,                                     // height
            0                                        // ID
    };

    /**
     * ICE Planet:
     * - Located at approximately 1/3rd of the screen width and 1/1.4th of the screen height.
     * - Has a fixed width and height of 150x150.
     * - Assigned an ID of 1.
     */
    public final static int[] ICE = {
            (int) (Gdx.graphics.getWidth() / 3.0f),
            (int) (Gdx.graphics.getHeight() / 1.4f),
            150,
            150,
            1
    };

    /**
     * LAVA Planet:
     * - Located at approximately 1/2.2th of the screen width and 1/7th of the screen height.
     * - Has a fixed width and height of 200x200.
     * - Assigned an ID of 2.
     */
    public final static int[] LAVA = {
            (int) (Gdx.graphics.getWidth() / 2.2f),
            (int) (Gdx.graphics.getHeight() / 7f),
            200,
            200,
            2
    };

    /**
     * An array of all the planet configurations.
     */
    public final static int[][] PLANETS = {DESERT, ICE, LAVA};


}
