package com.csse3200.game.components.tasks;

import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.ai.tasks.DefaultTask;
import com.csse3200.game.ai.tasks.PriorityTask;
import com.csse3200.game.ai.tasks.Task;
import com.csse3200.game.entities.Entity;
import com.csse3200.game.entities.factories.ProjectileFactory;
import com.csse3200.game.physics.PhysicsEngine;
import com.csse3200.game.physics.PhysicsLayer;
import com.csse3200.game.physics.raycast.RaycastHit;
import com.csse3200.game.services.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Move towards the left side of the screen. Every few steps stop and make a ranged attack before
 * progressing left again. Requires an entity with a PhysicsMovementComponent.
 */
public class RangeBossMovementTask extends DefaultTask implements PriorityTask {
    private static final short TARGET = PhysicsLayer.OBSTACLE;
    private static final Logger logger = LoggerFactory.getLogger(RangeBossMovementTask.class);

    private final Vector2 maxBossRange = new Vector2();
    private PhysicsEngine physics;
    private final RaycastHit hit = new RaycastHit();
    private final float waitTime;
    private Vector2 currentPos;
    private MovementTask movementTask;
    private WaitTask waitTask;
    private WaitTask rangeAttackTask;
    private Task currentTask;

    private enum STATE {
        WALK, RANGE, MELEE, IDLE
    }

    private STATE rangeBossState;

    /**
     * @param waitTime    How long in seconds to wait between wandering.
     */
    public RangeBossMovementTask(float waitTime) {
        physics = ServiceLocator.getPhysicsService().getPhysics();
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
        this.maxBossRange.set(0, currentPos.y);

        waitTask = new WaitTask(waitTime);
        waitTask.create(owner);

        rangeAttackTask = new WaitTask(waitTime);
        rangeAttackTask.create(owner);

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
                if (towerAhead()) {
                    startRangeAttack();
                } else {
                    startWaiting();
                }
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

    private void startRangeAttack() {
        logger.debug("Starting range attack");
        // fire a projectile
        Entity newProjectile;
        swapTask(rangeAttackTask);
    }

    private void swapTask(Task newTask) {
        if (currentTask != null) {
            currentTask.stop();
        }
        currentTask = newTask;
        currentTask.start();
    }

    private boolean towerAhead() {
        // use a raycast to find out if there is a tower on the lane
        return physics.raycast(currentPos, maxBossRange, TARGET, hit);
    }
}