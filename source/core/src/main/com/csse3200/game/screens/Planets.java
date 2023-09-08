package com.csse3200.game.screens;

import com.badlogic.gdx.Gdx;

public class Planets {

    // Stores the posx and posy in form of constants for each planet
    public final static int[] DESERT = {
            (int) (Gdx.graphics.getWidth() / 6.0f),
            (int) (Gdx.graphics.getHeight() / 2.3f)
    };

    public final static int[] ICE = {
            (int) (Gdx.graphics.getWidth() / 3.0f),
            (int) (Gdx.graphics.getHeight() / 1.4f)
    };

    public final static int[] LAVA = {
            (int) (Gdx.graphics.getWidth() / 2.2f),
            (int) (Gdx.graphics.getHeight() / 7f)
    };


}
