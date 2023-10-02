package com.csse3200.game.components.tasks;

import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.ai.tasks.DefaultTask;
import com.csse3200.game.ai.tasks.PriorityTask;
import com.csse3200.game.ai.tasks.Task;
import com.csse3200.game.components.tasks.MovementTask;
import com.csse3200.game.components.tasks.WaitTask;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.factories.ProjectileFactory;
import com.csse3200.game.physics.PhysicsEngine;
import com.csse3200.game.physics.raycast.RaycastHit;
import com.csse3200.game.services.ServiceLocator;
import com.csse3200.game.physics.PhysicsLayer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Wander around by moving a random position within a range of the starting position. Wait a little
 * bit between movements. Requires an entity with a PhysicsMovementComponent.
 */
public class BombshipCombatTask extends DefaultTask implements PriorityTask {
    private static final Logger logger = LoggerFactory.getLogger(BombshipCombatTask.class);

    private final float waitTime;
    private Vector2 currentPos;
    private MovementTask movementTask;
    private WaitTask waitTask;
    private Task currentTask;
    /**
     * Animation event names
     */
    private static final String IDLE = "idle";
    private static final String START = "start";
    private static final String DESTROY = "destroy";

    private enum STATE {
        IDLE, START, DESTROY
    }

    private PhysicsEngine physics;
    private static final short TARGET = PhysicsLayer.TOWER;
    private final RaycastHit hit = new RaycastHit();

    /**
     * @param waitTime How long in seconds to wait between wandering.
     */
    public BombshipCombatTask(float waitTime) {
        this.waitTime = waitTime;
        physics = ServiceLocator.getPhysicsService().getPhysics();
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
        movementTask = new MovementTask(currentPos.sub(2, 0));
        movementTask.create(owner);

        movementTask.start();
        owner.getEntity().getEvents().trigger("idle");
        currentTask = movementTask;

        this.owner.getEntity().getEvents().trigger("start");
    }

    @Override
    public void update() {
        if (currentTask.getStatus() != Status.ACTIVE) {
            if (currentTask != movementTask) {
                if (isEngineerDied()) {
                    owner.getEntity().getEvents().trigger("start");
                    this.owner.getEntity().getEvents().trigger("destroy");
                }
                owner.getEntity().getEvents().trigger(START);
                startWaiting();
            } else {
                startMoving();
            }
        }
        currentTask.update();
    }

    private void startWaiting() {
        logger.debug("Starting waiting");
        owner.getEntity().getEvents().trigger("idle");
        swapTask(waitTask);
    }

    private void startMoving() {
        logger.debug("Starting moving");
        owner.getEntity().getEvents().trigger("start");
        owner.getEntity().getEvents().trigger("destroy");
        movementTask.setTarget(currentPos.sub(2, 0));
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
