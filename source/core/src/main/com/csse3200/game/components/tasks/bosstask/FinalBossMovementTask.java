package com.csse3200.game.components.tasks.bosstask;

import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.ai.tasks.DefaultTask;
import com.csse3200.game.ai.tasks.PriorityTask;
import com.csse3200.game.ai.tasks.Task;
import com.csse3200.game.components.ProjectileEffects;
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
import static com.csse3200.game.screens.MainGameScreen.viewportHeight;

/**
 * Wander around by moving a random position within a range of the starting position. Wait a little
 * bit between movements. Requires an entity with a PhysicsMovementComponent.
 */
public class FinalBossMovementTask extends DefaultTask implements PriorityTask {
    private static final Logger logger = LoggerFactory.getLogger(FinalBossMovementTask.class);

    private final float waitTime;
    private int currLane;
    private Vector2 currentPos;
    private MovementTask movementTask;
    private MovementTask swapLaneTask;
    private WaitTask waitTask;
    private Task currentTask;
    private PhysicsEngine physics;
    private static final short TARGET = PhysicsLayer.TOWER;
    private final RaycastHit hit = new RaycastHit();

    /**
     * @param waitTime    How long in seconds to wait between moving.
     */
    public FinalBossMovementTask(float waitTime, int numLane) {
        this.waitTime = waitTime;
        this.currLane = numLane;

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

        swapLaneTask = new MovementTask(currentPos);
        swapLaneTask.create(owner);

        movementTask = new MovementTask(currentPos);
        movementTask.create(owner);

        movementTask.start();

        currentTask = movementTask;

        this.owner.getEntity().getEvents().trigger("finalBossMovementStart");
    }

    @Override
    public void update() {
        if (currentTask.getStatus() != Status.ACTIVE) {
            if (currentTask == movementTask) {
                // Melee attack
                if (towerAhead()) {
                    Entity newProjectile = ProjectileFactory.createEffectProjectile(PhysicsLayer.TOWER, new Vector2(0,currentPos.y + 0.75f), new Vector2(2f,2f), ProjectileEffects.BURN, false);
                    newProjectile.scaleHeight(-0.4f);
                    newProjectile.setPosition(currentPos.x, currentPos.y + 0.75f);
                    ServiceLocator.getEntityService().register(newProjectile);
                }
                startWaiting();
            } else if (currentTask == waitTask) {
//                startSwappingLane();
                startMoving();
            }
//            } else {
//                startMoving();
//            }
        }
        currentTask.update();
    }

    private void startWaiting() {
        logger.debug("Starting waiting");

        currentTask.stop();

        currentTask = waitTask;
        currentTask.start();
    }

    private void startMoving() {
        logger.debug("Starting moving");

        currentTask.stop();
        owner.getEntity().getEvents().trigger("walkStart");
        movementTask.setTarget(currentPos.sub(2,0));
        currentTask = movementTask;
        currentTask.start();

//        swapTask(movementTask);
    }

    private void startSwappingLane() {
        logger.debug("Starting swapping");

        currentTask.stop();

        float laneHeight = (float) (viewportHeight) / 8;

        if (currLane == 0) {
            // Move up
            swapLaneTask.setTarget(currentPos.add(0, laneHeight));

            currLane++;
        } else {
            // Temporary move down for all other cases
            swapLaneTask.setTarget(currentPos.sub(0, laneHeight));

            currLane--;
        }

        currentTask = swapLaneTask;
        swapLaneTask.start();

//        swapTask(swapLaneTask);
    }

    private void swapTask(Task newTask) {
        if (currentTask != null) {
            currentTask.stop();
        }
        currentTask = newTask;
        currentTask.start();
    }

    private boolean towerAhead() {
        return physics.raycast(currentPos, new Vector2(currentPos.x - 1, currentPos.y), TARGET, hit);
    }
}
