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
import com.csse3200.game.components.tasks.human.HumanWanderTask;
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
    private boolean moveClicked = false;

    public EngineerInputComponent(Game game, Camera camera) {
        this.game = game;
        this.camera = camera;
        this.entityService = ServiceLocator.getEntityService();
    }

    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        Vector3 worldCoordinates = new Vector3((float)  screenX , (float) screenY, 0);
        camera.unproject(worldCoordinates);
        Vector2 cursorPosition = new Vector2(worldCoordinates.x, worldCoordinates.y);
        camera.project(worldCoordinates);
        Entity engineer = entityService.getEntityAtPosition(cursorPosition.x, cursorPosition.y);
        //logger.info("Clicked entity: " + engineer);

        // Case when engineer is not clicked
        if (engineer == null || engineer.getComponent(HumanAnimationController.class) == null) {
            if (selectedEngineer != null && moveClicked) {
                moveEngineer(cursorPosition);
                selectedEngineer = null;
                moveClicked = false;
                return true;
            } else {
                return false;
            }
        } else if (engineer.equals(selectedEngineer)) {
            // Deselect the engineer by clicking on itself
            this.getWanderTask().setSelected(false);
            selectedEngineer = null;
            moveClicked = false;
            return true;
        }

        this.selectedEngineer = engineer;
        this.getWanderTask().setSelected(true);
        logger.info("Engineer size: {}", engineer.getScale());

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

    private HumanWanderTask getWanderTask() {
        AITaskComponent movementTask = selectedEngineer.getComponent(AITaskComponent.class);
        return movementTask.getTask(HumanWanderTask.class);
    }

    private void moveEngineer(Vector2 cursorPosition) {
        if (selectedEngineer == null) {
            logger.info("Trying to move an engineer that is not selected");
        }

        HumanWanderTask wander = this.getWanderTask();
        wander.startWaiting();
        Vector2 enggpos = selectedEngineer.getPosition();
        Vector2 offset = new Vector2(cursorPosition.x > enggpos.x ? 0.17f : -0.6f , cursorPosition.y > enggpos.y ? 0.0f : -0.5f);
        Vector2 dest = cursorPosition.add(offset);
        wander.startMoving(dest);
    }

    public void setMoveClicked(boolean moveClicked) {
        this.moveClicked = moveClicked;
    }

}
