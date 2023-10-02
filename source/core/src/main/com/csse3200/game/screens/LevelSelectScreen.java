package com.csse3200.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.csse3200.game.GdxGame;
import com.csse3200.game.components.mainmenu.MainMenuDisplay;
import com.csse3200.game.entities.factories.RenderFactory;
import com.csse3200.game.rendering.Renderer;
import com.csse3200.game.screens.text.AnimatedText;
import com.csse3200.game.screens.Planets;
import com.csse3200.game.services.GameEndService;
import com.csse3200.game.services.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Text;

import static com.badlogic.gdx.scenes.scene2d.ui.Table.Debug.table;

/**
 * The game screen where you can choose a planet to play on.
 */
public class LevelSelectScreen extends ScreenAdapter {
    Logger logger = LoggerFactory.getLogger(LevelSelectScreen.class);
    private final GdxGame game;
    private SpriteBatch batch;
    private int selectedLevel = -1;

    private static final String INTRO_TEXT = "Select a Planet for Conquest";
    private Stage stage;
    private AnimatedText text;
    private BitmapFont font;

    private Sprite background;

    // Stores a time to determine the frame of the planet
    // TODO: Account for integer overflow
    float timeCounter = 0;

    private static final String BG_PATH = "planets/background.png";

    public LevelSelectScreen(GdxGame game) {
        font = new BitmapFont();
        text = new AnimatedText(INTRO_TEXT, font, 0.05f);
        this.game = game;

        stage = new Stage(new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));

        Skin skin = new Skin(Gdx.files.internal("flat-earth/skin/flat-earth-ui.json"));
        TextButton BackButton = new TextButton("Back", skin); // Universal Skip button
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
        table1.top().right(); // Align to the top-right corner
        table1.pad(20); // Add padding to the top-right corner
        table1.add(buttonTable).row(); // Add button table and move to the next row
        stage.addActor(table1);
    }

    @Override
    public void show() {
        batch = new SpriteBatch();
        background = new Sprite(new Texture(BG_PATH));
        ServiceLocator.registerGameEndService(new GameEndService());
        Gdx.input.setInputProcessor(stage);
    }

    /**
     * Spawns the planets on the screen by doing contionous calls to spawnPlanet().
     * The rotation speed of a planet is determined by the frame variable, this
     * function can be modified.
     */
    private void spawnPlanets() {
        // Spawn desert planet
        spawnPlanet(150, 150, Planets.DESERT[0], Planets.DESERT[1], "Desert", 1, (int) (timeCounter * 60) % 60 + 1);
        // Spawn ice planet
        spawnPlanet(150, 150, Planets.ICE[0], Planets.ICE[1],"Barren_or_Moon", 2, (int) (timeCounter * 35) % 60 + 1);
        // Spawn lava planet
        spawnPlanet(200, 200, Planets.LAVA[0], Planets.LAVA[1],"Lava", 1, (int) (timeCounter * 15) % 60 + 1);

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
     */
    private void spawnPlanet(int width, int height, int posx, int posy, String planetName, int version, int frame) {
        Texture planet = new Texture(String.format("planets/%s/%d/%d.png", planetName, version, frame));
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
        // Iterates through the planets checking for the bounding box
        for (int[] planet : Planets.PLANETS) {
            Rectangle planetRect = new Rectangle(planet[0], planet[1], planet[2], planet[3]);
            if (planetRect.contains(mousePos.x, (float) Gdx.graphics.getHeight() - mousePos.y)) {
                // If the mouse is over a planet, draw the planet border
                Sprite planetBorder = new Sprite(new Texture("planets/planetBorder.png"));
                batch.draw(planetBorder, planet[0] - 2.0f, planet[1] - 2.0f, planet[2] + 3.0f, planet[3] + 3.0f);
                if (Gdx.input.justTouched()) {
                    // If the planet is clicked, load the corresponding level
                    dispose();
                    logger.info("Loading level {}", planet[4]);
                    GameLevelData.setSelectedLevel(planet[4]);
                    if (planet[4] == 0) {
                        handleDesertPlanetClick();
                        game.setScreen(new TurretSelectionScreen(game));
                    } else if (planet[4] == 1) {
                        handleIcePlanetClick();
                        game.setScreen(new TurretSelectionScreen(game));
                    } else if (planet[4] == 2) {
                        handleLavaPlanetClick();
                        game.setScreen(new TurretSelectionScreen(game));
                    }
                }
            }
        }
    }

    private void handleDesertPlanetClick() {
        // Implement logic for when the desert planet is clicked
        logger.info("Desert planet clicked.");
        game.setScreen(new DesertGameScreen(game)); // Load the DesertGameScreen
    }

    private void handleIcePlanetClick() {
        // Implement logic for when the ice planet is clicked
        logger.info("Ice planet clicked.");
        game.setScreen(new IceGameScreen(game)); // Load the IceGameScreen
    }

    private void handleLavaPlanetClick() {
        // Implement logic for when the lava planet is clicked
        logger.info("Lava planet clicked.");
        game.setScreen(new LavaGameScreen(game)); // Load the LavaGameScreen
    }

    // TODO: Make it display information about the planet
    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        timeCounter += delta;

        // Gets position of cursor
        batch.begin();
        // Draws the background
        batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        // Draws the planets on top of the background.
        spawnPlanets();
        text.update();
        text.draw(batch, 100, 700); // Adjust the position
        batch.end();
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
    }
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void dispose() {
        stage.dispose();
        batch.dispose();
    }
}
