package com.csse3200.game.components.gamearea;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
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
    private Camera camera;
    private TextButton scrapsTb;
    private TextButton crystalsTb;
    private Sound clickSound;
    private static final String DEFAULT_FONT = "determination_mono_18";

    /**
     * Adds actors to stage
     */
    @Override
    public void create() {
        super.create();
        clickSound = ServiceLocator.getResourceService().getAsset("sounds/economy/click.wav", Sound.class);
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
        table.padTop(140f).padLeft(20f);

        scrapsTb = createButton("images/economy/scrapBanner.png",
                ServiceLocator.getCurrencyService().getScrap().getAmount());
        crystalsTb = createButton("images/economy/crystalBanner.png",
                ServiceLocator.getCurrencyService().getCrystal().getAmount());

        table.add(scrapsTb).width(scrapsTb.getWidth() * 0.5f).height(scrapsTb.getHeight() * 0.5f);
        table.row();
        table.add(crystalsTb).width(crystalsTb.getWidth() * 0.5f).height(crystalsTb.getHeight() * 0.5f);
        stage.addActor(table);

        scrapsTb.setPosition(table.getX() - 200f, Gdx.graphics.getHeight() - 205f);
        scrapsTb.addAction(new SequenceAction(Actions.moveTo(table.getX() + 20f, Gdx.graphics.getHeight() - 205f,
                1f, Interpolation.fastSlow)));

        crystalsTb.setPosition(table.getX() - 200f, Gdx.graphics.getHeight() - 268f);
        crystalsTb.addAction(new SequenceAction(Actions.moveTo(table.getX() + 20f, Gdx.graphics.getHeight() - 268f,
                1f, Interpolation.fastSlow)));
    }

    private TextButton createButton(String imageFilePath, int value) {
        Drawable drawable = new TextureRegionDrawable(new TextureRegion(new Texture(imageFilePath)));
        TextButton.TextButtonStyle style = new TextButton.TextButtonStyle(
                drawable, drawable, drawable, getSkin().getFont(DEFAULT_FONT));

        // create button
        TextButton tb = new TextButton(String.format("%d", value), style);
        tb.setDisabled(true);
        tb.getLabel().setAlignment(Align.right);

        tb.pad(0, 0, 0, 50);
        tb.setTransform(true);

        return tb;
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
     * Displays a warning animation of the scraps display if the player tries to
     * build something that costs more than the balance
     */
    public void scrapBalanceFlash() {
        scrapsTb.addAction(Actions.repeat(2,
                new SequenceAction(Actions.fadeOut(0.5f, Interpolation.bounce),
                        Actions.fadeIn(0.5f, Interpolation.bounce))));
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
     * Also plays sound
     * @param x Screen x coordinate
     * @param y Screen y coordinate
     * @param amount value to display on the pop-up
     * @param offset value to offset the height of the label by
     */
    public void currencyPopUp(float x , float y, int amount, int offset) {
        Label label;
        if (amount > 0) {
            // play sound and set the volume
            long soundId = clickSound.play();
            clickSound.setVolume(soundId, 0.4f);
            label = new Label(String.format("+%d", amount), skin);
        } else {
            label = new Label(String.format("%d", amount), skin);
        }

        // remove label after it fades out
        label.addAction(new SequenceAction(Actions.fadeOut(1.5f), Actions.removeActor()));

        // get stage coordinates from entity coordinates
        Vector3 entityCoordinates = new Vector3(x, y, 0);
        Vector3 entityScreenCoordinate = this.camera.project(entityCoordinates);
        Vector2 stageCoordinates = stage.screenToStageCoordinates(
                new Vector2(entityScreenCoordinate.x, entityScreenCoordinate.y));
        stage.getViewport().unproject(stageCoordinates);

        label.setPosition(stageCoordinates.x - label.getWidth()/2, stageCoordinates.y + offset);
        stage.addActor(label);
    }

    public void setCamera(Camera camera) {
        this.camera = camera;
    }

    @Override
    public void dispose() {
        super.dispose();
        scrapsTb.remove();
        crystalsTb.remove();
    }
}
