package com.csse3200.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.csse3200.game.GdxGame;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.factories.TowerFactory;
import com.csse3200.game.physics.PhysicsService;
import com.csse3200.game.services.ResourceService;
import com.csse3200.game.services.ServiceLocator;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


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

                // Load the turret image
                Texture turretTexture = new Texture(Gdx.files.internal(turret.getImagePath()));
                Image turretImage = new Image(turretTexture);

                // Add the image to the nested table
                turretTable.add(turretImage).pad(10).row();

                // Create a TextButton for the turret name
                TextButton turretButton = new TextButton(turret.getTowerName(), skin);
                turretButton.addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        logger.info(String.valueOf(selectedTurrets.size()));
                        if (selectedTurrets.size() > MAX_SELECTED_TURRETS) {
                            message.setText("You can only select up to 5 turrets.");
                        } else {
                            message.setText("Select your turrets");
                        }
                        if (selectedTurrets.contains(turret)) {
                            // Turret is already selected, unselect it
                            selectedTurrets.remove(turret);
                            // You can also change the button appearance to indicate unselection
                            logger.info(selectedTurrets.toString());
                            turretsPicked.setText("Turrets picked: " + selectedTurrets.toString());
                        } else if (selectedTurrets.size() == MAX_SELECTED_TURRETS) {
                            // Turret is not selected, but the max number of turrets has been reached
                            message.setText("You can only select up to 5 turrets.");
                        } else if (selectedTurrets.size() < MAX_SELECTED_TURRETS) {
                            // Turret is not selected, select it
                            selectedTurrets.add(turret);
                            turretsPicked.setText("Turrets picked: " + selectedTurrets.toString());
                            logger.info(selectedTurrets.toString());
                        }
                        else {
                            // Turret is not selected, select it
                            selectedTurrets.add(turret);
                            turretsPicked.setText("Turrets picked: " + selectedTurrets.toString());
                            //logger.info(selectedTurrets.toString());

                            // You can change the button appearance to indicate selection
                        }
                    }
                });

                // Add the turret name button to the nested table
                turretTable.add(turretButton).center();

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

    public List<TowerType> getTurretList() {
        return turretList;
    }

    @Override
    public void dispose() {
        stage.dispose();
    }

}
