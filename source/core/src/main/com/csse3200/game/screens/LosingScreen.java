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
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.csse3200.game.GdxGame;
import com.csse3200.game.screens.text.AnimatedText;
import com.csse3200.game.services.ResourceService;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.ui.ButtonFactory;

public class LosingScreen extends ScreenAdapter {
    private final GdxGame game;
    private SpriteBatch batch;
    private Texture introImage;
    private Sprite introSprite;

    private static final String TEXTURE = "planets/background.png";
    private static final String INTRO_TEXT = """
            The aliens gained control. You lose!
            """;
    private static final String[] lossSounds = {"sounds/background/loss/RisingScreams.ogg"};
    private BitmapFont font;
    private AnimatedText text;
    private Stage stage;
    private ResourceService resourceService;
    private TextButton exitButton;
    private TextButton mainMenuButton;
    private TextButton playAgainButton;

    public LosingScreen(GdxGame game) {
        this.game = game;
        font = new BitmapFont();
        text = new AnimatedText(INTRO_TEXT, font, 0.05f);
        font.getData().setScale(2, 2);
        resourceService = new ResourceService();
        ServiceLocator.registerResourceService(resourceService);
        resourceService.loadSounds(lossSounds);
        resourceService.loadAll();
    }

    @Override
    public void show() {
        batch = new SpriteBatch();
        introImage = new Texture(TEXTURE);
        introSprite = new Sprite(introImage);
        introSprite.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        exitButton = ButtonFactory.createButton("Exit Game");
        exitButton.addListener(new ClickListener(){
            public void clicked(InputEvent even, float x, float y) {
                game.exit();
            }
        });
        mainMenuButton = ButtonFactory.createButton("Back to Main Menu");
        mainMenuButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(GdxGame.ScreenType.MAIN_MENU);
            }

        });

        playAgainButton = ButtonFactory.createButton("Play Again");
        playAgainButton.addListener(new ClickListener() {
            public void clicked(InputEvent even, float x, float y) {
                game.setScreen(GdxGame.ScreenType.LEVEL_SELECT);
            }
        });
        Table table = new Table();
        table.setFillParent(true);
        table.add(exitButton).padTop(-100).row();
        table.add(mainMenuButton).padTop(-200).row();
        table.add(playAgainButton).padTop(-300).row();
        stage.addActor(table);
        // play loss sound
        resourceService.getAsset(lossSounds[0], Sound.class).play(0.3f);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        introSprite.draw(batch);
        text.update();
        text.draw(batch, 730, 800); // Adjust the position
        batch.end();

        stage.draw();
    }

    @Override
    public void dispose() {
        batch.dispose();
        introImage.dispose();
        stage.dispose();
    }
}
