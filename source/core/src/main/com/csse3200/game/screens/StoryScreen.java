package com.csse3200.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.csse3200.game.GdxGame;
import com.csse3200.game.screens.text.AnimatedText;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;


public class StoryScreen extends ScreenAdapter {
    private final GdxGame game;
    private SpriteBatch batch;
    private Texture introImage;
    private Sprite introSprite;

    private static final String TEXTURE = "planets/background.png";
    private static final String INTRO_TEXT = """
            More than 100 years ago, the world was at peace.\s
            The people lived in harmony with nature, and the\s
            world was full of life. However, the people grew\s
            greedy and wanted more. They began to take more\s
            than they needed, and the world began to suffer.\s
            The people began to fight over the remaining\s
            resources, and the world was plunged into chaos.\s
            With nothing left to fight over, the people began\s
            to fight for resources that were not theirs.\s
            This is where our story begins.\s
            """;

    private BitmapFont font;
    private AnimatedText text;
    private Stage stage;
    private TextButton continueButton;
    public StoryScreen(GdxGame game) {
        this.game = game;
        font = new BitmapFont();
        text = new AnimatedText(INTRO_TEXT, font, 0.05f);
    }

    @Override
    public void show() {
        batch = new SpriteBatch();
        introImage = new Texture(TEXTURE);
        introSprite = new Sprite(introImage);
        introSprite.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        Skin skin = new Skin(Gdx.files.internal("images/ui/buttons/glass.json"));
        continueButton = new TextButton("Continue", skin);
        continueButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(GdxGame.ScreenType.LEVEL_SELECT);
            }

        });

        Table table = new Table();
        table.setFillParent(true);
        table.add(continueButton).padBottom(-400).row();
        stage.addActor(table);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        introSprite.draw(batch);
        text.update();
        text.draw(batch, 400, 500); // Adjust the position
        batch.end();

        stage.draw();
    }

    /**
     * Fixes the
     * @param width
     * @param height
     */
    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void dispose() {
        batch.dispose();
        introImage.dispose();
        stage.dispose();
    }
}
