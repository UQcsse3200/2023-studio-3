package com.csse3200.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.csse3200.game.GdxGame;
import com.csse3200.game.screens.text.AnimatedText;
import com.csse3200.game.services.GameEndService;
import com.csse3200.game.services.ResourceService;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.ui.ButtonFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static com.badlogic.gdx.scenes.scene2d.ui.Table.Debug.table;
import static com.csse3200.game.ui.UIComponent.getSkin;


/**
 * Represents the level selection screen of the game, where players can choose a planet to play on.
 * The screen displays different types of planets representing various game levels.
 * Players can interact with the planets to select a level or get information about it.
 * Background music and other UI elements like buttons and description boxes are also included.
 *
 * <p>Planets represent different levels in the game:
 * ICE planet is level 1, DESERT planet is level 0, and LAVA planet is level 2.
 * The appearance and properties of each planet vary based on the current game state and player progress.
 * </p>
 *
 */
public class LevelSelectScreen extends ScreenAdapter {
    Logger logger = LoggerFactory.getLogger(LevelSelectScreen.class);
    private final GdxGame game;
    private SpriteBatch batch;
    private int selectedLevel = -1;
    private int currentLevel;
    private boolean isMouseOverPlanet = false;

    private static final String INTRO_TEXT = "Select a Planet for Conquest";
    private final Stage stage;
    private final AnimatedText text;
    private BitmapFont font;
    private static final String defaultFont = "determination_mono_22";

    private Sprite background;
    private final Music music;

    // Stores a time to determine the frame of the planet
    float timeCounter = 0;

    private static final String BG_PATH = "planets/background.png";
    String[] bgm = {
            "sounds/background/pre_game/Sci-Fi8Loop_story.ogg"
    };

    // Description Box
    private final Label descriptionBox;
    private final Table descriptionTable;
    private final String[] planetDescriptions = {
            "         A desert planet with vast dunes and scorching heat.",
            "         An icy world with frozen landscapes and chilling winds.",
            "           A lava-filled planet with molten rivers and extreme temperatures."
    };


    /**
     * Constructor to initialize the LevelSelectScreen.
     *
     * @param game The main game object, used to switch screens and access global game properties.
     * @param currentLevel The current game level reached by the player.
     */
    public LevelSelectScreen(GdxGame game, int currentLevel) {
        this.currentLevel = currentLevel;
        font = getSkin().getFont(defaultFont);
        text = new AnimatedText(INTRO_TEXT, font, 0.05f);
        this.game = game;

        stage = new Stage(new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));

        Skin skin = new Skin(Gdx.files.internal("images/ui/buttons/glass.json"));
        TextButton BackButton = new TextButton("Back", skin);
        BackButton.addListener(new ClickListener() {
            @Override
            public void clicked(com.badlogic.gdx.scenes.scene2d.InputEvent event, float x, float y) {
                game.setScreen(GdxGame.ScreenType.MAIN_MENU);
            }
        });

        Table buttonTable = new Table();
        buttonTable.add(BackButton).padRight(10);
        Table table1 = new Table();
        table1.setFillParent(true);
        table1.top().right();
        table1.pad(20);
        table1.add(buttonTable).row();
        stage.addActor(table1);

        descriptionTable = new Table();
        descriptionTable.setFillParent(true);
        descriptionTable.center();
        descriptionTable.pad(20);
        descriptionBox = new Label("", skin);
        descriptionTable.add(descriptionBox);
        descriptionTable.setVisible(false); // Initially, the description box is hidden
        stage.addActor(descriptionTable);

