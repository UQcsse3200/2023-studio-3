package com.csse3200.game.components.tasks;

import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.ai.tasks.DefaultTask;
import com.csse3200.game.ai.tasks.PriorityTask;
import com.csse3200.game.ai.tasks.Task;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.factories.ProjectileFactory;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.physics.PhysicsLayer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Wander around by moving a random position within a range of the starting position. Wait a little
 * bit between movements. Requires an entity with a PhysicsMovementComponent.
 */
public class RangeBossMovementTask extends DefaultTask implements PriorityTask {
    private static final Logger logger = LoggerFactory.getLogger(RangeBossMovementTask.class);

    private final float waitTime;
    private Vector2 currentPos;
    private MovementTask movementTask;
    private WaitTask waitTask;
    private Task currentTask;

    /**
     * @param waitTime    How long in seconds to wait between wandering.
     */
    public RangeBossMovementTask(float waitTime) {

        this.waitTime = waitTime;
    }

    @Override
    public int getPriority() {
        return 1; // Low priority task
    }

    @Override
    public void start() {
        super.start();
        currentPos = owner.getEntity().getPosition();

        waitTask = new WaitTask(waitTime);
        waitTask.create(owner);
        movementTask = new MovementTask(currentPos.sub(2,0));
        movementTask.create(owner);

        movementTask.start();
        owner.getEntity().getEvents().trigger("walkStart");
        currentTask = movementTask;

        this.owner.getEntity().getEvents().trigger("rangeBossMovementStart");
    }

    @Override
    public void update() {
        if (currentTask.getStatus() != Status.ACTIVE) {
            if (currentTask == movementTask) {
                Entity newProjectile = ProjectileFactory.createFireBall(PhysicsLayer.OBSTACLE, new Vector2(0, currentPos.y + 0.75f), new Vector2(2f,2f));

                newProjectile.scaleHeight(-0.4f);
                newProjectile.setPosition((float) (currentPos.x), (float) (currentPos.y+0.75f));
                ServiceLocator.getEntityService().register(newProjectile);
                startWaiting();
            } else {
                startMoving();
            }
        }
        currentTask.update();
    }

    private void startWaiting() {
        logger.debug("Starting waiting");
        swapTask(waitTask);
    }

    private void startMoving() {
        logger.debug("Starting moving");
        movementTask.setTarget(currentPos.sub(2,0));
        swapTask(movementTask);
    }

    private void swapTask(Task newTask) {
        if (currentTask != null) {
            currentTask.stop();
        }
        currentTask = newTask;
        currentTask.start();
    }

}
