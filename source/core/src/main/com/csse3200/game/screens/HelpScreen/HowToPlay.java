package com.csse3200.game.screens.HelpScreen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.csse3200.game.GdxGame;
/**
 * A screen that provides information on how to play the game.
 */
public class HowToPlay extends ScreenAdapter {
    private final GdxGame game;
    private Stage stage;
    private SpriteBatch spriteBatch;
    /**
     * Creates a new HowToPlay screen.
     *
     * @param game The main game instance.
     */

    public HowToPlay(GdxGame game) {
        this.game = game;
        stage = new Stage(new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));
        spriteBatch = new SpriteBatch();
        Skin skin = new Skin(Gdx.files.internal("images/ui/buttons/glass.json"));

        Label.LabelStyle labelStyle = new Label.LabelStyle();
        BitmapFont customFont = new BitmapFont(Gdx.files.internal("images/ui/buttons/dot_gothic_16.fnt"));
        labelStyle.font = customFont;

        Texture backgroundTexture = new Texture("images/lose-screen/desktop-wallpaper-simple-stars-video-background-loop-black-and-white-aesthetic-space.jpg");
        Drawable backgroundDrawable = new TextureRegionDrawable(new TextureRegion(backgroundTexture));

        // Create a table to hold the background image
        Table backgroundTable = new Table();
        backgroundTable.setFillParent(true);
        backgroundTable.add(new Image(backgroundDrawable)).expand().fill();

        // Add the background table to the stage first
        stage.addActor(backgroundTable);
        // Create a table to organize the image placeholder
        Table table = new Table();
        table.setFillParent(true); // Makes the table the size of the stage

        // Create one image placeholder
        Image image = new Image(new Texture("images/HelpScreen/HTP.png"));

        // Add the image placeholder to the table
        table.add(image).expand().fill();
        table.pad(60f);

        // Add the table to the stage
        stage.addActor(table);


        TextButton BackButton = new TextButton("Back", skin);
        BackButton.addListener(new ClickListener() {
            @Override
            public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
                game.setScreen(GdxGame.ScreenType.MAIN_MENU);

            }
        });

        Texture imageTexture = new Texture("images/ui/Sprites/UI_Glass_Arrow_Large_01a - Copy.png");
        Drawable drawable = new TextureRegionDrawable(new TextureRegion(imageTexture));
        ImageButton HelpButton = new ImageButton(drawable);
        HelpButton.addListener(new ClickListener() {
            @Override
            public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
                game.setScreen(GdxGame.ScreenType.HELP_SCREEN);

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
        buttonTable1.add(HelpButton).padRight(10);
        Table table2 = new Table();
        table2.setFillParent(true);
        table2.center().top(); // Align to the middle-right corner
        table2.pad(20); // Add padding to the middle-right corner
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