        ServiceLocator.registerResourceService(new ResourceService());
        ServiceLocator.getResourceService().loadMusic(bgm);
        ServiceLocator.getResourceService().loadAll();
        music = ServiceLocator.getResourceService().getAsset(bgm[0], Music.class);
    }

    @Override
    public void show() {
        batch = new SpriteBatch();
        background = new Sprite(new Texture(BG_PATH));
        ServiceLocator.registerGameEndService(new GameEndService());
        Gdx.input.setInputProcessor(stage);

        music.setVolume(0.4f);
        music.setLooping(true);
        music.play();
    }

    /**
     * Spawns different types of planets on the screen based on the current game state.
     * These planets represent different levels in the game.
     *
     * ICE planet is level 1, DESERT planet is level 0, and LAVA planet is level 2.
     * The planets are spawned with their respective properties.
     */
    private void spawnPlanets() {
        // ICE is level 0
        spawnPlanet(150, 150, Planets.ICE[0], Planets.ICE[1],"Barren_or_Moon", 2, (int) (timeCounter * 35) % 60 + 1, 1);
        // DESERT is level 1
        spawnPlanet(150, 150, Planets.DESERT[0], Planets.DESERT[1], "Desert", 1, (int) (timeCounter * 60) % 60 + 1, 0);
        // LAVA is level 2
        spawnPlanet(200, 200, Planets.LAVA[0], Planets.LAVA[1],"Lava", 1, (int) (timeCounter * 15) % 60 + 1, 2);

        spawnPlanetBorders();
    }


    /**
     * Spawns a planet on the screen.
     * @param width The width of the planet
     * @param height The height of the planet
     * @param posx The x position of the planet
     * @param posy The y position of the planet
     * @param planetName The name of the planet
     * @param version The different type of planet
     * @param frame The frame of the planet
     * @param levelNumber The level associated with the planet
     */
    private void spawnPlanet(int width, int height, int posx, int posy, String planetName, int version, int frame, int levelNumber) {
        int highestLevelReached = currentLevel;
        Texture planet;

        levelNumber = mapToConventional(levelNumber);
        if (levelNumber == 0 && highestLevelReached >= -1)  { // ICE planet, which is always unlocked initially
            planet = new Texture(String.format("planets/%s/%d/%d.png", planetName, version, frame));
        } else if (levelNumber == 1 && highestLevelReached >= 0) { // DESERT planet, unlocked only when highestLevelReached is 0
            planet = new Texture(String.format("planets/%s/%d/%d.png", planetName, version, frame));
        } else if (levelNumber == 2 && highestLevelReached >= 1) { // LAVA planet, unlocked after DESERT
            planet = new Texture(String.format("planets/%s/%d/%d.png", planetName, version, frame));
        } else {
            // Display the planet in b&w if it's locked
            planet = new Texture(String.format("planets/%s_bw/%d/%d.png", planetName, version, frame));
        }

        Sprite planetSprite = new Sprite(planet);
        planetSprite.setSize(width, height);
        batch.draw(planetSprite, posx, posy, width, height);
    }


    /**
     * Spawns the borders of the planets. If a planet is clicked it will load the level
     * based on the planet. If a planet is hovered over it will display a border around
     * the planet.
     */
    private void spawnPlanetBorders() {
        Vector3 mousePos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
        int highestLevelReached = currentLevel;

        // Iterates through the planets checking for the bounding box
        for (int[] planet : Planets.PLANETS) {
            Rectangle planetRect = new Rectangle(planet[0], planet[1], planet[2], planet[3]);
            if (planetRect.contains(mousePos.x, (float) Gdx.graphics.getHeight() - mousePos.y)) {
                // If the mouse is over a planet, draw the planet border
                Sprite planetBorder = new Sprite(new Texture("planets/planetBorder.png"));
                batch.draw(planetBorder, planet[0] - 2.0f, planet[1] - 2.0f, planet[2] + 3.0f, planet[3] + 3.0f);

                int conventionalPlanetLevel = mapToConventional(planet[4]);
                // Check if planet level is unlocked before allowing click
                String description = getPlanetDescription(planet);
                descriptionBox.setText(description); // Set the description in the description box
                descriptionTable.setVisible(true); // Make the description box visible
                // Flag for rendering planet borders
                boolean isRenderingPlanetBorders = true;

                if (Gdx.input.justTouched()) {
                    if (conventionalPlanetLevel == 0 && highestLevelReached >= -1) { // ICE planet, which is always unlocked initially
                        loadPlanetLevel(planet);
                    } else if (conventionalPlanetLevel == 1 && highestLevelReached >= 0) { // DESERT planet, unlocked only when highestLevelReached is 0
                        loadPlanetLevel(planet);
                    } else if (conventionalPlanetLevel == 2 && highestLevelReached >= 1) { // LAVA planet, unlocked after DESERT
                        loadPlanetLevel(planet);
                    } else {
                        logger.info("Attempted to load locked level {}", planet[4]);
                        // Add feedback for the player here if necessary
                    }
                }
            }
        }
    }

    /**
     * Loads the specified planet level.
     *
     * @param planet An array containing information about the planet, where planet[4] represents the level to be loaded.
     * @throws IllegalArgumentException If the specified planet number is invalid.
     */
    private void loadPlanetLevel(int[] planet) {
        logger.info("Loading level {}", planet[4]);
        GameLevelData.setSelectedLevel(planet[4]);
        music.stop();
        game.setScreen(GdxGame.ScreenType.TURRET_SELECTION);
    }

    /**
     * Maps an unconventional planet number to a conventional one.
     *
     * @param unconventionalNumber The unconventional planet number to be mapped.
     * @return The corresponding conventional planet number.
     * @throws IllegalArgumentException If the unconventional planet number is not valid.
     */
    public int mapToConventional(int unconventionalNumber) {
        switch (unconventionalNumber) {
            case -1:
                return -1;
            case 1:
                return 0;
            case 0:
                return 1;
            case 2:
                return 2;
            default:
                throw new IllegalArgumentException("Invalid planet number");
        }
    }

    /**
     * Retrieves the description for a given planet.
     *
     * @param planet An array containing information about the planet.
     * @return A string describing the planet.
     */
    private String getPlanetDescription(int[] planet) {
        int planetIndex = getPlanetIndex(planet);
        if (planetIndex >= 0 && planetIndex < planetDescriptions.length) {
            return planetDescriptions[planetIndex];
        }
        return "Planet Description not available.";
    }

    /**
     * Retrieves the index of a given planet within the global planet array.
     *
     * @param planet An array containing information about the planet.
     * @return The index of the planet or -1 if not found.
     */
    private int getPlanetIndex(int[] planet) {
        for (int i = 0; i < Planets.PLANETS.length; i++) {
            if (planet == Planets.PLANETS[i]) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Renders the level selection screen at each frame. This includes drawing the background, planets,
     * and any necessary UI elements. It also handles user interactions such as hovering over and selecting planets.
     *
     * @param delta The time in seconds since the last render. This is used for frame rate independent updates.
     */
    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        timeCounter += delta;

        batch.begin();
        batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        spawnPlanets();
        text.update();
        text.draw(batch, 100, 700);
        batch.end();
        boolean isMouseOverPlanetNow = false;
        Vector3 mousePos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);

        for (int[] planet : Planets.PLANETS) {
            Rectangle planetRect = new Rectangle(planet[0], planet[1], planet[2], planet[3]);
            if (planetRect.contains(mousePos.x, (float) Gdx.graphics.getHeight() - mousePos.y)) {
                isMouseOverPlanetNow = true;
                int conventionalPlanetLevel = mapToConventional(planet[4]);
                String description = getPlanetDescription(planet);
                descriptionBox.setText(description);
                descriptionTable.setVisible(true);
                int highestLevelReached = currentLevel;
                if (Gdx.input.justTouched()) {
                    if (conventionalPlanetLevel == 0 && highestLevelReached >= -1) {
                        loadPlanetLevel(planet);
                    } else if (conventionalPlanetLevel == 1 && highestLevelReached >= 0) {
                        loadPlanetLevel(planet);
                    } else if (conventionalPlanetLevel == 2 && highestLevelReached >= 1) {
                        loadPlanetLevel(planet);
                    } else {
                        logger.info("Attempted to load locked level {}", planet[4]);
                    }
                }
            }
        }

        if (!isMouseOverPlanetNow && isMouseOverPlanet) {
            // Mouse was over a planet in the previous frame but not anymore
            // Hide the description box
            descriptionTable.setVisible(false);
        }

        isMouseOverPlanet = isMouseOverPlanetNow;

        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
    }

    /**
     * Called when the screen size is changed. This updates the viewport to ensure the UI scales
     * and positions correctly for the new window size.
     *
     * @param width The new width of the screen.
     * @param height The new height of the screen.
     */
    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    /**
     * Disposes of all the resources loaded by this screen to prevent memory leaks.
     * This includes any textures, stages, sprite batches, music, and other disposable assets.
     */
    @Override
    public void dispose() {
        ServiceLocator.getResourceService().unloadAssets(bgm);
        stage.dispose();
        batch.dispose();
        music.dispose();
    }
}