package com.csse3200.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.csse3200.game.GdxGame;
import com.csse3200.game.services.ResourceService;
import com.csse3200.game.services.ServiceLocator;

/**
 * Screen that displays a story with images and no text.
 */
public class StoryScreen extends ScreenAdapter {
    private final GdxGame game;
    private SpriteBatch batch;
    private Texture[] images;
    private int currentIndex;
    private float imageDuration;
    private float elapsedTime;
    private Stage stage;
    private TextButton continueButton;
    private TextButton skipButton; // Universal Skip button
    private String[] bgm = {
            "sounds/background/pre_game/Sci-Fi8Loop_story.ogg"
    };
    private Music music;

    // Image file paths
    private static final String[] IMAGE_PATHS = {
            "images/ui/game screen/earth before 1.0.png",
            "images/ui/game screen/earth before 1.1.png",
            "images/ui/game screen/earth dying 2.0.png",
            "images/ui/game screen/earth dying 2.1.png",
            "images/ui/game screen/meeting 3.0.png",
            "images/ui/game screen/meeting 3.1.png",
            "images/ui/game screen/spaceship 4.0.png",
            "images/ui/game screen/spaceship 4.1.png",
            "images/ui/game screen/arrival 5.0.png",
            "images/ui/game screen/arrival 5.1.png",
            "images/ui/game screen/survey 6.0.png",
            "images/ui/game screen/arrival 6.1.png",
            // Add more image paths as needed
    };

    public StoryScreen(GdxGame game) {
        this.game = game;
        this.images = new Texture[IMAGE_PATHS.length];
        this.currentIndex = 0;
        this.imageDuration = 100000.0f; // Time (in seconds) per image

        this.elapsedTime = 0f;

        for (int i = 0; i < IMAGE_PATHS.length; i++) {
            images[i] = new Texture(IMAGE_PATHS[i]);
        }
        ServiceLocator.registerResourceService(new ResourceService());
        ServiceLocator.getResourceService().loadMusic(bgm);
        ServiceLocator.getResourceService().loadAll();
        music = ServiceLocator.getResourceService().getAsset(bgm[0], Music.class);
    }

    @Override
    public void show() {
        // Initialize assets
        batch = new SpriteBatch();
        stage = new Stage(new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));
        Gdx.input.setInputProcessor(stage);

        Skin skin = new Skin(Gdx.files.internal("images/ui/buttons/glass.json"));

        continueButton = new TextButton("Continue", skin);
        continueButton.addListener(new ClickListener() {
            @Override
            public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
                next();
            }
        });

        skipButton = new TextButton("Skip", skin); // Universal Skip button
        skipButton.addListener(new ClickListener() {
            @Override
            public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
                currentIndex = images.length; // Skip to the end
                next();
            }
        });

        Table buttonTable = new Table();
        buttonTable.add(continueButton).padRight(10); // Add Continue button
        buttonTable.add(skipButton); // Add Universal Skip button

        Table table = new Table();
        table.setFillParent(true);
        table.top().right(); // Align to the top-right corner
        table.pad(20); // Add padding to the top-right corner
        table.add(buttonTable).row(); // Add button table and move to the next row
        stage.addActor(table);

        music.setVolume(0.4f);
        music.setLooping(true);
        music.play();
    }

    @Override
    public void render(float delta) {
        // Clear the screen outside the loop
        Gdx.gl.glClearColor(0, 0, 0, 1F);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        elapsedTime += delta;

        batch.begin();

        if (currentIndex < images.length) {
            // Display the current image
            batch.draw(images[currentIndex], 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        }

        batch.end();

        // Draw UI
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();

        if (currentIndex < images.length) {
            if (elapsedTime >= imageDuration) {
                next();
            }
        }
    }

    private void next() {
        currentIndex++;
        if (currentIndex >= images.length) {
            game.setScreen(GdxGame.ScreenType.LEVEL_SELECT);
        }
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void dispose() {
        batch.dispose();
        for (Texture texture : images) {
            texture.dispose();
        }
        stage.dispose();
        music.dispose();
    }
}
