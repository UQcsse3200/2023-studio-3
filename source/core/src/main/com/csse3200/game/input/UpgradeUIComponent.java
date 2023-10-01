package com.csse3200.game.input;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.csse3200.game.areas.ForestGameArea;
import com.csse3200.game.components.gamearea.TowerUpgradeDisplay;
import com.csse3200.game.components.tower.TowerUpgraderComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.EntityService;
import com.csse3200.game.services.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UpgradeUIComponent extends InputComponent {
    private static final Logger logger = LoggerFactory.getLogger(ForestGameArea.class);
    private final EntityService entityService;
    private final Camera camera;
    private final Stage stage;

    private Table upgradeTable;
    int value;

    /**
     * Constructor for the UpgradeUIComponent
     * @param camera the camera to be used, this is the camera that the game is rendered with
     */
    public UpgradeUIComponent(Camera camera, Stage stage) {
        this.value = ServiceLocator.getCurrencyService().getScrap().getAmount();
        this.entityService = ServiceLocator.getEntityService();
        this.camera = camera;
        this.stage = stage;
    }

    /**
     * Getter for the camera
     * @return the camera
     */
    public Camera getCamera() {
        return camera;
    }

    public Stage getStage() {
        return stage;
    }

    /**
     * When the mouse is clicked, this method is called.
     *
     * @param screenX The x coordinate, origin is in the upper left corner
     * @param screenY The y coordinate, origin is in the upper left corner
     * @param pointer the pointer for the event.
     * @param button the button
     * @return
     */
    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        Vector3 worldCoordinates = new Vector3((float)  screenX , (float) screenY, 0);
        getCamera().unproject(worldCoordinates); // translate from screen to world coordinates
        Vector2 cursorPosition = new Vector2(worldCoordinates.x, worldCoordinates.y);
        Entity clickedEntity = entityService.getEntityAtPosition(cursorPosition.x, cursorPosition.y);

        if (clickedEntity != null && clickedEntity.getComponent(TowerUpgraderComponent.class) != null) {
            logger.info("clicked a turret that is upgradable!");
            createUpgradeTable(clickedEntity, screenX, screenY);
            stage.addActor(upgradeTable);
            return true;
        }
        return false;
    }

    private void createUpgradeTable(Entity turretEntity, int screenX, int screenY) {
        upgradeTable = new Table();
        upgradeTable.align(Align.top);
        upgradeTable.setPosition(screenX, screenY);

        // Create a BitmapFont for the LabelStyle
        BitmapFont font = new BitmapFont(); // You can customize this font as needed

        Drawable drawable = new TextureRegionDrawable(new TextureRegion(new Texture("images/ui/Sprites/UI_Glass_Button_Small_Lock_01a2.png")));
        TextButton.TextButtonStyle style = new TextButton.TextButtonStyle(
                drawable, drawable, drawable, new BitmapFont());

        // create button
        TextButton tb = new TextButton("Hello World", style);
        tb.setDisabled(true);

        upgradeTable.add(tb);

        upgradeTable.setVisible(true);
    }



}
