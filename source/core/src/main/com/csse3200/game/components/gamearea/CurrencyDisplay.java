package com.csse3200.game.components.gamearea;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.ui.UIComponent;

/**
 * A UI component for displaying the currency owned
 */
public class CurrencyDisplay extends UIComponent {
    Table table;
    private TextButton scrapsTb;

    /**
     * Adds actors to stage
     */
    @Override
    public void create() {
        super.create();
        addActors();
        entity.getEvents().addListener("updateCurrency", this::updateScrapsStats);
    }

    /**
     * Initialises the currency label
     * Positions it on the stage using a table
     */
    private void addActors() {
        table = new Table();
        table.top().left();
        table.setFillParent(true);
        table.padTop(50f).padLeft(20f);

        // create text button style
        Drawable drawable = new TextureRegionDrawable(new TextureRegion(new Texture("images/economy/scrapsUI.png")));
        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle(
                drawable, drawable, drawable, new BitmapFont());

        // create scraps button
        int value = ServiceLocator.getCurrencyService().getScrap().getAmount();
        String text = String.format("%d", value);
        scrapsTb = new TextButton(text, textButtonStyle);
        scrapsTb.setDisabled(true);
        scrapsTb.getLabel().setAlignment(Align.right);
        scrapsTb.getLabel().setFontScale(2, 2); // font size
        scrapsTb.pad(0, 0, 0, 70);
        scrapsTb.setTransform(true);
        scrapsTb.setScale(0.5f); // button size

        table.add(scrapsTb);
        stage.addActor(table);
    }

    @Override
    public void draw(SpriteBatch batch) {
        // handled by stage
    }

    /**
     * Updates the currency (Scraps) value on the UI component
     */
    public void updateScrapsStats() {
        int value = ServiceLocator.getCurrencyService().getScrap().getAmount();
        CharSequence text = String.format("%d", value);
        scrapsTb.getLabel().setText(text);
    }

    @Override
    public void dispose() {
        super.dispose();
        scrapsTb.remove();
    }
}
