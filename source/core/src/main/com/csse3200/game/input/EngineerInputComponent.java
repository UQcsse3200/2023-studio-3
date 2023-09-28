package com.csse3200.game.input;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.csse3200.game.components.player.HumanAnimationController;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.EntityService;
import com.csse3200.game.entities.factories.EngineerFactory;
import com.csse3200.game.rendering.AnimationRenderComponent;
import com.csse3200.game.services.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EngineerInputComponent extends InputComponent {

    private static final Logger logger = LoggerFactory.getLogger(EngineerInputComponent.class);
    private Game game;
    private Camera camera;
    private EntityService entityService;

    public EngineerInputComponent(Game game, Camera camera) {
        this.game = game;
        this.camera = camera;
        this.entityService = ServiceLocator.getEntityService();
    }

    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        Vector3 worldCoordinates = new Vector3((float)  screenX , (float) screenY, 0);
        camera.unproject(worldCoordinates);
        Vector2 cursorPosition = new Vector2(worldCoordinates.x, worldCoordinates.y);
        Entity engineer = entityService.getEntityAtPosition(cursorPosition.x, cursorPosition.y);
        logger.info("Clicked entity: " + engineer);

        // Case when engineer is not clicked
        if (engineer == null || engineer.getComponent(HumanAnimationController.class) == null) {
            return false;
        }
        // Case when engineer is clicked
        engineer.removeComponent(AnimationRenderComponent.class);
        logger.info("Engineer has animation render component " + engineer.getComponent(AnimationRenderComponent.class));
        AnimationRenderComponent animator = EngineerFactory.createAnimationRenderComponent(true);
        engineer.addComponent(animator);
        logger.info("Engineer has animation render component " + engineer.getComponent(AnimationRenderComponent.class));

        return true;
    }

}
