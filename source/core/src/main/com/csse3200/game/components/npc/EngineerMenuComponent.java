package com.csse3200.game.components.npc;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.csse3200.game.components.player.HumanAnimationController;
import com.csse3200.game.rendering.AnimationRenderComponent;
import com.csse3200.game.ui.UIComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EngineerMenuComponent extends UIComponent {
    private Logger logger = LoggerFactory.getLogger(EngineerMenuComponent.class);
    Table table;

    @Override
    public void create() {
        super.create();
    }

    @Override
    public void draw(SpriteBatch batch)  {
        // draw is handled by the stage
    }

    /**
     * Creates a menu for the engineer
     * @param x cursor x coordinate
     * @param y cursor y coordinate
     * @param camera camera of the game
     */
    public void createMenu(float x, float y, Camera camera) {
        this.table = createTable(x, y, camera);

        // add buttons
        TextButton moveButton = createButton("Move");
        TextButton repairButton = createButton("Repair");

        // add listeners to buttons
        AnimationRenderComponent animator = getEntity().getComponent(AnimationRenderComponent.class);
        HumanAnimationController controller = getEntity().getComponent(HumanAnimationController.class);

        moveButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                controller.deselectEngineer(animator.getCurrentAnimation());
                logger.info("Move button clicked");
            }
        });

        repairButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                controller.deselectEngineer(animator.getCurrentAnimation());
                logger.info("Repair button clicked");
            }
        });

        table.add(moveButton).grow();
        table.row();
        table.add(repairButton).grow();
        table.row();
        stage.addActor(table);

    }

    /**
     * Creates a table for the menu
     * @param x cursor x coordinate
     * @param y cursor y coordinate
     * @param camera camera of the game
     * @return table for the menu
     */
    private Table createTable(float x, float y, Camera camera) {
        Table table = new Table();
        table.top();
        table.defaults().pad(0).space(0);
        table.setSize(90, 60); // fixed table size

        // convert cursor position to stage coordinates
        Vector3 entityCoordinates = new Vector3(x, y, 0);
        Vector3 entityScreenCoordinate = camera.project(entityCoordinates);
        Vector2 stageCoordinates = stage.screenToStageCoordinates(
                new Vector2(entityScreenCoordinate.x, entityScreenCoordinate.y));
        stage.getViewport().unproject(stageCoordinates);
        table.setPosition(stageCoordinates.x, stageCoordinates.y);

        // set table background
        String imageFilePath = "images/ui/Sprites/UI_Glass_Frame_Standard_01a.png";
        Drawable drawable = new TextureRegionDrawable(new TextureRegion(new Texture(imageFilePath)));
        table.setBackground(drawable);

        return table;
    }

    /**
     * Creates a button for the menu
     * @param text text to be displayed on the button
     * @return the button
     */
    private TextButton createButton(String text) {
        String upImageFilePath = "images/ui/Sprites/UI_Glass_Button_Medium_Lock_01a2.png";
        String downImageFilePath = "images/ui/Sprites/UI_Glass_Button_Medium_Press_01a2.png";

        Drawable upDrawable = new TextureRegionDrawable(new TextureRegion(new Texture(upImageFilePath)));
        Drawable downDrawable = new TextureRegionDrawable(new TextureRegion(new Texture(downImageFilePath)));
        TextButton button = new TextButton(text,
                new TextButton.TextButtonStyle(upDrawable, downDrawable, null, new BitmapFont()));
        button.setTransform(true);
        return button;
    }

    /**
     * Removes the menu from the stage
     */
    public void removeMenu() {
        table.clear();
        table.remove();
    }

}
