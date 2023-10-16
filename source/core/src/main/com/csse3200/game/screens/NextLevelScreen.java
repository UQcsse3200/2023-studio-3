/**
 * This class represents the screen displayed when the player completes a level
 * and can choose to proceed to the next level or return to the main menu.
 */
package com.csse3200.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.csse3200.game.GdxGame;
import com.badlogic.gdx.Preferences;
import com.csse3200.game.ui.ButtonFactory;

public class NextLevelScreen extends ScreenAdapter {
    private final SpriteBatch batch;
    private final Texture backgroundTexture;
    private final BitmapFont font;
    private final Stage stage;
    private int currentLevel;
    private Preferences preferences;

    /**
     * Initializes the NextLevelScreen with the necessary assets and buttons.
     *
     * @param game The game instance managing the screens.
     * @param currentLevel The current level the player is on.
     */
    public NextLevelScreen(GdxGame game, int currentLevel) {
        this.currentLevel = currentLevel;

        preferences = Gdx.app.getPreferences("MyPreferences");
        int highestLevelReached = preferences.getInteger("HighestLevelReached", -1);
        if (currentLevel > highestLevelReached) {
            preferences.putInteger("HighestLevelReached", currentLevel);
            preferences.flush();
        }

        batch = new SpriteBatch();
        backgroundTexture = new Texture("images/ui/Screen/Nextlevel.png");
        font = new BitmapFont();

        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        Skin skin = new Skin(Gdx.files.internal("images/ui/buttons/glass.json"));
        TextButton nextLevelButton = ButtonFactory.createButton("Next Level");
        nextLevelButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (currentLevel == 1) {
                    game.setScreen(new LevelSelectScreen(game, 0)); // Pass the next level to LevelSelectScreen
                }
                else if (currentLevel == 0) {
                    game.setScreen(new LevelSelectScreen(game, 2)); // Pass the next level to LevelSelectScreen
                } else {
                    // Handle the case where all levels are completed
                    game.setScreen(GdxGame.ScreenType.LEVEL_SELECT);
                }
                // Logic to move to the next level
                // You can implement your logic here, such as updating the level and calling game.setScreen()
                // Example: game.setScreen(new MainGameScreen(game, nextLevel));
            }
        });
        TextButton mainMenuButton = ButtonFactory.createButton("Main Menu");
        mainMenuButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(GdxGame.ScreenType.MAIN_MENU);
            }
        });

        Table table = new Table();
        table.setFillParent(true);
        table.add(nextLevelButton).padTop(400).row();
        table.add(mainMenuButton).padTop(-200).row();
        stage.addActor(table);
    }

    /**
     * Renders the screen visuals.
     *
     * @param delta Time in seconds since the last frame.
     */
    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        batch.draw(backgroundTexture, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        // Customize font size, color, and style
        BitmapFont font = new BitmapFont();
        font.getData().setScale(3.0f); // Set font size to 2x the default size
        Color textColor = new Color(200 / 255f, 246 / 255f, 244 / 255f, 1f); // Convert RGB values to 0-1 range
        font.setColor(textColor);

        // GlyphLayout layout = new GlyphLayout(font, "Congratulations on completing the level!");
        // float textX = (Gdx.graphics.getWidth() - layout.width) / 2; // Center the text horizontally
        // float textY = 750; // Customize the vertical position

        //  font.draw(batch, layout, textX, textY);

        batch.end();

        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
    }

    /**
     * Adjusts the size of elements based on screen width and height.
     *
     * @param width  The new width.
     * @param height The new height.
     */
    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    /**
     * Releases all resources associated with this screen.
     */
    @Override
    public void dispose() {
        batch.dispose();
        backgroundTexture.dispose();
        stage.dispose();
    }
}