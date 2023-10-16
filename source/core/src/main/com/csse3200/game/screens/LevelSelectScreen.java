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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LevelSelectScreen extends ScreenAdapter {
    Logger logger = LoggerFactory.getLogger(LevelSelectScreen.class);
    private final GdxGame game;
    private SpriteBatch batch;

    private static final String INTRO_TEXT = "Select a Planet for Conquest";
    private final Stage stage;
    private final AnimatedText text;

    private Sprite background;
    private final Music music;

    float timeCounter = 0;

    private static final String BG_PATH = "planets/background.png";

    // Description Box
    private final Label descriptionBox;
    private final Table descriptionTable;
    private final String[] planetDescriptions = {
            "         A desert planet with vast dunes and scorching heat.",
            "         An icy world with frozen landscapes and chilling winds.",
            "           A lava-filled planet with molten rivers and extreme temperatures."
    };

    public LevelSelectScreen(GdxGame game) {
        BitmapFont font = new BitmapFont();
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
        String[] bgm = {
                "sounds/background/pre_game/Sci-Fi8Loop_story.ogg"
        };
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

    private void spawnPlanets() {
        // Spawn desert planet
        spawnPlanet(150, 150, Planets.DESERT[0], Planets.DESERT[1], "Desert", 1, (int) (timeCounter * 60) % 60 + 1);
        // Spawn ice planet
        spawnPlanet(150, 150, Planets.ICE[0], Planets.ICE[1], "Barren_or_Moon", 2, (int) (timeCounter * 35) % 60 + 1);
        // Spawn lava planet
        spawnPlanet(200, 200, Planets.LAVA[0], Planets.LAVA[1], "Lava", 1, (int) (timeCounter * 15) % 60 + 1);

        spawnPlanetBorders();
    }

    private void spawnPlanet(int width, int height, int posx, int posy, String planetName, int version, int frame) {
        Texture planet = new Texture(String.format("planets/%s/%d/%d.png", planetName, version, frame));
        Sprite planetSprite = new Sprite(planet);
        planetSprite.setSize(width, height);
        batch.draw(planetSprite, posx, posy, width, height);
    }

    private void spawnPlanetBorders() {
        Vector3 mousePos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
        for (int[] planet : Planets.PLANETS) {
            Rectangle planetRect = new Rectangle(planet[0], planet[1], planet[2], planet[3]);
            if (planetRect.contains(mousePos.x, (float) Gdx.graphics.getHeight() - mousePos.y)) {
                String description = getPlanetDescription(planet);
                descriptionBox.setText(description); // Set the description in the description box
                descriptionTable.setVisible(true); // Make the description box visible
                // Flag for rendering planet borders
                boolean isRenderingPlanetBorders = true;
                if (isRenderingPlanetBorders) {
                    Sprite planetBorder = new Sprite(new Texture("planets/planetBorder.png"));
                    batch.draw(planetBorder, planet[0] - 2.0f, planet[1] - 2.0f, planet[2] + 3.0f, planet[3] + 3.0f);
                }
                if (Gdx.input.justTouched()) {
                    int selectedLevel = planet[4];
                    game.setScreen(new TurretSelectionScreen(game));
                    dispose();
                    logger.info("Loading level {}", planet[4]);
                    GameLevelData.setSelectedLevel(planet[4]);
                    game.setScreen(new TurretSelectionScreen(game));
                }
            }
        }
    }

    private String getPlanetDescription(int[] planet) {
        int planetIndex = getPlanetIndex(planet);
        if (planetIndex >= 0 && planetIndex < planetDescriptions.length) {
            return planetDescriptions[planetIndex];
        }
        return "Planet Description not available.";
    }

    private int getPlanetIndex(int[] planet) {
        for (int i = 0; i < Planets.PLANETS.length; i++) {
            if (planet == Planets.PLANETS[i]) {
                return i;
            }
        }
        return -1;
    }

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
        music.dispose();
    }
}
