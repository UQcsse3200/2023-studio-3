package com.csse3200.game.screens;

import com.badlogic.gdx.Gdx;

public class Planets {

    // Stores the posx and posy in form of constants for each planet and some form of ID.
    public final static int[] DESERT = {
            (int) (Gdx.graphics.getWidth() / 6.0f),  // pos x
            (int) (Gdx.graphics.getHeight() / 2.3f), // pos y
            150,                                     // width
            150,                                     // height
            0                                        // ID
    };

    public final static int[] ICE = {
            (int) (Gdx.graphics.getWidth() / 3.0f),
            (int) (Gdx.graphics.getHeight() / 1.4f),
            150,
            150,
            1
    };

    public final static int[] LAVA = {
            (int) (Gdx.graphics.getWidth() / 2.2f),
            (int) (Gdx.graphics.getHeight() / 7f),
            200,
            200,
            2
    };

    public final static int[][] PLANETS = {DESERT, ICE, LAVA};


}
