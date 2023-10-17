package com.csse3200.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.csse3200.game.GdxGame;

/**
 * Screen that displays a story with images and text.
 */
public class StoryScreen extends ScreenAdapter {
    private final GdxGame game;
    private SpriteBatch batch;
    private Texture[] images;
    private String[] texts;
    private int currentIndex;
    private float imageDuration;
    private float elapsedTime;
    private BitmapFont font;
    private BitmapFont boldFont;
    private Stage stage;
    private TextButton continueButton;
    private TextButton skipButton; // Universal Skip button
    private ShapeRenderer shapeRenderer;
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
            "images/ui/game screen/5.1 arrival.png",
            "images/ui/game screen/6.0 survey.png",
            "images/ui/game screen/6.1 survey.png",
            // Add more image paths as needed
    };
    // Texts to display
    private static final String[] TEXTS = {
            "Over a century ago, a tranquil world basked in an era of serenity. ",
            "Nature's embrace cradled humanity, and life flourished abundantly. ",
            "However, this harmony soon succumbed to the relentless grip of human greed. ",
            "As desires grew insatiable, the delicate balance fractured, and the world's vitality waned",
            "as everything was about to be lost a group of people cam together to save humanity and an idea was born",
            "to set out towards the stars and conquer planets",
            "humanity pooled its resources together and made giant ships called ARKs that would carry us onto the stars",
            "we set out with our iron will and firm resolve to not fade away ",
            "we arrived at planets and built outposts that would help us survive this harsh environment ",
            "we terraformed and procured resources that would help the future generations survive ",
            "the brightest and best began researching anf evaluating the newly found planets",
            "all seems perfect until we picked up on a looming threat that maybe we aren't alone......",
            // Add more text as needed
    };
    /**
     * Creates a new StoryScreen.
     *
     * @param game The game instance
     */
    public StoryScreen(GdxGame game) {
        this.game = game;
        this.images = new Texture[IMAGE_PATHS.length];
        this.texts = TEXTS;
        this.currentIndex = 0;
        this.imageDuration = 2.0f; // Time (in seconds) per image
        this.elapsedTime = 0f;

        for (int i = 0; i < IMAGE_PATHS.length; i++) {
            images[i] = new Texture(IMAGE_PATHS[i]);
        }
    }

    @Override
    public void show() {
        // Initialize assets
        batch = new SpriteBatch();
        font = new BitmapFont();
        boldFont = new BitmapFont();
        boldFont.getData().setScale(1.5f); // Set the font scale for bold text
        boldFont.setColor(Color.WHITE); // Set the font color to white for bold text
        stage = new Stage(new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));
        Gdx.input.setInputProcessor(stage);
        shapeRenderer = new ShapeRenderer();

        // Create UI
        Skin skin = new Skin(Gdx.files.internal("flat-earth/skin/flat-earth-ui.json"));
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
        // Add buttons to table
        Table buttonTable = new Table();
        buttonTable.add(continueButton).padRight(10); // Add Continue button
        buttonTable.add(skipButton); // Add Universal Skip button

        Table table = new Table();
        table.setFillParent(true);
        table.top().right(); // Align to the top-right corner
        table.pad(20); // Add padding to the top-right corner
        table.add(buttonTable).row(); // Add button table and move to the next row
        stage.addActor(table);
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

            // Display text if enough time has passed
            if (elapsedTime >= imageDuration) {
                float textX = 100;
                float textY = 100;
                float padding = 10; // Padding around the text

                // Calculate the text bounds for box size
                GlyphLayout glyphLayout = new GlyphLayout(boldFont, texts[currentIndex]);
                float boxWidth = glyphLayout.width + 2 * padding;
                float boxHeight = glyphLayout.height + 2 * padding;

                // Draw a black background box
                shapeRenderer.begin(ShapeType.Filled);
                shapeRenderer.setColor(Color.BLACK);
                shapeRenderer.rect(textX - padding, textY - padding, boxWidth, boxHeight);
                shapeRenderer.end();

                // Draw the text in white
                batch.setShader(null); // Reset the shader
                boldFont.setColor(Color.WHITE);
                boldFont.draw(batch, texts[currentIndex], textX, textY + glyphLayout.height); // Adjust text position
            }
        }

        batch.end();

        // Draw UI
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
    }
    /**
     * Advances to next image/text.
     */
    private void next() {
        currentIndex++;
        if (currentIndex < images.length) {
            elapsedTime = 0;
        } else {
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
        font.dispose();
        boldFont.dispose();
        stage.dispose();
        shapeRenderer.dispose();
    }
}
