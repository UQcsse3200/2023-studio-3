package com.csse3200.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.csse3200.game.GdxGame;
import com.csse3200.game.services.ResourceService;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.ui.ButtonFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.util.*;
import java.util.List;

import static com.csse3200.game.ui.UIComponent.getSkin;

public class TurretSelectionScreen extends ScreenAdapter {

    private static final int MAX_SELECTED_TURRETS = 5;
    private Stage stage;
    private Label descText;
    private List<TowerType> turretList;
    private final String[] sounds = {
            "sounds/ui/click/click_01.ogg",
    };
    private Sound click;
    private TextButton confirmButton;
    private String turretDescriptionText;

    private GdxGame game;

    private SpriteBatch batch;
    private String turretDescription;
    private String turretName;

    TextButton turretDescriptionLabel;
    private Sprite introSprite;

    private Label message;
    private Table table;
    private TextButton descriptionLabel;
    private static final String TEXTURE = "planets/background.png";
    private Set<TowerType> selectedTurrets = new HashSet<>();
    private TextButton backButton;
    private String[] bgm = {
            "sounds/background/pre_game/Sci-Fi7Loop.ogg"
    };
    private static final String defaultFont = "determination_mono_18";
    private Music music;
    private static final Logger logger = LoggerFactory.getLogger(MainMenuScreen.class);

