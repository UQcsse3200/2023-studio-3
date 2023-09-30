package com.csse3200.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.csse3200.game.GdxGame;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Text;


import java.util.*;

public class TurretSelectionScreen extends ScreenAdapter {

    private static final int MAX_SELECTED_TURRETS = 5;
    private Stage stage;
    private List<TowerType> turretList;
    private TextButton confirmButton;

    private GdxGame game;

    private SpriteBatch batch;

    private Sprite introSprite;

    private Label message;
    private Label turretsPicked;
    private Table table;
    private static final String TEXTURE = "planets/background.png";
    private Set<TowerType> selectedTurrets = new HashSet<>();

    private static final Logger logger = LoggerFactory.getLogger(MainMenuScreen.class);

    public TurretSelectionScreen(GdxGame game) {
        this.game = game;
        stage = new Stage(new ScreenViewport());
        table = new Table();

        // Set up the background
        batch = new SpriteBatch();
        Texture backgroundImage = new Texture(TEXTURE);
        introSprite = new Sprite(backgroundImage);
        introSprite.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        // Add all turrets to turret list
        turretList = new ArrayList<>();
        // Add turrets to the list
        turretList.addAll(Arrays.asList(TowerType.values()));
        // Restrictions can be added to the arrays i.e. map == "Forest" && level == 1 using for loop


        Skin skin = new Skin(Gdx.files.internal("flat-earth/skin/flat-earth-ui.json"));
        message = new Label("Select your turrets", skin);
        turretsPicked = new Label("Turrets picked: ", skin);

        confirmButton = new TextButton("Continue", skin);
        confirmButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(GdxGame.ScreenType.MAIN_GAME);
            }
        });

        // Centered the message and turrets label
        table.add(message).center().colspan(4).row();
        table.add(turretsPicked).center().colspan(4).row();
        int towersPerRow = 4; // Set the number of towers to display per row
        int numRows = (int) Math.ceil((double)turretList.size() / towersPerRow); // Calculate the number of rows

        for (int row = 0; row < numRows; row++) {
            for (int col = 0; col < towersPerRow; col++) {
                int index = row * towersPerRow + col;
                if (index >= turretList.size()) {
                    break; // Ensure we don't try to access a turret beyond the list size
                }

                TowerType turret = turretList.get(index);

                // Create a nested table for each turret
                Table turretTable = new Table();
                turretTable.center(); // Center the contents of the nested table

                Pixmap pixmap200 = new Pixmap(Gdx.files.internal(String.valueOf(Gdx.files.internal(turret.getImagePath()))));
                Pixmap pixmap100 = new Pixmap(150, 150, pixmap200.getFormat());
                pixmap100.drawPixmap(pixmap200,
                        0, 0, pixmap200.getWidth(), pixmap200.getHeight(),
                        0, 0, pixmap100.getWidth(), pixmap100.getHeight()
                );

                // Load the turret image
                Texture turretTexture = new Texture(pixmap100);
                Image turretImage = new Image(turretTexture);
                TextButton button = createButton("images/turret-select/imageedit_2_8132799771.png",
                        "images/ui/Sprites/UI_Glass_Frame_Lite_01a.png", 100);
                // Add the image to the nested table
                //turretTable.add(turretImage).pad(10).row();
                turretTable.add(button).pad(10).row();

                // Add the nested table to the main table
                table.add(turretTable).pad(10).center();

                // Center the contents of the main table cell
                table.getCells().peek().center();
            }

            // Center the entire row
            table.row().center();

        }

        // Centered the "continue" button
        table.add(confirmButton).center().colspan(4).padBottom(20).row();


        // Center the table within the stage
        table.center();
        stage.addActor(table);
        table.setFillParent(true);
        Gdx.input.setInputProcessor(stage);

    }
    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.begin();
        introSprite.draw(batch);
        batch.end();
        stage.draw();
    }

    private TextButton createButton(String defaultImageFilePath, String alternateImageFilePath, int cost) {
        Drawable defaultDrawable = new TextureRegionDrawable(new TextureRegion(new Texture(defaultImageFilePath)));
        Drawable alternateDrawable = new TextureRegionDrawable(new TextureRegion(new Texture(alternateImageFilePath)));

        TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.font = new BitmapFont(); // Set your desired font
        buttonStyle.up = defaultDrawable; // Default state

        // Create button
        TextButton tb = new TextButton(String.format("%d", cost), buttonStyle);

        // Add click listener to toggle the image
        final boolean[] isDefaultImage = {true}; // Keep track of the image state

        tb.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);

                // Toggle the image
                if (isDefaultImage[0]) {
                    tb.getStyle().up = alternateDrawable;
                } else {
                    tb.getStyle().up = defaultDrawable;
                }

                // Update the image state
                isDefaultImage[0] = !isDefaultImage[0];
            }
        });

        tb.setDisabled(true);

        return tb;
    }



    public List<TowerType> getTurretList() {
        return turretList;
    }

    @Override
    public void dispose() {
        stage.dispose();
    }

}
