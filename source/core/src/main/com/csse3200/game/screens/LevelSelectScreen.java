package com.csse3200.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.TimeUtils;
import com.csse3200.game.GdxGame;
import com.csse3200.game.screens.text.AnimatedText;

public class LevelSelectScreen extends ScreenAdapter {
    private final GdxGame game;
    private SpriteBatch batch;
    private Texture bgText;
    private Sprite bgSprite;

    float timeCounter = 0;

    private static final String TEXTURE = "planets/background.png";

    public LevelSelectScreen(GdxGame game) {
        this.game = game;
    }

    @Override
    public void show() {
        batch = new SpriteBatch();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        timeCounter += delta;


        batch.begin();
        bgText = new Texture(String.format("planets/Desert/1/%d.png", (int) (timeCounter * 60) % 60 + 1));
        bgSprite = new Sprite(bgText);
        bgSprite.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        bgSprite.draw(batch);
        batch.end();
    }

    @Override
    public void dispose() {
        batch.dispose();
        bgSprite.getTexture().dispose();
    }
}
