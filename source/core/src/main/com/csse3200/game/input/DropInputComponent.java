package com.csse3200.game.input;


import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.EntityService;
import com.csse3200.game.entities.factories.DropFactory;
import com.csse3200.game.services.ServiceLocator;

public class DropInputComponent extends InputComponent {
    private EntityService entityService;
    int value;

    public DropInputComponent(EntityService entityService) {
        this.value = ServiceLocator.getCurrencyService().getScrap().getAmount();
        this.entityService = entityService;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        Vector2 worldCoordinates = new Vector2(screenX, screenY);
        Camera camera = ServiceLocator.getRenderService().getStage().getCamera();

        Entity clickedEntity = entityService.getEntityAtPosition(worldCoordinates.x, worldCoordinates.y);
        if (clickedEntity != null && clickedEntity.getComponent(DropInputComponent.class) != null) {
            ServiceLocator.getCurrencyService()
                    .getScrap().setAmount(value + clickedEntity.getComponent(DropInputComponent.class).value);
            EntityService.removeEntity(clickedEntity);
            return true;
        }
        return false;
    }


}
