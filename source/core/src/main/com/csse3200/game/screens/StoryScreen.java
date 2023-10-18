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
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import static com.csse3200.game.ui.UIComponent.getSkin;

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
    private Preferences preferences;
    private BitmapFont font;
    private static final String defaultFont = "determination_mono_22";
    private GlyphLayout layout;

    // Image file paths
    private static final String[] IMAGE_PATHS = {
            "images/ui/game screen/1 earth before.png",
            "images/ui/game screen/1.1 earth before.png",
            "images/ui/game screen/2.0 earth dying.png",
            "images/ui/game screen/2.1 earth dying.png",
            "images/ui/game screen/3. meeting.png",
            "images/ui/game screen/3.1 meeting turret.png",
            "images/ui/game screen/4.0 spaceship built.png",
            "images/ui/game screen/4.1 spaceship leaving.png",
            "images/ui/game screen/5.1 arrival.png",
            "images/ui/game screen/5 arrival.png",
            "images/ui/game screen/6.0 survey.png",
            "images/ui/game screen/6.1 survey.png",
    };

    private static final String[] IMAGE_TEXTS = {
            "Over a century ago, a tranquil world basked in an era of serenity. ",
            "Nature's embrace cradled humanity, and life flourished abundantly. ",
            "However, this harmony soon succumbed to the relentless grip of human greed. ",
            "As desires grew insatiable, the delicate balance fractured, and the world's vitality waned",
            "as everything was about to be lost a group of people came together to save humanity and an idea was born",
            "to set out towards the stars and conquer planets",
            "humanity pooled its resources together and made giant ships called ARKs that would carry us onto the stars",
            "we set out with our iron will and firm resolve to not fade away ",
            "we arrived at planets and built outposts that would help us survive this harsh environment ",
            "we terraformed and procured resources that would help the future generations survive ",
            "the brightest and best began researching and evaluating the newly found planets",
            "all seems perfect until we picked up on a looming threat that maybe we aren't alone......",
    };

    /**
     * Constructs a new StoryScreen.
     *
     * @param game The main game instance that this screen is associated with.
     *             It provides access to game-related functionality.
     */
    public StoryScreen(GdxGame game) {
        this.game = game;
        this.images = new Texture[IMAGE_PATHS.length];
        this.currentIndex = 0;
        this.imageDuration = 100000.0f; // Time (in seconds) per image

        preferences = Gdx.app.getPreferences("MyPreferences");
        preferences.putInteger("HighestLevelReached", -1);
        preferences.flush();

        this.elapsedTime = 0f;

        for (int i = 0; i < IMAGE_PATHS.length; i++) {
            images[i] = new Texture(IMAGE_PATHS[i]);
        }
        ServiceLocator.registerResourceService(new ResourceService());
        ServiceLocator.getResourceService().loadMusic(bgm);
        ServiceLocator.getResourceService().loadAll();
        music = ServiceLocator.getResourceService().getAsset(bgm[0], Music.class);
    }

    /**
     * Initializes assets and sets up the user interface for the StoryScreen.
     * This method is called when the screen is displayed.
     */
    @Override
    public void show() {
        // Initialize assets
        batch = new SpriteBatch();
        stage = new Stage(new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));
        Gdx.input.setInputProcessor(stage);

        font = getSkin().getFont(defaultFont);
        layout = new GlyphLayout();

        Skin skin = new Skin(Gdx.
                files.internal("images/ui/buttons/glass.json"));

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

    /**
     * Renders the StoryScreen by clearing the screen, displaying images and text,
     * and handling user interface interactions.
     *
     * @param delta The time in seconds since the last frame.
     */
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

        if (currentIndex < images.length && currentIndex < IMAGE_TEXTS.length) {
            String text = IMAGE_TEXTS[currentIndex];
            layout.setText(font, text);
            font.draw(batch, text, (Gdx.graphics.getWidth() - layout.width) / 2, Gdx.graphics.getHeight() * 0.1f);
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

    /**
     * Increments the current index to display the next image. If the index exceeds
     * the total number of images, the game will navigate to the LevelSelectScreen.
     */
    private void next() {
        currentIndex++;
        if (currentIndex >= images.length) {
            music.stop();
            game.setScreen(new LevelSelectScreen(game, -1));
        }
    }

    /**
     * Resizes and updates the viewport dimensions based on the given width and height.
     *
     * @param width  The new width of the screen.
     * @param height The new height of the screen.
     */
    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    /**
     * Disposes all the resources associated with this screen to free up memory.
     */
    @Override
    public void dispose() {
        batch.dispose();
        for (Texture texture : images) {
            texture.dispose();
        }
        stage.dispose();
        music.dispose();
        font.dispose();
    }
}
