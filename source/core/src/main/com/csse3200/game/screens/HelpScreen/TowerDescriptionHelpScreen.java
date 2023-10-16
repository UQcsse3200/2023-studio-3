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
 * The `TowerDescriptionHelpScreen` class represents a screen that displays information about various tower types in the game.
 * It includes tower images and descriptions.
 */
public class TowerDescriptionHelpScreen extends ScreenAdapter {
    private final GdxGame game;
    private Stage stage;
    private SpriteBatch spriteBatch;
    /**
     * Constructs a new `TowerDescriptionHelpScreen`.
     *
     * @param game The GdxGame instance.
     */

    public TowerDescriptionHelpScreen(GdxGame game) {
        this.game = game;
        stage = new Stage(new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));
        spriteBatch = new SpriteBatch();

        // Create a table to organize the image placeholder
        Table table = new Table();
        table.setFillParent(true); // Makes the table the size of the stage

        // Create one image placeholder
        Image image1 = new Image(new Texture("images/lose-screen/desktop-wallpaper-simple-stars-video-background-loop-black-and-white-aesthetic-space.jpg"));

        // Add the image placeholder to the table
        table.add(image1).expand().fill();

        // Add the table to the stage
        stage.addActor(table);

        Skin skin = new Skin(Gdx.files.internal("images/ui/buttons/glass.json"));
        TextButton BackButton = new TextButton("Back", skin);
        BackButton.addListener(new ClickListener() {
            public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
                game.setScreen(GdxGame.ScreenType.MAIN_MENU);

            }
        });

        Texture imageTexture = new Texture("images/ui/Sprites/UI_Glass_Arrow_Large_01a.png");
        Drawable drawable = new TextureRegionDrawable(new TextureRegion(imageTexture));
        ImageButton MobsButton = new ImageButton(drawable);
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

        Table imageTextTable = new Table();
        imageTextTable.setFillParent(true);
        imageTextTable.center();

        float leftPadding = 30f;
        imageTextTable.padLeft(leftPadding);

        // Create an array of image file names
        String[] imageFileNames = {
                "images/turret-select/droid-tower-default.png",
                "images/turret-select/fire-tower-default.png",
                "images/turret-select/mine-tower-default.png",
                "images/turret-select/stun-tower-default.png",
                "images/turret-select/tnt-tower-default.png",
                "images/turret-select/wall-tower-default.png",
                "images/turret-select/Weapon-Tower-Default.png"
        };

        // Create an array of text descriptions
        String[] textDescriptions = {
                "Droid Towers deploy robotic helpers that assist in combat and provide support to nearby turrets.",
                "The Fire Tower emits flames, causing damage over time to enemies caught in its fiery radius.",
                "The Income Tower generates additional in-game currency over time.",
                "The Stun Tower releases electric shocks that temporarily immobilize and damage enemies.",
                "The TNT Tower launches explosive projectiles, dealing area damage to groups of enemies.",
                "The Wall Tower creates barriers to block enemy paths, slowing down their progress.",
                "The Weapon Tower is a simple and basic turret that fires rapid shots at enemies dealing damage over time."
        };

        // Add images and text to the table
        for (int i = 0; i < imageFileNames.length; i++) {
            // Create an image from the file
            Image image = new Image(new Texture( imageFileNames[i]));
            float imageSize = 200f;
            image.setSize(imageSize, imageSize);

            // Create a label for text description
            Label.LabelStyle labelStyle = new Label.LabelStyle();
            BitmapFont customFont = new BitmapFont(Gdx.files.internal("images/ui/buttons/dot_gothic_16.fnt")); // Replace "your-font.fnt" with your font file path
            customFont.getData().setScale(1.2f); // Adjust the scale factor to change the text size
            labelStyle.font = customFont;

            Label label = new Label(textDescriptions[i], labelStyle);
            label.setWrap(true);

            // Add the image and label to the table in two columns
            Cell<Image> imageCell = imageTextTable.add(image).expandX().pad(10); // Add spacing around the image
            Cell<Label> labelCell = imageTextTable.add(label).expandX().pad(5);

            float maxCellWidth = 1100f; // Adjust the width as needed
            labelCell.width(maxCellWidth);

            labelCell.left();
            // Move to the next row
            imageTextTable.row();
        }

        // Add the table to the stage
        stage.addActor(imageTextTable);
    }
    /**
     * Sets the screen as the input processor when it is shown.
     */
    @Override
    public void show() {
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
