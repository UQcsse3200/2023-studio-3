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

/**
 * Represents the loading screen of the game.
 */
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

    /**
     * Initializes a new instance of the LoadingScreen class.
     *
     * @param game The game instance to which this screen belongs.
     */
    public LoadingScreen(GdxGame game) {
        this.game = game;
        spriteBatch = new SpriteBatch();
        backgroundTexture = new Texture("images/LoadingScreen.png");

        // loadingTexture = new Texture("images/mobboss/patrick.png");
        stage = new Stage(new ScreenViewport());
        Skin skin = new Skin(Gdx.files.internal("uiskin.json")); // Use your own skin file
        loadingLabel = new Label("Loading", skin);
        loadingLabel.setPosition((float) Gdx.graphics.getWidth() / 2 - 50f, (float) Gdx.graphics.getHeight() / 2);
        stage.addActor(loadingLabel);
    }

    /**
     * Called when this screen becomes the current screen for the game.
     */
    @Override
    public void show() {

    }

    /**
     * Called by the game loop from the application every frame.
     * Renders the screen and handles the transition to the main game.
     *
     * @param delta The time in seconds since the last frame.
     */
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

    /**
     * Called when the screen should resize itself.
     *
     * @param width  The new width.
     * @param height The new height.
     */
    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    /**
     * Called when the application is paused.
     */
    @Override
    public void pause() {
        // Handle pause if needed
    }

    /**
     * Called when the application is resumed from a paused state.
     */
    @Override
    public void resume() {
        // Handle resume if needed
    }

    /**
     * Called when this screen is no longer the current screen for the game.
     */
    @Override
    public void hide() {
        // Hide any elements when the screen is not visible
    }

    /**
     * Called when disposing the screen to free up resources.
     */
    @Override
    public void dispose() {
        spriteBatch.dispose();
        backgroundTexture.dispose();
        // loadingTexture.dispose();
    }
}