    /**
     * Constructor for the TurretSelectionScreen
     * @param game The game object
     */
    public TurretSelectionScreen(GdxGame game) {
        this.game = game;
        stage = new Stage(new ScreenViewport());
        table = new Table();

        loadSounds();

        ServiceLocator.registerResourceService(new ResourceService());
        ServiceLocator.getResourceService().loadMusic(bgm);
        ServiceLocator.getResourceService().loadAll();
        music = ServiceLocator.getResourceService().getAsset(bgm[0], Music.class);

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

        Skin skin = new Skin(Gdx.files.internal("images/ui/buttons/glass.json"));

        backButton = ButtonFactory.createButton("Back");
        backButton.setPosition(10, Gdx.graphics.getHeight() - backButton.getHeight() - 10); // Adjust position as needed
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                click.play(0.4f);
                music.stop();
                // Handle the "back" action, e.g., return to the previous screen
                game.setScreen(GdxGame.ScreenType.LEVEL_SELECT); // Replace PREVIOU
                // S_SCREEN with the appropriate screen type
            }
        });

        message = new Label("Select up to 5 towers", skin);

        confirmButton = ButtonFactory.createButton("Continue");
        confirmButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                click.play(0.4f);
                // Store the selected towers in the ServiceLocator for transferring across screens
                // (as an Array)
                Array<TowerType> towers = new Array<>();
                for (TowerType t : selectedTurrets) {
                    towers.add(t);
                }
                music.stop();
                ServiceLocator.setTowerTypes(towers);;
                game.setScreen(GdxGame.ScreenType.LOAD_SCREEN);
            }
        });

        turretDescriptionLabel = createButton("images/turret-select/imageedit_28_4047785594.png",
                "images/turret-select/imageedit_28_4047785594.png", "", "", turretDescriptionText);

        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = getSkin().getFont("determination_mono_18");  // Set your desired BitmapFont
        descText = new Label(turretDescriptionText, labelStyle);
        descText.setWrap(true);
        descText.setWidth(190f);

        turretName = "";

        // Centered the message and turrets label
        table.add(message).center().colspan(4).row();
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

                descriptionLabel = createButton("images/turret-select/imageedit_15_5627113584.png",
                        "images/turret-select/imageedit_15_5627113584.png", "", turretName, "");

                TextButton button = createButton(turret.getDefaultImage(),
                        turret.getClickedImage(), turret.getPrice(), turret.getTowerName(), turret.getDescription());

                button.pad(103, 15, 0, 0);
                button.addListener(new ClickListener() {

                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        logger.info(String.valueOf(selectedTurrets.size()));
                        if (selectedTurrets.size() > MAX_SELECTED_TURRETS) {
                            message.setText("You can only select up to 5 turrets.");
                        } else {
                            message.setText("Select up to 5 towers");
                        }
                        if (selectedTurrets.contains(turret)) {
                            // Turret is already selected, unselect it
                            selectedTurrets.remove(turret);
                            // You can also change the button appearance to indicate unselection
                            logger.info(selectedTurrets.toString());
                        } else if (selectedTurrets.size() == MAX_SELECTED_TURRETS) {
                            // Turret is not selected, but the max number of turrets has been reached
                            message.setText("You can only select up to 5 turrets.");
                        } else if (selectedTurrets.size() < MAX_SELECTED_TURRETS) {
                            // Turret is not selected, select it
                            selectedTurrets.add(turret);
                            logger.info(selectedTurrets.toString());
                        }
                        else {
                            // Turret is not selected, select it
                            selectedTurrets.add(turret);
                            //logger.info(selectedTurrets.toString());

                            // You can change the button appearance to indicate selection
                        }
                        click.play(0.4f);
                    }
                });

                // Add the image to the nested table
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

        descriptionLabel.setPosition(Gdx.graphics.getWidth() - descriptionLabel.getWidth(), Gdx.graphics.getHeight() - descriptionLabel.getHeight());
        float turretDescriptionLabelX = Gdx.graphics.getWidth() - turretDescriptionLabel.getWidth() - 11;
        float turretDescriptionLabelY = Gdx.graphics.getHeight() - descriptionLabel.getHeight() - turretDescriptionLabel.getHeight() - 7;  // Adjusted vertical position

        // Set the position for turretDescriptionLabel
        turretDescriptionLabel.setPosition(turretDescriptionLabelX, turretDescriptionLabelY);
        descText.setPosition(turretDescriptionLabelX + 18, turretDescriptionLabelY + 65);

        // Add the actors to the stage
        stage.addActor(backButton);
        stage.addActor(turretDescriptionLabel);
        stage.addActor(descriptionLabel);
        stage.addActor(descText);



        // Center the table within the stage
        table.center();
        stage.addActor(table);
        table.setFillParent(true);
        Gdx.input.setInputProcessor(stage);
        logger.info("Playing music");
        music.setVolume(0.4f);
        music.setLooping(true);
        music.play();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        introSprite.draw(batch);
        batch.end();

        stage.act();  // Update the stage
        stage.draw();  // Draw the stage
    }

    /**
     * Returns the list of selected turrets
     * @return The list of selected turrets
     */
    public List<TowerType> getTurretList() {
        return turretList;
    }

    /**
     * Creates a button with the specified images and text
     * @param defaultImageFilePath The file path to the default image
     * @param alternateImageFilePath The file path to the alternate image
     * @param cost The cost of the turret
     * @param towerName The name of the turret
     * @param turretDesc The description of the turret
     * @return The created button
     */
    private TextButton createButton(String defaultImageFilePath, String alternateImageFilePath, String cost,
                                    String towerName, String turretDesc) {
        Drawable defaultDrawable = new TextureRegionDrawable(new TextureRegion(new Texture(defaultImageFilePath)));
        Drawable alternateDrawable = new TextureRegionDrawable(new TextureRegion(new Texture(alternateImageFilePath)));

        TextButton.TextButtonStyle buttonStyle = new TextButton.TextButtonStyle();
        buttonStyle.font = getSkin().getFont(defaultFont); // Set your desired font
        buttonStyle.up = defaultDrawable; // Default state

        // Create button
        TextButton tb = new TextButton(cost, buttonStyle);

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

        // Add a hover listener to update turretName when hovered over
        tb.addListener(new InputListener() {
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, com.badlogic.gdx.scenes.scene2d.Actor fromActor) {
                super.enter(event, x, y, pointer, fromActor);
                turretDescription = towerName;  // Update turretDescription when hovering over the button
                turretDescriptionText = turretDesc;
                updateDescriptionLabel();
                updateDescriptionText();
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, com.badlogic.gdx.scenes.scene2d.Actor toActor) {
                super.exit(event, x, y, pointer, toActor);
                turretDescription = "";  // Reset turretDescription when exiting the button
                turretDescriptionText = "";
                updateDescriptionLabel();
                updateDescriptionText();
            }
        });

        tb.setDisabled(true);

        return tb;
    }

    /**
     * Updates the description label
     */
    private void updateDescriptionLabel() {
        descriptionLabel.setText("Info: " + turretDescription);
    }

    /**
     * Updates the description text
     */
    private void updateDescriptionText() {
        descText.setText(turretDescriptionText);
    }

    public void loadSounds() {
        ServiceLocator.getResourceService().loadSounds(sounds);
        ServiceLocator.getResourceService().loadAll();
        click = ServiceLocator.getResourceService().getAsset(sounds[0], Sound.class);
    }

    public void unloadSounds() {
        ServiceLocator.getResourceService().unloadAssets(sounds);
        ServiceLocator.getResourceService().unloadAssets(bgm);
    }
    /**
     * Disposes of the stage
     */
    @Override
    public void dispose() {
        stage.dispose();
        music.dispose();
        unloadSounds();
    }

}
