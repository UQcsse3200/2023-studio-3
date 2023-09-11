package com.csse3200.game.components.gamearea;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.ui.UIComponent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

/**
 * A UI component for displaying the currency owned
 */
public class CurrencyDisplay extends UIComponent {
    Table table;
    private TextButton scrapsTb;
    private TextButton crystalsTb;

    /**
     * Adds actors to stage
     */
    @Override
    public void create() {
        super.create();
        addActors();
    }

    /**
     * Initialises the currency labels
     * Positions it on the stage using a table
     */
    private void addActors() {
        table = new Table();
        table.top().left();
        table.setFillParent(true);
        table.padTop(50f).padLeft(20f);

        // create scraps text button style
        Drawable scrapDrawable = new TextureRegionDrawable(new TextureRegion(new Texture("images/economy/scrapsUI.png")));
        TextButton.TextButtonStyle scrapStyle = new TextButton.TextButtonStyle(
                scrapDrawable, scrapDrawable, scrapDrawable, new BitmapFont());

        // create scraps button
        String scrapText = String.format("%d", ServiceLocator.getCurrencyService().getScrap().getAmount());
        scrapsTb = new TextButton(scrapText, scrapStyle);
        scrapsTb.setDisabled(true);
        scrapsTb.getLabel().setAlignment(Align.right);
        scrapsTb.getLabel().setFontScale(2, 2); // font size
        scrapsTb.pad(0, 0, 0, 70);
        scrapsTb.setTransform(true);
        scrapsTb.setScale(0.5f); // button size

        // create crystals text button style
        Drawable crystalDrawable = new TextureRegionDrawable(new TextureRegion(new Texture("images/economy/crystalUI.png")));
        TextButton.TextButtonStyle crystalStyle = new TextButton.TextButtonStyle(
                crystalDrawable, crystalDrawable,crystalDrawable, new BitmapFont());

        // create crystals button
        String crystalText = String.format("%d", ServiceLocator.getCurrencyService().getCrystal().getAmount());
        crystalsTb = new TextButton(crystalText, crystalStyle);
        crystalsTb.setDisabled(true);
        crystalsTb.getLabel().setAlignment(Align.right);
        crystalsTb.getLabel().setFontScale(2, 2); // font size
        crystalsTb.pad(0, 0, 0, 70);
        crystalsTb.setTransform(true);
        crystalsTb.setScale(0.5f); // button size

        table.add(scrapsTb);
        table.add(crystalsTb);
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

    /**
     * Updates the currency (Crystals) value on the UI component
     */
    public void updateCrystalsStats() {
        int value = ServiceLocator.getCurrencyService().getCrystal().getAmount();
        CharSequence text = String.format("%d", value);
        crystalsTb.getLabel().setText(text);
    }

    /**
     * A label that appears once currency is gained, to give the player visual feedback
     * @param x Screen x coordinate
     * @param y Screen y coordinate
     * @param amount value to display on the pop up
     */
    public void currencyPopUp(float x , float y, int amount) {
        Label label = new Label(String.format("+%d", amount), skin);
        // remove label after it fades out
        label.addAction(new SequenceAction(Actions.fadeOut(1.5f), Actions.removeActor()));

        Vector3 worldCoordinates = new Vector3(x , y, 0);
        stage.getViewport().unproject(worldCoordinates);
        label.setPosition(worldCoordinates.x, worldCoordinates.y);
        stage.addActor(label);
    }

    @Override
    public void dispose() {
        super.dispose();
        scrapsTb.remove();
        crystalsTb.remove();
    }
}
