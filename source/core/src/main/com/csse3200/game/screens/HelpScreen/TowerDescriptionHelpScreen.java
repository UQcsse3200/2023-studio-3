package com.csse3200.game.screens.HelpScreen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.csse3200.game.GdxGame;

public class TowerDescriptionHelpScreen extends ScreenAdapter {
    private final GdxGame game;
    private Stage stage;
    private SpriteBatch spriteBatch;


    public TowerDescriptionHelpScreen(GdxGame game) {
        this.game = game;
        stage = new Stage(new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));
        spriteBatch = new SpriteBatch();

        // Create a table to organize the image placeholder
        Table table = new Table();
        table.setFillParent(true); // Makes the table the size of the stage

        // Create one image placeholder
        Image image = new Image(new Texture("images/background/HelpScreenBG.png"));

        // Add the image placeholder to the table
        table.add(image).expand().fill();

        // Add the table to the stage
        stage.addActor(table);

        Skin skin = new Skin(Gdx.files.internal("images/ui/buttons/glass.json"));
        TextButton BackButton = new TextButton("Back", skin);
        BackButton.addListener(new ClickListener() {
            public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
                game.setScreen(GdxGame.ScreenType.MAIN_MENU);

            }
        });

        TextButton MobsButton = new TextButton("Mobs", skin);
        MobsButton.addListener(new ClickListener() {
            public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
                game.setScreen(GdxGame.ScreenType.HELP_MOBS_SCREEN);

            }
        });

        Table buttonTable = new Table();
        buttonTable.add(BackButton).padRight(10);
        Table table1 = new Table();
        table1.setFillParent(true);
        table1.top().right(); // Align to the top-right corner
        table1.pad(20); // Add padding to the top-right corner
        table1.add(buttonTable).row(); // Add button table and move to the next row
        stage.addActor(table1);

        Table buttonTable1 = new Table();
        buttonTable1.add(MobsButton).padRight(10);
        Table table2 = new Table();
        table2.setFillParent(true);
        table2.center().left(); // Align to the top-right corner
        table2.pad(20); // Add padding to the top-right corner
        table2.add(buttonTable1).row(); // Add button table and move to the next row
        stage.addActor(table2);
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
    public void dispose() {
        stage.dispose();
        spriteBatch.dispose();
    }
}
