package com.csse3200.game.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.async.AsyncExecutor;
import com.badlogic.gdx.utils.async.AsyncTask;
import com.csse3200.game.GdxGame;
import com.csse3200.game.services.ServiceLocator;

public class LoadingScreen implements Screen {
    private final GdxGame game;
    private SpriteBatch spriteBatch;
    private Texture backgroundTexture; // Background image
    private Texture loadingTexture; // Loading animation
    private AsyncExecutor asyncExecutor = new AsyncExecutor(1);

    public LoadingScreen(GdxGame game) {
        this.game = game;
        spriteBatch = new SpriteBatch();
        backgroundTexture = new Texture("planets/background.png");
        loadingTexture = new Texture("images/ui/Sprites/UI_Glass_Scrollbar_01a.png");
    }

    @Override
    public void show() {
        AssetLoader.loadAllAssets();
    }

    @Override
    public void render(float delta) {
        // Clear the screen
        spriteBatch.begin();

        // Draw the background image
        spriteBatch.draw(backgroundTexture, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        // Draw the loading animation on top of the background
        spriteBatch.draw(loadingTexture, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        spriteBatch.end();

        if (AssetLoader.areAllAssetsLoaded()) {
            // Asset loading is complete, transition to the main game screen
            game.setScreen(GdxGame.ScreenType.MAIN_GAME);
        }
    }

    @Override
    public void resize(int width, int height) {
        // Resize any necessary assets or elements here
    }

    @Override
    public void pause() {
        // Handle pause if needed
    }

    @Override
    public void resume() {
        // Handle resume if needed
    }

    @Override
    public void hide() {
        // Hide any elements when the screen is not visible
    }

    @Override
    public void dispose() {
        spriteBatch.dispose();
        backgroundTexture.dispose();
        loadingTexture.dispose();
    }
}
