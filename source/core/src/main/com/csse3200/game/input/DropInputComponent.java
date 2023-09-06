package com.csse3200.game.input;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.csse3200.game.areas.ForestGameArea;
import com.csse3200.game.areas.terrain.TerrainComponent;
import com.csse3200.game.components.CameraComponent;
import com.csse3200.game.components.npc.DropComponent;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.EntityService;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.csse3200.game.entities.factories.DropFactory;
import com.csse3200.game.entities.factories.RenderFactory;
import com.csse3200.game.rendering.Renderer;
import com.csse3200.game.services.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.Provider;

public class DropInputComponent extends InputComponent {
    private static final Logger logger = LoggerFactory.getLogger(ForestGameArea.class);
    private final EntityService entityService;
    private final Camera camera;
    int value;

    /**
     * Constructor for the DropInputComponent
     * @param camera the camera to be used, this is the camera that the game is rendered with
     */
    public DropInputComponent(Camera camera) {
        this.value = ServiceLocator.getCurrencyService().getScrap().getAmount();
        this.entityService = ServiceLocator.getEntityService();
        this.camera = camera;
    }

    /**
     * Getter for the camera
     * @return the camera
     */
    public Camera getCamera() {
        return camera;
    }

    /**
     * When the mouse is clicked, this method is called.
     * It checks if the mouse is clicked on a drop entity and if it is,
     * it adds the value of the drop to the scrap
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
        //logger.info("Clicked entity: " + clickedEntity);
        if (clickedEntity != null && clickedEntity.getComponent(DropComponent.class) != null) {
            ServiceLocator.getCurrencyService().getScrap()
                    .modify(clickedEntity.getComponent(DropComponent.class).getValue());
            // add the value of the drop to the scrap
            EntityService.removeEntity(clickedEntity); // remove the entity from the game
            //logger.info("Scrap amount: " + ServiceLocator.getCurrencyService().getScrap().getAmount());
            ServiceLocator.getCurrencyService().getDisplay().updateScrapsStats(); // update the display
            return true;
        }
        return false;
    }


}