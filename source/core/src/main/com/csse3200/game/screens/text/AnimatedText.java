package com.csse3200.game.screens.text;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.utils.TimeUtils;

public class AnimatedText {
    private String text;
    private BitmapFont font;
    private GlyphLayout glyphLayout;
    private float revealSpeed; // Adjust the speed of typing animation
    private int charactersToShow;
    private long lastCharTime;

    public AnimatedText(String text, BitmapFont font, float revealSpeed) {
        this.text = text;
        this.font = font;
        this.glyphLayout = new GlyphLayout();
        this.revealSpeed = revealSpeed;
        this.charactersToShow = 0;
        this.lastCharTime = TimeUtils.nanoTime();
    }

    public void update() {
        long currentTime = TimeUtils.nanoTime();
        float deltaTime = (currentTime - lastCharTime) / 1_000_000_000.0f; // Convert to seconds

        if (deltaTime >= revealSpeed && charactersToShow < text.length()) {
            charactersToShow++;
            lastCharTime = currentTime;
        }
    }

    public void draw(Batch batch, float x, float y) {
        String visibleText = text.substring(0, charactersToShow);
        glyphLayout.setText(font, visibleText);
        font.draw(batch, glyphLayout, x, y);
    }

    public boolean isFinished() {
        return charactersToShow >= text.length();
    }
}
