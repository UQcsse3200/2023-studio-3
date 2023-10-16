package com.csse3200.game.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.TextureData;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.csse3200.game.GdxGame;

public class LoadingScreen implements Screen {
    private final GdxGame game;
    private SpriteBatch spriteBatch;
    private Texture backgroundTexture; // Background image
    private Texture loadingTexture; // Loading animation
    private float elapsedTime = 0;
    private boolean transitionToMainGame = false;
    private Stage stage;
    private Label loadingLabel;
    private float dotTimer;
    public LoadingScreen(GdxGame game) {
        this.game = game;
        spriteBatch = new SpriteBatch();
        backgroundTexture = new Texture("planets/background.png");

        // loadingTexture = new Texture("images/mobboss/patrick.png");
        stage = new Stage(new ScreenViewport());
        Skin skin = new Skin(Gdx.files.internal("uiskin.json")); // Use your own skin file
        loadingLabel = new Label("Loading", skin);
        loadingLabel.setPosition(Gdx.graphics.getWidth() / 2 - 50, Gdx.graphics.getHeight() / 2);
        stage.addActor(loadingLabel);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        elapsedTime += delta;

        // Clear the screen
        spriteBatch.begin();

        // Draw the background image
        spriteBatch.draw(backgroundTexture, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        // Draw the loading animation on top of the background
        // spriteBatch.draw(loadingTexture, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        spriteBatch.end();

        if (AssetLoader.areAllAssetsLoaded() || elapsedTime >= 1.5) {
            transitionToMainGame = true;
        }

        if (transitionToMainGame) {
            // Transition to the main game screen
            game.setScreen(GdxGame.ScreenType.MAIN_GAME);
        }
        dotTimer += delta;
        int numDots = (int) (dotTimer % 1 * 3); // Add a dot every second

        // Add dots to the label text
        StringBuilder labelText = new StringBuilder("Loading");
        for (int i = 0; i < numDots; i++) {
            labelText.append('.');
        }
        loadingLabel.setText(labelText);
        stage.act();
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
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
        // loadingTexture.dispose();
    }
}

