package com.csse3200.game.components.gamearea;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.ui.UIComponent;

public class EngineerCountDisplay extends UIComponent {
    private Table table;
    private TextButton engineerTb;

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
        table.padTop(80f).padLeft(20f);

        Drawable drawable = new TextureRegionDrawable(new TextureRegion(
                new Texture("images/engineers/engineerBanner.png")));
        TextButton.TextButtonStyle style = new TextButton.TextButtonStyle(
                drawable, drawable, drawable, new BitmapFont());

        String text = String.format("%d", ServiceLocator.getGameEndService().getEngineerCount());
        engineerTb = new TextButton(text, style);
        engineerTb.setDisabled(true);
        engineerTb.getLabel().setAlignment(Align.right);

        engineerTb.pad(0, 0, 0, 70);
        engineerTb.setTransform(true);

        table.add(engineerTb).width(engineerTb.getWidth() * 0.5f).height(engineerTb.getHeight() * 0.5f);
        stage.addActor(table);
    }

    public void updateCount() {
        String text = String.format("%d", ServiceLocator.getGameEndService().getEngineerCount());
        engineerTb.getLabel().setText(text);
    }

    @Override
    protected void draw(SpriteBatch batch) {

    }

    @Override
    public void dispose() {
        super.dispose();
        engineerTb.remove();
    }
}
