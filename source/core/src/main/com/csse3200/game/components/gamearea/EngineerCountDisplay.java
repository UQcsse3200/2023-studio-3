package com.csse3200.game.components.gamearea;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextTooltip;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.ui.UIComponent;

public class EngineerCountDisplay extends UIComponent {
    private TextButton engineerTb;

    private static final String DEFAULT_FONT = "determination_mono_18";

    private static final float Z_INDEX = 2f;

    @Override
    public void create() {
        super.create();
        addActors();
    }

    /**
     * Initialises the engineer count display
     * Positions it on the stage using a table
     */
    private void addActors() {
        Table table = new Table();
        table.top().left();
        table.setFillParent(true);
        table.padTop(80f).padLeft(20f);

        Drawable drawable = new TextureRegionDrawable(new TextureRegion(
                new Texture("images/engineers/engineerBanner.png")));
        TextButton.TextButtonStyle style = new TextButton.TextButtonStyle(
                drawable, drawable, drawable, getSkin().getFont(DEFAULT_FONT));

        String text = String.format("%d", ServiceLocator.getGameEndService().getEngineerCount());
        engineerTb = new TextButton(text, style);
        engineerTb.setDisabled(true);
        engineerTb.getLabel().setAlignment(Align.right);
        engineerTb.setTouchable(Touchable.enabled);
        engineerTb.pad(0, 0, 0, 50);
        engineerTb.setTransform(true);
        TextTooltip tooltip = new TextTooltip(
                "Humans left. If this reaches 0, the game ends", getSkin());
        engineerTb.addListener(tooltip);

        table.add(engineerTb).width(engineerTb.getWidth() * 0.5f).height(engineerTb.getHeight() * 0.5f);
        stage.addActor(table);

        // Animate the engineer count label
        engineerTb.setPosition(table.getX() - 200f, Gdx.graphics.getHeight() - 145f);
        engineerTb.addAction(new SequenceAction(Actions.moveTo(table.getX() + 20f, Gdx.graphics.getHeight() - 145f,
                1f, Interpolation.fastSlow)));
    }

    /**
     * Updates the engineer count on the UI component
     */
    public void updateCount() {
        if (engineerTb != null) { // fix for null pointer exception
            int currentCount = ServiceLocator.getGameEndService().getEngineerCount();
            String text = String.format("%d", currentCount);
            engineerTb.getLabel().setText(text);
            if (currentCount < ServiceLocator.getGameEndService().getThreshold()) {
//            engineerTb.addAction(Actions.color(Color.RED, 0.5f, Interpolation.swingIn));
                engineerTb.addAction(Actions.forever(new SequenceAction(Actions.fadeOut(0.5f),
                        Actions.fadeIn(0.5f))));
            }
        }
    }

    @Override
    public float getZIndex() {
        return Z_INDEX;
    }

    @Override
    protected void draw(SpriteBatch batch) {
        // handled by stage
    }

    @Override
    public void dispose() {
        super.dispose();
        engineerTb.remove();
    }
}
