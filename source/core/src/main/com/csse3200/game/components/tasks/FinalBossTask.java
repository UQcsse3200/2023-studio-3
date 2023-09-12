package com.csse3200.game.components.tasks;

import com.badlogic.gdx.math.Vector2;
import com.csse3200.game.ai.tasks.DefaultTask;
import com.csse3200.game.ai.tasks.PriorityTask;
import com.csse3200.game.ai.tasks.Task;
import com.csse3200.game.components.ProjectileEffects;
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
public class FinalBossTask extends DefaultTask implements PriorityTask {
    private static final Logger logger = LoggerFactory.getLogger(FinalBossTask.class);

    private final float waitTime;
    private int currLane;
    private Vector2 currentPos;
    private MovementTask movementTask;
    private MovementTask swapLaneTask;
    private WaitTask waitTask;
    private Task currentTask;
    /** Animations */


    private PhysicsEngine physics;
    private static final short TARGET = PhysicsLayer.TOWER;
    private final RaycastHit hit = new RaycastHit();

    /**
     * @param waitTime    How long in seconds to wait between moving.
     */
    public FinalBossTask(float waitTime, int numLane) {
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

        movementTask = new MovementTask(currentPos.sub(2,0));
        movementTask.create(owner);

        movementTask.start();
        owner.getEntity().getEvents().trigger("walk");

        currentTask = movementTask;

        this.owner.getEntity().getEvents().trigger("finalBossMovementStart");
    }

    @Override
    public void update() {
        if (currentTask.getStatus() != Status.ACTIVE) {
            if (currentTask == movementTask) {
                // Melee attack
                if (towerAhead() || engineerAhead()) {
                    TouchAttackComponent attackComp = owner.getEntity().getComponent(TouchAttackComponent.class);
                    HitboxComponent hitboxComp = owner.getEntity().getComponent(HitboxComponent.class);
                    attackComp.onCollisionStart(hitboxComp.getFixture(), target);
                    this.owner.getEntity().getEvents().trigger("meleeStart");
                }
                startWaiting();
            } else  {
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
        owner.getEntity().getEvents().trigger("idle");
        swapTask(waitTask);
    }

    private void startMoving() {
        logger.debug("Starting moving");
        owner.getEntity().getEvents().trigger("walk");
        movementTask.setTarget(currentPos.sub(2,0));
        swapTask(movementTask);
    }

    /** private void startSwappingLane() {
        logger.debug("Starting swapping");

        currentTask.stop();

        float laneHeight = viewportHeight / 8;

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
*/
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
    private boolean engineerAhead() {
        return physics.raycast(currentPos, new Vector2(0, currentPos.y), PhysicsLayer.ENGINEER, hit);
    }
}
