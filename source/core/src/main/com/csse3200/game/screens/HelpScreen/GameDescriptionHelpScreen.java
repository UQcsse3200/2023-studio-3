package com.csse3200.game.screens.HelpScreen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.csse3200.game.GdxGame;
import com.csse3200.game.entities.factories.PauseMenuFactory;
/**
 * A screen that provides a description of the game and its components.
 */

public class GameDescriptionHelpScreen extends ScreenAdapter {
    private final GdxGame game;
    private Stage stage;
    private SpriteBatch spriteBatch;

    /**
     * Creates a new GameDescriptionHelpScreen.
     *
     * @param game The main game instance.
     */
    public GameDescriptionHelpScreen(GdxGame game) {
        this.game = game;
        stage = new Stage(new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));
        spriteBatch = new SpriteBatch();
        Skin skin = new Skin(Gdx.files.internal("images/ui/buttons/glass.json"));

        Label.LabelStyle labelStyle = new Label.LabelStyle();
        BitmapFont customFont = new BitmapFont(Gdx.files.internal("images/ui/buttons/dot_gothic_16.fnt"));
        labelStyle.font = customFont;

        // Create a table to organize the image placeholder
        Table table = new Table();
        table.setFillParent(true); // Makes the table the size of the stage

        // Create one image placeholder
        Image image = new Image(new Texture("images/lose-screen/desktop-wallpaper-simple-stars-video-background-loop-black-and-white-aesthetic-space.jpg"));

        // Add the image placeholder to the table
        table.add(image).expand().fill();

        // Add the table to the stage
        stage.addActor(table);


        Table contentTable = new Table();
        contentTable.center(); // Center the content table
        contentTable.pad(20); // Makes the table the size of the stage
        contentTable.setFillParent(true);
        stage.addActor(contentTable);

// Create and add the text label in the first row
        Label titleLabel = new Label("In a peaceful past, nature thrived, but human greed disrupted harmony. To save humanity, they set out to conquer planets in massive ARKs. They built outposts, terraformed, and researched, only to uncover a looming, unknown threat in the cosmos.", labelStyle);
        titleLabel.setAlignment(Align.left);
        titleLabel.setWrap(true);
        contentTable.add(titleLabel).colspan(2).pad(10).row(); // colspan makes it span two columns

        Cell<Label> titlelabelcell = contentTable.add(titleLabel).expandX().pad(5);
        float maxCellWidth = 1100f; // Adjust the width as needed
        titlelabelcell.width(maxCellWidth);

        titlelabelcell.center();
        contentTable.row();

        String[] imagePaths = {
                "images/HelpScreen/Engineer.png",
                "images/HelpScreen/Turret.png",
                "images/HelpScreen/Mob.png",
                "images/HelpScreen/Econ.png"
        };

        String[] textData = {
                "Save the engineers from foreign creatures",
                "Strategically place towers to stop the aliens",
                "Aliens, all they want is you off of their planet",
                "Strategically place towers to stop the aliens"
        };

// Create and add the rows with images and text
        for (int i = 0; i < imagePaths.length; i++) {
            // Create an image placeholder for each row
            Image image1 = new Image(new Texture(imagePaths[i])); // Use the appropriate image path
            float imageWidth = 150f; // Change this to your desired width
            float imageHeight = 150f; // Change this to your desired height

            image1.setWidth(imageWidth);
            image1.setHeight(imageHeight);
            // Create a label for text
            Label textLabel = new Label(textData[i], labelStyle);
            textLabel.setAlignment(Align.left);
            textLabel.setFontScale(1.2f);

            Table rowTable = new Table();
            float rowHeight = 500f; // Change this to your desired row height
            rowTable.setHeight(rowHeight);

            rowTable.add(image1).pad(10);
            rowTable.add(textLabel).width(stage.getWidth() / 2).pad(10).row();

            // Add the rowTable to the contentTable
            contentTable.add(rowTable).colspan(2).expandX().fillX().pad(5).row();
        }


        TextButton BackButton = new TextButton("Back", skin);
        BackButton.addListener(new ClickListener() {
            @Override
            public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
                game.setScreen(GdxGame.ScreenType.MAIN_MENU);

            }
        });

        TextButton MobsButton = new TextButton("Mobs", skin);
        MobsButton.addListener(new ClickListener() {
            @Override
            public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
                game.setScreen(GdxGame.ScreenType.HELP_MOBS_SCREEN);

            }
        });

        TextButton HTPButton = new TextButton("HowToPlay", skin);
        HTPButton.addListener(new ClickListener() {
            @Override
            public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
                game.setScreen(GdxGame.ScreenType.HOW_TO_PLAY);

            }
        });

        TextButton TutorialButton = new TextButton("Tutorial", skin);
        TutorialButton.addListener(new ClickListener() {
            @Override
            public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
                game.setScreen(GdxGame.ScreenType.TUTORIAL_SCREEN);
                PauseCompTutorial.TutorialMenu(game);

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
        table2.center().right(); // Align to the middle-right corner
        table2.pad(20); // Add padding to the middle-right corner
        table2.add(buttonTable1).row(); // Add button table and move to the next row
        stage.addActor(table2);

        Table buttonTable2 = new Table();
        buttonTable2.add(HTPButton).padRight(10);
        Table table3 = new Table();
        table3.setFillParent(true);
        table3.center().bottom(); // Align to the middle-right corner
        table3.pad(20); // Add padding to the middle-right corner
        table3.add(buttonTable2).row(); // Add button table and move to the next row
        stage.addActor(table3);

        Table buttonTable3 = new Table();
        buttonTable3.add(TutorialButton).padRight(10);
        Table table4 = new Table();
        table4.setFillParent(true);
        table4.center().top(); // Align to the middle-right corner
        table4.pad(20); // Add padding to the middle-right corner
        table4.add(buttonTable3).row(); // Add button table and move to the next row
        stage.addActor(table4);

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