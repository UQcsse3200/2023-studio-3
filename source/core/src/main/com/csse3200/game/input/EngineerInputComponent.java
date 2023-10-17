package com.csse3200.game.input;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.csse3200.game.ai.tasks.AITaskComponent;
import com.csse3200.game.components.player.HumanAnimationController;
import com.csse3200.game.components.tasks.human.HumanWanderTask;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.EntityService;
import com.csse3200.game.physics.PhysicsLayer;
import com.csse3200.game.rendering.AnimationRenderComponent;
import com.csse3200.game.services.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EngineerInputComponent extends InputComponent {

    private static final Logger logger = LoggerFactory.getLogger(EngineerInputComponent.class);
    private Game game;
    private Camera camera;
    private EntityService entityService;

    public Entity selectedEngineer = null;

    private final String OUTLINE_STRING = "_outline";

    public EngineerInputComponent(Game game, Camera camera) {
        this.game = game;
        this.camera = camera;
        this.entityService = ServiceLocator.getEntityService();
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        Vector3 worldCoordinates = new Vector3(screenX , screenY, 0);
        camera.unproject(worldCoordinates);
        Vector2 cursorPosition = new Vector2(worldCoordinates.x, worldCoordinates.y);
        camera.project(worldCoordinates);
        Entity engineer = entityService.getEntityAtPositionLayer(cursorPosition.x, cursorPosition.y, PhysicsLayer.ENGINEER);
        //logger.info("Clicked entity: " + engineer);

        // Case when engineer is not clicked
        if (engineer == null || engineer.getComponent(HumanAnimationController.class) == null) {
            if (selectedEngineer != null) {
                // Clicked a tile with an engineer selected and clicked on not an engineer
                moveEngineer(cursorPosition);
                return true;
            } else {
                // Clicked a tile with no engineer selected or engineer on the tile
                return false;
            }
        }
        // Case when engineer is clicked
        switchEngineer(engineer);
        return true;
    }

    @Override
    public boolean keyDown(int keycode) {
        if (selectedEngineer == null) {
            return false;
        }


        if (Gdx.input.isKeyPressed(Input.Keys.P)) {
            manualShoot();
        }
        return true;
    }

    private void manualShoot() {
        HumanWanderTask wander = this.getWanderTask();
        wander.startWaiting();
        wander.startCombat();
    }

    /**
     * Switches the specified engineer
     * If the given engineer is already selected, deselect it
     * If another engineer is selected, deselect it and select the new given engineer
     * @param engineer (Entity) the specified engineer
     */
    private void switchEngineer(Entity engineer) {
        if (engineer.equals(this.selectedEngineer)) {
            this.getWanderTask().setSelected(false);
            this.selectedEngineer = null;
            switchOutline(engineer);
        }
        else if (selectedEngineer == null) {
            this.selectedEngineer = engineer;
            switchOutline(engineer);
            this.getWanderTask().setSelected(true);

        } else {
            this.getWanderTask().setSelected(false);
            switchOutline(this.selectedEngineer);
            switchOutline(engineer);
            this.selectedEngineer = engineer;
            this.getWanderTask().setSelected(true);

        }
    }

    /**
     * Switches the outline of the given engineer and deselects/selects engineer as needed
     * If outlined -> remove outline, deselect engineer and vice versa
     * @param engineer (Entity) the specified engineer
     */
    private void switchOutline(Entity engineer) {
        AnimationRenderComponent animator = engineer.getComponent(AnimationRenderComponent.class);
        String currentAnimation = animator.getCurrentAnimation();
        HumanAnimationController controller = engineer.getComponent(HumanAnimationController.class);
        if (currentAnimation.contains(OUTLINE_STRING)) {
            animator.startAnimation(currentAnimation.substring(0, currentAnimation.lastIndexOf('_')));
            controller.setClicked(false);
        } else {
            animator.startAnimation(currentAnimation + OUTLINE_STRING);
            controller.setClicked(true);
        }
    }

    /**
     * Returns the wander task of the selected engineer
     * @return (HumanWanderTask) the wander task of the selected engineer
     */
    private HumanWanderTask getWanderTask() {
        AITaskComponent movementTask = selectedEngineer.getComponent(AITaskComponent.class);
        return movementTask.getTask(HumanWanderTask.class);
    }

    /**
     * Moves the selected engineer to the given cursor position
     * @param cursorPosition (Vector2) the cursor position
     */
    public void moveEngineer(Vector2 cursorPosition) {
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
}
