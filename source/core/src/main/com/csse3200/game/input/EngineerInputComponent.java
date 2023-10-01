package com.csse3200.game.input;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.csse3200.game.ai.tasks.AITaskComponent;
import com.csse3200.game.ai.tasks.PriorityTask;
import com.csse3200.game.ai.tasks.Task;
import com.csse3200.game.components.npc.EngineerMenuComponent;
import com.csse3200.game.components.player.HumanAnimationController;
import com.csse3200.game.components.tasks.human.HumanMovementTask;
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

    private Entity selectedEngineer = null;

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
        //logger.info("Clicked entity: " + engineer);

        // Case when engineer is not clicked
        if (engineer == null || engineer.getComponent(HumanAnimationController.class) == null) {
            if (selectedEngineer == null) {
                return false;
            }
            // TODO: handle moving the engineer to cursorPosition
            moveEngineer(cursorPosition);
            return true;
        }

        this.selectedEngineer = engineer;

        // Case when engineer is clicked
        AnimationRenderComponent animator = engineer.getComponent(AnimationRenderComponent.class);
        String currentAnimation = animator.getCurrentAnimation();
        HumanAnimationController controller = engineer.getComponent(HumanAnimationController.class);
        EngineerMenuComponent menu = engineer.getComponent(EngineerMenuComponent.class);
        // outline image if it is not already outlined and vice versa
        if (currentAnimation.contains("_outline")) {
            controller.deselectEngineer(currentAnimation);
            //logger.info("Engineer deselected");
        } else {
            animator.startAnimation(currentAnimation + "_outline");
            menu.createMenu(cursorPosition.x, cursorPosition.y, camera);
            controller.setClicked(true);
        }
        return true;
    }

    private void moveEngineer(Vector2 cursorPosition) {
        if (selectedEngineer == null) {
            logger.info("Trying to move an engineer that is not selected");
        }
        AITaskComponent movementTask = selectedEngineer.getComponent(AITaskComponent.class);
        HumanMovementTask task = new HumanMovementTask(cursorPosition);
        movementTask.addTask(task);
        logger.info("Moving engineer to {}", cursorPosition);
    }

}
