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

    public DropInputComponent(Camera camera) {
        this.value = ServiceLocator.getCurrencyService().getScrap().getAmount();
        this.entityService = ServiceLocator.getEntityService();
        this.camera = camera;
    }

    public Camera getCamera() {
        return camera;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        Vector3 worldCoordinates = new Vector3((float)  screenX , (float) screenY, 0);
        getCamera().unproject(worldCoordinates);
        Vector2 cursorPosition = new Vector2(worldCoordinates.x, worldCoordinates.y);
        Entity clickedEntity = entityService.getEntityAtPosition(cursorPosition.x, cursorPosition.y);
        if (clickedEntity != null && clickedEntity.getComponent(DropComponent.class) != null) {
            ServiceLocator.getCurrencyService().getScrap()
                    .modify(clickedEntity.getComponent(DropComponent.class).getValue());
            EntityService.removeEntity(clickedEntity);
            //logger.info("Scrap amount: " + ServiceLocator.getCurrencyService().getScrap().getAmount());
            ServiceLocator.getCurrencyService().getDisplay().updateScrapsStats();
            return true;
        }
        return false;
    }


}
