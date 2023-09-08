package com.csse3200.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.TimeUtils;
import com.csse3200.game.GdxGame;
import com.csse3200.game.screens.text.AnimatedText;

public class LevelSelectScreen extends ScreenAdapter {
    private final GdxGame game;
    private SpriteBatch batch;

    private Sprite background;
    private Texture backgroundTexture;


    float timeCounter = 0;

    private static final String BG_PATH = "planets/background.png";

    public LevelSelectScreen(GdxGame game) {
        this.game = game;
    }

    @Override
    public void show() {
        batch = new SpriteBatch();
        backgroundTexture = new Texture(BG_PATH);
        background = new Sprite(backgroundTexture);
    }

    private void spawnPlanets() {
        // Spawn desert planet
        spawnPlanet(150, 150, (int) (Gdx.graphics.getWidth() / 6.0f), (int) (Gdx.graphics.getHeight() / 2.3f),"Desert", 1, (int) (timeCounter * 60) % 60 + 1);
        // Spawn ice planet
        spawnPlanet(150, 150, (int) (Gdx.graphics.getWidth() / 3.0f), (int) (Gdx.graphics.getHeight() / 1.4f),"Barren_or_Moon", 2, (int) (timeCounter * 60) % 60 + 1);
        // Spawn lava planet
        spawnPlanet(150, 150, (int) (Gdx.graphics.getWidth() / 2.2f), (int) (Gdx.graphics.getHeight() / 7f),"Lava", 1, (int) (timeCounter * 60) % 60 + 1);

    }

    private void spawnPlanet(int width, int height, int posx, int posy, String planetName, int version, int planetNumber) {
        Texture planet = new Texture(String.format("planets/%s/%d/%d.png", planetName, version, planetNumber));
        Sprite planetSprite = new Sprite(planet);
        planetSprite.setSize(width, height);
        batch.draw(planetSprite, posx, posy, width, height);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        timeCounter += delta;



        batch.begin();
        batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        spawnPlanets();
        batch.end();
    }

    @Override
    public void dispose() {
        batch.dispose();
    }
}
