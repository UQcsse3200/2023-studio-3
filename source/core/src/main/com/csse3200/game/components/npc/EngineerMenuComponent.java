package com.csse3200.game.components.npc;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.csse3200.game.ui.UIComponent;

public class EngineerMenuComponent extends UIComponent {
    Table table;

    @Override
    public void create() {
        super.create();
    }

    @Override
    public void draw(SpriteBatch batch)  {
        // draw is handled by the stage
    }

    public void createMenu(float x, float y, Camera camera) {
        table = new Table();

        Vector3 entityCoordinates = new Vector3(x, y, 0);
        Vector3 entityScreenCoordinate = camera.project(entityCoordinates);
        Vector2 stageCoordinates = stage.screenToStageCoordinates(
                new Vector2(entityScreenCoordinate.x, entityScreenCoordinate.y));
        stage.getViewport().unproject(stageCoordinates);

        table.setPosition(x, y);
        table.setFillParent(true);
        String imageFilePath = "images/ui/Sprites/UI_Glass_Banner_01b.png";
        Drawable drawable = new TextureRegionDrawable(new TextureRegion(new Texture(imageFilePath)));
        table.setBackground(drawable);
        //stage.addActor(table);

    }

    public void removeMenu() {
        table.clear();
        table.remove();
    }

}
