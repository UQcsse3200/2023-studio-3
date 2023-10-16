package com.csse3200.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.csse3200.game.GdxGame;
import com.csse3200.game.services.ResourceService;
import com.csse3200.game.services.ServiceLocator;
import com.badlogic.gdx.Preferences;
import com.csse3200.game.ui.ButtonFactory;

/**
 * Represents the winning screen of the game, shown when the player wins.
 */
public class WinningScreen extends ScreenAdapter {
    private final GdxGame game;
    private SpriteBatch batch;
    private Texture backgroundTexture;
    private Sprite backgroundSprite;
    private Preferences preferences;

    private static final String BACKGROUND_TEXTURE = "images/ui/Screen/WinningScreen.png"; // Set the path to your winning background image

    // private static final String[] winSounds = {"win_sound.ogg"}; // Set the path to your winning sound
    private final BitmapFont font;
    private Stage stage;
    private final ResourceService resourceService;

    /**
     * Initializes a new instance of the WinningScreen class.
     *
     * @param game The game instance to which this screen belongs.
     */
    public WinningScreen(GdxGame game) {
        this.game = game;
        font = new BitmapFont();
        resourceService = new ResourceService();
        ServiceLocator.registerResourceService(resourceService);
      //  resourceService.loadSounds(winSounds);
        resourceService.loadAll();
        preferences = Gdx.app.getPreferences("MyPreferences");
    }

    /**
     * Called when this screen becomes the current screen for the game.
     * Sets up the UI elements and resources for the screen.
     */
    @Override
    public void show() {
        batch = new SpriteBatch();
        backgroundTexture = new Texture(BACKGROUND_TEXTURE);
        backgroundSprite = new Sprite(backgroundTexture);
        backgroundSprite.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        Skin skin = new Skin(Gdx.files.internal("images/ui/buttons/glass.json"));
        TextButton exitButton = ButtonFactory.createButton("Exit Game");
        exitButton.addListener(new ClickListener(){
            public void clicked(InputEvent event, float x, float y) {
                game.exit();
            }
        });

        TextButton mainMenuButton = ButtonFactory.createButton("Back to Main Menu");
        mainMenuButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(GdxGame.ScreenType.MAIN_MENU);
            }
        });

        Table table = new Table();
        table.setFillParent(true);
        table.center(); // Center align the buttons vertically at the top of the screen
        table.padTop(-100); // Add a 100-pixel gap at the top
        table.padRight(-120);
        // Add buttons with a 100-pixel gap in between
        table.add(exitButton).padRight(200);
        table.add(mainMenuButton);

        stage.addActor(table);

        preferences.putInteger("HighestLevelReached", 2);
        preferences.flush();
    }

    /**
     * Called by the game loop to render the screen.
     *
     * @param delta The time in seconds since the last frame.
     */
    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        backgroundSprite.draw(batch);
        batch.end();

        stage.draw();
    }

    /**
     * Disposes of all resources and assets when no longer needed.
     */
    @Override
    public void dispose() {
        batch.dispose();
        backgroundTexture.dispose();
        stage.dispose();
    }
}
