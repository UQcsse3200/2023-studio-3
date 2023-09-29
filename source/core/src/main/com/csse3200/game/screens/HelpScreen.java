package com.csse3200.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.csse3200.game.GdxGame;

public class HelpScreen extends ScreenAdapter {
    private final GdxGame game;
    private Stage stage;
    private SpriteBatch spriteBatch;

    public HelpScreen(GdxGame game) {
        this.game = game;
        stage = new Stage(new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));
        spriteBatch = new SpriteBatch();

        // Create a table to organize the four image placeholders
        Table table = new Table();
        table.setFillParent(true); // Makes the table the size of the stage

        // Create four image placeholders
        Image image1 = new Image(new Texture("images/lava_bg.png"));
        Image image2 = new Image(new Texture("images/lava_bg.png"));
        Image image3 = new Image(new Texture("images/lava_bg.png"));
        Image image4 = new Image(new Texture("images/lava_bg.png"));

        // Add the image placeholders to the table
        table.add(image1).expand().fill();
        table.row(); // Move to the next row
        table.add(image2).expand().fill();
        table.row();
        table.add(image3).expand().fill();
        table.row();
        table.add(image4).expand().fill();

        // Add the table to the stage
        stage.addActor(table);
    }

    @Override
    public void show() {
        // Set this screen as the input processor
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        // Clear the screen
        spriteBatch.begin();
        spriteBatch.end();

        // Draw the stage
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void hide() {
        // Remove this screen as the input processor
        Gdx.input.setInputProcessor(null);
    }

    @Override
    public void dispose() {
        stage.dispose();
        spriteBatch.dispose();
    }
}
