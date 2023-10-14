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

public class NextLevelScreen extends ScreenAdapter {
    private final SpriteBatch batch;
    private final Texture backgroundTexture;
    private final BitmapFont font;
    private final Stage stage;

    public NextLevelScreen(GdxGame game) {
        batch = new SpriteBatch();
        backgroundTexture = new Texture("images/ui/Screen/Nextlevel.png"); // Replace with the path to your background image
        font = new BitmapFont();

        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        Skin skin = new Skin(Gdx.files.internal("images/ui/buttons/glass.json")); // Replace with the path to your UI skin JSON
        TextButton nextLevelButton = new TextButton("Next Level", skin);
        nextLevelButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // Example: game.setScreen(new MainGameScreen(game, nextLevel));
            }
        });
        TextButton mainMenuButton = new TextButton("Main Menu", skin);
        mainMenuButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(GdxGame.ScreenType.MAIN_MENU);
            }
        });

        Table table = new Table();
        table.setFillParent(true);
        table.add(nextLevelButton).padTop(400).row();
        table.add(mainMenuButton).padTop(-150).row();
        stage.addActor(table);
    }

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


    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void dispose() {
        batch.dispose();
        backgroundTexture.dispose();
        stage.dispose();
    }
}

