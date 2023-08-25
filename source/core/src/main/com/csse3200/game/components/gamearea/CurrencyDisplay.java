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


public class CurrencyDisplay extends UIComponent {
    Table table;
    private TextButton scrapsTb;

    @Override
    public void create() {
        super.create();
        addActors();
    }

    private void addActors() {
        table = new Table();
        table.top().left();
        table.setFillParent(true);
        table.padTop(50f).padLeft(20f);
        //currencyImage = new Image(ServiceLocator.getResourceService().getAsset("images/scrapsUI.png", Texture.class));

        // create text button style
        Drawable drawable = new TextureRegionDrawable(new TextureRegion(new Texture("images/scrapsUI.png")));
        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle(
                drawable, drawable, drawable, new BitmapFont());

        // create scraps button
        scrapsTb = new TextButton("0", textButtonStyle);
        scrapsTb.getLabel().setAlignment(Align.right);
        scrapsTb.getLabel().setFontScale(2, 2);
        scrapsTb.pad(0, 0, 0, 70);
        scrapsTb.setTransform(true);
        scrapsTb.setScale(0.5f);
        scrapsTb.setDisabled(true);

        table.add(scrapsTb);
        stage.addActor(table);
    }

    @Override
    public void draw(SpriteBatch batch) {
        //
    }

    public void updateScrapsStats(int value) {
        CharSequence text = String.format("%d", value);
        scrapsTb.getLabel().setText(text);
    }

    @Override
    public void dispose() {
        super.dispose();
        scrapsTb.remove();
    }
}
