package com.csse3200.game.components.gamearea;


import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.csse3200.game.ui.UIComponent;


public class TowerPlacementDisplay extends UIComponent {

    public TowerPlacementDisplay() {
        super();
    }

    private void addActors() {
        Table table = new Table();
        table.setDebug(true);
        table.top();
        // split the table into 4 columns
        table.setFillParent(true);
        table.padTop(50f).padLeft(20f);

        // make basic text button
        for (int i = 0; i < 4; i++) {
            TextureRegion region = new TextureRegion(new Texture("images/towers/mine_tower.png"));
            Image image = new Image(region);
            table.add(image).size(100, 100).pad(10);

        }
        stage.addActor(table);
    }

    @Override
    public void create() {
        super.create();
        addActors();
    }

    @Override
    protected void draw(SpriteBatch batch) {

    }
}
