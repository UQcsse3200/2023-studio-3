package com.csse3200.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.csse3200.game.GdxGame;
import com.csse3200.game.services.ResourceService;
import com.csse3200.game.services.ServiceLocator;

public class WinningScreen extends ScreenAdapter {
    private final GdxGame game;
    private SpriteBatch batch;
    private Texture backgroundTexture;
    private Sprite backgroundSprite;

    private static final String BACKGROUND_TEXTURE = "winning_background.png"; // Set the path to your winning background image
    private static final String WIN_TEXT = """
            Congratulations! You won!
            """;
    private static final String[] winSounds = {"win_sound.ogg"}; // Set the path to your winning sound
    private final BitmapFont font;
    private Stage stage;
    private final ResourceService resourceService;

    public WinningScreen(GdxGame game) {
        this.game = game;
        font = new BitmapFont();
        resourceService = new ResourceService();
        ServiceLocator.registerResourceService(resourceService);
        resourceService.loadSounds(winSounds);
        resourceService.loadAll();
    }

    @Override
    public void show() {
        batch = new SpriteBatch();
        backgroundTexture = new Texture(BACKGROUND_TEXTURE);
        backgroundSprite = new Sprite(backgroundTexture);
        backgroundSprite.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        Skin skin = new Skin(Gdx.files.internal("configs/text.json"));
        TextButton exitButton = new TextButton("Exit Game", skin);
        exitButton.addListener(new ClickListener(){
            public void clicked(InputEvent event, float x, float y) {
                game.exit();
            }
        });

        TextButton mainMenuButton = new TextButton("Back to Main Menu", skin);
        mainMenuButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(GdxGame.ScreenType.MAIN_MENU);
            }
        });

        TextButton playAgainButton = new TextButton("Play Again", skin);
        playAgainButton.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(GdxGame.ScreenType.LEVEL_SELECT);
            }
        });

        Table table = new Table();
        table.setFillParent(true);
        table.add(exitButton).padTop(-100).row();
        table.add(mainMenuButton).padTop(-200).row();
        table.add(playAgainButton).padTop(-300).row();
        stage.addActor(table);

        // Play win sound
        resourceService.getAsset(winSounds[0], Sound.class).play(0.3f);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        backgroundSprite.draw(batch);
        font.draw(batch, WIN_TEXT, 730, 800); // Adjust the position
        batch.end();

        stage.draw();
    }

    @Override
    public void dispose() {
        batch.dispose();
        backgroundTexture.dispose();
        stage.dispose();
    }
}
